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

public class Line extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final Constant water;
    public final List<LineStats> lineStats;

    private Line(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity").orElse(Constant.COMMON.ID)));
        this.name = nbtCompound.getString("name").orElse(null);
        this.customModelData = customModelData;
        this.water = Constant.valueOfId(nbtCompound.getString("water").orElse(Constant.FRESHWATER.ID));
        NbtList nbtList = nbtCompound.getList("base").orElse(new NbtList());
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i).orElse(new NbtCompound()));
        }
        this.lineStats = nbtCompoundList.stream().map(LineStats::new).toList();
    }

    public static class LineStats {
        public final int cur;
        public final String id;

        private LineStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur").orElse(0);
            this.id = nbtCompound.getString("id").orElse(null);
        }
    }

    public static Line getLine(ItemStack itemStack, String type) {
        return new Line(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Line getLine(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.LORE) != null
                && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type").orElse(""), Defaults.ItemTypes.LINE)) {
                return Line.getLine(itemStack, Defaults.ItemTypes.LINE);
            }
        }
        return null;
    }
}
