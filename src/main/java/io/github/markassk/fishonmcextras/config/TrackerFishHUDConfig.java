package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFishHUDConfig {
    public static class FishTracker {
        public boolean showFishTrackerHUD = true;
        @ConfigEntry.Gui.Tooltip()
        public boolean isFishTrackerOnTimer = false;
        public boolean showTimerOnAllTime = true;
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
                public boolean showTotalXp = false;
                public boolean showTotalValue = false;
                public boolean showPetCaught = true;
                public boolean showPetPerHour = false;
                public boolean showShardCaught = true;
                public boolean showShardPerHour = false;
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
                public boolean showSpecial = false;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public FishSizeToggles fishSizeToggles = new FishSizeToggles();
            public static class FishSizeToggles {
                @ConfigEntry.Gui.Tooltip
                public boolean showFishSizes = false;
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
            public DryStreakToggles dryStreakToggles = new DryStreakToggles();
            public static class DryStreakToggles {
                @ConfigEntry.Gui.PrefixText
                public boolean showCommon = false;
                public boolean showRare = false;
                public boolean showEpic = false;
                public boolean showLegendary = false;
                public boolean showMythical = false;
                public boolean showSpecial = true;
                @ConfigEntry.Gui.PrefixText
                public boolean showBaby = false;
                public boolean showJuvenile = false;
                public boolean showAdult = false;
                public boolean showLarge = false;
                public boolean showGigantic = true;
                @ConfigEntry.Gui.PrefixText
                public boolean showAlbino = true;
                public boolean showMelanistic = true;
                public boolean showTrophy = true;
                public boolean showFabled = false;
                @ConfigEntry.Gui.PrefixText
                public boolean showPet = false;
                public boolean showShard = false;
            }

            @ConfigEntry.Gui.CollapsibleObject
            public OtherToggles otherToggles = new OtherToggles();
            public static class OtherToggles {
                public boolean showPercentages = true;
                @ConfigEntry.Gui.Tooltip
                public boolean useNewTitle = true;
                @ConfigEntry.Gui.Tooltip
                public boolean showStatsOnCatch = true;
                @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
                public int showStatsOnCatchTime = 5;
            }
        }

        public boolean rightAlignment = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 0;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 30;
    }
}
