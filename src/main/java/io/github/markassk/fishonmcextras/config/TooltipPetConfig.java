package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TooltipPetConfig {
    public static class PetTooltip {
        public boolean showPetPercentages = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 2)
        public int decimalPlaces = 1;
    }
}
