package io.github.markassk.fishonmcextras.config;

import io.github.markassk.fishonmcextras.common.FlairDecor;
import io.github.markassk.fishonmcextras.common.Theming;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ThemingConfig {
    public static class Theme {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Theming.ThemeType themeType = Theming.ThemeType.OFF;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker(allowAlpha = false)
        public int colorOverlay = 0xFFFFFF;
        public int opacity = 100;
    }

    public static class Flair {
        @ConfigEntry.Gui.CollapsibleObject
        public ContestHudFlair contestFlair = new ContestHudFlair();
        public static class ContestHudFlair {
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopLeft flairTopLeft = FlairDecor.FlairTopLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopRight flairTopRight = FlairDecor.FlairTopRight.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomLeft flairBottomLeft = FlairDecor.FlairBottomLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomRight flairBottomRight = FlairDecor.FlairBottomRight.Off;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public FishTrackerFlair fishTrackerFlair = new FishTrackerFlair();
        public static class FishTrackerFlair {
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopLeft flairTopLeft = FlairDecor.FlairTopLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopRight flairTopRight = FlairDecor.FlairTopRight.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomLeft flairBottomLeft = FlairDecor.FlairBottomLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomRight flairBottomRight = FlairDecor.FlairBottomRight.Off;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public NotificationFlair notificationFlair = new NotificationFlair();
        public static class NotificationFlair {
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopLeft flairTopLeft = FlairDecor.FlairTopLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopRight flairTopRight = FlairDecor.FlairTopRight.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomLeft flairBottomLeft = FlairDecor.FlairBottomLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomRight flairBottomRight = FlairDecor.FlairBottomRight.Off;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public PetEquipFlair petEquipFlair = new PetEquipFlair();
        public static class PetEquipFlair {
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopLeft flairTopLeft = FlairDecor.FlairTopLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopRight flairTopRight = FlairDecor.FlairTopRight.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomLeft flairBottomLeft = FlairDecor.FlairBottomLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomRight flairBottomRight = FlairDecor.FlairBottomRight.Off;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public QuestFlair questFlair = new QuestFlair();
        public static class QuestFlair {
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopLeft flairTopLeft = FlairDecor.FlairTopLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairTopRight flairTopRight = FlairDecor.FlairTopRight.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomLeft flairBottomLeft = FlairDecor.FlairBottomLeft.Off;
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public FlairDecor.FlairBottomRight flairBottomRight = FlairDecor.FlairBottomRight.Off;
        }
    }
}
