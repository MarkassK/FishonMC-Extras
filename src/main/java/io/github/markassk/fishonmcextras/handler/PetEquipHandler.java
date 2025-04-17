package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetEquipHandler  {
    private static PetEquipHandler INSTANCE = new PetEquipHandler();

    private ItemStack currentPetItem;

    public boolean isDoneScanning = false;
    public long startScanTime = 0;

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

    public void tick() {
        //TODO Find Pet Entity & Find Pet in Hotbar
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
            }
        }
    }

    private void handlePetUnequip() {
        this.currentPetItem = null;
        ProfileStatsHandler.instance().resetPet();
    }
}
