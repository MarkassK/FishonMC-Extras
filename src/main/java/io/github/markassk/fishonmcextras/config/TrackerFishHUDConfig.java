package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFishHUDConfig {
    public static class FishTracker {
        public boolean showFishTrackerHUD = true;
        @ConfigEntry.Gui.Tooltip()
        public boolean isFishTrackerOnTimer = true;
        @ConfigEntry.BoundedDiscrete(min = 30, max = 300)
        @ConfigEntry.Gui.Tooltip
        public int autoPauseTimer = 60;

        @ConfigEntry.Gui.CollapsibleObject
        public FishTrackerToggles fishTrackerToggles = new FishTrackerToggles();
        public static class FishTrackerToggles {
            @ConfigEntry.Gui.CollapsibleObject
            public GeneralToggles generalToggles = new GeneralToggles();
            public static class GeneralToggles {
                public boolean showFishCaught = true;
                @ConfigEntry.Gui.Tooltip
                public boolean showTimer = true;
                public boolean showFishPerHour = true;
                public boolean showTotalXp = true;
                public boolean showTotalValue = true;
                public boolean showPetCaught = true;
                public boolean showPetPerHour = true;
                public boolean showShardCaught = true;
                public boolean showShardPerHour = true;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public RarityToggles rarityToggles = new RarityToggles();
            public static class RarityToggles {
                @ConfigEntry.Gui.Tooltip
                public boolean showRarities = true;
                public boolean showCommon = true;
                public boolean showRare = true;
                public boolean showEpic = true;
                public boolean showLegendary = true;
                public boolean showMythical = true;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public FishSizeToggles fishSizeToggles = new FishSizeToggles();
            public static class FishSizeToggles {
                @ConfigEntry.Gui.Tooltip
                public boolean showFishSizes = true;
                public boolean showBaby = true;
                public boolean showJuvenile = true;
                public boolean showAdult = true;
                public boolean showLarge = true;
                public boolean showGigantic = true;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public VariantToggles variantToggles = new VariantToggles();
            public static class VariantToggles {
                @ConfigEntry.Gui.Tooltip
                public boolean showVariants = true;
                public boolean showAlbino = true;
                public boolean showMelanistic = true;
                public boolean showTrophy = true;
                public boolean showFabled = false;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public OtherToggles otherToggles = new OtherToggles();
            public static class OtherToggles {
                public boolean showPercentages = true;
            }
        }

        public boolean rightAlignment = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 1;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 2;
    }
}
