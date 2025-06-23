package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NotificationSoundHandler {
    private static NotificationSoundHandler INSTANCE = new NotificationSoundHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private final Map<NotificationType, Long> lastPlayedSoundTime = new HashMap<>();

    public static NotificationSoundHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationSoundHandler();
        }
        return INSTANCE;
    }

    public void init() {
        lastPlayedSoundTime.put(NotificationType.PET_EQUIP, 0L);
        lastPlayedSoundTime.put(NotificationType.INVENTORY_FULL, 0L);
        lastPlayedSoundTime.put(NotificationType.WEATHER_ALERT, 0L);
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player != null) {
            if(config.notifications.showWarningHud) {
                // Pet Equip Warning Sound
                if(config.petEquipTracker.warningOptions.showPetEquipWarningHUD
                        && config.petEquipTracker.warningOptions.usePetEquipWarningSound
                        && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET
                        && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                        && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
                ) {
                    if(System.currentTimeMillis() - lastPlayedSoundTime.get(NotificationType.PET_EQUIP) > config.petEquipTracker.warningOptions.timePetEquipWarningSound * 1000L) {
                        playSoundWarning(config.petEquipTracker.warningOptions.petEquipSoundType, minecraftClient);
                        lastPlayedSoundTime.put(NotificationType.PET_EQUIP, System.currentTimeMillis());
                    }
                }

                // Full Inventory Warning Sound
                if(config.fullInventoryTracker.showFullInventoryWarningHUD
                        && config.fullInventoryTracker.useInventoryWarningSound
                        && FullInventoryHandler.instance().isOverThreshold
                        && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                        && BossBarHandler.instance().currentLocation != Constant.SPAWNHUB
                ) {
                    if(System.currentTimeMillis() - lastPlayedSoundTime.get(NotificationType.INVENTORY_FULL) > config.fullInventoryTracker.timeInventoryWarningSound * 1000L) {
                        playSoundWarning(config.fullInventoryTracker.fullInventorySoundType, minecraftClient);
                        lastPlayedSoundTime.put(NotificationType.INVENTORY_FULL, System.currentTimeMillis());
                    }
                }

                // Weather Alert Warning Sound
                if(config.weatherTracker.showAlertHUD
                        && config.weatherTracker.useAlertWarningSound
                        && BossBarHandler.instance().currentLocation != Constant.CREW_ISLAND
                        && System.currentTimeMillis() - WeatherHandler.instance().weatherChangedAtTime <= config.weatherTracker.alertDismissSeconds * 1000L
                ) {
                    if(Objects.equals(WeatherHandler.instance().currentWeather, Constant.THUNDERSTORM.ID)
                            && System.currentTimeMillis() - lastPlayedSoundTime.get(NotificationType.WEATHER_ALERT) > config.weatherTracker.intervalWarningSound * 1000L
                    ) {
                        playSoundWarning(config.weatherTracker.alertSoundType, minecraftClient);
                        lastPlayedSoundTime.put(NotificationType.WEATHER_ALERT, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public void playSoundWarning(SoundType soundType, MinecraftClient client) {
        if(client.player != null) {
            switch (soundType) {
                case PLING -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case BASS -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case BELL -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case BIT -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case CHIME -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case DIDGERIDOO -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case COW_BELL -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case FLUTE -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_FLUTE.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case GUITAR -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
                case HARP -> client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), client.options.getSoundVolume(SoundCategory.RECORDS), 1f);
            }
        }
    }

    public enum SoundType {
        PLING, // Default Pet
        BASS, // Default Inventory
        BELL,
        BIT,
        CHIME,
        DIDGERIDOO,
        COW_BELL,
        FLUTE,
        GUITAR,
        HARP
    }

    private enum NotificationType {
        PET_EQUIP,
        INVENTORY_FULL,
        WEATHER_ALERT
    }
}
