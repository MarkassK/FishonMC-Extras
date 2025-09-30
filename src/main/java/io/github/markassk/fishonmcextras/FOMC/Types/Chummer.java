package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class Chummer extends FOMCItem {
    public final float timer;
    public final float bitespeed;
    public final CustomModelDataComponent customModelData;
    public final Constant rarity;

    private Chummer(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.DEFAULT);
        this.timer = nbtCompound.getFloat("timer").orElse(0F);
        this.bitespeed = nbtCompound.getFloat("bitespeed").orElse(0F);
        this.customModelData = customModelData;
        this.rarity = Constant.valueOfId(nbtCompound.getString("rarity").orElse(Constant.COMMON.ID));
    }

    public static Chummer getChummer(ItemStack itemStack, String type) {
        return new Chummer(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Chummer getChummer(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.LORE) != null
                && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type").orElse(""), Defaults.ItemTypes.CHUMMER)) {
                return Chummer.getChummer(itemStack, Defaults.ItemTypes.CHUMMER);
            }
        }
        return null;
    }
}
