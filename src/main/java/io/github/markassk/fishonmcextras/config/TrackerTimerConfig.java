package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerTimerConfig {
    public static class TimerTracker {
        public boolean baitShopNotification = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 300)
        public int alertDismissSeconds = 15;

        @ConfigEntry.Gui.Excluded
        public long hiddenOffsetBaitShop = 0L;
        
        public boolean showMoonTimerWidget = true;
    }
}
