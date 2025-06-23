package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.CrewHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerCrewHUDConfig {
    public static class CrewTracker {
        public boolean showCrewNearby = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public CrewHandler.CrewChatLocation crewChatLocation = CrewHandler.CrewChatLocation.IN_CHAT;
        public boolean notifyCrewOnJoin = true;
        public boolean notifyCrewOnLeave = true;
        public boolean showCrewTag = true;
        @ConfigEntry.Gui.Tooltip
        public boolean isPrefix = true;
    }
}
