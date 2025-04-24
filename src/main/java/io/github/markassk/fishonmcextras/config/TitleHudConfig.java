package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TitleHudConfig {
    public static class TitlePopup {
        public boolean useNewTitleSystem = true;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int scale = 10;
    }
}
