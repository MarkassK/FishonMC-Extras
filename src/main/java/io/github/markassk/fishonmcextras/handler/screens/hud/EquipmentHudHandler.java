package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.handler.ArmorHandler;
import io.github.markassk.fishonmcextras.handler.FishingRodHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EquipmentHudHandler {
    private static EquipmentHudHandler INSTANCE = new EquipmentHudHandler();

    public static EquipmentHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentHudHandler();
        }
        return INSTANCE;
    }

    public ItemStack getChestPlate() {
        if(ArmorHandler.instance().currentChestplateItem.getItem() != Items.AIR) {
            return ArmorHandler.instance().currentChestplateItem;
        }
        return Items.AIR.getDefaultStack();
    }

    public ItemStack getLeggings() {
        if(ArmorHandler.instance().currentLeggingsItem.getItem() != Items.AIR) {
            return ArmorHandler.instance().currentLeggingsItem;
        }
        return Items.AIR.getDefaultStack();
    }

    public ItemStack getBoots() {
        if(ArmorHandler.instance().currentBootsItem.getItem() != Items.AIR) {
            return ArmorHandler.instance().currentBootsItem;
        }
        return Items.AIR.getDefaultStack();
    }

    public ItemStack getReel() {
        if(FishingRodHandler.instance().fishingRod.reel != null) {
            ItemStack itemStack = Items.FLINT.getDefaultStack().copy();
            itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, FishingRodHandler.instance().fishingRod.reel.customModelData);
            return itemStack;
        }
        return Items.AIR.getDefaultStack();
    }

    public ItemStack getPole() {
        if(FishingRodHandler.instance().fishingRod.pole != null) {
            ItemStack itemStack = Items.BLAZE_ROD.getDefaultStack().copy();
            itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA,FishingRodHandler.instance().fishingRod.pole.customModelData);
            return itemStack;
        }
        return Items.AIR.getDefaultStack();
    }

    public ItemStack getLine() {
        if(FishingRodHandler.instance().fishingRod.line != null) {
            ItemStack itemStack = Items.FEATHER.getDefaultStack().copy();
            itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, FishingRodHandler.instance().fishingRod.line.customModelData);
            return itemStack;
        }
        return Items.AIR.getDefaultStack();
    }
}
