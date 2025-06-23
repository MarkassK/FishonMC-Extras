package io.github.markassk.fishonmcextras.screens.debug;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
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
                        assembleText("isOnServer", loadingHandler.isOnServer),
                        assembleText("wasOnServer", loadingHandler.wasOnServer)
                ));
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
            case PROFILEDATA -> {
                ProfileDataHandler profileDataHandler = ProfileDataHandler.instance();

                textList.addAll(List.of(
                        assembleText("lastUpdateTime", profileDataHandler.lastUpdateTime),
                        assembleText("playerUUID", profileDataHandler.playerUUID.toString()),
                        assembleText("profileStats.fishCaughtCount", profileDataHandler.profileData.fishCaughtCount),
                        assembleText("profileStats.totalXP", profileDataHandler.profileData.totalXP),
                        assembleText("profileStats.totalValue", profileDataHandler.profileData.totalValue),
                        assembleText("profileStats.variantCounts[ALBINO]", profileDataHandler.profileData.variantCounts.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.variantCounts[MELANISTIC]", profileDataHandler.profileData.variantCounts.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.variantCounts[TROPHY]", profileDataHandler.profileData.variantCounts.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.variantCounts[FABLED]", profileDataHandler.profileData.variantCounts.getOrDefault(Constant.FABLED, 0)),
                        assembleText("profileStats.rarityCounts[COMMON]", profileDataHandler.profileData.rarityCounts.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.rarityCounts[RARE]", profileDataHandler.profileData.rarityCounts.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.rarityCounts[EPIC]", profileDataHandler.profileData.rarityCounts.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.rarityCounts[LEGENDARY]", profileDataHandler.profileData.rarityCounts.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.rarityCounts[MYTHICAL]", profileDataHandler.profileData.rarityCounts.getOrDefault(Constant.MYTHICAL, 0)),
                        assembleText("profileStats.fishSizeCounts[BABY]", profileDataHandler.profileData.fishSizeCounts.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.fishSizeCounts[JUVENILE]", profileDataHandler.profileData.fishSizeCounts.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.fishSizeCounts[ADULT]", profileDataHandler.profileData.fishSizeCounts.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.fishSizeCounts[LARGE]", profileDataHandler.profileData.fishSizeCounts.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.fishSizeCounts[GIGANTIC]", profileDataHandler.profileData.fishSizeCounts.getOrDefault(Constant.GIGANTIC, 0)),
                        assembleText("profileStats.petCaughtCount", profileDataHandler.profileData.petCaughtCount),
                        assembleText("profileStats.shardCaughtCount", profileDataHandler.profileData.shardCaughtCount),
                        assembleText("profileStats.activeTime", profileDataHandler.profileData.activeTime),
                        assembleText("profileStats.lastFishCaughtTime", profileDataHandler.profileData.lastFishCaughtTime),
                        assembleText("profileStats.timerPaused", profileDataHandler.profileData.timerPaused),
                        assembleText("profileStats.allFishCaughtCount", profileDataHandler.profileData.allFishCaughtCount),
                        assembleText("profileStats.allTotalXP", profileDataHandler.profileData.allTotalXP),
                        assembleText("profileStats.allTotalValue", profileDataHandler.profileData.allTotalValue),
                        assembleText("profileStats.allVariantCounts[ALBINO]", profileDataHandler.profileData.allVariantCounts.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.allVariantCounts[MELANISTIC]", profileDataHandler.profileData.allVariantCounts.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.allVariantCounts[TROPHY]", profileDataHandler.profileData.allVariantCounts.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.allVariantCounts[FABLED]", profileDataHandler.profileData.allVariantCounts.getOrDefault(Constant.FABLED, 0)),
                        assembleText("profileStats.allRarityCounts[COMMON]", profileDataHandler.profileData.allRarityCounts.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.allRarityCounts[RARE]", profileDataHandler.profileData.allRarityCounts.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.allRarityCounts[EPIC]", profileDataHandler.profileData.allRarityCounts.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.allRarityCounts[LEGENDARY]", profileDataHandler.profileData.allRarityCounts.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.allRarityCounts[MYTHICAL]", profileDataHandler.profileData.allRarityCounts.getOrDefault(Constant.MYTHICAL, 0)),
                        assembleText("profileStats.allFishSizeCounts[BABY]", profileDataHandler.profileData.allFishSizeCounts.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.allFishSizeCounts[JUVENILE]", profileDataHandler.profileData.allFishSizeCounts.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.allFishSizeCounts[ADULT]", profileDataHandler.profileData.allFishSizeCounts.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.allFishSizeCounts[LARGE]", profileDataHandler.profileData.allFishSizeCounts.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.allFishSizeCounts[GIGANTIC]", profileDataHandler.profileData.allFishSizeCounts.getOrDefault(Constant.GIGANTIC, 0)),
                        assembleText("profileData.timerFishCaughtCount", profileDataHandler.profileData.timerFishCaughtCount),
                        assembleText("profileStats.equippedPetSlot", profileDataHandler.profileData.equippedPetSlot),
                        assembleText("profileStats.equippedPet", profileDataHandler.profileData.equippedPet != null ? Objects.requireNonNull(profileDataHandler.profileData.equippedPet.pet).ID : "null"),
                        assembleText("profileStats.petDryStreak", profileDataHandler.profileData.petDryStreak),
                        assembleText("profileStats.shardDryStreak", profileDataHandler.profileData.shardDryStreak),

                        assembleText("profileStats.variantDryStreak[ALBINO]", profileDataHandler.profileData.variantDryStreak.getOrDefault(Constant.ALBINO, 0)),
                        assembleText("profileStats.variantDryStreak[MELANISTIC]", profileDataHandler.profileData.variantDryStreak.getOrDefault(Constant.MELANISTIC, 0)),
                        assembleText("profileStats.variantDryStreak[TROPHY]", profileDataHandler.profileData.variantDryStreak.getOrDefault(Constant.TROPHY, 0)),
                        assembleText("profileStats.variantDryStreak[FABLED]", profileDataHandler.profileData.variantDryStreak.getOrDefault(Constant.FABLED, 0)),

                        assembleText("profileStats.rarityDryStreak[COMMON]", profileDataHandler.profileData.rarityDryStreak.getOrDefault(Constant.COMMON, 0)),
                        assembleText("profileStats.rarityDryStreak[RARE]", profileDataHandler.profileData.rarityDryStreak.getOrDefault(Constant.RARE, 0)),
                        assembleText("profileStats.rarityDryStreak[EPIC]", profileDataHandler.profileData.rarityDryStreak.getOrDefault(Constant.EPIC, 0)),
                        assembleText("profileStats.rarityDryStreak[LEGENDARY]", profileDataHandler.profileData.rarityDryStreak.getOrDefault(Constant.LEGENDARY, 0)),
                        assembleText("profileStats.rarityDryStreak[MYTHICAL]", profileDataHandler.profileData.rarityDryStreak.getOrDefault(Constant.MYTHICAL, 0)),

                        assembleText("profileStats.fishSizeDryStreak[BABY]", profileDataHandler.profileData.fishSizeDryStreak.getOrDefault(Constant.BABY, 0)),
                        assembleText("profileStats.fishSizeDryStreak[JUVENILE]", profileDataHandler.profileData.fishSizeDryStreak.getOrDefault(Constant.JUVENILE, 0)),
                        assembleText("profileStats.fishSizeDryStreak[ADULT]", profileDataHandler.profileData.fishSizeDryStreak.getOrDefault(Constant.ADULT, 0)),
                        assembleText("profileStats.fishSizeDryStreak[LARGE]", profileDataHandler.profileData.fishSizeDryStreak.getOrDefault(Constant.LARGE, 0)),
                        assembleText("profileStats.fishSizeDryStreak[GIGANTIC]", profileDataHandler.profileData.fishSizeDryStreak.getOrDefault(Constant.GIGANTIC, 0)),
                        assembleText("profileStats.isInCrewChat", profileDataHandler.profileData.isInCrewChat),
                        assembleText("profileStats.isStatsInitialized", profileDataHandler.profileData.isStatsInitialized)
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

                textList.addAll(List.of(
                        assembleText("player", tabHandler.player.getString()),
                        assembleText("rank", tabHandler.rank.ID),
                        assembleText("instance", tabHandler.instance),
                        assembleText("isInstance", tabHandler.isInstance)
                ));
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
                        assembleText("temps", bossBarHandler.temperature),
                        assembleText("currentLocation", bossBarHandler.currentLocation.ID)
                ));
            }
            case ARMOR -> {
                ArmorHandler armorHandler = ArmorHandler.instance();

                textList.addAll(List.of(
                        assembleText("currentChestplateItem", armorHandler.currentChestplateItem.getName().getString()),
                        assembleText("currentChestplate", armorHandler.currentChestplate != null ? armorHandler.currentChestplate.climate.ID : ""),
                        assembleText("currentLeggingsItem", armorHandler.currentLeggingsItem.getName().getString()),
                        assembleText("currentLeggings", armorHandler.currentLeggings != null ? armorHandler.currentLeggings.climate.ID : ""),
                        assembleText("currentBootsItem", armorHandler.currentBootsItem.getName().getString()),
                        assembleText("currentBoots", armorHandler.currentBoots != null ? armorHandler.currentBoots.climate.ID : ""),
                        assembleText("isWrongChestplateClimate", armorHandler.isWrongChestplateClimate),
                        assembleText("isWrongLeggingsClimate", armorHandler.isWrongLeggingsClimate),
                        assembleText("isWrongBootsClimate", armorHandler.isWrongBootsClimate)
                ));
            }
            case FISHINGROD -> {
                FishingRodHandler fishingRodHandler = FishingRodHandler.instance();

                textList.addAll(List.of(
                        assembleText("fishingRod.name", fishingRodHandler.fishingRod.name),
                        assembleText("fishingRod.soulboundRod", fishingRodHandler.fishingRod.soulboundRod),
                        assembleText("fishingRod.skin", fishingRodHandler.fishingRod.skin),
                        assembleText("fishingRod.owner", fishingRodHandler.fishingRod.owner != null ? fishingRodHandler.fishingRod.owner.toString() : "")
                ));

                textList.add(assembleText("fishingRod.tacklebox.size()", fishingRodHandler.fishingRod.tacklebox.size()));
                textList.addAll(fishingRodHandler.fishingRod.tacklebox.stream().map(fomcItem ->
                        fomcItem instanceof Bait bait ? Text.literal(bait.name).formatted(Formatting.WHITE) :
                                fomcItem instanceof Lure lure ? Text.literal(lure.name).formatted(Formatting.WHITE) : Text.empty()).toList());

                textList.addAll(List.of(
                        assembleText("fishingRod.line.name", fishingRodHandler.fishingRod.line != null ? fishingRodHandler.fishingRod.line.name : ""),
                        assembleText("fishingRod.pole.name", fishingRodHandler.fishingRod.pole != null ? fishingRodHandler.fishingRod.pole.name : ""),
                        assembleText("fishingRod.reel.name", fishingRodHandler.fishingRod.reel != null ? fishingRodHandler.fishingRod.reel.name : ""),
                        assembleText("isWrongBait", fishingRodHandler.isWrongBait),
                        assembleText("isWrongLure", fishingRodHandler.isWrongLure),
                        assembleText("isWrongReel", fishingRodHandler.isWrongReel),
                        assembleText("isWrongPole", fishingRodHandler.isWrongPole),
                        assembleText("isWrongLine", fishingRodHandler.isWrongLine)
                ));
            }
            case CREW -> {
                CrewHandler crewHandler = CrewHandler.instance();

                textList.addAll(List.of(
                        assembleText("crewMenuState", crewHandler.crewMenuState),
                        assembleText("isCrewNearby", crewHandler.isCrewNearby),
                        assembleText("isCrewInRenderDistance", crewHandler.isCrewInRenderDistance)
                ));
            }
            case QUEST -> {
                QuestHandler questHandler = QuestHandler.instance();

                textList.add(
                        assembleText("isQuestInitialized", questHandler.isQuestInitialized())
                );
            }
            case STAFF -> {
                StaffHandler staffHandler = StaffHandler.instance();

                textList.add(
                        assembleText("isVanished", staffHandler.isVanished)
                );
            }
            case STATSIMPORT -> {
                StatsImportHandler statsImportHandler = StatsImportHandler.instance();

                textList.addAll(List.of(
                        assembleText("screenInit", statsImportHandler.screenInit),
                        assembleText("isOnScreen", statsImportHandler.isOnScreen)
                ));
            }
            case THEMING -> {
                ThemingHandler themingHandler = ThemingHandler.instance();

                textList.addAll(List.of(
                        assembleText("currentThemeType", themingHandler.currentThemeType.ID),
                        assembleText("flairDecorContest.GUI_FLAIR_TOP_LEFT", themingHandler.flairDecorContest.GUI_FLAIR_TOP_LEFT.getPath()),
                        assembleText("flairDecorContest.GUI_FLAIR_TOP_RIGHT", themingHandler.flairDecorContest.GUI_FLAIR_TOP_RIGHT.getPath()),
                        assembleText("flairDecorContest.GUI_FLAIR_BOTTOM_LEFT", themingHandler.flairDecorContest.GUI_FLAIR_BOTTOM_LEFT.getPath()),
                        assembleText("flairDecorContest.GUI_FLAIR_BOTTOM_RIGHT", themingHandler.flairDecorContest.GUI_FLAIR_BOTTOM_RIGHT.getPath()),
                        assembleText("flairDecorFishTracker.GUI_FLAIR_TOP_LEFT", themingHandler.flairDecorFishTracker.GUI_FLAIR_TOP_LEFT.getPath()),
                        assembleText("flairDecorFishTracker.GUI_FLAIR_TOP_RIGHT", themingHandler.flairDecorFishTracker.GUI_FLAIR_TOP_RIGHT.getPath()),
                        assembleText("flairDecorFishTracker.GUI_FLAIR_BOTTOM_LEFT", themingHandler.flairDecorFishTracker.GUI_FLAIR_BOTTOM_LEFT.getPath()),
                        assembleText("flairDecorFishTracker.GUI_FLAIR_BOTTOM_RIGHT", themingHandler.flairDecorFishTracker.GUI_FLAIR_BOTTOM_RIGHT.getPath()),
                        assembleText("flairDecorNotification.GUI_FLAIR_TOP_LEFT", themingHandler.flairDecorNotification.GUI_FLAIR_TOP_LEFT.getPath()),
                        assembleText("flairDecorNotification.GUI_FLAIR_TOP_RIGHT", themingHandler.flairDecorNotification.GUI_FLAIR_TOP_RIGHT.getPath()),
                        assembleText("flairDecorNotification.GUI_FLAIR_BOTTOM_LEFT", themingHandler.flairDecorNotification.GUI_FLAIR_BOTTOM_LEFT.getPath()),
                        assembleText("flairDecorNotification.GUI_FLAIR_BOTTOM_RIGHT", themingHandler.flairDecorNotification.GUI_FLAIR_BOTTOM_RIGHT.getPath()),
                        assembleText("flairDecorPetEquip.GUI_FLAIR_TOP_LEFT", themingHandler.flairDecorPetEquip.GUI_FLAIR_TOP_LEFT.getPath()),
                        assembleText("flairDecorPetEquip.GUI_FLAIR_TOP_RIGHT", themingHandler.flairDecorPetEquip.GUI_FLAIR_TOP_RIGHT.getPath()),
                        assembleText("flairDecorPetEquip.GUI_FLAIR_BOTTOM_LEFT", themingHandler.flairDecorPetEquip.GUI_FLAIR_BOTTOM_LEFT.getPath()),
                        assembleText("flairDecorPetEquip.GUI_FLAIR_BOTTOM_RIGHT", themingHandler.flairDecorPetEquip.GUI_FLAIR_BOTTOM_RIGHT.getPath()),
                        assembleText("flairDecorQuest.GUI_FLAIR_TOP_LEFT", themingHandler.flairDecorQuest.GUI_FLAIR_TOP_LEFT.getPath()),
                        assembleText("flairDecorQuest.GUI_FLAIR_TOP_RIGHT", themingHandler.flairDecorQuest.GUI_FLAIR_TOP_RIGHT.getPath()),
                        assembleText("flairDecorQuest.GUI_FLAIR_BOTTOM_LEFT", themingHandler.flairDecorQuest.GUI_FLAIR_BOTTOM_LEFT.getPath()),
                        assembleText("flairDecorQuest.GUI_FLAIR_BOTTOM_RIGHT", themingHandler.flairDecorQuest.GUI_FLAIR_BOTTOM_RIGHT.getPath())
                ));
            }
            case CHATSCREEN -> {
                ChatScreenHandler chatScreenHandler = ChatScreenHandler.instance();

                textList.add(assembleText("screenInit", chatScreenHandler.screenInit));
            }
            case DISCORD -> {
                DiscordHandler discordHandler = DiscordHandler.instance();
            }
            case FISHINGSTATS -> {
                FishingStatsHandler fishingStatsHandler = FishingStatsHandler.instance();
            }
            case INVENTORYSCREEN -> {
                InventoryScreenHandler inventoryScreenHandler = InventoryScreenHandler.instance();

                textList.addAll(List.of(
                        assembleText("screenInit", inventoryScreenHandler.screenInit),
                        assembleText("isRecipeBookOpen", inventoryScreenHandler.isRecipeBookOpen)
                ));
            }
            case KEYBIND -> {
                KeybindHandler keybindHandler = KeybindHandler.instance();

                textList.addAll(List.of(
                        assembleText("openConfigKeybind", keybindHandler.openConfigKeybind.isPressed()),
                        assembleText("openExtraInfoKeybind", keybindHandler.openExtraInfoKeybind.isPressed()),
                        assembleText("showExtraInfo", keybindHandler.showExtraInfo)
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
        LOOKTICK(6, "LookTickhandler"),
        NOTIFICATIONSOUND(7, "NotificationSoundHandler"),
        PETCALCULATOR(8, "PetCalculatorHandler"),
        PETEQUIP(9, "PetEquipHandler"),
        PROFILEDATA(10, "ProfileDataHandler"),
        RAYTRACING(11, "RayTracingHandler"),
        SCOREBOARD(12, "ScoreboardHandler"),
        TAB(13, "TabHandler"),
        TITLE(14, "TitleHandler"),
        PETTOOLTIP(15, "PetTooltipHandler"),
        BOSSBAR(16, "BossBarHandler"),
        ARMOR(17, "ArmorHandler"),
        FISHINGROD(18, "FishingRodHandler"),
        CREW(19, "CrewHandler"),
        QUEST(20, "QuestHandler"),
        STAFF(21, "StaffHandler"),
        STATSIMPORT(22, "StatsImportHandler"),
        THEMING(23, "ThemingHandler"),
        CHATSCREEN(24, "ChatScreenHandler"),
        DISCORD(25, "DiscordHandler"),
        FISHINGSTATS(26, "FishingStatsHandler"),
        INVENTORYSCREEN(27, "InventoryScreenHandler"),
        KEYBIND(28, "KeybindHandler")
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
