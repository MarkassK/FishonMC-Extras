package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

public class ItemStackHelper {
    private static final Gson gson = new Gson();

    public static NbtCompound getNbt(ItemStack stack) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        return component != null ? component.copyNbt() : null;
    }

    public static String itemStackToJson(ItemStack itemStack) {
        return gson.toJson(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, itemStack).getOrThrow());
    }

    public static ItemStack jsonToItemStack(String json) {
        return ItemStack.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(json, JsonElement.class))
                .mapOrElse((Pair::getFirst), (pairError -> Items.STICK.getDefaultStack()));
    }
}
