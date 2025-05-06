package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ContestHandler;
import io.github.markassk.fishonmcextras.handler.LocationHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ContestHudHandler {
    private static ContestHudHandler INSTANCE = new ContestHudHandler();

    public static ContestHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ContestHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleContestText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        List<Text> textList = new ArrayList<>();



        long timeLeftMinutes = TimeUnit.MILLISECONDS.toMinutes(ContestHandler.instance().timeLeft) % 60;
        long timeLeftSeconds = TimeUnit.MILLISECONDS.toSeconds(ContestHandler.instance().timeLeft) % 60;

        long timeAgo = System.currentTimeMillis() - ContestHandler.instance().lastUpdated;
        long lastUpdatedMinutes = TimeUnit.MILLISECONDS.toMinutes(timeAgo) % 60;
        long lastUpdatedSeconds = TimeUnit.MILLISECONDS.toSeconds(timeAgo) % 60;

        Text location = Constant.valueOfTag(ContestHandler.instance().location) != null ? Objects.requireNonNull(Constant.valueOfTag(ContestHandler.instance().location)) == Constant.SPAWNHUB ? Constant.CYPRESS_LAKE.TAG : Constant.valueOfTag(ContestHandler.instance().location).TAG : Text.literal(ContestHandler.instance().location).formatted(Formatting.WHITE);



        if(!ContestHandler.instance().isReset) {
            if(ContestHandler.instance().isContest) {
                textList.add(TextHelper.concat(
                        Text.literal("ᴄᴏɴᴛᴇѕᴛ ᴛɪᴍᴇ ʟᴇꜰᴛ: ").formatted(Formatting.GRAY),
                        Text.literal(String.format("%02d:%02d", timeLeftMinutes, timeLeftSeconds)).formatted(Formatting.GREEN)
                ));
            } else if (config.contestTracker.showFullContest){
                textList.add(TextHelper.concat(
                        Text.literal("ᴄᴏɴᴛᴇѕᴛ ʀᴇѕᴜʟᴛѕ").formatted(Formatting.GRAY)
                ));
            }
            if(!Objects.equals(ContestHandler.instance().type, "") && config.contestTracker.showFullContest) {
                textList.add(TextHelper.concat(
                        Text.literal("ᴛʏᴘᴇ: ").formatted(Formatting.GRAY),
                        Text.literal(ContestHandler.instance().type).formatted(Formatting.WHITE)
                ));
                textList.add(TextHelper.concat(
                        Text.literal("ʟᴏᴄᴀᴛɪᴏɴ: ").formatted(Formatting.GRAY),
                        location
                ));
                if (Objects.equals(Objects.requireNonNull(Constant.valueOfTag(ContestHandler.instance().location)) == Constant.SPAWNHUB ? Constant.CYPRESS_LAKE.ID : Objects.requireNonNull(Constant.valueOfTag(ContestHandler.instance().location).ID), LocationHandler.instance().currentLocation.ID)) {
                    if(!Objects.equals(ContestHandler.instance().firstName, "")) {
                        textList.add(Text.empty());
                        textList.add(TextHelper.concat(
                                Text.literal("\uF060 ").formatted(Formatting.WHITE),
                                Text.literal(ContestHandler.instance().firstName).formatted(Formatting.WHITE),
                                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                                Text.literal(ContestHandler.instance().firstStat).formatted(Formatting.GRAY),
                                Text.literal(")").formatted(Formatting.DARK_GRAY)
                        ));
                    }
                    if(!Objects.equals(ContestHandler.instance().secondName, "")) {
                        textList.add(TextHelper.concat(
                                Text.literal("\uF061 ").formatted(Formatting.WHITE),
                                Text.literal(ContestHandler.instance().secondName).formatted(Formatting.WHITE),
                                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                                Text.literal(ContestHandler.instance().secondStat).formatted(Formatting.GRAY),
                                Text.literal(")").formatted(Formatting.DARK_GRAY)
                        ));
                    }
                    if(!Objects.equals(ContestHandler.instance().thirdName, "")) {
                        textList.add(TextHelper.concat(
                                Text.literal("\uF062 ").formatted(Formatting.WHITE),
                                Text.literal(ContestHandler.instance().thirdName).formatted(Formatting.WHITE),
                                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                                Text.literal(ContestHandler.instance().thirdStat).formatted(Formatting.GRAY),
                                Text.literal(")").formatted(Formatting.DARK_GRAY)
                        ));
                    }
                    if(!Objects.equals(ContestHandler.instance().firstName, "")) {
                        assert MinecraftClient.getInstance().player != null;
                        textList.add(TextHelper.concat(
                                Text.literal(ContestHandler.instance().rank).formatted(Formatting.GRAY),
                                Text.literal(" " + MinecraftClient.getInstance().player.getName().getString()).formatted(Formatting.YELLOW),
                                !Objects.equals(ContestHandler.instance().rankStat, "") ? Text.literal(" (").formatted(Formatting.DARK_GRAY).append(Text.literal(ContestHandler.instance().rankStat).formatted(Formatting.GRAY)).append(Text.literal(")").formatted(Formatting.DARK_GRAY)) : Text.empty()
                        ));
                        textList.add(Text.empty());
                    }
                    if(ContestHandler.instance().isContest) {
                        textList.add(TextHelper.concat(
                                Text.literal("ʟᴀѕᴛ ᴜᴘᴅᴀᴛᴇ ᴡᴀѕ ").formatted(Formatting.GRAY),
                                Text.literal(String.format("%02d:%02d", lastUpdatedMinutes, lastUpdatedSeconds)).formatted(Formatting.GREEN),
                                Text.literal(" ᴀɢᴏ").formatted(Formatting.GRAY)
                        ));
                    }
                }
            } else if (config.contestTracker.showFullContest) {
                textList.add(TextHelper.concat(
                        Text.literal("ᴡᴀɪᴛɪɴɢ ɴᴇxᴛ ᴜᴘᴅᴀᴛᴇ...").formatted(Formatting.GRAY)
                ));
                textList.add(TextHelper.concat(
                        Text.literal("ᴏʀ ᴅᴏ ").formatted(Formatting.GRAY),
                        Text.literal("/contest").formatted(Formatting.AQUA)
                ));
            }
        } else if (!ContestHandler.instance().isContest && (ContestHandler.instance().isReset || !config.contestTracker.showFullContest)){
            textList.add(TextHelper.concat(
                    Text.literal("ɴᴇxᴛ ᴄᴏɴᴛᴇѕᴛ ɪɴ: ").formatted(Formatting.GRAY),
                    Text.literal(String.format("%02d:%02d", timeLeftMinutes, timeLeftSeconds)).formatted(Formatting.GREEN)
            ));
        }

        return textList;
    }
}
