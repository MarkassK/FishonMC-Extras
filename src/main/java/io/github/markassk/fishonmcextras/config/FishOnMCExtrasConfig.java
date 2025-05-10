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

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerContestHUDConfig.ContestTracker contestTracker = new TrackerContestHUDConfig.ContestTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerEquipmentHUDConfig.EquipmentTracker equipmentTracker = new TrackerEquipmentHUDConfig.EquipmentTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerBaitHUDConfig.BaitTracker baitTracker = new TrackerBaitHUDConfig.BaitTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerCrewHUDConfig.CrewTracker crewTracker = new TrackerCrewHUDConfig.CrewTracker();

    @ConfigEntry.Category(value = TRACKERS)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerQuestHUDConfig.QuestTracker questTracker = new TrackerQuestHUDConfig.QuestTracker();

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipPetConfig.PetTooltip petTooltip = new TooltipPetConfig.PetTooltip();

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipItemFrameConfig.ItemFrameTooltip itemFrameTooltip = new TooltipItemFrameConfig.ItemFrameTooltip();

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipArmorStatsConfig.ArmorStatsTooltip armorStatsTooltip = new TooltipArmorStatsConfig.ArmorStatsTooltip();

    @ConfigEntry.Category(value = TOOLTIPS)
    @ConfigEntry.Gui.CollapsibleObject
    public TooltipFishStatsConfig.FishStatsTooltip fishStatsTooltip = new TooltipFishStatsConfig.FishStatsTooltip();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public NotificationsConfig.Notifications notifications = new NotificationsConfig.Notifications();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public TitleHudConfig.TitlePopup titlePopup = new TitleHudConfig.TitlePopup();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public BarHUDConfig.BarHUD barHUD = new BarHUDConfig.BarHUD();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerScoreboardHUDConfig.ScoreboardTracker scoreboardTracker = new TrackerScoreboardHUDConfig.ScoreboardTracker();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public TrackerBossBarHUDConfig.BossBarTracker bossBarTracker = new TrackerBossBarHUDConfig.BossBarTracker();

    @ConfigEntry.Category(value = OTHER)
    @ConfigEntry.Gui.CollapsibleObject
    public DiscordIPCConfig.DiscordIPC discordIPC = new DiscordIPCConfig.DiscordIPC();
    //endregion
}