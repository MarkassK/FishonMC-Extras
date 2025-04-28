package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FishOnMCExtrasClient;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.FullInventoryHandler;
import io.github.markassk.fishonmcextras.handler.LocationHandler;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.handler.ScoreboardHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

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

        if(ScoreboardHandler.instance().noScoreBoard) {
            textList.add(Text.literal("Turn on scoreboard in server settings.").formatted(Formatting.RED));
            if(!config.scoreboardTracker.hideScoreboard) {
                textList.add(TextHelper.concat(
                        Text.literal("And instead, turn off scoreboard in ").formatted(Formatting.RED),
                        Text.literal("FoE config ").formatted(Formatting.AQUA),
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        FishOnMCExtrasClient.openConfigKeybind.getBoundKeyLocalizedText().copy().formatted(Formatting.YELLOW),
                        Text.literal(" Key").formatted(Formatting.YELLOW),
                        Text.literal(")").formatted(Formatting.DARK_GRAY)
                ));
            } else {
                textList.add(TextHelper.concat(
                        Text.literal("It will be automatically be hidden by FoE").formatted(Formatting.RED)
                ));
            }
        }

        // No Pet equipped Warning
        if(config.petEquipTracker.warningOptions.showPetEquipWarningHUD
                && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET
        ) {
            textList.add(Text.literal("No pet equipped").formatted(Formatting.RED));
        }

        // Full Inventory Warning
        if(config.fullInventoryTracker.showFullInventoryWarningHUD
                && FullInventoryHandler.instance().isOverThreshold
                && LocationHandler.instance().currentLocation != Constant.CREW_ISLAND
        ) {
            textList.add(TextHelper.concat(
                    Text.literal("Inventory almost full. Slots left: ").formatted(Formatting.RED),
                    Text.literal(String.valueOf(FullInventoryHandler.instance().slotsLeft)).formatted(Formatting.WHITE)
            ));
        }

        if(!textList.isEmpty()) {
            textList.addFirst(Text.literal("ɴᴏᴛɪꜰɪᴄᴀᴛɪᴏɴѕ").formatted(Formatting.GRAY));
        }

        return textList;
    }
}
