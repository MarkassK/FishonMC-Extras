package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;

public class WarningHandler {
    private static WarningHandler INSTANCE = new WarningHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private Map<WarningType, Long> lastPlayedTime = new HashMap<>();

    public static WarningHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new WarningHandler();
        }
        return INSTANCE;
    }

    public void init() {
        lastPlayedTime.put(WarningType.PET_EQUIP, 0L);
        lastPlayedTime.put(WarningType.INVENTORY_FULL, 0L);
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player != null) {
            // Pet Equip Warning Sound
            if(config.petEquipTracker.warningOptions.showPetEquipWarningHUD
                    && config.petEquipTracker.warningOptions.usePetEquipWarningSound
                    && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET
            ) {
                if(System.currentTimeMillis() - lastPlayedTime.get(WarningType.PET_EQUIP) > config.petEquipTracker.warningOptions.timePetEquipWarningSound * 1000L) {
                    playSoundWarning(config.petEquipTracker.warningOptions.petEquipWarningSound, minecraftClient);
                    lastPlayedTime.put(WarningType.PET_EQUIP, System.currentTimeMillis());
                }
            }

            // Full Inventory Warning Sound
            if(config.fullInventoryTracker.showFullInventoryWarningHUD
                    && config.fullInventoryTracker.useInventoryWarningSound
                    && FullInventoryHandler.instance().isOverThreshold
            ) {
                if(System.currentTimeMillis() - lastPlayedTime.get(WarningType.INVENTORY_FULL) > config.fullInventoryTracker.timeInventoryWarningSound * 1000L) {
                    playSoundWarning(config.fullInventoryTracker.fullInventoryWarningSound, minecraftClient);
                    lastPlayedTime.put(WarningType.INVENTORY_FULL, System.currentTimeMillis());
                }
            }
        }
    }

    private void playSoundWarning(WarningSound warningSound, MinecraftClient client) {
        if(client.player != null) {
            switch (warningSound) {
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

    public enum WarningSound {
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

    private enum WarningType {
        PET_EQUIP,
        INVENTORY_FULL
    }
}
