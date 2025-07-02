package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class FunConfig {
    public static class Fun {
        @ConfigEntry.Gui.Tooltip
        public boolean minigameOnBobber = false;
        @ConfigEntry.Gui.Tooltip
        public boolean immersionMode = false;
        public boolean isFoeTagPrefix = true;
        public boolean hideArmor = false;
        @ConfigEntry.Gui.Tooltip
        public boolean biteBobber = false;
        public boolean lightOnBobber = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
        public int lightLevel = 10;
    }
}
