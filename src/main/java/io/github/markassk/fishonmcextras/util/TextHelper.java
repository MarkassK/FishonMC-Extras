package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.markassk.fishonmcextras.FOMC.Constants;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class TextHelper {
    private static final Gson gson = new Gson();

    public static Text concat(Text... texts) {
        MutableText text = Text.empty();
        for (Text t : texts) {
            text.append(t);
        }
        return text;
    }

    public static String fmt(float d)
    {
        return String.format("%.0f", d);
    }

    public static String fmt(float d, int decimalPlaces) {
        switch (decimalPlaces) {
            case 1 -> {
                return String.format("%.1f", d);
            }
            case 2 -> {
                return String.format("%.2f", d);
            }
            default -> {
                return String.format("%.0f", d);
            }
        }
    }

    public static String capitalize(String str) {
        if(str == null || str.length()<=1) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String textToJson(Text text) {
        return gson.toJson(TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text).getOrThrow());
    }

    public static Text jsonToText(String text) {
        return TextCodecs.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(text, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }

    public static String convertRarity(String rarity) {
        return switch(rarity) {
            case Constants.Identifier.COMMON -> Constants.Tag.COMMON;
            case Constants.Identifier.RARE -> Constants.Tag.RARE;
            case Constants.Identifier.EPIC -> Constants.Tag.EPIC;
            case Constants.Identifier.LEGENDARY -> Constants.Tag.LEGENDARY;
            case Constants.Identifier.MYTHICAL -> Constants.Tag.MYTHICAL;
            default -> Constants.Identifier.DEFAULT;
        };
    }
}
