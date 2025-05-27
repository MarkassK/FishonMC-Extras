package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class FOMCItem {
    public final String type;
    public final Constant rarity;

    public FOMCItem(String type, Constant rarity) {
        this.type = type;
        this.rarity = rarity;
    }

    public static FOMCItem getFOMCItem(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null && ItemStackHelper.getNbt(itemStack) != null && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")) {
                // Check for types
                return switch (nbtCompound.getString("type").orElse("null")) {
                    case Defaults.ItemTypes.PET -> Pet.getPet(itemStack, Defaults.ItemTypes.PET);
                    case Defaults.ItemTypes.SHARD -> Shard.getShard(itemStack, Defaults.ItemTypes.SHARD);
                    case Defaults.ItemTypes.ARMOR -> Armor.getArmor(itemStack, Defaults.ItemTypes.ARMOR);
                    case Defaults.ItemTypes.BAIT -> Bait.getBait(itemStack, Defaults.ItemTypes.BAIT);
                    case Defaults.ItemTypes.LURE -> Lure.getLure(itemStack, Defaults.ItemTypes.LURE);
                    case Defaults.ItemTypes.LINE -> Line.getLine(itemStack, Defaults.ItemTypes.LINE);
                    case Defaults.ItemTypes.POLE -> Pole.getPole(itemStack, Defaults.ItemTypes.POLE);
                    case Defaults.ItemTypes.REEL -> Reel.getReel(itemStack, Defaults.ItemTypes.REEL);
                    default -> null;
                };
                // Fish
            } else if (itemStack.getItem() == Items.COD
                    || itemStack.getItem() == Items.WHITE_DYE
                    || itemStack.getItem() == Items.BLACK_DYE
                    || itemStack.getItem() == Items.GOLD_INGOT
            ) {
                String line = Objects.requireNonNull(itemStack.getComponents().get(DataComponentTypes.LORE)).lines().get(15).getString();
                return Fish.getFish(itemStack, Defaults.ItemTypes.FISH, line.substring(line.lastIndexOf(" ") + 1));
            } else if (itemStack.getItem() == Items.FISHING_ROD && Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("soulbound_rod").isPresent()) {
                return FishingRod.getFishingRod(itemStack, Defaults.ItemTypes.FISHINGROD, itemStack.getName().getString());
            }
        }
        return null;
    }

    public static boolean isFOMCItem(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")) {
                // Check for types
                return switch (nbtCompound.getString("type").orElse("null")) {
                    case Defaults.ItemTypes.PET, Defaults.ItemTypes.REEL, Defaults.ItemTypes.POLE,
                         Defaults.ItemTypes.LINE, Defaults.ItemTypes.LURE, Defaults.ItemTypes.BAIT,
                         Defaults.ItemTypes.ARMOR, Defaults.ItemTypes.SHARD -> true;
                    default -> false;
                };
                // Fish
            } else {
                return itemStack.getItem() == Items.COD
                        || itemStack.getItem() == Items.WHITE_DYE
                        || itemStack.getItem() == Items.BLACK_DYE
                        || itemStack.getItem() == Items.GOLD_INGOT
                        || itemStack.getItem() == Items.FISHING_ROD;
            }
        }
        return false;
    }
}