package io.github.markassk.fishonmcextras.handler;


import io.github.markassk.fishonmcextras.common.FlairDecor;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;

public class ThemingHandler {
    private static ThemingHandler INSTANCE = new ThemingHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    // Theme
    public Theming.ThemeType currentThemeType = Theming.ThemeType.OFF;
    public Theming currentTheme = new Theming(currentThemeType);

    // Flair

    /// Contest
    private FlairDecor.FlairTopLeft flairTopLeftContest = FlairDecor.FlairTopLeft.Off;
    private FlairDecor.FlairTopRight flairTopRightContest = FlairDecor.FlairTopRight.Off;
    private FlairDecor.FlairBottomLeft flairBottomLeftContest = FlairDecor.FlairBottomLeft.Off;
    private FlairDecor.FlairBottomRight flairBottomRightContest = FlairDecor.FlairBottomRight.Off;
    public FlairDecor flairDecorContest = new FlairDecor(flairTopLeftContest, flairTopRightContest, flairBottomLeftContest, flairBottomRightContest);

    /// FishTracker
    private FlairDecor.FlairTopLeft flairTopLeftFishTracker = FlairDecor.FlairTopLeft.Off;
    private FlairDecor.FlairTopRight flairTopRightFishTracker = FlairDecor.FlairTopRight.Off;
    private FlairDecor.FlairBottomLeft flairBottomLeftFishTracker = FlairDecor.FlairBottomLeft.Off;
    private FlairDecor.FlairBottomRight flairBottomRightFishTracker = FlairDecor.FlairBottomRight.Off;
    public FlairDecor flairDecorFishTracker = new FlairDecor(flairTopLeftFishTracker, flairTopRightFishTracker, flairBottomLeftFishTracker, flairBottomRightFishTracker);

    /// Notification
    private FlairDecor.FlairTopLeft flairTopLeftNotification = FlairDecor.FlairTopLeft.Off;
    private FlairDecor.FlairTopRight flairTopRightNotification = FlairDecor.FlairTopRight.Off;
    private FlairDecor.FlairBottomLeft flairBottomLeftNotification = FlairDecor.FlairBottomLeft.Off;
    private FlairDecor.FlairBottomRight flairBottomRightNotification = FlairDecor.FlairBottomRight.Off;
    public FlairDecor flairDecorNotification = new FlairDecor(flairTopLeftNotification, flairTopRightNotification, flairBottomLeftNotification, flairBottomRightNotification);

    /// PetEquip
    private FlairDecor.FlairTopLeft flairTopLeftPetEquip = FlairDecor.FlairTopLeft.Off;
    private FlairDecor.FlairTopRight flairTopRightPetEquip = FlairDecor.FlairTopRight.Off;
    private FlairDecor.FlairBottomLeft flairBottomLeftPetEquip = FlairDecor.FlairBottomLeft.Off;
    private FlairDecor.FlairBottomRight flairBottomRightPetEquip = FlairDecor.FlairBottomRight.Off;
    public FlairDecor flairDecorPetEquip = new FlairDecor(flairTopLeftPetEquip, flairTopRightPetEquip, flairBottomLeftPetEquip, flairBottomRightPetEquip);

    /// Quest
    private FlairDecor.FlairTopLeft flairTopLeftQuest = FlairDecor.FlairTopLeft.Off;
    private FlairDecor.FlairTopRight flairTopRightQuest = FlairDecor.FlairTopRight.Off;
    private FlairDecor.FlairBottomLeft flairBottomLeftQuest = FlairDecor.FlairBottomLeft.Off;
    private FlairDecor.FlairBottomRight flairBottomRightQuest = FlairDecor.FlairBottomRight.Off;
    public FlairDecor flairDecorQuest = new FlairDecor(flairTopLeftQuest, flairTopRightQuest, flairBottomLeftQuest, flairBottomRightQuest);

    public static ThemingHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemingHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        if(currentThemeType != config.theme.themeType) {
            this.currentThemeType = config.theme.themeType;
            this.currentTheme = new Theming(currentThemeType);
        }

