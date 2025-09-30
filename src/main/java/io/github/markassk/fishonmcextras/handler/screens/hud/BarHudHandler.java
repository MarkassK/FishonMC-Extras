package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LevelColors;
import io.github.markassk.fishonmcextras.handler.BossBarHandler;
import io.github.markassk.fishonmcextras.handler.ScoreboardHandler;
import io.github.markassk.fishonmcextras.handler.TabHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class BarHudHandler {
    private static BarHudHandler INSTANCE = new BarHudHandler();

    public static BarHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BarHudHandler();
        }
        return INSTANCE;
    }

    public Text assembleLeftText() {
        float repetitions = 100f / 20f;
        float stringRepetitions = (float) Math.floor(ScoreboardHandler.instance().percentLevel * 100);
        float restRepetitions = 100 - stringRepetitions;

        return TextHelper.concat(
                TabHandler.instance().player,
                Text.literal(" [").formatted(Formatting.DARK_GRAY),
                Text.literal(String.valueOf(ScoreboardHandler.instance().level)).withColor(LevelColors.valueOfLvl(ScoreboardHandler.instance().level).color),
                Text.literal("] ").formatted(Formatting.DARK_GRAY),
                Text.literal(" ".repeat(Math.round(stringRepetitions / repetitions))).formatted(Formatting.GREEN, Formatting.STRIKETHROUGH),
                Text.literal(" ".repeat(Math.round(restRepetitions / repetitions))).formatted(Formatting.DARK_GRAY, Formatting.STRIKETHROUGH),
                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                Text.literal(TextHelper.fmt(ScoreboardHandler.instance().percentLevel * 100)).formatted(Formatting.GRAY),
                Text.literal("%").formatted(Formatting.GRAY),
                Text.literal(")").formatted(Formatting.DARK_GRAY)
        );
    }

    public Text assembleMiddleText() {
        Text weather;
        if(BossBarHandler.instance().weather.contains(Constant.SUN.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.SUN.COLOR);
        } else if(BossBarHandler.instance().weather.contains(Constant.RAIN.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.RAIN.COLOR);
        } else if(BossBarHandler.instance().weather.contains(Constant.THUNDERSTORM.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.THUNDERSTORM.COLOR);
        } else if(BossBarHandler.instance().weather.contains(Constant.BLOOMINGOASIS.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.BLOOMINGOASIS.COLOR);
        } else if(BossBarHandler.instance().weather.contains(Constant.FABLEDWEATHER.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.FABLEDWEATHER.COLOR);
        } else if(BossBarHandler.instance().weather.contains(Constant.GOLDRUSH.ID)) {
            weather = Text.literal(BossBarHandler.instance().weather).withColor(Constant.GOLDRUSH.COLOR);
        } else{
            // Moon
            weather = Text.literal(BossBarHandler.instance().weather);
        }
        Text time = Text.empty();

        if(BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND) {
            time = Text.literal(" ").append(weather).append(Text.literal(" ")).append(Text.literal(BossBarHandler.instance().time).formatted(Formatting.WHITE)).append(BossBarHandler.instance().timeSuffix.contains("AM") ? Text.literal("ᴀᴍ").formatted(Formatting.GRAY) : Text.literal("ᴘᴍ").formatted(Formatting.GRAY));
        }

        Text locationCatch = !Objects.equals(ScoreboardHandler.instance().locationMin, ScoreboardHandler.instance().locationMax) && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND ? TextHelper.concat(
                Text.literal(" (").formatted(Formatting.DARK_GRAY),
                Text.literal(ScoreboardHandler.instance().locationMin).formatted(Formatting.GOLD),
                Text.literal("/").formatted(Formatting.GRAY),
                Text.literal(ScoreboardHandler.instance().locationMax).formatted(Formatting.GOLD),
                Text.literal(")").formatted(Formatting.DARK_GRAY)
        ) : Text.literal("");

        return TextHelper.concat(
                Text.literal("\uF039 ").formatted(Formatting.WHITE),
                BossBarHandler.instance().currentLocation != null ? BossBarHandler.instance().currentLocation.TAG : Text.empty(),
                locationCatch,
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
