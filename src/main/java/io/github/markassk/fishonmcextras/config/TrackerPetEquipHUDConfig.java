package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.NotificationSoundHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerPetEquipHUDConfig {
    public static class PetEquipTracker {
        @ConfigEntry.Gui.Tooltip
        public boolean showPetEquipTrackerHUD = true;

        @ConfigEntry.Gui.CollapsibleObject
        public ActivePetHUDOptions activePetHUDOptions = new ActivePetHUDOptions();
        public static class ActivePetHUDOptions {
            public boolean rightAlignment = true;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
            public int fontSize = 8;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int backgroundOpacity = 40;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudX = 0;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudY = 0;

        }

        @ConfigEntry.Gui.CollapsibleObject
        public WarningOptions warningOptions = new WarningOptions();
        public static class WarningOptions {
            @ConfigEntry.Gui.Tooltip
            public boolean showPetEquipWarningHUD = false;
            @ConfigEntry.Gui.Tooltip
            public boolean usePetEquipWarningSound = false;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
            public int timePetEquipWarningSound = 10;
            public NotificationSoundHandler.SoundType petEquipSoundType = NotificationSoundHandler.SoundType.DIDGERIDOO;
        }
    }
}
