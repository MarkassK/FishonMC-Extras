package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFullInventoryHUDConfig {
    public static class FullInventoryTracker {
        @ConfigEntry.Gui.Tooltip
        public boolean showFullInventoryWarningHUD = true;
    }
}
