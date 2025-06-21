package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.NotificationSoundHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerWeatherConfig {
    public static class WeatherTracker {
        public boolean showAlertHUD = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
        public int alertDismissSeconds = 15;
        @ConfigEntry.Gui.Tooltip
        public boolean useAlertWarningSound = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int intervalWarningSound = 5;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public NotificationSoundHandler.SoundType alertSoundType = NotificationSoundHandler.SoundType.BELL;
    }
}
