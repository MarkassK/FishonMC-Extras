package io.github.markassk.fishonmcextras.v1.tooltip;

import io.github.markassk.fishonmcextras.v1.handler.PetMergeCalculatorHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.v1.model_types.PetStats;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TooltipPetRating {

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
            PetStats petStats = PetStats.getStats(compound, PetMergeCalculatorHandler.getRatingFromJson(PetMergeCalculatorHandler.petToJson(itemStack)));

            if(Objects.equals(compound.getString("type"), "pet")) {
                if (!config.petTooltipToggles.showAccuratePercentage) {

                    if (config.petTooltipToggles.showIndividualRating) {
                        float petClimateLuck = petStats.getcLuck() * 4 / PetMergeCalculatorHandler.rarityMultiplier(petStats.getRarity());
                        float petClimateScale = petStats.getcScale() * 4 / PetMergeCalculatorHandler.rarityMultiplier(petStats.getRarity());
                        float petLocationLuck = petStats.getlLuck() * 4 / PetMergeCalculatorHandler.rarityMultiplier(petStats.getRarity());
                        float petLocationScale = petStats.getlScale() * 4 / PetMergeCalculatorHandler.rarityMultiplier(petStats.getRarity());

                        Text petClimateLuckLine = TextHelper.concat(textList.get(9), Text.literal(" (" + TextHelper.fmt(petClimateLuck, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petClimateScaleLine = TextHelper.concat(textList.get(10), Text.literal(" (" + TextHelper.fmt(petClimateScale, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petLocationLuckLine = TextHelper.concat(textList.get(13), Text.literal(" (" + TextHelper.fmt(petLocationLuck, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petLocationScaleLine = TextHelper.concat(textList.get(14), Text.literal(" (" + TextHelper.fmt(petLocationScale, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));


                        textList.set(9, petClimateLuckLine);
                        textList.set(10, petClimateScaleLine);
                        textList.set(13, petLocationLuckLine);
                        textList.set(14, petLocationScaleLine);
                    }

                    if (config.petTooltipToggles.showFullRating) {
                        float total = (petStats.getcLuck() + petStats.getcScale() + petStats.getlLuck() + petStats.getlScale()) / PetMergeCalculatorHandler.rarityMultiplier(petStats.getRarity());
                        Text petRatingLine = TextHelper.concat(textList.get(16), Text.literal(" (" + TextHelper.fmt(total, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));

                        textList.set(16, petRatingLine);
                    }
                } else {
                    if (config.petTooltipToggles.showIndividualRating) {
                        Text petClimateLuckLine = TextHelper.concat(textList.get(9), Text.literal(" (" + TextHelper.fmt(petStats.getcLuckPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petClimateScaleLine = TextHelper.concat(textList.get(10), Text.literal(" (" + TextHelper.fmt(petStats.getcScalePercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petLocationLuckLine = TextHelper.concat(textList.get(13), Text.literal(" (" + TextHelper.fmt(petStats.getlLuckPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));
                        Text petLocationScaleLine = TextHelper.concat(textList.get(14), Text.literal(" (" + TextHelper.fmt(petStats.getlScalePercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));

                        textList.set(9, petClimateLuckLine);
                        textList.set(10, petClimateScaleLine);
                        textList.set(13, petLocationLuckLine);
                        textList.set(14, petLocationScaleLine);
                    }

                    if (config.petTooltipToggles.showFullRating) {
                        Text petRatingLine = TextHelper.concat(textList.get(16), Text.literal(" (" + TextHelper.fmt(petStats.getTotalPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%)").formatted(Formatting.DARK_GRAY));

                        textList.set(16, petRatingLine);
                    }
                }
            }
        }
        return textList;
    }

    public static Text appendTooltipRating(Text textLine) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        String json = TextHelper.textToJson(textLine.copy());
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
                return TextHelper.jsonToText(json.replace(petStr, petStrNew));
            }
        }
        return TextHelper.jsonToText(json);
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
}
