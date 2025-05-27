package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.LocationInfo;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetEquipHandler  {
    private static PetEquipHandler INSTANCE = new PetEquipHandler();
    private boolean isInInventory = false;
    private boolean isEquipHandled = true;
    private boolean isUnequipHandled = true;

    public ItemStack currentPetItem;
    public long startScanTime = 0;
    public PetStatus petStatus = PetStatus.LOADING;

    private static final Pattern PET_EQUIP_PATTERN =
            Pattern.compile("PETS\\s*[»:]\\s*Equipped your (.+?)\\.?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PET_UNEQUIP_PATTERN =
            Pattern.compile("PETS\\s*[»:]\\s*Pet unequipped!$", Pattern.CASE_INSENSITIVE);

    public static PetEquipHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PetEquipHandler();
        }
        return INSTANCE;
    }

    public void init() {
        petStatus = PetStatus.LOADING;
        isInInventory = false;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(ProfileDataHandler.instance().profileData.equippedPetSlot == -1) {
            petStatus = PetStatus.NO_PET;
        } else if (System.currentTimeMillis() - startScanTime < 5000
                && LoadingHandler.instance().isLoadingDone
                && petStatus == PetStatus.LOADING
        ) {
            if(minecraftClient.player != null && !isInInventory) {
                ItemStack itemInSlot = minecraftClient.player.getInventory().getStack(ProfileDataHandler.instance().profileData.equippedPetSlot);
                Pet pet = Pet.getPet(itemInSlot);

                if(pet != null && pet.id.equals(ProfileDataHandler.instance().profileData.equippedPet.id)) {
                    isInInventory = true;
                    currentPetItem = itemInSlot.copy();
                }
            }

            if(minecraftClient.world != null && isInInventory) {
                minecraftClient.world.getEntities().forEach(entity -> {
                    if(entity.getName().getString().contains(minecraftClient.player.getName().getString() + "'s " + currentPetItem.getName().getString())) {
                        petStatus = PetStatus.HAS_PET;
                    }
                });
            }
        } else if (LoadingHandler.instance().isLoadingDone && petStatus == PetStatus.LOADING) {
            FishOnMCExtras.LOGGER.warn("[FoE] Did not find Pet");
            petStatus = PetStatus.NO_PET;
        }

        if(!this.isUnequipHandled) {
            this.currentPetItem = null;
            ProfileDataHandler.instance().resetPet();

            FishOnMCExtras.LOGGER.info("[FoE] Unequipped Pet");
            this.isUnequipHandled = true;
        }

        if(!this.isEquipHandled) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if(player != null) {
                int itemSlot = player.getInventory().selectedSlot;
                ItemStack heldItem = player.getInventory().getStack(itemSlot);
                Pet pet = Pet.getPet(heldItem);

                if(pet != null) {
                    this.currentPetItem = heldItem.copy();
                    ProfileDataHandler.instance().updatePet(pet, itemSlot);
                    petStatus = PetStatus.HAS_PET;

                    FishOnMCExtras.LOGGER.info("[FoE] Equipped Pet");
                    this.isEquipHandled = true;
                }
            }
        }
    }

    public void onReceiveMessage(Text message) {
        String rawMessage = message.getString();

        Matcher equipMatcher = PET_EQUIP_PATTERN.matcher(rawMessage);
        Matcher unequipMatcher = PET_UNEQUIP_PATTERN.matcher(rawMessage);

        if (equipMatcher.find()) {
            handlePetEquip();
        } else if (unequipMatcher.find()) {
            handlePetUnequip();
        }
    }

    private void handlePetEquip() {
        this.isEquipHandled = false;
    }

    private void handlePetUnequip() {
        this.petStatus = PetStatus.NO_PET;
        this.isUnequipHandled = false;
    }

    public void updatePet(PlayerEntity player) {
        if(petStatus == PetStatus.HAS_PET) {
            ItemStack itemInSlot = player.getInventory().getStack(ProfileDataHandler.instance().profileData.equippedPetSlot);
            Pet pet = Pet.getPet(itemInSlot);

            if(pet != null && pet.id.equals(ProfileDataHandler.instance().profileData.equippedPet.id)) {
                ProfileDataHandler.instance().updatePet(pet, ProfileDataHandler.instance().profileData.equippedPetSlot);
            }
        }
    }

    public boolean isWrongPet() {
        return PetEquipHandler.instance().petStatus == PetStatus.HAS_PET
                && this.isUnequipHandled
                && this.isEquipHandled
                && ProfileDataHandler.instance().profileData.equippedPet.location != LocationHandler.instance().currentLocation
                && LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).CLIMATE != ProfileDataHandler.instance().profileData.equippedPet.climate;
    }

    public enum PetStatus {
        LOADING,
        NO_PET,
        HAS_PET
    }
}
