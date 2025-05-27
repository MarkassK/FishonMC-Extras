package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.NotificationSoundHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFullInventoryHUDConfig {
    public static class FullInventoryTracker {
        public boolean showFullInventoryWarningHUD = true;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 35)
        public int fullInventoryWarningThreshold = 3;
        @ConfigEntry.Gui.Tooltip
        public boolean useInventoryWarningSound = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int timeInventoryWarningSound = 10;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public NotificationSoundHandler.SoundType fullInventorySoundType = NotificationSoundHandler.SoundType.DIDGERIDOO;
    }
}
