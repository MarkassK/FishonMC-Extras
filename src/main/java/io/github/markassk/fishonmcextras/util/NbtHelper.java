package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtCompound;

public class NbtHelper {

    private static final Gson gson = new Gson();

    public static String nbtCompoundToJson(NbtCompound nbtCompound) {
        String json = gson.toJson(NbtCompound.CODEC.encodeStart(JsonOps.INSTANCE, nbtCompound).getOrThrow());
        System.out.println(json);
        json = json.replace("\"show_in_tooltip\":0", "\"show_in_tooltip\":false");
        json = json.replace("\"show_in_tooltip\":1", "\"show_in_tooltip\":true");
        json = json.replace(",\"minecraft:hide_additional_tooltip\":{}", "");
        json = json.replace(",\"modifiers\":[]", "");
        json = json.replace("{\"show_in_tooltip\":false}", "[]");
        json = json.replace("{\"show_in_tooltip\":true}", "[]");
        return json;
    }

    public static NbtCompound jsonToNbtCompound(String json) {
        return NbtCompound.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(json, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }
}
