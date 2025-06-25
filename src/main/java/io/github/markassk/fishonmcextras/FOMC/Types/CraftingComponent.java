package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class CraftingComponent extends FOMCItem {
    public final CustomModelDataComponent customModelData;

    public CraftingComponent(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").orElse(Constant.COMMON.ID)));
        this.customModelData = customModelData;
    }

    public static CraftingComponent getCraftingComponent(ItemStack itemStack, String type) {
        return new CraftingComponent(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static CraftingComponent getCraftingComponent(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type").orElse(""), Defaults.ItemTypes.CRAFTINGCOMPONENT)) {
                return CraftingComponent.getCraftingComponent(itemStack, Defaults.ItemTypes.CRAFTINGCOMPONENT);
            }
        }
        return null;
    }
}
