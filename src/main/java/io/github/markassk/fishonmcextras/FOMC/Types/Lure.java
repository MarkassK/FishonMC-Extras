package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.name = nbtCompound.getString("name");
        this.customModelData = customModelData;
        this.counter = nbtCompound.getInt("counter");
        this.water = Constant.valueOfId(nbtCompound.getString("water"));
        this.intricacy = nbtCompound.getString("intricacy");
        NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i));
        }
        this.lureStats = nbtCompoundList.stream().map(LureStats::new).toList();
        this.totalUses = nbtCompound.getInt("totalUses");
        this.size = nbtCompound.getString("size");
    }

    public static class LureStats {
        public final int cur;
        public final String id;

        private LureStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur");
            this.id = nbtCompound.getString("id");
        }
    }

    public static Lure getLure(ItemStack itemStack, String type) {
        return new Lure(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }
}
