package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LevelColors;
import io.github.markassk.fishonmcextras.handler.BossBarHandler;
import io.github.markassk.fishonmcextras.handler.LocationHandler;
import io.github.markassk.fishonmcextras.handler.ScoreboardHandler;
import io.github.markassk.fishonmcextras.handler.TabHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BarHudHandler {
    private static BarHudHandler INSTANCE = new BarHudHandler();

    public static BarHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BarHudHandler();
        }
        return INSTANCE;
    }

    public Text assembleLeftText() {
        int stringRepetitions = (int) Math.floor(ScoreboardHandler.instance().percentLevel * 100 / 10);

        return TextHelper.concat(
                TabHandler.instance().player,
                Text.literal(" [").formatted(Formatting.DARK_GRAY),
                Text.literal(String.valueOf(ScoreboardHandler.instance().level)).withColor(LevelColors.valueOfLvl(ScoreboardHandler.instance().level).color),
                Text.literal("] ").formatted(Formatting.DARK_GRAY),
                Text.literal("━".repeat(stringRepetitions)).formatted(Formatting.GREEN),
                Text.literal("━".repeat(10 - stringRepetitions)).formatted(Formatting.DARK_GRAY),
                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                Text.literal(TextHelper.fmt(ScoreboardHandler.instance().percentLevel * 100)).formatted(Formatting.GRAY),
                Text.literal("%").formatted(Formatting.GRAY),
                Text.literal(")").formatted(Formatting.DARK_GRAY)
        );
    }

    public Text assembleMiddleText() {
        Text weather;
        if(BossBarHandler.instance().weather.contains("☀")) {
            weather = Text.literal(BossBarHandler.instance().weather).formatted(Formatting.YELLOW);
        } else if(BossBarHandler.instance().weather.contains("☂")) {
            weather = Text.literal(BossBarHandler.instance().weather).formatted(Formatting.BLUE);
        } else {
            // Moon
            weather = Text.literal(BossBarHandler.instance().weather);
        }
        Text time = Text.empty();

        if(LocationHandler.instance().currentLocation != Constant.CREW_ISLAND) {
            time = Text.literal(" ").append(weather).append(Text.literal(" ")).append(Text.literal(BossBarHandler.instance().time).formatted(Formatting.WHITE)).append(BossBarHandler.instance().timeSuffix.contains("AM") ? Text.literal("ᴀᴍ").formatted(Formatting.GRAY) : Text.literal("ᴘᴍ").formatted(Formatting.GRAY));
        }

        return TextHelper.concat(
                Text.literal("\uF039 ").formatted(Formatting.WHITE),
                LocationHandler.instance().currentLocation != null ? LocationHandler.instance().currentLocation.TAG : Text.empty(),
                time
        );
    }

    public Text assembleRightText() {
        Text padding = Text.literal("    ");

        return TextHelper.concat(
                Text.literal("ʙᴀʟᴀɴᴄᴇ: ").formatted(Formatting.GRAY),
                Text.literal("$").formatted(Formatting.DARK_GREEN),
                Text.literal(ScoreboardHandler.instance().wallet).formatted(Formatting.GREEN),
                Text.literal(" \uF00C").formatted(Formatting.WHITE),
                Text.literal(ScoreboardHandler.instance().credits).formatted(Formatting.YELLOW),

                padding,


                Text.literal("ᴄᴀᴛᴄʜᴇѕ: ").formatted(Formatting.GRAY),
                Text.literal(ScoreboardHandler.instance().catches).formatted(Formatting.RED),
                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                Text.literal(ScoreboardHandler.instance().catchRate).formatted(Formatting.GREEN),
                Text.literal(")").formatted(Formatting.DARK_GRAY)

        );
    }
}
