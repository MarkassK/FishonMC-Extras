package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

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
                .getOrThrow()
                .getFirst();
    }

    public static int getCustomModelData(ItemStack stack) {
        if(stack.get(DataComponentTypes.CUSTOM_MODEL_DATA) != null) {
            return Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_MODEL_DATA)).value();
        }
        return -1;
    }
}
