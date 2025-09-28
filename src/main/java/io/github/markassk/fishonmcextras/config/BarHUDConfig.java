package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class BarHUDConfig {
    public static class BarHUD {
        public boolean showBar = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showClimate = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
    }
}
