package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class FunConfig {
    public static class Fun {
        @ConfigEntry.Gui.Tooltip
        public boolean minigameOnBobber = false;
        @ConfigEntry.Gui.Tooltip
        public boolean immersionMode = false;
        public boolean isFoeTagPrefix = true;
    }
}
