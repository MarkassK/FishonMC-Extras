package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LocationInfo;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.*;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NotificationHudHandler {
    private static NotificationHudHandler INSTANCE = new NotificationHudHandler();

    public static NotificationHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleNotificationText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        List<Text> textList = new ArrayList<>();

        if (config.notifications.showWarningHud) {
            // No Pet equipped Warning
            if(config.petEquipTracker.warningOptions.showPetEquipWarningHUD
                    && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET
            ) {
                textList.add(Text.empty());
                textList.add(Text.literal("No pet equipped.").formatted(Formatting.RED));
            }

            // Full Inventory Warning
            if(config.fullInventoryTracker.showFullInventoryWarningHUD
                    && FullInventoryHandler.instance().isOverThreshold
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Inventory almost full. Slots left: ").formatted(Formatting.RED),
                        Text.literal(String.valueOf(FullInventoryHandler.instance().slotsLeft)).formatted(Formatting.WHITE),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }

            // Wrong Armor Warning
            if(config.equipmentTracker.showArmorWarningHUD
                    && ArmorHandler.instance().isWrongChestplateClimate
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("You have equipped a ").formatted(Formatting.RED),
                        ArmorHandler.instance().currentChestplateItem.getName(),
                        Text.literal(" in a ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).CLIMATE.TAG,
                        Text.literal(" location").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)

                ));
            }
            if(config.equipmentTracker.showArmorWarningHUD
                    && ArmorHandler.instance().isWrongLeggingsClimate
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("You have equipped a ").formatted(Formatting.RED),
                        ArmorHandler.instance().currentLeggingsItem.getName(),
                        Text.literal(" in a ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).CLIMATE.TAG,
                        Text.literal(" location").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)

                ));
            }
            if(config.equipmentTracker.showArmorWarningHUD
                    && ArmorHandler.instance().isWrongBootsClimate
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("You have equipped a ").formatted(Formatting.RED),
                        ArmorHandler.instance().currentBootsItem.getName(),
                        Text.literal(" in a ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).CLIMATE.TAG,
                        Text.literal(" location").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)

                ));
            }

            // Wrong Bait Warning
            if(config.baitTracker.showBaitWarningHUD
                    && FishingRodHandler.instance().isWrongBait
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                if(FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Bait bait) {
                    textList.add(Text.empty());
                    textList.add(TextHelper.concat(
                            Text.literal("Your ").formatted(Formatting.RED),
                            Text.literal(TextHelper.upperCaseAllFirstCharacter(bait.name)).formatted(Formatting.WHITE),
                            Text.literal(" has no use in ").formatted(Formatting.RED),
                            LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER.TAG,
                            Text.literal(" here").formatted(Formatting.RED),
                            Text.literal(".").formatted(Formatting.RED)
                    ));
                } else if(FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Lure lure) {
                    textList.add(Text.empty());
                    textList.add(TextHelper.concat(
                            Text.literal("Your ").formatted(Formatting.RED),
                            Text.literal(lure.name).formatted(Formatting.WHITE),
                            Text.literal(" has no use in ").formatted(Formatting.RED),
                            LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER.TAG,
                            Text.literal(" here").formatted(Formatting.RED),
                            Text.literal(".").formatted(Formatting.RED)
                    ));
                }
            }

            // Wrong Rod Parts Warning
            if(config.equipmentTracker.showLineWarningHUD
                    && FishingRodHandler.instance().isWrongLine
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Your ").formatted(Formatting.RED),
                        Text.literal(FishingRodHandler.instance().fishingRod.line.name).formatted(Formatting.WHITE),
                        Text.literal(" has no use in ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER.TAG,
                        Text.literal(" here").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }
            if(config.equipmentTracker.showPoleWarningHUD
                    && FishingRodHandler.instance().isWrongPole
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Your ").formatted(Formatting.RED),
                        Text.literal(FishingRodHandler.instance().fishingRod.pole.name).formatted(Formatting.WHITE),
                        Text.literal(" has no use in ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER.TAG,
                        Text.literal(" here").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }
            if(config.equipmentTracker.showReelWarningHUD
                    && FishingRodHandler.instance().isWrongReel
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Your ").formatted(Formatting.RED),
                        Text.literal(FishingRodHandler.instance().fishingRod.reel.name).formatted(Formatting.WHITE),
                        Text.literal(" has no use in ").formatted(Formatting.RED),
                        LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER.TAG,
                        Text.literal(" here").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }

            if(config.petEquipTracker.warningOptions.showWrongPetWarningHUD
                    && PetEquipHandler.instance().isWrongPet()
                    && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.HAS_PET
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
            ) {
                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Your ").formatted(Formatting.RED),
                        ProfileDataHandler.instance().profileData.equippedPet.pet.TAG,
                        Text.literal(" has no use in ").formatted(Formatting.RED),
                        BossBarHandler.instance().currentLocation.TAG,
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }

            if(config.weatherTracker.showAlertHUD
                    && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                    && System.currentTimeMillis() - WeatherHandler.instance().weatherChangedAtTime <= config.weatherTracker.alertDismissSeconds * 1000L
            ) {
                int seconds = config.weatherTracker.alertDismissSeconds - ((int) (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - WeatherHandler.instance().weatherChangedAtTime) % 60));

                if (Objects.equals(WeatherHandler.instance().currentWeather, Constant.THUNDERSTORM.ID)) {
                    textList.add(Text.empty());
                    textList.add(TextHelper.concat(
                            Constant.THUNDERSTORM.TAG.copy().withColor(Constant.THUNDERSTORM.COLOR),
                            Text.literal(" Lightning Storm").formatted(Formatting.YELLOW, Formatting.BOLD),
                            Text.literal(" Alert!").formatted(Formatting.YELLOW)
                    ));
                    textList.add(TextHelper.concat(
                            Text.literal("ᴛʜɪѕ ᴀʟᴇʀᴛ ᴡɪʟʟ ʙᴇ ᴅɪѕᴍɪѕѕᴇᴅ ɪɴ ").formatted(Formatting.GRAY),
                            Text.literal("" + seconds),
                            Text.literal(" ѕᴇᴄᴏɴᴅѕ").formatted(Formatting.GRAY)
                    ));
                }
            }

            if(config.timerTracker.baitShopNotification
                    && System.currentTimeMillis() - TimerHandler.instance().baitShopAlertTime <= config.timerTracker.alertDismissSeconds * 1000L
            ) {
                int seconds = config.timerTracker.alertDismissSeconds - ((int) (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - TimerHandler.instance().baitShopAlertTime) % 60));

                textList.add(Text.empty());
                textList.add(TextHelper.concat(
                        Text.literal("Tackle Shop").formatted(Formatting.YELLOW, Formatting.BOLD),
                        Text.literal(" has been restocked!").formatted(Formatting.WHITE)
                ));
                textList.add(TextHelper.concat(
                        Text.literal("ᴛʜɪѕ ɴᴏᴛɪꜰɪᴄᴀᴛɪᴏɴ ᴡɪʟʟ ʙᴇ ᴅɪѕᴍɪѕѕᴇᴅ ɪɴ ").formatted(Formatting.GRAY),
                        Text.literal("" + seconds),
                        Text.literal(" ѕᴇᴄᴏɴᴅѕ").formatted(Formatting.GRAY)
                ));
            }
        }

        if(ScoreboardHandler.instance().noScoreBoard) {
            textList.add(Text.empty());
            textList.add(Text.literal("Turn on scoreboard in server settings.").formatted(Formatting.RED));
            if(!config.scoreboardTracker.hideScoreboard) {
                textList.add(TextHelper.concat(
                        Text.literal("And instead, turn off scoreboard in ").formatted(Formatting.RED),
                        Text.literal("FoE config ").formatted(Formatting.AQUA),
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        KeybindHandler.instance().openConfigKeybind.getBoundKeyLocalizedText().copy().formatted(Formatting.YELLOW),
                        Text.literal(" Key").formatted(Formatting.YELLOW),
                        Text.literal(")").formatted(Formatting.DARK_GRAY),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            } else {
                textList.add(TextHelper.concat(
                        Text.literal("It will be automatically be hidden by FoE").formatted(Formatting.RED),
                        Text.literal(".").formatted(Formatting.RED)
                ));
            }
        }

        if(CrewHandler.instance().isNotInitialized) {
            textList.add(Text.empty());
            textList.add(TextHelper.concat(
                    Text.literal("Please do ").formatted(Formatting.RED),
                    Text.literal("/crew ").formatted(Formatting.AQUA),
                    Text.literal("to initialize the Crew Tracker.").formatted(Formatting.RED)
            ));
            textList.add(TextHelper.concat(
                    Text.literal("If you don't have a crew, do ").formatted(Formatting.RED),
                    Text.literal("/foe nocrew").formatted(Formatting.AQUA),
                    Text.literal(".").formatted(Formatting.RED)
            ));
        }

        if(!QuestHandler.instance().isQuestInitialized()
                && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
        ) {
            textList.add(Text.empty());
            textList.add(TextHelper.concat(
                    Text.literal("Please do ").formatted(Formatting.RED),
                    Text.literal("/quest ").formatted(Formatting.AQUA),
                    Text.literal("to initialize the Quest Tracker").formatted(Formatting.RED)
            ));
            textList.add(TextHelper.concat(
                    Text.literal("for ").formatted(Formatting.RED),
                    BossBarHandler.instance().currentLocation.TAG,
                    Text.literal(".").formatted(Formatting.RED)
            ));
        }

        if(!ProfileDataHandler.instance().profileData.isStatsInitialized) {
            textList.add(Text.empty());
            textList.add(TextHelper.concat(
                    Text.literal("Please do ").formatted(Formatting.RED),
                    Text.literal("/stats ").formatted(Formatting.AQUA),
                    Text.literal("and press the ").formatted(Formatting.RED),
                    Text.literal("Import Stats ").formatted(Formatting.GREEN),
                    Text.literal("button").formatted(Formatting.RED)
            ));
            textList.add(TextHelper.concat(
                    Text.literal("to import your stats into FoE.").formatted(Formatting.RED)
            ));
            textList.add(TextHelper.concat(
                    Text.literal("Do ").formatted(Formatting.RED),
                    Text.literal("/foe cancelimport ").formatted(Formatting.AQUA),
                    Text.literal("to dismiss this import stats notification.").formatted(Formatting.RED)
            ));
        }

        if(config.crewTracker.crewChatLocation == CrewHandler.CrewChatLocation.IN_NOTIFICATION
                && ProfileDataHandler.instance().profileData.isInCrewChat
                && ChatScreenHandler.instance().screenInit) {
            textList.add(Text.empty());
            textList.add(TextHelper.concat(
                    Text.literal("You are in ").formatted(Formatting.RED),
                    Text.literal("Crew Chat").formatted(Formatting.GREEN)
            ));
        }

        if(!textList.isEmpty() && ThemingHandler.instance().currentThemeType == Theming.ThemeType.OFF) {
            textList.addFirst(getTitle().copy().formatted(Formatting.GRAY));
        } else if(!textList.isEmpty() && Objects.equals(textList.getFirst(), Text.empty())) {
            textList.removeFirst();
        }

        return textList;
    }

    public Text getTitle() {
        return Text.literal("ɴᴏᴛɪꜰɪᴄᴀᴛɪᴏɴѕ").formatted(Formatting.BOLD);
    }
}
