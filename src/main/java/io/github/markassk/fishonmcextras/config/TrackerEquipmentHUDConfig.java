package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerEquipmentHUDConfig {
    public static class EquipmentTracker {
        public boolean showEquipmentHud = true;
        public boolean showArmorWarningHUD = true;
        public boolean showPoleWarningHUD = true;
        public boolean showReelWarningHUD = true;
        public boolean showLineWarningHUD = true;

        @ConfigEntry.Gui.CollapsibleObject
        public ArmorHUDOptions armorHUDOptions = new ArmorHUDOptions();
        public static class ArmorHUDOptions{
            @ConfigEntry.BoundedDiscrete(min = -300, max = 300)
            public int offsetFromMiddle = -1;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
            public int offsetFromBottom = 28;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public RodPartsHUDOptions rodPartsHUDOptions = new RodPartsHUDOptions();
        public static class RodPartsHUDOptions {
            @ConfigEntry.BoundedDiscrete(min = -300, max = 300)
            public int offsetFromMiddle = 1;
            @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
            public int offsetFromBottom = 28;
        }

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
    }
}
