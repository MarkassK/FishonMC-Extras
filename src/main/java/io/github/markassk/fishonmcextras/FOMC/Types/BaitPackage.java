package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;
import java.util.UUID;

public class BaitPackage extends FOMCItem{
    public final CustomModelDataComponent customModelData;
    public final Constant location;
    public final UUID id;
    public final String intricacy;

    public BaitPackage(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.customModelData = customModelData;
        this.location = Constant.valueOfId(nbtCompound.getString("location"));
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
        this.intricacy = nbtCompound.getString("intricacy");
    }

    public static BaitPackage getBaitPackage(ItemStack itemStack, String type) {
        return new BaitPackage(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static BaitPackage getBaitPackage(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.LORE) != null
                && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem")) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type"), Defaults.ItemTypes.BAITPACKAGE)) {
                return BaitPackage.getBaitPackage(itemStack, Defaults.ItemTypes.BAITPACKAGE);
            }
        }
        return null;
    }
}
