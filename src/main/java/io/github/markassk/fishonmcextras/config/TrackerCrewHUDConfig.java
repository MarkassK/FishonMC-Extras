package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerCrewHUDConfig {
    public static class CrewTracker {
        public boolean showCrewNearby = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
    }
}
