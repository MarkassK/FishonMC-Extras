package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.AutoConfig;

@Config(name = "fishonmcextras")
public class FishOnMCExtrasConfig implements ConfigData {

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
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showTimeSinceReset = false;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showFishPerHour = true;

        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showRarities = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showBaby = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showJuvenile = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showAdult = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showLarge = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showGigantic = true;

        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showVariants = false;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showAlbino = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showMelanistic = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showTrophy = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showFabled = false;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showZombie = false;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showUnique = false;

    }

    public static class PetTooltipToggles {
        @ConfigEntry.Gui.Tooltip()
        public boolean showFullRating = true;
        @ConfigEntry.Gui.Tooltip()
        public boolean showIndividualRating = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean showAccuratePercentage = true;
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
        @ConfigEntry.Gui.Tooltip()
        public boolean showItemFrameTooltip = true;
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
        @ConfigEntry.BoundedDiscrete(min = 10, max = 40)
        public int FullInvFontSize = 10;
        @ConfigEntry.ColorPicker
        public int FullInvFontColor = 0xFF5555;
        public boolean FullInvHUDShadows = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 35)
        public int FullInvHUDWarningSlot = 3;

    }
}