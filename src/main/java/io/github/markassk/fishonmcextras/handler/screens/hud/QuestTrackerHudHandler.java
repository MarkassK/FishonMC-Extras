package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.BossBarHandler;
import io.github.markassk.fishonmcextras.handler.QuestHandler;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class QuestTrackerHudHandler {
    private static QuestTrackerHudHandler INSTANCE = new QuestTrackerHudHandler();

    public static QuestTrackerHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestTrackerHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleQuestText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        List<Text> textList = new ArrayList<>();

        if (ThemingHandler.instance().currentThemeType == Theming.ThemeType.OFF) {
            if(config.questTracker.rightAlignment) {
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

        if(QuestHandler.instance().isQuestInitialized()) {
            List<QuestHandler.Quest> activeQuests = BossBarHandler.instance().currentLocation == Constant.SPAWNHUB ? QuestHandler.instance().activeQuests.get(Constant.CYPRESS_LAKE) : QuestHandler.instance().activeQuests.get(BossBarHandler.instance().currentLocation);

            textList.add(TextHelper.concat(
                    Text.literal("ʟᴏᴄ.: ").formatted(Formatting.GRAY),
                    BossBarHandler.instance().currentLocation == Constant.SPAWNHUB ? Constant.CYPRESS_LAKE.TAG : BossBarHandler.instance().currentLocation.TAG
            ));

            if(activeQuests != null) {
                activeQuests.forEach(quest -> {
                    if(quest.questDone()) {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                Text.literal("ꞯᴜᴇѕᴛ ᴅᴏɴᴇ").formatted(Formatting.GREEN, Formatting.BOLD)
                        ));
                    } else if (!quest.isStarted) {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                Text.literal("ɴᴏᴛ ѕᴛᴀʀᴛᴇᴅ").formatted(Formatting.WHITE)
                        ));
                    } else {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                quest.goal.TAG,
                                Text.literal(" (").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.progress)).formatted(Formatting.YELLOW),
                                Text.literal("/").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.needed)).formatted(Formatting.WHITE),
                                Text.literal(")").formatted(Formatting.GRAY)
                        ));
                    }
                });
            }
        }

        return textList;
    }

    public Text getTitle() {
        return Text.literal("ꞯᴜᴇѕᴛѕ").formatted(Formatting.BOLD);
    }
}
