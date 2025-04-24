package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.WarningHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFullInventoryHUDConfig {
    public static class FullInventoryTracker {
        @ConfigEntry.Gui.Tooltip
        public boolean showFullInventoryWarningHUD = true;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 35)
        public int fullInventoryWarningThreshold = 3;
        @ConfigEntry.Gui.Tooltip
        public boolean useInventoryWarningSound = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int timeInventoryWarningSound = 10;
        public WarningHandler.WarningSound fullInventoryWarningSound = WarningHandler.WarningSound.DIDGERIDOO;
    }
}
