package io.github.markassk.fishonmcextras.common.Tooltip;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TooltipPetRating {
    private static final Gson gson = new Gson();

    private static float findMultiplier(String petStr) {
        if (petStr.indexOf('\uf033') != -1) return 1f;
        else if (petStr.indexOf('\uf034') != -1) return 2f;
        else if (petStr.indexOf('\uf035') != -1) return 3f;
        else if (petStr.indexOf('\uf036') != -1) return 5f;
        else if (petStr.indexOf('\uf037') != -1) return 7.5f;
        return 1;
    }

    public static List<Text> appendTooltipRating(List<Text> textList, ItemStack itemStack) {

        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        if(textList.size() >= 3 && textList.get(1).getString().contains(" Pet") && textList.get(3).getString().contains(" ᴘᴇᴛ") && itemStack.getItem() == Items.PLAYER_HEAD && itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
            NbtCompound compound = itemStack.get(DataComponentTypes.CUSTOM_DATA).getNbt();

            if(Objects.equals(compound.getString("type"), "pet")) {
                String petClimateLuck = getMaxFromString(textList.get(9).copy().getString());
                String petClimateScale = getMaxFromString(textList.get(10).copy().getString());
                String petLocationLuck = getMaxFromString(textList.get(13).copy().getString());
                String petLocationScale = getMaxFromString(textList.get(14).copy().getString());

                float multiplier = findMultiplier(textList.get(2).getString());
                float total = Stream.of(petClimateLuck, petClimateScale, petLocationLuck, petLocationScale).mapToInt(Integer::parseInt).sum();

                if (config.petTooltipToggles.showIndividualRating) {
                    Text petClimateLuckLine = appendRating(textList.get(9), Float.parseFloat(petClimateLuck), multiplier, 4, "\",\"italic\"", 3);
                    Text petClimateScaleLine = appendRating(textList.get(10), Float.parseFloat(petClimateScale), multiplier, 4, "\",\"italic\"", 3);
                    Text petLocationLuckLine = appendRating(textList.get(13), Float.parseFloat(petLocationLuck), multiplier, 4, "\",\"italic\"", 3);
                    Text petLocationScaleLine = appendRating(textList.get(14), Float.parseFloat(petLocationScale), multiplier, 4, "\",\"italic\"", 3);

                    textList.set(9, petClimateLuckLine);
                    textList.set(10, petClimateScaleLine);
                    textList.set(13, petLocationLuckLine);
                    textList.set(14, petLocationScaleLine);
                }

                if (config.petTooltipToggles.showFullRating) {
                    Text petRatingLine = appendRating(textList.get(16), total, multiplier, 1, "\",\"italic\"", 2);

                    textList.set(16, petRatingLine);
                }
            }
        }
        return textList;
    }

    public static Text appendTooltipRating(Text textLine) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        String json = textToJson(textLine.copy());
        if (json.contains("ᴘᴇᴛ ʀᴀᴛɪɴɢ")) {
            String petStr = json.substring(json.indexOf(" Pet\\n"), json.indexOf("ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴏᴘᴇɴ ᴘᴇᴛ ᴍᴇɴᴜ"));
            Pattern statNumber = Pattern.compile("(?<=\\+)(.*?)(?=\")");
            Matcher statNumberMatcher = statNumber.matcher(petStr);

            if(statNumberMatcher.find()) {
                List<String> matches = statNumberMatcher.results().map(MatchResult::group).toList();

                String petClimateLuck = matches.get(matches.size() - 7);
                String petClimateScale = matches.get(matches.size() - 5);
                String petLocationLuck = matches.get(matches.size() - 3);
                String petLocationScale = matches.getLast();

                float multiplier = findMultiplier(petStr);
                float total = Stream.of(petClimateLuck, petClimateScale, petLocationLuck, petLocationScale).mapToInt(Integer::parseInt).sum();

                StringBuilder builder = new StringBuilder(petStr);
                String petStrNew = builder.toString();

                if (config.petTooltipToggles.showIndividualRating) {
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 9), " (" + String.format("%.0f", (Float.parseFloat(petClimateLuck) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 10), " (" + String.format("%.0f", (Float.parseFloat(petClimateScale) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 13), " (" + String.format("%.0f", (Float.parseFloat(petLocationLuck) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 14), " (" + String.format("%.0f", (Float.parseFloat(petLocationScale) * 4 / multiplier)) + "%)").toString();
                }

                if (config.petTooltipToggles.showFullRating) {
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 16), " (" + String.format("%.0f", (total / multiplier)) + "%)").toString();
                }
                return jsonToText(json.replace(petStr, petStrNew));
            }
        }
        return jsonToText(json);
    }

    private static Text appendRating(Text line, float rating, float rarityMultiplier, float extraMultiplier, String substr, int occurrence) {
        String json = textToJson(line);
        StringBuilder builder = new StringBuilder(json);
        float ratingPercentage = rating * extraMultiplier / rarityMultiplier;
        String newJson = builder.insert(ordinalIndexOf(json, substr, occurrence), " (" + String.format("%.0f", ratingPercentage) + "%)").toString();

        return jsonToText(newJson);
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    private static String getMaxFromString(String str) {
        return str.substring(str.indexOf("/") + 1, str.indexOf(")"));
    }

    private static String textToJson(Text text) {
        return gson.toJson(TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text).getOrThrow());
    }

    private static Text jsonToText(String text) {
        return TextCodecs.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(text, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }
}
