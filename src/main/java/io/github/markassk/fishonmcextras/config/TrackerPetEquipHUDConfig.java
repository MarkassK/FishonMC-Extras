package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerPetEquipHUDConfig {
    public static class PetEquipTracker {
        @ConfigEntry.Gui.Tooltip
        public boolean showPetEquipTracker = true;

        @ConfigEntry.Gui.CollapsibleObject
        public ActivePetHUDOptions activePetHUDOptions = new ActivePetHUDOptions();
        public static class ActivePetHUDOptions {
            @ConfigEntry.Gui.Tooltip
            public boolean showPetEquipTrackerHUD = true;
            @ConfigEntry.ColorPicker
            public int fontColor = 0x00FF00;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
            public int fontSize = 10;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudX = 65;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int hudY = 96;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public PetEquipWarningHUDOptions petEquipWarningHUDOptions = new PetEquipWarningHUDOptions();
        public static class PetEquipWarningHUDOptions {
            @ConfigEntry.Gui.Tooltip
            public boolean showPetEquipWarningHUD = true;
            public boolean textFlashes = true;
            @ConfigEntry.ColorPicker
            public int fontColor = 0xFF0000;
            @ConfigEntry.BoundedDiscrete(min = 10, max = 40)
            public int fontSize = 18;
        }
    }
}
