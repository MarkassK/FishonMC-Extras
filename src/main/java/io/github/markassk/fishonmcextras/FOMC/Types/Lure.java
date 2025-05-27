package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lure extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final int totalUses;
    public final int counter;
    public final Constant water;
    public final String intricacy;
    public final List<LureStats> lureStats;
    public final String size;

    private Lure(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").get()));
        this.name = nbtCompound.getString("name").get();
        this.customModelData = customModelData;
        this.counter = nbtCompound.getInt("counter").get();
        this.water = Constant.valueOfId(nbtCompound.getString("water").get());
        this.intricacy = nbtCompound.getString("intricacy").get();
        NbtList nbtList = nbtCompound.getList("base").get();
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i).get());
        }
        this.lureStats = nbtCompoundList.stream().map(LureStats::new).toList();
        this.totalUses = nbtCompound.getInt("totalUses").get();
        this.size = nbtCompound.getString("size").get();
    }

    public static class LureStats {
        public final int cur;
        public final String id;

        private LureStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur").get();
            this.id = nbtCompound.getString("id").get();
        }
    }

    public static Lure getLure(ItemStack itemStack, String type) {
        return new Lure(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Lure getLure(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type").orElse(""), Defaults.ItemTypes.LURE)) {
                return Lure.getLure(itemStack, Defaults.ItemTypes.LURE);
            }
        }
        return null;
    }
}
