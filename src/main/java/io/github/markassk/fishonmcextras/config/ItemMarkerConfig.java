package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.handler.ItemMarkerHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ItemMarkerConfig {
    public static class ItemMarker {
        @ConfigEntry.Gui.CollapsibleObject
        public ItemSlotMarker itemSlotMarker = new ItemSlotMarker();
        public static class ItemSlotMarker {
            public boolean showItemMarker = true;
            public boolean showFishRarityMarker = true;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public ItemMarkerHandler.FishSizeMarkerToggle showFishSizeMarker = ItemMarkerHandler.FishSizeMarkerToggle.CHARACTER;
            public boolean showOtherRarityMarker = true;
            public boolean showPetItemEquippedMarker = true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public ItemSearchMarker itemSearchMarker = new ItemSearchMarker();
        public static class ItemSearchMarker {
            @ConfigEntry.ColorPicker()
            public int searchHighlightColor = 0x55FF55;
        }


        public boolean showSelectedPetHighlight = true;
        @ConfigEntry.ColorPicker()
        public int selectedPetHighlightColor = 0xFFAA00;
    }
}
