package io.github.markassk.fishonmcextras.FOMC.Types;

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
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").get()));
        List<ArmorBonus> tempArmorBonuses;
        NbtList nbtLineList = (NbtList) nbtCompound.get("fish_bonus");
        tempArmorBonuses = new ArrayList<>();
        if(nbtLineList != null) {
            tempArmorBonuses = List.of(
                    new ArmorBonus(nbtLineList.getCompound(0).get()),
                    new ArmorBonus(nbtLineList.getCompound(1).get()),
                    new ArmorBonus(nbtLineList.getCompound(2).get()),
                    new ArmorBonus(nbtLineList.getCompound(3).get()),
                    new ArmorBonus(nbtLineList.getCompound(4).get())
            );
        }
        this.armorBonuses = tempArmorBonuses;
        this.customModelData = customModelData;
        this.color = ColorHelper.getColorFromNbt(nbtCompound.getString("rgb").get());
        this.quality = nbtCompound.getInt("quality").get();
        this.identified = nbtCompound.getBoolean("identified").get();
        this.armorPiece = nbtCompound.getString("piece").get();
        this.climate = ClimateConstant.valueOfId(nbtCompound.getString("name").get());
        this.crafter = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid").get());
        this.luck = new ArmorStat(nbtCompound.getList(
                "base"
        ).get()
                .getCompound(0).get());
        this.scale = new ArmorStat(nbtCompound.getList(
                "base"
        ).get()
                .getCompound(1).get());
        this.prospect = new ArmorStat(nbtCompound.getList(
                "base"
        ).get()
                .getCompound(2).get());
    }

    public static class ArmorBonus {
        public final int tier;
        public final boolean rolled;
        public final int rolls;
        public final boolean unlocked;
        public final float cur;
        public final String id;

        private ArmorBonus(NbtCompound nbtCompound) {
            this.tier = nbtCompound.getInt("tier").get();
            this.rolled = nbtCompound.getBoolean("rolled").get();
            this.rolls = nbtCompound.getInt("rolls").get();
            this.unlocked = nbtCompound.getBoolean("unlocked").get();
            this.cur = nbtCompound.getFloat("cur").get();
            this.id = nbtCompound.getString("id").get();
        }
    }

    public static class ArmorStat {
        public final int amount;
        public final float max;

        private ArmorStat(NbtCompound nbtCompound) {
            this.amount = nbtCompound.getInt("cur").get();
            this.max = nbtCompound.getFloat("max").get();
        }
    }

    public static Armor getArmor(ItemStack itemStack, String type) {
        return new Armor(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }
}
