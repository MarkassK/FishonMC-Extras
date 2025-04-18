package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types;
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
    }

    public void tick(MinecraftClient minecraftClient) {
        if(ProfileStatsHandler.instance().profileStats.equippedPetSlot == -1) {
            petStatus = PetStatus.NO_PET;
        } else if (System.currentTimeMillis() - startScanTime < 10000
                && LoadingHandler.instance().isLoadingDone
                && petStatus == PetStatus.LOADING
        ) {
            if(minecraftClient.player != null && !isInInventory) {
                ItemStack itemInSlot = minecraftClient.player.getInventory().getStack(ProfileStatsHandler.instance().profileStats.equippedPetSlot);

                if(Types.getFOMCItem(itemInSlot) instanceof Types.Pet pet && pet.id.equals(ProfileStatsHandler.instance().profileStats.equippedPet.id)) {
                    isInInventory = true;
                    currentPetItem = itemInSlot;
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
            petStatus = PetStatus.NO_PET;
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
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null) {
            int itemSlot = player.getInventory().selectedSlot;
            ItemStack heldItem = player.getInventory().getStack(itemSlot);

            if(Types.getFOMCItem(heldItem) instanceof Types.Pet pet) {
                this.currentPetItem = heldItem;
                ProfileStatsHandler.instance().updatePet(pet, itemSlot);
                petStatus = PetStatus.HAS_PET;

                FishOnMCExtras.LOGGER.info("[FoE] Equipped Pet");
            }
        }
    }

    private void handlePetUnequip() {
        this.currentPetItem = null;
        petStatus = PetStatus.NO_PET;
        ProfileStatsHandler.instance().resetPet();

        FishOnMCExtras.LOGGER.info("[FoE] Unequipped Pet");
    }

    public void updatePet(PlayerEntity player) {
        if(petStatus == PetStatus.HAS_PET) {
            ItemStack itemInSlot = player.getInventory().getStack(ProfileStatsHandler.instance().profileStats.equippedPetSlot);

            if(Types.getFOMCItem(itemInSlot) instanceof Types.Pet pet && pet.id.equals(ProfileStatsHandler.instance().profileStats.equippedPet.id)) {
                ProfileStatsHandler.instance().updatePet(pet, ProfileStatsHandler.instance().profileStats.equippedPetSlot);
            }
        }
    }

    public enum PetStatus {
        LOADING,
        NO_PET,
        HAS_PET
    }
}
