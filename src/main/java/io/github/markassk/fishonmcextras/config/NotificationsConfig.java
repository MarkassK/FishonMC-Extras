package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class NotificationsConfig {
    public static class Notifications {
        @ConfigEntry.Gui.Tooltip
        public boolean showWarningHud = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 10;
    }
}
