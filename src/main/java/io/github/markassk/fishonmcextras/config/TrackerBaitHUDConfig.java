package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerBaitHUDConfig {
    public static class BaitTracker {
        public boolean showBaitHud = true;
        public boolean showBaitWarningHUD = true;
        public boolean rightAlignment = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 100;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
        public int offsetFromMiddle = 116;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
    }
}
