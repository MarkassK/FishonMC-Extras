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

public class Bait extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final int counter;
    public final Constant water;
    public final String intricacy;
    public final List<BaitStats> baitStats;

    private Bait(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").orElse(Constant.COMMON.ID)));
        this.name = nbtCompound.getString("name").orElse(null);
        this.customModelData = customModelData;
        this.counter = nbtCompound.getInt("counter").orElse(0);
        this.water = Constant.valueOfId(nbtCompound.getString("water").orElse(Constant.FRESHWATER.ID));
        this.intricacy = nbtCompound.getString("intricacy").orElse(null);
        NbtList nbtList = nbtCompound.getList("base").orElse(new NbtList());
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i).orElse(new NbtCompound()));
        }
        this.baitStats = nbtCompoundList.stream().map(BaitStats::new).toList();
    }

    public static class BaitStats {
        public final int cur;
        public final String id;

        private BaitStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur").orElse(0);
            this.id = nbtCompound.getString("id").orElse(null);
        }
    }

    public static Bait getBait(ItemStack itemStack, String type) {
        return new Bait(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Bait getBait(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type").orElse(""), Defaults.ItemTypes.BAIT)) {
                return Bait.getBait(itemStack, Defaults.ItemTypes.BAIT);
            }
        }
        return null;
    }
}
