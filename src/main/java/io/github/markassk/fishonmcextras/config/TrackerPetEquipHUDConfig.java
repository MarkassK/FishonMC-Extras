package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.WarningHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerPetEquipHUDConfig {
    public static class PetEquipTracker {
        @ConfigEntry.Gui.Tooltip
        public boolean showPetEquipTrackerHUD = true;

        @ConfigEntry.Gui.CollapsibleObject
        public ActivePetHUDOptions activePetHUDOptions = new ActivePetHUDOptions();
        public static class ActivePetHUDOptions {
            @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
            public int fontSize = 10;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int backgroundOpacity = 40;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudX = 65;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudY = 90;

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
            public WarningHandler.WarningSound petEquipWarningSound = WarningHandler.WarningSound.DIDGERIDOO;
        }
    }
}
