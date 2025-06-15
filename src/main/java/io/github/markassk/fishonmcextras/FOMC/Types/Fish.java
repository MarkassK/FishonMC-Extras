package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").orElse(Constant.COMMON.ID)));
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id").orElse(new int[]{0}));
        this.customModelData = customModelData;
        this.fishId = nbtCompound.getString("fish").orElse(null);
        this.scientific = nbtCompound.getString("scientific").orElse(null);
        this.variant = Constant.valueOfId(nbtCompound.getString("variant").orElse(Constant.NORMAL.ID));
        this.value = nbtCompound.getFloat("value").orElse(0f);
        this.xp = nbtCompound.getFloat("xp").orElse(0f);
        this.natureId = nbtCompound.getString("nature").orElse(null);
        this.location = Constant.valueOfId(nbtCompound.getString("location").orElse(null));
        this.size = Constant.valueOfId(nbtCompound.getString("size").orElse(null));
        this.sex = nbtCompound.getString("sex").orElse(null);
        this.weight = nbtCompound.getFloat("weight").orElse(0f);
        this.length = nbtCompound.getFloat("length").orElse(0f);
        this.groupId = nbtCompound.getString("group").orElse(null);
        this.lifestyleId = nbtCompound.getString("lifestyle").orElse(null);
        this.ecosystem = nbtCompound.getString("native").orElse(null);
        this.migrationId = nbtCompound.getString("migration").orElse(null);
        this.catcherName = name;
        this.catcher = UUIDHelper.getUUID(nbtCompound.getIntArray("catcher").orElse(new int[]{0}));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.date = LocalDate.parse(nbtCompound.getString("date").orElse("01/01/2000"), formatter);
        this.rodName = nbtCompound.getString("rod").orElse(null);
    }

    public static Fish getFish(ItemStack itemStack, String type, String name) {
        return new Fish(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA), name);
    }

    public static Fish getFish(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            if (itemStack.getItem() == Items.COD
                    || itemStack.getItem() == Items.WHITE_DYE
                    || itemStack.getItem() == Items.BLACK_DYE
                    || itemStack.getItem() == Items.GOLD_INGOT
            ) {
                String line = Objects.requireNonNull(itemStack.getComponents().get(DataComponentTypes.LORE)).lines().get(15).getString();
                return Fish.getFish(itemStack, Defaults.ItemTypes.FISH, line.substring(line.lastIndexOf(" ") + 1));
            }
        }
        return null;
    }
}
