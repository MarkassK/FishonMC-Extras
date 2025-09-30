package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.ClimateConstant;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ColorHelper;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Armor extends FOMCItem {
    public final List<ArmorBonus> armorBonuses;
    public final UUID id;
    public final CustomModelDataComponent customModelData;
    public final int color;
    public final int quality;
    public final boolean identified;
    public final String armorPiece;
    public final ClimateConstant climate;
    public final UUID crafter;
    public final ArmorStat luck;
    public final ArmorStat scale;
    public final ArmorStat prospect;

    private Armor(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        List<ArmorBonus> tempArmorBonuses;
        NbtList nbtFishBonusList = (NbtList) nbtCompound.get("fish_bonus");
        tempArmorBonuses = new ArrayList<>();
        if(nbtFishBonusList != null) {
            tempArmorBonuses = List.of(
                    new ArmorBonus(nbtFishBonusList.getCompound(0)),
                    new ArmorBonus(nbtFishBonusList.getCompound(1)),
                    new ArmorBonus(nbtFishBonusList.getCompound(2)),
                    new ArmorBonus(nbtFishBonusList.getCompound(3)),
                    new ArmorBonus(nbtFishBonusList.getCompound(4))
            );
        }
        this.armorBonuses = tempArmorBonuses;
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("itemUUID"));
        this.customModelData = customModelData;
        this.color = ColorHelper.getColorFromNbt(nbtCompound.getString("rgb"));
        this.quality = nbtCompound.getInt("quality");
        this.identified = nbtCompound.getBoolean("identified");
        this.armorPiece = nbtCompound.getString("piece");
        this.climate = ClimateConstant.valueOfId(nbtCompound.getString("name"));
        this.crafter = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));

        NbtList armorStatsList = (NbtList) nbtCompound.get("base");
        if(armorStatsList != null) {
            this.luck = new ArmorStat(armorStatsList.getCompound(0));
            this.scale = new ArmorStat(armorStatsList.getCompound(1));
            this.prospect = new ArmorStat(armorStatsList.getCompound(2));
        } else {
            this.luck = new ArmorStat();
            this.scale = new ArmorStat();
            this.prospect = new ArmorStat();

        }

    }

    public static class ArmorBonus {
        public final int tier;
        public final boolean rolled;
        public final int rolls;
        public final boolean unlocked;
        public final float cur;
        public final String id;

        private ArmorBonus(NbtCompound nbtCompound) {
            this.tier = nbtCompound.getInt("tier");
            this.rolled = nbtCompound.getBoolean("rolled");
            this.rolls = nbtCompound.getInt("rolls");
            this.unlocked = nbtCompound.getBoolean("unlocked");
            this.cur = nbtCompound.getFloat("cur");
            this.id = nbtCompound.getString("id");
        }
    }

    public static class ArmorStat {
        public final int amount;
        public final float max;

        private ArmorStat(NbtCompound nbtCompound) {
            this.amount = nbtCompound.getInt("cur");
            this.max = nbtCompound.getFloat("max");
        }

        private ArmorStat() {
            this.amount = 0;
            this.max = 0;
        }
    }

    public static Armor getArmor(ItemStack itemStack, String type) {
        return new Armor(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Armor getArmor(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.LORE) != null
                && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem")) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type"), Defaults.ItemTypes.ARMOR)) {
                return Armor.getArmor(itemStack, Defaults.ItemTypes.ARMOR);
            }
        }
        return null;
    }
}
