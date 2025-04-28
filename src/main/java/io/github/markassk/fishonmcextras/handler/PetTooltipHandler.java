package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.ordinalIndexOf;

public class PetTooltipHandler {
    private static PetTooltipHandler INSTANCE = new PetTooltipHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public static PetTooltipHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PetTooltipHandler();
        }
        return INSTANCE;
    }

    public void appendTooltip(List<Text> textList, ItemStack itemStack) {
        if(config.petTooltip.showPetPercentages) {
            if(Types.getFOMCItem(itemStack) instanceof Types.Pet pet) {
                Text petClimateLuckLine = TextHelper.concat(
                        textList.get(9),
                        getPercentage(pet.climateStat.percentLuck, config.petTooltip.decimalPlaces)
                ).formatted(Formatting.DARK_GRAY);
                Text petClimateScaleLine = TextHelper.concat(
                        textList.get(10),
                        getPercentage(pet.climateStat.percentScale, config.petTooltip.decimalPlaces)
                ).formatted(Formatting.DARK_GRAY);
                Text petLocationLuckLine = TextHelper.concat(
                        textList.get(13),
                        getPercentage(pet.locationStat.percentLuck, config.petTooltip.decimalPlaces)
                ).formatted(Formatting.DARK_GRAY);
                Text petLocationScaleLine = TextHelper.concat(
                        textList.get(14),
                        getPercentage(pet.locationStat.percentScale, config.petTooltip.decimalPlaces)
                ).formatted(Formatting.DARK_GRAY);
                Text petRatingLine = TextHelper.concat(
                        textList.get(16),
                        getPercentage(pet.percentPetRating, config.petTooltip.decimalPlaces)
                ).withColor(Types.Pet.getConstantFromPercent(pet.percentPetRating).COLOR);

                textList.set(9, petClimateLuckLine);
                textList.set(10, petClimateScaleLine);
                textList.set(13, petLocationLuckLine);
                textList.set(14, petLocationScaleLine);
                textList.set(16, petRatingLine);
            }
        }
    }

    public Text appendTooltip(Text textLine) {
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

                if (config.petTooltip.showPetPercentages) {
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 9), " (" + TextHelper.fmt((Float.parseFloat(petClimateLuck) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 10), " (" + TextHelper.fmt((Float.parseFloat(petClimateScale) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 13), " (" + TextHelper.fmt((Float.parseFloat(petLocationLuck) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 14), " (" + TextHelper.fmt((Float.parseFloat(petLocationScale) * 4 / multiplier)) + "%)").toString();
                    petStrNew = builder.insert(ordinalIndexOf(petStrNew, "\\n", 16), " (" + TextHelper.fmt((total / multiplier)) + "%)").toString();
                }

                return TextHelper.jsonToText(json.replace(petStr, petStrNew));
            }
        }
        return TextHelper.jsonToText(json);
    }

    private static Text getPercentage(float value, int decimal) {
        return TextHelper.concat(
                Text.literal(" ("),
                Text.literal(TextHelper.fmt(value * 100, decimal)),
                Text.literal("%)")
        );
    }

    private static float findMultiplier(String petStr) {
        if (petStr.indexOf('\uf033') != -1) return 1f;
        else if (petStr.indexOf('\uf034') != -1) return 2f;
        else if (petStr.indexOf('\uf035') != -1) return 3f;
        else if (petStr.indexOf('\uf036') != -1) return 5f;
        else if (petStr.indexOf('\uf037') != -1) return 7.5f;
        return 1;
    }
}
