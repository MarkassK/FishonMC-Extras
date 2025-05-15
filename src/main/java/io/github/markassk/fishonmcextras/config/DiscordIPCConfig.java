package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class DiscordIPCConfig {
    public static class DiscordIPC {
        @ConfigEntry.Gui.Excluded
        public long clientId = 1369418648078258356L;
        @ConfigEntry.Gui.Tooltip // Must restart on change
        public boolean isEnabled = true;
    }
}
