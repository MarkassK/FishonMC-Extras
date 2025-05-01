package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerQuestHUDConfig {
    public static class QuestTracker {
        public boolean showQuestHud = true;
        public boolean rightAlignment = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 0;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 100;
    }
}
