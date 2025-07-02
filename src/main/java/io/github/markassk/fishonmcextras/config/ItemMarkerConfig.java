package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.ItemMarkerHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ItemMarkerConfig {
    public static class ItemMarker {
        public boolean showItemMarker = true;
        public boolean showFishRarityMarker = true;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ItemMarkerHandler.FishSizeMarkerToggle showFishSizeMarker = ItemMarkerHandler.FishSizeMarkerToggle.CHARACTER;
        public boolean showOtherRarityMarker = true;
    }
}