        if(flairTopLeftContest != config.flair.contestHudFlair.flairTopLeft
                || flairTopRightContest != config.flair.contestHudFlair.flairTopRight
                || flairBottomLeftContest != config.flair.contestHudFlair.flairBottomLeft
                || flairBottomRightContest != config.flair.contestHudFlair.flairBottomRight
        ) {
            flairTopLeftContest = config.flair.contestHudFlair.flairTopLeft;
            flairTopRightContest = config.flair.contestHudFlair.flairTopRight;
            flairBottomLeftContest = config.flair.contestHudFlair.flairBottomLeft;
            flairBottomRightContest = config.flair.contestHudFlair.flairBottomRight;
            flairDecorContest = new FlairDecor(flairTopLeftContest, flairTopRightContest, flairBottomLeftContest, flairBottomRightContest);
        }

        if(flairTopLeftFishTracker != config.flair.fishTrackerFlair.flairTopLeft
                || flairTopRightFishTracker != config.flair.fishTrackerFlair.flairTopRight
                || flairBottomLeftFishTracker != config.flair.fishTrackerFlair.flairBottomLeft
                || flairBottomRightFishTracker != config.flair.fishTrackerFlair.flairBottomRight
        ) {
            flairTopLeftFishTracker = config.flair.fishTrackerFlair.flairTopLeft;
            flairTopRightFishTracker = config.flair.fishTrackerFlair.flairTopRight;
            flairBottomLeftFishTracker = config.flair.fishTrackerFlair.flairBottomLeft;
            flairBottomRightFishTracker = config.flair.fishTrackerFlair.flairBottomRight;
            flairDecorFishTracker = new FlairDecor(flairTopLeftFishTracker, flairTopRightFishTracker, flairBottomLeftFishTracker, flairBottomRightFishTracker);
        }

        if(flairTopLeftNotification != config.flair.notificationFlair.flairTopLeft
                || flairTopRightNotification != config.flair.notificationFlair.flairTopRight
                || flairBottomLeftNotification != config.flair.notificationFlair.flairBottomLeft
                || flairBottomRightNotification != config.flair.notificationFlair.flairBottomRight
        ) {
            flairTopLeftNotification = config.flair.notificationFlair.flairTopLeft;
            flairTopRightNotification = config.flair.notificationFlair.flairTopRight;
            flairBottomLeftNotification = config.flair.notificationFlair.flairBottomLeft;
            flairBottomRightNotification = config.flair.notificationFlair.flairBottomRight;
            flairDecorNotification = new FlairDecor(flairTopLeftNotification, flairTopRightNotification, flairBottomLeftNotification, flairBottomRightNotification);
        }

        if(flairTopLeftPetEquip != config.flair.petEquipFlair.flairTopLeft
                || flairTopRightPetEquip != config.flair.petEquipFlair.flairTopRight
                || flairBottomLeftPetEquip != config.flair.petEquipFlair.flairBottomLeft
                || flairBottomRightPetEquip != config.flair.petEquipFlair.flairBottomRight
        ) {
            flairTopLeftPetEquip = config.flair.petEquipFlair.flairTopLeft;
            flairTopRightPetEquip = config.flair.petEquipFlair.flairTopRight;
            flairBottomLeftPetEquip = config.flair.petEquipFlair.flairBottomLeft;
            flairBottomRightPetEquip = config.flair.petEquipFlair.flairBottomRight;
            flairDecorPetEquip = new FlairDecor(flairTopLeftPetEquip, flairTopRightPetEquip, flairBottomLeftPetEquip, flairBottomRightPetEquip);
        }

        if(flairTopLeftQuest != config.flair.questFlair.flairTopLeft
                || flairTopRightQuest != config.flair.questFlair.flairTopRight
                || flairBottomLeftQuest != config.flair.questFlair.flairBottomLeft
                || flairBottomRightQuest != config.flair.questFlair.flairBottomRight
        ) {
            flairTopLeftQuest = config.flair.questFlair.flairTopLeft;
            flairTopRightQuest = config.flair.questFlair.flairTopRight;
            flairBottomLeftQuest = config.flair.questFlair.flairBottomLeft;
            flairBottomRightQuest = config.flair.questFlair.flairBottomRight;
            flairDecorQuest = new FlairDecor(flairTopLeftQuest, flairTopRightQuest, flairBottomLeftQuest, flairBottomRightQuest);
        }
    }
}
