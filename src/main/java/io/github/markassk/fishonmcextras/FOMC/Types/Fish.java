package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Fish extends FOMCItem {
    public final UUID id; // id
    public final CustomModelDataComponent customModelData;
    public final String fishId; // fish
    public final String scientific; // scientific
    public final Constant variant; // variant

    public final float value; // value
    public final float xp;
    public final String natureId; // nature
    public final Constant location; // location
    public final Constant size; // size
    public final String sex; // sex
    public final float weight; // weight in lb
    public final float length; // length in in

    public final String groupId; // group
    public final String lifestyleId; // lifestyle
    public final String ecosystem; // ecosystem
    public final String migrationId; // migration

    public final String catcherName; // Tooltip Name
    public final UUID catcher; // catcher
    public final LocalDate date; // date
    public final String rodName; // rod

    private Fish(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData, String name) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
        this.customModelData = customModelData;
        this.fishId = nbtCompound.getString("fish");
        this.scientific = nbtCompound.getString("scientific");
        this.variant = Constant.valueOfId(nbtCompound.getString("variant"));
        this.value = nbtCompound.getFloat("value");
        this.xp = nbtCompound.getFloat("xp");
        this.natureId = nbtCompound.getString("nature");
        this.location = Constant.valueOfId(nbtCompound.getString("location"));
        this.size = Constant.valueOfId(nbtCompound.getString("size"));
        this.sex = nbtCompound.getString("sex");
        this.weight = nbtCompound.getFloat("weight");
        this.length = nbtCompound.getFloat("length");
        this.groupId = nbtCompound.getString("group");
        this.lifestyleId = nbtCompound.getString("lifestyle");
        this.ecosystem = nbtCompound.getString("native");
        this.migrationId = nbtCompound.getString("migration");
        this.catcherName = name;
        this.catcher = UUIDHelper.getUUID(nbtCompound.getIntArray("catcher"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.date = LocalDate.parse(nbtCompound.getString("date"), formatter);
        this.rodName = nbtCompound.getString("rod");
    }

    public static Fish getFish(ItemStack itemStack, String type, String name) {
        return new Fish(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA), name);
    }
}
