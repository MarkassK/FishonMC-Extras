package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LocationInfo;
import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Objects;

public class ArmorHandler {
    private static ArmorHandler INSTANCE = new ArmorHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public ItemStack currentChestplateItem = Items.AIR.getDefaultStack();
    public ItemStack currentLeggingsItem = Items.AIR.getDefaultStack();
    public ItemStack currentBootsItem = Items.AIR.getDefaultStack();
    public Types.Armor currentChestplate = null;
    public Types.Armor currentLeggings = null;
    public Types.Armor currentBoots = null;
    public boolean isWrongChestplateClimate = false;
    public boolean isWrongLeggingsClimate = false;
    public boolean isWrongBootsClimate = false;

    public static ArmorHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ArmorHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        assert minecraftClient.player != null;

        if(minecraftClient.player.getInventory().armor.get(EquipmentSlot.FEET.getEntitySlotId()).getItem() == Items.LEATHER_BOOTS && Types.getFOMCItem(minecraftClient.player.getInventory().armor.get(EquipmentSlot.FEET.getEntitySlotId())) instanceof Types.Armor armor) {
            this.currentBootsItem = minecraftClient.player.getInventory().armor.get(EquipmentSlot.FEET.getEntitySlotId());
            this.currentBoots = armor;
        } else {
            this.currentBootsItem = Items.AIR.getDefaultStack();
            this.currentBoots = null;
        }

        if(minecraftClient.player.getInventory().armor.get(EquipmentSlot.LEGS.getEntitySlotId()).getItem() == Items.LEATHER_LEGGINGS && Types.getFOMCItem(minecraftClient.player.getInventory().armor.get(EquipmentSlot.LEGS.getEntitySlotId())) instanceof Types.Armor armor) {
            this.currentLeggingsItem = minecraftClient.player.getInventory().armor.get(EquipmentSlot.LEGS.getEntitySlotId());
            this.currentLeggings = armor;
        } else {
            this.currentLeggingsItem = Items.AIR.getDefaultStack();
            this.currentLeggings = null;
        }

        if(minecraftClient.player.getInventory().armor.get(EquipmentSlot.CHEST.getEntitySlotId()).getItem() == Items.LEATHER_CHESTPLATE && Types.getFOMCItem(minecraftClient.player.getInventory().armor.get(EquipmentSlot.CHEST.getEntitySlotId())) instanceof Types.Armor armor) {
            this.currentChestplateItem = minecraftClient.player.getInventory().armor.get(EquipmentSlot.CHEST.getEntitySlotId());
            this.currentChestplate = armor;
        } else {
            this.currentChestplateItem = Items.AIR.getDefaultStack();
            this.currentChestplate = null;
        }

        if(LocationHandler.instance().currentLocation != Constant.DEFAULT && LocationHandler.instance().currentLocation != Constant.CREW_ISLAND) {
            String currentLocationClimate = LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).CLIMATE.ID;
            this.isWrongChestplateClimate = currentChestplate != null && !Objects.equals(currentChestplate.climate.ID, currentLocationClimate);
            this.isWrongLeggingsClimate = currentLeggings != null && !Objects.equals(currentLeggings.climate.ID, currentLocationClimate);
            this.isWrongBootsClimate = currentBoots != null && !Objects.equals(currentBoots.climate.ID, currentLocationClimate);
        }
    }
}
