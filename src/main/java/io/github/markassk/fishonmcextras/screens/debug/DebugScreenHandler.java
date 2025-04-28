package io.github.markassk.fishonmcextras.screens.debug;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.handler.*;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DebugScreenHandler {
    private static DebugScreenHandler INSTANCE = new DebugScreenHandler();

    public static DebugScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DebugScreenHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleDebugText(HandlerType type) {
        List<Text> textList = new ArrayList<>();

        textList.add(Text.literal(type.name).formatted(Formatting.YELLOW, Formatting.BOLD));

        switch (type) {
            case EXAMPLE -> textList.add(assembleText("exampleField", "Example Field"));
            case CONTEST -> {
                ContestHandler contestHandler = ContestHandler.instance();

                textList.addAll(List.of(
                        assembleText("timeLeft", contestHandler.timeLeft),
                        assembleText("isContest", contestHandler.isContest),
                        assembleText("type", contestHandler.type),
                        assembleText("location", contestHandler.location),
                        assembleText("lastUpdated", contestHandler.lastUpdated),
                        assembleText("firstName", contestHandler.firstName),
                        assembleText("firstStat", contestHandler.firstStat),
                        assembleText("secondName", contestHandler.secondName),
                        assembleText("secondStat", contestHandler.secondStat),
                        assembleText("thirdName", contestHandler.thirdName),
                        assembleText("thirdStat", contestHandler.secondStat),
                        assembleText("rank", contestHandler.rank),
                        assembleText("rankStat", contestHandler.rankStat),
                        assembleText("isReset", contestHandler.isReset)
                ));
            }
            case FISHCATCH -> {
                FishCatchHandler fishCatchHandler = FishCatchHandler.instance();

                textList.add(
                        assembleText("lastTimeUsedRod", fishCatchHandler.lastTimeUsedRod)
                );
            }
            case FULLINVENTORY -> {
                FullInventoryHandler fullInventoryHandler = FullInventoryHandler.instance();

                textList.addAll(List.of(
                        assembleText("isOverThreshold", fullInventoryHandler.isOverThreshold),
                        assembleText("slotsLeft", fullInventoryHandler.slotsLeft)
                ));
            }
            case LOADING -> {
                LoadingHandler loadingHandler = LoadingHandler.instance();

                textList.addAll(List.of(
                        assembleText("isLoadingDone", loadingHandler.isLoadingDone),
                        assembleText("isOnServer", loadingHandler.isOnServer)
                ));
            }
            case LOCATION -> {
                LocationHandler locationHandler = LocationHandler.instance();

                textList.add(
                        assembleText("currentLocation", locationHandler.currentLocation.ID)
                );
            }
            case LOOKTICK -> {
                LookTickHandler lookTickHandler = LookTickHandler.instance();

                textList.add(
                        assembleText("targetedItemInItemFrame", lookTickHandler.targetedItemInItemFrame != null ? lookTickHandler.targetedItemInItemFrame.getName().getString() : "null")
                );
            }
            case NOTIFICATIONSOUND -> {
                NotificationSoundHandler notificationSoundHandler = NotificationSoundHandler.instance();
            }
            case PETCALCULATOR -> {
                PetCalculatorHandler petCalculatorHandler = PetCalculatorHandler.instance();

                textList.addAll(List.of(
                        assembleText("selectedPetStacks[0]", petCalculatorHandler.selectedPetStacks[0] != null ? petCalculatorHandler.selectedPetStacks[0].getName().getString() : "null"),
                        assembleText("selectedPetStacks[1]", petCalculatorHandler.selectedPetStacks[1] != null ? petCalculatorHandler.selectedPetStacks[1].getName().getString() : "null"),
                        assembleText("selectedPet[0]", petCalculatorHandler.selectedPet[0] != null ? Objects.requireNonNull(petCalculatorHandler.selectedPet[0].pet).ID : "null"),
                        assembleText("selectedPet[1]", petCalculatorHandler.selectedPet[1] != null ? Objects.requireNonNull(petCalculatorHandler.selectedPet[1].pet).ID : "null"),
                        assembleText("calculatedPetName", petCalculatorHandler.calculatedPetName.getString()),
                        assembleText("selectedIndex[0]", petCalculatorHandler.selectedIndex[0]),
                        assembleText("selectedIndex[1]", petCalculatorHandler.selectedIndex[1])
                ));
            }
            case PETEQUIP -> {
                PetEquipHandler petEquipHandler = PetEquipHandler.instance();

                textList.addAll(List.of(
                        assembleText("currentPetItem", petEquipHandler.currentPetItem != null ? petEquipHandler.currentPetItem.getName().getString() : "null"),
                        assembleText("startScanTime", petEquipHandler.startScanTime),
                        assembleText("petStatus", petEquipHandler.petStatus.name())
                ));
            }
            case PROFILESTATS -> {
                ProfileStatsHandler profileStatsHandler = ProfileStatsHandler.instance();

                textList.addAll(List.of(
                        assembleText("lastUpdateTime", profileStatsHandler.lastUpdateTime),
                        assembleText("playerUUID", profileStatsHandler.playerUUID.toString()),
                        assembleText("profileStats.fishCaughtCount", profileStatsHandler.profileStats.fishCaughtCount),
                        assembleText("profileStats.totalXP", profileStatsHandler.profileStats.totalXP),
                        assembleText("profileStats.totalValue", profileStatsHandler.profileStats.totalValue),
                        assembleText("profileStats.variantCounts[ALBINO]", profileStatsHandler.profileStats.variantCounts.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.variantCounts[MELANISTIC]", profileStatsHandler.profileStats.variantCounts.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.variantCounts[TROPHY]", profileStatsHandler.profileStats.variantCounts.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.variantCounts[FABLED]", profileStatsHandler.profileStats.variantCounts.getOrDefault(Constant.FABLED, 0)),
                        assembleText("profileStats.rarityCounts[COMMON]", profileStatsHandler.profileStats.rarityCounts.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.rarityCounts[RARE]", profileStatsHandler.profileStats.rarityCounts.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.rarityCounts[EPIC]", profileStatsHandler.profileStats.rarityCounts.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.rarityCounts[LEGENDARY]", profileStatsHandler.profileStats.rarityCounts.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.rarityCounts[MYTHICAL]", profileStatsHandler.profileStats.rarityCounts.getOrDefault(Constant.MYTHICAL, 0)),
                        assembleText("profileStats.fishSizeCounts[BABY]", profileStatsHandler.profileStats.fishSizeCounts.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.fishSizeCounts[JUVENILE]", profileStatsHandler.profileStats.fishSizeCounts.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.fishSizeCounts[ADULT]", profileStatsHandler.profileStats.fishSizeCounts.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.fishSizeCounts[LARGE]", profileStatsHandler.profileStats.fishSizeCounts.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.fishSizeCounts[GIGANTIC]", profileStatsHandler.profileStats.fishSizeCounts.getOrDefault(Constant.GIGANTIC, 0)),
                        assembleText("profileStats.petCaughtCount", profileStatsHandler.profileStats.petCaughtCount),
                        assembleText("profileStats.shardCaughtCount", profileStatsHandler.profileStats.shardCaughtCount),
                        assembleText("profileStats.activeTime", profileStatsHandler.profileStats.activeTime),
                        assembleText("profileStats.lastFishCaughtTime", profileStatsHandler.profileStats.lastFishCaughtTime),
                        assembleText("profileStats.timerPaused", profileStatsHandler.profileStats.timerPaused),
                        assembleText("profileStats.allFishCaughtCount", profileStatsHandler.profileStats.allFishCaughtCount),
                        assembleText("profileStats.allTotalXP", profileStatsHandler.profileStats.allTotalXP),
                        assembleText("profileStats.allTotalValue", profileStatsHandler.profileStats.allTotalValue),
                        assembleText("profileStats.allVariantCounts[ALBINO]", profileStatsHandler.profileStats.allVariantCounts.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.allVariantCounts[MELANISTIC]", profileStatsHandler.profileStats.allVariantCounts.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.allVariantCounts[TROPHY]", profileStatsHandler.profileStats.allVariantCounts.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.allVariantCounts[FABLED]", profileStatsHandler.profileStats.allVariantCounts.getOrDefault(Constant.FABLED, 0)),
                        assembleText("profileStats.allRarityCounts[COMMON]", profileStatsHandler.profileStats.allRarityCounts.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.allRarityCounts[RARE]", profileStatsHandler.profileStats.allRarityCounts.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.allRarityCounts[EPIC]", profileStatsHandler.profileStats.allRarityCounts.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.allRarityCounts[LEGENDARY]", profileStatsHandler.profileStats.allRarityCounts.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.allRarityCounts[MYTHICAL]", profileStatsHandler.profileStats.allRarityCounts.getOrDefault(Constant.MYTHICAL, 0)),
                        assembleText("profileStats.allFishSizeCounts[BABY]", profileStatsHandler.profileStats.allFishSizeCounts.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.allFishSizeCounts[JUVENILE]", profileStatsHandler.profileStats.allFishSizeCounts.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.allFishSizeCounts[ADULT]", profileStatsHandler.profileStats.allFishSizeCounts.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.allFishSizeCounts[LARGE]", profileStatsHandler.profileStats.allFishSizeCounts.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.allFishSizeCounts[GIGANTIC]", profileStatsHandler.profileStats.allFishSizeCounts.getOrDefault(Constant.GIGANTIC, 0)),
                        assembleText("profileStats.equippedPetSlot", profileStatsHandler.profileStats.equippedPetSlot),
                        assembleText("profileStats.equippedPet", profileStatsHandler.profileStats.equippedPet != null ? Objects.requireNonNull(profileStatsHandler.profileStats.equippedPet.pet).ID : "null"),
                        assembleText("profileStats.petDryStreak", profileStatsHandler.profileStats.petDryStreak),
                        assembleText("profileStats.shardDryStreak", profileStatsHandler.profileStats.shardDryStreak),

                        assembleText("profileStats.variantDryStreak[ALBINO]", profileStatsHandler.profileStats.variantDryStreak.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.variantDryStreak[MELANISTIC]", profileStatsHandler.profileStats.variantDryStreak.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.variantDryStreak[TROPHY]", profileStatsHandler.profileStats.variantDryStreak.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.variantDryStreak[FABLED]", profileStatsHandler.profileStats.variantDryStreak.getOrDefault(Constant.FABLED, 0)),

                        assembleText("profileStats.rarityDryStreak[COMMON]", profileStatsHandler.profileStats.rarityDryStreak.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.rarityDryStreak[RARE]", profileStatsHandler.profileStats.rarityDryStreak.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.rarityDryStreak[EPIC]", profileStatsHandler.profileStats.rarityDryStreak.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.rarityDryStreak[LEGENDARY]", profileStatsHandler.profileStats.rarityDryStreak.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.rarityDryStreak[MYTHICAL]", profileStatsHandler.profileStats.rarityDryStreak.getOrDefault(Constant.MYTHICAL, 0)),

                        assembleText("profileStats.fishSizeDryStreak[BABY]", profileStatsHandler.profileStats.fishSizeDryStreak.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.fishSizeDryStreak[JUVENILE]", profileStatsHandler.profileStats.fishSizeDryStreak.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.fishSizeDryStreak[ADULT]", profileStatsHandler.profileStats.fishSizeDryStreak.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.fishSizeDryStreak[LARGE]", profileStatsHandler.profileStats.fishSizeDryStreak.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.fishSizeDryStreak[GIGANTIC]", profileStatsHandler.profileStats.fishSizeDryStreak.getOrDefault(Constant.GIGANTIC, 0))
                ));
            }
            case RAYTRACING -> {
                RayTracingHandler rayTracingHandler = RayTracingHandler.instance();

                textList.add(assembleText("target", rayTracingHandler.getTarget() != null? rayTracingHandler.getTarget().getType().name() : "null"));
            }
            case SCOREBOARD -> {
                ScoreboardHandler scoreboardHandler = ScoreboardHandler.instance();

                textList.addAll(List.of(
                        assembleText("playerName", scoreboardHandler.playerName),
                        assembleText("level", scoreboardHandler.level),
                        assembleText("percentLevel", scoreboardHandler.percentLevel),
                        assembleText("wallet", scoreboardHandler.wallet),
                        assembleText("credits", scoreboardHandler.credits),
                        assembleText("catches", scoreboardHandler.catches),
                        assembleText("catchRate", scoreboardHandler.catchRate),
                        assembleText("crewName", scoreboardHandler.crewName),
                        assembleText("crewLevel", scoreboardHandler.crewLevel),
                        assembleText("isCrewNearby", scoreboardHandler.isCrewNearby),
                        assembleText("noScoreBoard", scoreboardHandler.noScoreBoard)
                ));
            }
            case TAB -> {
                TabHandler tabHandler = TabHandler.instance();

                textList.add(assembleText("player", tabHandler.player.getString()));
            }
            case TITLE -> {
                TitleHandler titleHandler = TitleHandler.instance();

                textList.addAll(List.of(
                        assembleText("showedAt", titleHandler.showedAt),
                        assembleText("title", titleHandler.title.isEmpty() ? "" : titleHandler.title.getFirst().getString()),
                        assembleText("time", titleHandler.time),
                        assembleText("subtitle", titleHandler.subtitle.isEmpty() ? "" : titleHandler.subtitle.getFirst().getString())
                ));
            }
            case PETTOOLTIP -> {
                PetTooltipHandler petTooltipHandler = PetTooltipHandler.instance();
            }
            case BOSSBAR -> {
                BossBarHandler bossBarHandler = BossBarHandler.instance();

                textList.addAll(List.of(
                        assembleText("time", bossBarHandler.time),
                        assembleText("weather", bossBarHandler.weather),
                        assembleText("timeSuffix", bossBarHandler.timeSuffix),
                        assembleText("temps", bossBarHandler.temperature)
                ));
            }
        }

        return textList;
    }

    private Text assembleText(String field, String value) {
        return TextHelper.concat(
                Text.literal(field).formatted(Formatting.GRAY),
                Text.literal(": ").formatted(Formatting.GRAY),
                Text.literal(value).formatted(Formatting.WHITE)
        );
    }

    private Text assembleText(String field, int value) {
        return TextHelper.concat(
                Text.literal(field).formatted(Formatting.GRAY),
                Text.literal(": ").formatted(Formatting.GRAY),
                Text.literal(String.valueOf(value)).formatted(Formatting.WHITE)
        );
    }

    private Text assembleText(String field, float value) {
        return TextHelper.concat(
                Text.literal(field).formatted(Formatting.GRAY),
                Text.literal(": ").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(value, 2)).formatted(Formatting.WHITE)
        );
    }

    private Text assembleText(String field, long value) {
        return TextHelper.concat(
                Text.literal(field).formatted(Formatting.GRAY),
                Text.literal(": ").formatted(Formatting.GRAY),
                Text.literal(String.valueOf(value)).formatted(Formatting.WHITE)
        );
    }

    private Text assembleText(String field, boolean value) {
        return TextHelper.concat(
                Text.literal(field).formatted(Formatting.GRAY),
                Text.literal(": ").formatted(Formatting.GRAY),
                Text.literal(String.valueOf(value)).formatted(Formatting.WHITE)
        );
    }

    public enum HandlerType {
        EXAMPLE(0, "ExampleHandler"),
        CONTEST(1, "ContestHandler"),
        FISHCATCH(2, "FishCatchHandler"),
        FULLINVENTORY(3, "FullInventoryHandler"),
        LOADING(4, "LoadingHandler"),
        LOCATION(5, "LocationHandler"),
        LOOKTICK(6, "LookTickhandler"),
        NOTIFICATIONSOUND(7, "NotificationSoundHandler"),
        PETCALCULATOR(8, "PetCalculatorHandler"),
        PETEQUIP(9, "PetEquipHandler"),
        PROFILESTATS(10, "ProfileStatsHandler"),
        RAYTRACING(11, "RayTracingHandler"),
        SCOREBOARD(12, "ScoreboardHandler"),
        TAB(13, "TabHandler"),
        TITLE(14, "TitleHandler"),
        PETTOOLTIP(15, "PetTooltipHandler"),
        BOSSBAR(16, "BossBarHandler")
        ;

        public final int id;
        public final String name;

        HandlerType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static HandlerType valueOfId(int id) {
            for (HandlerType c : values()) {
                if (c.id == id) {
                    return c;
                }
            }
            return null;
        }
    }
}
