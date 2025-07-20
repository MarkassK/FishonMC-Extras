package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
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
        ProfileDataHandler.ProfileData profileData = ProfileDataHandler.instance().profileData;
        List<Text> textList = new ArrayList<>();

        long timeSinceReset = ProfileDataHandler.instance().profileData.activeTime;

        // All-time or not display stat strings
        int displayFishCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileData.fishCaughtCount
                : profileData.allFishCaughtCount;
        float displayTotalXp = config.fishTracker.isFishTrackerOnTimer
                ? profileData.totalXP
                : profileData.allTotalXP;
        float displayTotalValue = config.fishTracker.isFishTrackerOnTimer
                ? profileData.totalValue
                : profileData.allTotalValue;
        int displayPetCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileData.petCaughtCount
                : profileData.allPetCaughtCount;
        int displayShardCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileData.shardCaughtCount
                : profileData.allShardCaughtCount;
        int displayLightningBottleCaughtCount = config.fishTracker.isFishTrackerOnTimer
                ? profileData.lightningBottleCount
                : profileData.allLightningBottleCount;
        Map<Constant, Integer> displayRarityCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileData.rarityCounts
                : profileData.allRarityCounts;
        Map<Constant, Integer> displayFishSizeCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileData.fishSizeCounts
                : profileData.allFishSizeCounts;
        Map<Constant, Integer> displayVariantCounts = config.fishTracker.isFishTrackerOnTimer
                ? profileData.variantCounts
                : profileData.allVariantCounts;
        int displayCommonCount = displayRarityCounts.getOrDefault(Constant.COMMON, 0);
        int displayRareCount = displayRarityCounts.getOrDefault(Constant.RARE, 0);
        int displayEpicCount = displayRarityCounts.getOrDefault(Constant.EPIC, 0);
        int displayLegendaryCount = displayRarityCounts.getOrDefault(Constant.LEGENDARY, 0);
        int displayMythicalCount = displayRarityCounts.getOrDefault(Constant.MYTHICAL, 0);
        int displayBabyCount = displayFishSizeCounts.getOrDefault(Constant.BABY, 0);
        int displayJuvenileCount = displayFishSizeCounts.getOrDefault(Constant.JUVENILE, 0);
        int displayAdultCount = displayFishSizeCounts.getOrDefault(Constant.ADULT, 0);
        int displayLargeCount = displayFishSizeCounts.getOrDefault(Constant.LARGE, 0);
        int displayGiganticCount = displayFishSizeCounts.getOrDefault(Constant.GIGANTIC, 0);
        int displayAlbinoCount = displayVariantCounts.getOrDefault(Constant.ALBINO, 0);
        int displayMelanisticCount = displayVariantCounts.getOrDefault(Constant.MELANISTIC, 0);
        int displayTrophyCount = displayVariantCounts.getOrDefault(Constant.TROPHY, 0);
        int displayFabledCount = displayVariantCounts.getOrDefault(Constant.FABLED, 0);

        Map<Constant, Integer> displayRarityDryStreak = profileData.rarityDryStreak;
        Map<Constant, Integer> displayFishSizeDryStreak = profileData.fishSizeDryStreak;
        Map<Constant, Integer> displayVariantDryStreak = profileData.variantDryStreak;

        int displayDryStreakCommonCount = displayRarityDryStreak.getOrDefault(Constant.COMMON, 0);
        int displayDryStreakRareCount = displayRarityDryStreak.getOrDefault(Constant.RARE, 0);
        int displayDryStreakEpicCount = displayRarityDryStreak.getOrDefault(Constant.EPIC, 0);
        int displayDryStreakLegendaryCount = displayRarityDryStreak.getOrDefault(Constant.LEGENDARY, 0);
        int displayDryStreakMythicalCount = displayRarityDryStreak.getOrDefault(Constant.MYTHICAL, 0);
        int displayDryStreakBabyCount = displayFishSizeDryStreak.getOrDefault(Constant.BABY, 0);
        int displayDryStreakJuvenileCount = displayFishSizeDryStreak.getOrDefault(Constant.JUVENILE, 0);
        int displayDryStreakAdultCount = displayFishSizeDryStreak.getOrDefault(Constant.ADULT, 0);
        int displayDryStreakLargeCount = displayFishSizeDryStreak.getOrDefault(Constant.LARGE, 0);
        int displayDryStreakGiganticCount = displayFishSizeDryStreak.getOrDefault(Constant.GIGANTIC, 0);
        int displayDryStreakAlbinoCount = displayVariantDryStreak.getOrDefault(Constant.ALBINO, 0);
        int displayDryStreakMelanisticCount = displayVariantDryStreak.getOrDefault(Constant.MELANISTIC, 0);
        int displayDryStreakTrophyCount = displayVariantDryStreak.getOrDefault(Constant.TROPHY, 0);
        int displayDryStreakFabledCount = displayVariantDryStreak.getOrDefault(Constant.FABLED, 0);

        int displaySpecialCount = displayRarityCounts.getOrDefault(Constant.SPECIAL, 0);
        int displayDryStreakSpecialCount = displayRarityDryStreak.getOrDefault(Constant.SPECIAL, 0);

        int displayAlternateCount = displayVariantCounts.getOrDefault(Constant.ALTERNATE, 0);
        int displayDryStreakAlternateCount = displayVariantDryStreak.getOrDefault(Constant.ALTERNATE, 0);

        int displayTimerFishCaughtCount = profileData.timerFishCaughtCount;

        if (ThemingHandler.instance().currentThemeType == Theming.ThemeType.OFF) {
            if(config.fishTracker.rightAlignment) {
                textList.add(TextHelper.concat(
                        this.getTitle().copy().formatted(Formatting.GRAY),
                        Text.literal(" ◀").formatted(Formatting.GRAY)
                ));
            } else {
                textList.add(TextHelper.concat(
                        Text.literal("▶ ").formatted(Formatting.GRAY),
                        this.getTitle().copy().formatted(Formatting.GRAY)
                ));
            }
        }

        // Put into Texts if enabled in config
        if (config.fishTracker.fishTrackerToggles.generalToggles.showFishCaught) textList.add(TextHelper.concat(
                    Text.literal("ꜰɪѕʜ ᴄᴀᴜɢʜᴛ: ").formatted(Formatting.GRAY),
                    Text.literal(getNumber(displayFishCaughtCount)).formatted(Formatting.WHITE)
        ));

        if (config.fishTracker.isFishTrackerOnTimer || config.fishTracker.showTimerOnAllTime) {

            if (config.fishTracker.fishTrackerToggles.generalToggles.showFishPerHour) {
                double fishPerHour = (displayTimerFishCaughtCount / (timeSinceReset / 3600000.0));
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
                        Text.literal("ꜰɪѕʜ ᴛɪᴍᴇ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                ));
            }
        }

        if (config.fishTracker.fishTrackerToggles.generalToggles.showTotalXp) textList.add(TextHelper.concat(
                Text.literal("ᴛᴏᴛᴀʟ xᴘ: ").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmnt(displayTotalXp)).formatted(Formatting.WHITE)
        ));

        if (config.fishTracker.fishTrackerToggles.generalToggles.showTotalValue) textList.add(TextHelper.concat(
                Text.literal("ᴛᴏᴛᴀʟ ᴠᴀʟᴜᴇ: ").formatted(Formatting.GRAY),
                Text.literal("$").formatted(Formatting.WHITE),
                Text.literal(TextHelper.fmnt(displayTotalValue)).formatted(Formatting.WHITE)

        ));

        if(config.fishTracker.fishTrackerToggles.generalToggles.showPetCaught
                || config.fishTracker.fishTrackerToggles.generalToggles.showShardCaught
                || config.fishTracker.fishTrackerToggles.generalToggles.showLightningBottleCaught
        ) {
            textList.add(Text.empty());

            if (config.fishTracker.fishTrackerToggles.generalToggles.showPetCaught) {
                textList.add(TextHelper.concat(
                        Text.literal("ᴘᴇᴛѕ: ").formatted(Formatting.GRAY),
                        Text.literal(String.valueOf(displayPetCaughtCount)).formatted(Formatting.WHITE)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showPet) {
                    textList.add(getDryStreak(profileData.petDryStreak));
                }

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
                        Text.literal("ѕʜᴀʀᴅѕ: ").formatted(Formatting.GRAY),
                        Text.literal(String.valueOf(displayShardCaughtCount)).formatted(Formatting.WHITE)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showShard) {
                    textList.add(getDryStreak(profileData.shardDryStreak));
                }

                if (config.fishTracker.fishTrackerToggles.generalToggles.showShardPerHour && config.fishTracker.isFishTrackerOnTimer) {
                    double shardPerHour = (displayShardCaughtCount / (timeSinceReset / 3600000.0));
                    textList.add(TextHelper.concat(
                            Text.literal("ѕʜᴀʀᴅѕ/ʜᴏᴜʀ: ").formatted(Formatting.GRAY),
                            Text.literal(String.format("%.1f", shardPerHour))
                    ));
                }
            }

            if(config.fishTracker.fishTrackerToggles.generalToggles.showLightningBottleCaught) {
                textList.add(TextHelper.concat(
                        Text.literal("ʟɪɢʜᴛ. ʙᴏᴛᴛʟᴇѕ: ").formatted(Formatting.GRAY),
                        Text.literal(String.valueOf(displayLightningBottleCaughtCount)).formatted(Formatting.WHITE)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showLightningBottle) {
                    textList.add(getDryStreak(profileData.lightningBottleDryStreak));
                }
            }
        }



        if (config.fishTracker.fishTrackerToggles.rarityToggles.showRarities) {
            textList.add(Text.empty());

            if (config.fishTracker.fishTrackerToggles.rarityToggles.showCommon) {
                textList.add(TextHelper.concat(
                        Constant.COMMON.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayCommonCount)),
                        getPercentage(displayCommonCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showCommon) {
                    textList.add(getDryStreak(displayDryStreakCommonCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showRare) {
                textList.add(TextHelper.concat(
                        Constant.RARE.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayRareCount)),
                        getPercentage(displayRareCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showRare) {
                    textList.add(getDryStreak(displayDryStreakRareCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showEpic) {
                textList.add(TextHelper.concat(
                        Constant.EPIC.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayEpicCount)),
                        getPercentage(displayEpicCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showEpic) {
                    textList.add(getDryStreak(displayDryStreakEpicCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showLegendary) {
                textList.add(TextHelper.concat(
                        Constant.LEGENDARY.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayLegendaryCount)),
                        getPercentage(displayLegendaryCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showLegendary) {
                    textList.add(getDryStreak(displayDryStreakLegendaryCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.rarityToggles.showMythical) {
                textList.add(TextHelper.concat(
                        Constant.MYTHICAL.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayMythicalCount)),
                        getPercentage(displayMythicalCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showMythical) {
                    textList.add(getDryStreak(displayDryStreakMythicalCount));
                }
            }
        }

        if(config.fishTracker.fishTrackerToggles.rarityToggles.showSpecial || config.fishTracker.fishTrackerToggles.variantToggles.showAlternate) {
            textList.add(Text.empty());

            if(config.fishTracker.fishTrackerToggles.rarityToggles.showSpecial) {
                textList.add(TextHelper.concat(
                        Constant.SPECIAL.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displaySpecialCount)),
                        getPercentage(displaySpecialCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showSpecial){
                    textList.add(getDryStreak(displayDryStreakSpecialCount));
                }
            }

            if(config.fishTracker.fishTrackerToggles.variantToggles.showAlternate) {
                textList.add(TextHelper.concat(
                        Constant.ALTERNATE.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayAlternateCount)),
                        getPercentage(displayAlternateCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showAlternate) {
                    textList.add(getDryStreak(displayDryStreakAlternateCount));
                }
            }
        }

        if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showFishSizes) {
            textList.add(Text.empty());

            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showBaby) {
                textList.add(TextHelper.concat(
                        Constant.BABY.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayBabyCount)),
                        getPercentage(displayBabyCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showBaby) {
                    textList.add(getDryStreak(displayDryStreakBabyCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showJuvenile) {
                textList.add(TextHelper.concat(
                        Constant.JUVENILE.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayJuvenileCount)),
                        getPercentage(displayJuvenileCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showJuvenile) {
                    textList.add(getDryStreak(displayDryStreakJuvenileCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showAdult) {
                textList.add(TextHelper.concat(
                        Constant.ADULT.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayAdultCount)),
                        getPercentage(displayAdultCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showAdult) {
                    textList.add(getDryStreak(displayDryStreakAdultCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showLarge) {
                textList.add(TextHelper.concat(
                        Constant.LARGE.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayLargeCount)),
                        getPercentage(displayLargeCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showLarge) {
                    textList.add(getDryStreak(displayDryStreakLargeCount));
                }
            }
            if (config.fishTracker.fishTrackerToggles.fishSizeToggles.showGigantic) {
                textList.add(TextHelper.concat(
                        Constant.GIGANTIC.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayGiganticCount)),
                        getPercentage(displayGiganticCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showGigantic) {
                    textList.add(getDryStreak(displayDryStreakGiganticCount));
                }
            }
        }

        if (config.fishTracker.fishTrackerToggles.variantToggles.showVariants) {
            textList.add(Text.empty());

            if(config.fishTracker.fishTrackerToggles.variantToggles.showAlbino) {
                textList.add(TextHelper.concat(
                        Constant.ALBINO.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayAlbinoCount)),
                        getPercentage(displayAlbinoCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showAlbino) {
                    textList.add(getDryStreak(displayDryStreakAlbinoCount));
                }
            }
            if(config.fishTracker.fishTrackerToggles.variantToggles.showMelanistic) {
                textList.add(TextHelper.concat(
                        Constant.MELANISTIC.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayMelanisticCount)),
                        getPercentage(displayMelanisticCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showMelanistic) {
                    textList.add(getDryStreak(displayDryStreakMelanisticCount));
                }
            }
            if(config.fishTracker.fishTrackerToggles.variantToggles.showTrophy) {
                textList.add(TextHelper.concat(
                        Constant.TROPHY.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayTrophyCount)),
                        getPercentage(displayTrophyCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showTrophy) {
                    textList.add(getDryStreak(displayDryStreakTrophyCount));
                }
            }
            if(config.fishTracker.fishTrackerToggles.variantToggles.showFabled) {
                textList.add(TextHelper.concat(
                        Constant.FABLED.TAG,
                        Text.literal(" "),
                        Text.literal(getNumber(displayFabledCount)),
                        getPercentage(displayFabledCount, displayFishCaughtCount)
                ));
                if(config.fishTracker.fishTrackerToggles.dryStreakToggles.showFabled) {
                    textList.add(getDryStreak(displayDryStreakFabledCount));
                }
            }
        }

        return textList;
    }

    public Text getTitle() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        return config.fishTracker.isFishTrackerOnTimer ? Text.literal("ѕᴇѕѕɪᴏɴ ѕᴛᴀᴛѕ").formatted(Formatting.BOLD) : Text.literal("ᴀʟʟ-ᴛɪᴍᴇ ѕᴛᴀᴛѕ").formatted(Formatting.BOLD);
    }

    private Text getPercentage(int count, int totalCount) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        if (config.fishTracker.fishTrackerToggles.otherToggles.showPercentages) {
            float percentage = (count * 100f) / totalCount;
            float roundedPercentage = TextHelper.roundFirstSignificantDigit(percentage);
            return Text.literal(roundedPercentage >= 0.1f ? String.format(" (%.1f%%)", percentage) : " (" + roundedPercentage + "%)").formatted(Formatting.GRAY);
        } else {
            return Text.empty();
        }
    }

    private Text getDryStreak(int value) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        ProfileDataHandler.ProfileData profileData = ProfileDataHandler.instance().profileData;
        if(config.fishTracker.rightAlignment) {
            return TextHelper.concat(
                    Text.literal("ᴅʀʏ ѕᴛʀᴇᴀᴋ: ").formatted(Formatting.GRAY),
                    Text.literal(TextHelper.fmt(profileData.allFishCaughtCount - value)).formatted(Formatting.WHITE),
                    Text.literal(" ┘ ").formatted(Formatting.GRAY)
            );
        } else {
            return TextHelper.concat(
                    Text.literal(" └ ").formatted(Formatting.GRAY),
                    Text.literal("ᴅʀʏ ѕᴛʀᴇᴀᴋ: ").formatted(Formatting.GRAY),
                    Text.literal(TextHelper.fmt(profileData.allFishCaughtCount - value)).formatted(Formatting.WHITE)
            );
        }
    }

    private String getNumber(int value) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        return config.fishTracker.fishTrackerToggles.otherToggles.abbreviateNumbers ? TextHelper.fmnt(value) : String.valueOf(value);
    }
}