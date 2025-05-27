package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.NbtHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FishingRod extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final boolean soulboundRod;
    public final String skin;
    public final UUID owner;
    public final List<FOMCItem> tacklebox;
    public final Line line;
    public final Pole pole;
    public final Reel reel;

    private FishingRod(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData, String name) {
        super(type, Constant.DEFAULT);
        this.name = name;
        this.customModelData = customModelData;
        this.soulboundRod = nbtCompound.getBoolean("soulbound_rod").get();
        this.skin = nbtCompound.getString("skin").get();
        this.owner = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid").get());

        if(nbtCompound.get("tacklebox") instanceof NbtList nbtList) {
            this.tacklebox = nbtList.stream().map(nbtElement -> {
                if (nbtElement instanceof NbtCompound compound) {
                    ItemStack baitStack = ItemStackHelper.jsonToItemStack(NbtHelper.nbtCompoundToJson(compound));
                    Lure lure = Lure.getLure(baitStack);
                    Bait bait = Bait.getBait(baitStack);
                    if(lure != null) return lure;
                    return bait;
                } return null;
            }).filter(Objects::nonNull).toList();
        } else this.tacklebox = new ArrayList<>();

        if(nbtCompound.get("line") instanceof NbtList nbtList) {
            this.line = nbtList.stream().map(nbtElement -> {
                if(nbtElement instanceof NbtCompound compound) {
                    ItemStack lineStack = ItemStackHelper.jsonToItemStack(NbtHelper.nbtCompoundToJson(compound));
                    return Line.getLine(lineStack);
                } return null;
            }).filter(Objects::nonNull).findFirst().orElse(null);
        } else this.line = null;

        if(nbtCompound.get("pole") instanceof NbtList nbtList) {
            this.pole = nbtList.stream().map(nbtElement -> {
                if(nbtElement instanceof NbtCompound compound) {
                    ItemStack poleStack = ItemStackHelper.jsonToItemStack(NbtHelper.nbtCompoundToJson(compound));
                    return Pole.getPole(poleStack);
                } return null;
            }).filter(Objects::nonNull).findFirst().orElse(null);
        } else this.pole = null;

        if(nbtCompound.get("reel") instanceof NbtList nbtList) {
            this.reel = nbtList.stream().map(nbtElement -> {
                if(nbtElement instanceof NbtCompound compound) {
                    ItemStack reelStack = ItemStackHelper.jsonToItemStack(NbtHelper.nbtCompoundToJson(compound));
                    return Reel.getReel(reelStack);
                } return null;
            }).filter(Objects::nonNull).findFirst().orElse(null);
        } else this.reel = null;
    }

    public static FishingRod getFishingRod(ItemStack itemStack, String type, String name) {
        return new FishingRod(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA), name);
    }

    public static FishingRod getFishingRod(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem").orElse(false)) {
            if (itemStack.getItem() == Items.FISHING_ROD && Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("soulbound_rod").isPresent()) {
                return FishingRod.getFishingRod(itemStack, Defaults.ItemTypes.FISHINGROD, itemStack.getName().getString());
            }
        }
        return null;
    }
}
