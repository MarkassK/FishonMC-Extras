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
        this.climateId = nbtCompound.getString("name").get();
        this.customModelData = customModelData;
        this.rarity = Constant.valueOfId(nbtCompound.getString("rarity").get());
    }

    public static Shard getShard(ItemStack itemStack, String type) {
        return new Shard(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }
}
