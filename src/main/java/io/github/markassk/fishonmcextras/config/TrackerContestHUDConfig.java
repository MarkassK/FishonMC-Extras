package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerContestHUDConfig {
    public static class ContestTracker {
        public boolean showContest = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showFullContest = true;
        @ConfigEntry.Gui.Tooltip
        public boolean refreshOnContestPB = true;
        @ConfigEntry.Gui.Tooltip
        public boolean recieveLocalPBs = true;
        public boolean rightAlignment = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 0;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 0;
    }
}
