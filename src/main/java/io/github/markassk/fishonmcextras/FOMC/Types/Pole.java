package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
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

public class Pole extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final UUID id;
    public final Constant water;
    public final List<PoleStats> poleStats;

    private Pole(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.name = nbtCompound.getString("name");
        this.customModelData = customModelData;
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
        this.water = Constant.valueOfId(nbtCompound.getString("water"));
        NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i));
        }
        this.poleStats = nbtCompoundList.stream().map(PoleStats::new).toList();
    }

    public static class PoleStats {
        public final int cur;
        public final String id;

        private PoleStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur");
            this.id = nbtCompound.getString("id");
        }
    }

    public static Pole getPole(ItemStack itemStack, String type) {
        return new Pole(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }
}
