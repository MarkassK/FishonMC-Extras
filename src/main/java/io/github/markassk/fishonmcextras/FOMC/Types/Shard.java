package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class Shard extends FOMCItem {
    public final String climateId;
    public final CustomModelDataComponent customModelData;
    public final Constant rarity;

    private Shard(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.DEFAULT);
        this.climateId = nbtCompound.getString("name");
        this.customModelData = customModelData;
        this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
    }

    public static Shard getShard(ItemStack itemStack, String type) {
        return new Shard(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Shard getShard(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem")) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type"), Defaults.ItemTypes.SHARD)) {
                return Shard.getShard(itemStack, Defaults.ItemTypes.SHARD);
            }
        }
        return null;
    }
}
