package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.v1.hud.HudRenderer;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.AutoConfig;

import static io.github.markassk.fishonmcextras.config.ConfigConstants.TRACKERS;

@Config(name = "fishonmcextras")
public class FishOnMCExtrasConfig implements ConfigData {
    //region Old Config
    // ----------- Fish Options -----------
    //Fish HUD Options
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category(value = "fishoptions")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean fishHUD = true;

    @ConfigEntry.Category(value = "fishoptions")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean trackTimed = true;

    @ConfigEntry.Category(value = "fishoptions")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip()
    public FishHUDToggles fishHUDToggles = new FishHUDToggles();

    // ----------- Pet Options -----------
    // Pet Options
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category(value = "petoptions")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean petHUD = true;

    // Pet Tooltip Options
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category(value = "petoptions")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip()
    public PetTooltipToggles petTooltipToggles = new PetTooltipToggles();

    @ConfigEntry.Category(value = "petoptions")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip()
    public PetWarningHUDConfig petWarningHUDConfig = new PetWarningHUDConfig();

    @ConfigEntry.Category(value = "petoptions")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip()
    public PetActiveHUDConfig petActiveHUDConfig = new PetActiveHUDConfig();

    // ----------- Other Options -----------
    // Other Options
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category(value = "otheroptions")
    @ConfigEntry.Gui.Tooltip()
    public OtherHUDConfig otherHUDConfig = new OtherHUDConfig();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category(value = "otheroptions")
    @ConfigEntry.Gui.Tooltip()
    public FullInvHUDConfig fullInvHUDConfig = new FullInvHUDConfig();


    // ----------- HUD Styling -----------
    @ConfigEntry.Category(value = "textStyling")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip()
    public FishHUDConfig fishHUDConfig = new FishHUDConfig();


    public static FishOnMCExtrasConfig getConfig() {
        return AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
    }

    public static class FishHUDToggles {
        @ConfigEntry.Gui.Tooltip
        public boolean showTimeSinceReset = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showFishCaught = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showFishPerHour = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showFishTotalXP = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showFishTotalValue = true;

        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public boolean showRarities = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showBaby = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showJuvenile = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showAdult = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showLarge = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showGigantic = true;

        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public boolean showVariants = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showAlbino = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showMelanistic = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showTrophy = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showFabled = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showZombie = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showUnique = false;

    }

    public static class PetTooltipToggles {
        @ConfigEntry.Gui.Tooltip()
        public boolean showFullRating = true;
        @ConfigEntry.Gui.Tooltip()
        public boolean showIndividualRating = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showAccuratePercentage = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 2)
        public int percentageDecimalPlaces = 1;
    }

    public static class PetWarningHUDConfig{
        public boolean enableWarning = true;
        public boolean flashWarning = true;
        public boolean petWarningHUDShadows = true;
        @ConfigEntry.ColorPicker
        public int warningColor = 0xFF0000; // Red by default
        @ConfigEntry.BoundedDiscrete(min = 10, max = 40)
        public int warningFontSize = 18;
    }

    public static class PetActiveHUDConfig{
        public boolean petActiveHUDShadows = true;
        @ConfigEntry.ColorPicker
        public int petActiveColor = 0x00FF00; // Green by default
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int petActiveFontSize = 10;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int petHUDX = 65;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int petHUDY = 96;
    }

    public static class OtherHUDConfig {
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip()
        public boolean showItemFrameTooltip = true;

        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip()
        public boolean showExtraFishingStats = true;
        public boolean showSizeRating = true;
        public boolean showWeight = true;
        public boolean showLength = true;
        @ConfigEntry.ColorPicker
        public int statColor = 0xAAAAAA;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int statHeight = 61;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
        public int statTime = 5;
    }

    public static class FishHUDConfig{
        @ConfigEntry.BoundedDiscrete(min = 30, max = 300)
        public int fishHUDAutoPause = 60;
        public boolean showRarityPercentages = true;
        public boolean showSizePercentages = true;
        public boolean showVariantPercentages = false;

        public boolean fishHUDShadows = true;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fishHUDFontSize = 8;
        @ConfigEntry.Gui.CollapsibleObject
        public FishHUDColorConfig fishHUDColorConfig = new FishHUDColorConfig();

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int fishHUDX = 1;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int fishHUDY = 2;
    }

    public static class FishHUDColorConfig{
        @ConfigEntry.ColorPicker
        public int fishHUDCaughtColor = 0xFFFFFF; // White by default
        @ConfigEntry.ColorPicker
        public int fishHUDTimerColor = 0xFFFFFF; // White by default
        @ConfigEntry.ColorPicker
        public int fishHUDXPColor = 0xFFFFFF; // White by default
        @ConfigEntry.ColorPicker
        public int fishHUDValueColor = 0xFFFFFF; // White by default
        @ConfigEntry.ColorPicker
        public int fishHUDUniqueColor = 0x00FFFF; // Cyan by default
    }

    public static class FullInvHUDConfig{
        public boolean FullInvWarningEnable = true;
        @ConfigEntry.BoundedDiscrete(min = 10, max = 40)
        public int FullInvFontSize = 10;
        @ConfigEntry.ColorPicker
        public int FullInvFontColor = 0xFF5555;
        public boolean FullInvHUDShadows = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 35)
        public int FullInvHUDWarningSlot = 3;
        @ConfigEntry.BoundedDiscrete(min = 45, max = 500)
        public int FullInvHUDHeight = 45;
        public boolean FullInvPlayWarningSound = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int FullInvPlayWarningSoundTime = 5;
        public HudRenderer.InventorySound FullInvWarningSoundSample = HudRenderer.InventorySound.PLING;
    }
    //endregion

    //region Trackers
    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerFishHUDConfig.FishTracker fishTracker = new TrackerFishHUDConfig.FishTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerPetEquipHUDConfig.PetEquipTracker petEquipTracker = new TrackerPetEquipHUDConfig.PetEquipTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerFullInventoryHUDConfig.FullInventoryTracker fullInventoryTracker = new TrackerFullInventoryHUDConfig.FullInventoryTracker();
    //endregion
}