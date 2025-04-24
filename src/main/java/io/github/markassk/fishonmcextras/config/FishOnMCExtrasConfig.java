package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.AutoConfig;

import static io.github.markassk.fishonmcextras.config.ConfigConstants.*;

@Config(name = "fishonmcextras")
public class FishOnMCExtrasConfig implements ConfigData {
    public static FishOnMCExtrasConfig getConfig() {
        return AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
    }

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

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipPetConfig.PetTooltip petTooltip = new TooltipPetConfig.PetTooltip();

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipItemFrameConfig.ItemFrameTooltip itemFrameTooltip = new TooltipItemFrameConfig.ItemFrameTooltip();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public WarningConfig.Warning warning = new WarningConfig.Warning();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public TitleHudConfig.TitlePopup titlePopup = new TitleHudConfig.TitlePopup();
    //endregion
}