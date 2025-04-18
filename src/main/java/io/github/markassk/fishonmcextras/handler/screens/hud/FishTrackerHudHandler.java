package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constants;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ProfileStatsHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FishTrackerHudHandler {
    private static FishTrackerHudHandler INSTANCE = new FishTrackerHudHandler();

    public static FishTrackerHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishTrackerHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleFishText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        ProfileStatsHandler.ProfileStats profileStats = ProfileStatsHandler.instance().profileStats;
        List<Text> textList = new ArrayList<>();

        long timeSinceReset = ProfileStatsHandler.instance().profileStats.activeTime;

        // All-time or not display stat strings
        int displayFishCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.fishCaughtCount
                : profileStats.allFishCaughtCount;
        float displayTotalXp = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.totalXP
                : profileStats.allTotalXP;
        float displayTotalValue = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.totalValue
                : profileStats.allTotalValue;
        int displayPetCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.petCaughtCount
                : profileStats.allPetCaughtCount;
        int displayShardCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.shardCaughtCount
                : profileStats.allShardCaughtCount;
        Map<String, Integer> displayRarityCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.rarityCounts
                : profileStats.allRarityCounts;
        Map<String, Integer> displayFishSizeCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.fishSizeCounts
                : profileStats.allFishSizeCounts;
        Map<String, Integer> displayVariantCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileStats.variantCounts
                : profileStats.allVariantCounts;
        int displayCommonCount = displayRarityCounts.getOrDefault(Constants.Identifier.COMMON, 0);
        int displayRareCount = displayRarityCounts.getOrDefault(Constants.Identifier.RARE, 0);
        int displayEpicCount = displayRarityCounts.getOrDefault(Constants.Identifier.EPIC, 0);
        int displayLegendaryCount = displayRarityCounts.getOrDefault(Constants.Identifier.LEGENDARY, 0);
        int displayMythicalCount = displayRarityCounts.getOrDefault(Constants.Identifier.MYTHICAL, 0);
        int displayBabyCount = displayFishSizeCounts.getOrDefault(Constants.Identifier.BABY, 0);
        int displayJuvenileCount = displayFishSizeCounts.getOrDefault(Constants.Identifier.JUVENILE, 0);
        int displayAdultCount = displayFishSizeCounts.getOrDefault(Constants.Identifier.ADULT, 0);
        int displayLargeCount = displayFishSizeCounts.getOrDefault(Constants.Identifier.LARGE, 0);
        int displayGiganticCount = displayFishSizeCounts.getOrDefault(Constants.Identifier.GIGANTIC, 0);
        int displayAlbinoCount = displayVariantCounts.getOrDefault(Constants.Identifier.ALBINO, 0);
        int displayMelanisticCount = displayVariantCounts.getOrDefault(Constants.Identifier.MELANISTIC, 0);
        int displayTrophyCount = displayVariantCounts.getOrDefault(Constants.Identifier.TROPHY, 0);
        int displayFabledCount = displayVariantCounts.getOrDefault(Constants.Identifier.FABLED, 0);

        int displaySpecialCount = displayRarityCounts.getOrDefault(Constants.Identifier.SPECIAL, 0);

        // Put into Texts if enabled in config
        if (config.fishTracker.fishTrackerToggles.generalToggles.showFishCaught) textList.add(TextHelper.concat(
                    Text.literal("ꜰɪѕʜ ᴄᴀᴜɢʜᴛ: ").formatted(Formatting.GRAY),
                    Text.literal(String.valueOf(displayFishCaughtCount)).formatted(Formatting.WHITE)
        ));

        if (config.fishTracker.isFishTrackerOnTimer) {

            if (config.fishTracker.fishTrackerToggles.generalToggles.showFishPerHour) {
                double fishPerHour = (displayFishCaughtCount / (timeSinceReset / 3600000.0));
                textList.add(TextHelper.concat(
                        Text.literal("ꜰɪѕʜ/ʜᴏᴜʀ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%.1f", fishPerHour))
                ));
            }

            if (config.fishTracker.fishTrackerToggles.generalToggles.showTimer) {
                long hours = TimeUnit.MILLISECONDS.toHours(timeSinceReset);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeSinceReset) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeSinceReset) % 60;
                textList.add(TextHelper.concat(
                        Text.literal("ᴛɪᴍᴇ ѕɪɴᴄᴇ ʀᴇѕᴇᴛ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                ));
            }
        }

        if (config.fishTracker.fishTrackerToggles.generalToggles.showTotalXp) textList.add(TextHelper.concat(
                Text.literal("ᴛᴏᴛᴀʟ xᴘ: ").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(displayTotalXp, 2)).formatted(Formatting.WHITE)
        ));

        if (config.fishTracker.fishTrackerToggles.generalToggles.showTotalValue) textList.add(TextHelper.concat(
                Text.literal("ᴛᴏᴛᴀʟ ᴠᴀʟᴜᴇ: ").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(displayTotalValue, 2)).formatted(Formatting.WHITE),
                Text.literal("$").formatted(Formatting.WHITE)
        ));

        if (config.fishTracker.fishTrackerToggles.generalToggles.showPetCaught) {
            textList.add(TextHelper.concat(
                    Text.literal("ᴘᴇᴛѕ ᴄᴀᴜɢʜᴛ: ").formatted(Formatting.GRAY),
                    Text.literal(String.valueOf(displayPetCaughtCount)).formatted(Formatting.WHITE)
            ));

            if (config.fishTracker.fishTrackerToggles.generalToggles.showPetPerHour && config.fishTracker.isFishTrackerOnTimer) {
                double petPerHour = (displayPetCaughtCount / (timeSinceReset / 3600000.0));
                textList.add(TextHelper.concat(
                        Text.literal("ᴘᴇᴛѕ/ʜᴏᴜʀ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%.1f", petPerHour))
                ));
            }
        }

        if (config.fishTracker.fishTrackerToggles.generalToggles.showShardCaught) {
            textList.add(TextHelper.concat(
                    Text.literal("ѕʜᴀʀᴅѕ ᴄᴀᴜɢʜᴛ: ").formatted(Formatting.GRAY),
                    Text.literal(String.valueOf(displayShardCaughtCount)).formatted(Formatting.WHITE)
            ));

            if (config.fishTracker.fishTrackerToggles.generalToggles.showShardPerHour && config.fishTracker.isFishTrackerOnTimer) {
                double shardPerHour = (displayShardCaughtCount / (timeSinceReset / 3600000.0));
                textList.add(TextHelper.concat(
                        Text.literal("ѕʜᴀʀᴅѕ/ʜᴏᴜʀ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%.1f", shardPerHour))
                ));
            }
        }

        if (config.fishTracker.fishTrackerToggles.rarityToggles.showRarities) {
            textList.add(Text.empty());

            if (config.fishTracker.fishTrackerToggles.rarityToggles.showCommon) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.COMMON + " "),
                    Text.literal(String.valueOf(displayCommonCount)),
                    getPercentage(displayCommonCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showRare) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.RARE + " "),
                    Text.literal(String.valueOf(displayRareCount)),
                    getPercentage(displayRareCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showEpic) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.EPIC + " "),
                    Text.literal(String.valueOf(displayEpicCount)),
                    getPercentage(displayEpicCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showLegendary) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.LEGENDARY + " "),
                    Text.literal(String.valueOf(displayLegendaryCount)),
                    getPercentage(displayLegendaryCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showMythical) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.MYTHICAL + " "),
                    Text.literal(String.valueOf(displayMythicalCount)),
                    getPercentage(displayMythicalCount, displayFishCaughtCount)
            ));

            //TODO Remove after event
            textList.add(Text.empty());

            textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.SPECIAL + " "),
                    Text.literal(String.valueOf(displaySpecialCount)),
                    getPercentage(displaySpecialCount, displayFishCaughtCount)
            ));
        }

        if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showFishSizes) {
            textList.add(Text.empty());

            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showBaby) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.BABY + " ").withColor(Constants.Color.BABY),
                    Text.literal(String.valueOf(displayBabyCount)),
                    getPercentage(displayBabyCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showJuvenile) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.JUVENILE + " ").withColor(Constants.Color.JUVENILE),
                    Text.literal(String.valueOf(displayJuvenileCount)),
                    getPercentage(displayJuvenileCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showAdult) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.ADULT + " ").withColor(Constants.Color.ADULT),
                    Text.literal(String.valueOf(displayAdultCount)),
                    getPercentage(displayAdultCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showLarge) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.LARGE + " ").withColor(Constants.Color.LARGE),
                    Text.literal(String.valueOf(displayLargeCount)),
                    getPercentage(displayLargeCount, displayFishCaughtCount)
            ));
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showGigantic) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.GIGANTIC + " ").withColor(Constants.Color.GIGANTIC),
                    Text.literal(String.valueOf(displayGiganticCount)),
                    getPercentage(displayGiganticCount, displayFishCaughtCount)
            ));
        }

        if (config.fishTracker.fishTrackerToggles.variantToggles.showVariants) {
            textList.add(Text.empty());

            if(config.fishTracker.fishTrackerToggles.variantToggles.showAlbino) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.ALBINO + " "),
                    Text.literal(String.valueOf(displayAlbinoCount)),
                    getPercentage(displayAlbinoCount, displayFishCaughtCount)
            ));
            if(config.fishTracker.fishTrackerToggles.variantToggles.showMelanistic) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.MELANISTIC + " "),
                    Text.literal(String.valueOf(displayMelanisticCount)),
                    getPercentage(displayMelanisticCount, displayFishCaughtCount)
            ));
            if(config.fishTracker.fishTrackerToggles.variantToggles.showTrophy) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.TROPHY + " "),
                    Text.literal(String.valueOf(displayTrophyCount)),
                    getPercentage(displayTrophyCount, displayFishCaughtCount)
            ));
            if(config.fishTracker.fishTrackerToggles.variantToggles.showFabled) textList.add(TextHelper.concat(
                    Text.literal(Constants.Tag.FABLED + " "),
                    Text.literal(String.valueOf(displayFabledCount)),
                    getPercentage(displayFabledCount, displayFishCaughtCount)
            ));
        }

        return textList;
    }

    private Text getPercentage(int count, int totalCount) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        if (config.fishTracker.fishTrackerToggles.otherToggles.showPercentages) {
            float percentage = (count * 100f) / totalCount;
            return Text.literal(String.format(" (%.1f%%)", percentage)).formatted(Formatting.GRAY);
        } else {
            return Text.empty();
        }
    }
}
