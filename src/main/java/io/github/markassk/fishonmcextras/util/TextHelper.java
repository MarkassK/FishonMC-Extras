package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.regex.Pattern;

public class TextHelper {
    private static final Gson gson = new Gson();

    public static MutableText concat(Text... texts) {
        MutableText text = Text.empty();
        for (Text t : texts) {
            text.append(t);
        }
        return text;
    }

    // Format to string
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

    // Format to number string
    public static String fmnt(float d) {
        if(d > 1000 && d < 1000000) {
            return String.format("%.1fK", d / 1000);
        } else if (d > 1000000 && d < 1000000000 ){
            return String.format("%.1fM", d / 1000000);
        } else if (d > 1000000000) {
            return String.format("%.1fB", d / 1000000000);
        } else {
            return String.format("%.0f", d);
        }
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
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

    public static String upperCaseAllFirstCharacter(String text) {
        String regex = "\\b(.)(.*?)\\b";
        return Pattern.compile(regex).matcher(text).replaceAll(
                matched -> matched.group(1).toUpperCase() + matched.group(2)
        );
    }
}
