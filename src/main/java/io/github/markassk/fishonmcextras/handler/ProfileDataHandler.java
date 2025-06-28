package io.github.markassk.fishonmcextras.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Fish;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProfileDataHandler {
    private static ProfileDataHandler INSTANCE = new ProfileDataHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private boolean isSavedAfterTimer = false;

    public ProfileData profileData = new ProfileData();
    public ProfileData prevProfileData = new ProfileData();
    public boolean isDataLoaded = false;
    public long lastUpdateTime = System.currentTimeMillis();
    public UUID playerUUID = null;

    public static ProfileDataHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileDataHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        if(isDataLoaded && LoadingHandler.instance().wasOnServer && !prevProfileData.equals(profileData)) {
            prevProfileData = new ProfileData(profileData);
            this.saveStats();
        }

        //TODO remove in 0.2.4
        if(profileData.lightningBottleDryStreak == 0) {
            profileData.lightningBottleDryStreak = profileData.allFishCaughtCount;
        }
    }

    public void onJoinServer(PlayerEntity player) {
        ProfileDataHandler.instance().playerUUID = player.getUuid();
        ProfileDataHandler.instance().loadStats();
    }

    /**
     * Update stats from new Fish
     */
    public void updateStatsOnCatch(Fish fish) {
        // All-time stats
        this.profileData.allFishCaughtCount++;
        this.profileData.timerFishCaughtCount++;
        this.profileData.allTotalXP += fish.xp;
        this.profileData.allTotalValue += fish.value;
        this.profileData.allFishSizeCounts.put(fish.size, this.profileData.allFishSizeCounts.getOrDefault(fish.size, 0) + 1);
        this.profileData.allVariantCounts.put(fish.variant, this.profileData.allVariantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileData.allRarityCounts.put(fish.rarity, this.profileData.allRarityCounts.getOrDefault(fish.rarity, 0) + 1);

        this.profileData.lastFishCaughtTime = System.currentTimeMillis();
        this.profileData.timerPaused = false;

        // Session stats
        this.profileData.fishCaughtCount++;
        this.profileData.totalXP += fish.xp;
        this.profileData.totalValue += fish.value;
        this.profileData.fishSizeCounts.put(fish.size, this.profileData.fishSizeCounts.getOrDefault(fish.size, 0) + 1);
        this.profileData.variantCounts.put(fish.variant, this.profileData.variantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileData.rarityCounts.put(fish.rarity, this.profileData.rarityCounts.getOrDefault(fish.rarity, 0) + 1);

        this.profileData.fishSizeDryStreak.put(fish.size, this.profileData.allFishCaughtCount);
        this.profileData.variantDryStreak.put(fish.variant, this.profileData.allFishCaughtCount);
        this.profileData.rarityDryStreak.put(fish.rarity, this.profileData.allFishCaughtCount);

        this.isSavedAfterTimer = false;
    }

    public void updateStatsOnCatch() {
        if(!Objects.equals(BossBarHandler.instance().weather, Constant.THUNDERSTORM.ID)) {
            this.profileData.lightningBottleDryStreak++;
        }
    }

    public void updatePetCaughtStatsOnCatch() {
        // All-time stats
        this.profileData.allPetCaughtCount++;

        // Session stats
        this.profileData.petCaughtCount++;

        this.profileData.petDryStreak = this.profileData.allFishCaughtCount;
    }

    public void updateShardCaughtStatsOnCatch(int count) {
        // All-time stats
        this.profileData.allShardCaughtCount += count;

        // Session stats
        this.profileData.shardCaughtCount += count;

        this.profileData.shardDryStreak = this.profileData.allFishCaughtCount;
    }

    public void updateLightningBottleCaughtStatsOnCatch() {
        // All-time stats
        this.profileData.allLightningBottleCount++;

        // Session stats
        this.profileData.lightningBottleCount++;

        this.profileData.lightningBottleDryStreak = this.profileData.allFishCaughtCount;
    }

    public void updatePet(Pet pet, int slot) {
        this.profileData.equippedPet = pet;
        this.profileData.equippedPetSlot = slot;
    }

    public void resetPet() {
        this.profileData.equippedPet = null;
        this.profileData.equippedPetSlot = -1;
    }

    /**
     * Save Stats to disk
     */
    public void saveStats() {
        try {
            if (playerUUID != null) {
                Path configDir = FabricLoader.getInstance().getConfigDir();
                Path subDir = configDir.resolve("foe");
                Path statsDir = subDir.resolve("stats");
                Files.createDirectories(statsDir);
                Path filePath = statsDir.resolve(playerUUID.toString() + ".json");
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(this.profileData);
                Files.writeString(filePath, json);
            }
        } catch (IOException e) {
            FishOnMCExtras.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Load Stats from disk
     */
    public void loadStats() {
        try {
            if(playerUUID != null) {
                Path configDir = FabricLoader.getInstance().getConfigDir();
                Path subDir = configDir.resolve("foe");
                Path statsDir = subDir.resolve("stats");
                Files.createDirectories(statsDir);
                Path filePath = statsDir.resolve(playerUUID.toString() + ".json");
                if (!Files.exists(filePath)) return;
                String json = Files.readString(filePath, StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder().create();
                this.profileData = gson.fromJson(json, ProfileData.class);
                isDataLoaded = true;
            }
        } catch (IOException e) {
            FishOnMCExtras.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Reset Stats, but not all-time stats
     */
    public void resetStats() {
        this.profileData.fishCaughtCount = 0;
        this.profileData.totalXP = 0.0f;
        this.profileData.totalValue = 0.0f;
        this.profileData.variantCounts.clear();
        this.profileData.rarityCounts.clear();
        this.profileData.fishSizeCounts.clear();
        this.profileData.petCaughtCount = 0;
        this.profileData.shardCaughtCount = 0;
        this.profileData.lightningBottleCount = 0;
        if(config.fishTracker.isFishTrackerOnTimer) {
            this.profileData.timerFishCaughtCount = 0;
            this.profileData.activeTime = 0;
        }
        this.profileData.lastFishCaughtTime = 0;
        this.profileData.timerPaused = true;

        FishCatchHandler.instance().reset();
    }

    public void resetTimer() {
        this.profileData.timerFishCaughtCount = 0;
        this.profileData.activeTime = 0;
    }

    public void tickTimer() {
        long currentTime = System.currentTimeMillis();
        // Pause timer when not fishing after x seconds
        long timeSinceLastFish = currentTime - ProfileDataHandler.instance().profileData.lastFishCaughtTime;
        if (timeSinceLastFish >= TimeUnit.SECONDS.toMillis(config.fishTracker.autoPauseTimer)) {
            ProfileDataHandler.instance().profileData.timerPaused = true;
            if(!isSavedAfterTimer) {
                this.isSavedAfterTimer = true;
            }
        }

        long delta = currentTime - ProfileDataHandler.instance().lastUpdateTime;
        ProfileDataHandler.instance().lastUpdateTime = currentTime;

        // Track time when fishing
        if(!ProfileDataHandler.instance().profileData.timerPaused) {
            ProfileDataHandler.instance().profileData.activeTime += delta;
        }
    }

    public void resetDryStreak() {
        profileData.petDryStreak = profileData.allFishCaughtCount;
        profileData.shardDryStreak = profileData.allFishCaughtCount;
        profileData.lightningBottleDryStreak = profileData.allFishCaughtCount;
        profileData.rarityDryStreak.put(Constant.COMMON, profileData.allFishCaughtCount);
        profileData.rarityDryStreak.put(Constant.RARE, profileData.allFishCaughtCount);
        profileData.rarityDryStreak.put(Constant.EPIC, profileData.allFishCaughtCount);
        profileData.rarityDryStreak.put(Constant.LEGENDARY, profileData.allFishCaughtCount);
        profileData.rarityDryStreak.put(Constant.MYTHICAL, profileData.allFishCaughtCount);
        profileData.rarityDryStreak.put(Constant.SPECIAL, profileData.allFishCaughtCount);
        profileData.fishSizeDryStreak.put(Constant.BABY, profileData.allFishCaughtCount);
        profileData.fishSizeDryStreak.put(Constant.JUVENILE, profileData.allFishCaughtCount);
        profileData.fishSizeDryStreak.put(Constant.ADULT, profileData.allFishCaughtCount);
        profileData.fishSizeDryStreak.put(Constant.LARGE, profileData.allFishCaughtCount);
        profileData.fishSizeDryStreak.put(Constant.GIGANTIC, profileData.allFishCaughtCount);
        profileData.variantDryStreak.put(Constant.ALBINO, profileData.allFishCaughtCount);
        profileData.variantDryStreak.put(Constant.MELANISTIC, profileData.allFishCaughtCount);
        profileData.variantDryStreak.put(Constant.TROPHY, profileData.allFishCaughtCount);
        profileData.variantDryStreak.put(Constant.FABLED, profileData.allFishCaughtCount);
    }

    public static class ProfileData {
        // Session stats
        public int fishCaughtCount = 0;
        public float totalXP = 0.0f;
        public float totalValue = 0.0f;
        public Map<Constant, Integer> variantCounts = new HashMap<>();
        public Map<Constant, Integer> rarityCounts = new HashMap<>();
        public Map<Constant, Integer> fishSizeCounts = new HashMap<>();
        public int petCaughtCount = 0;
        public int shardCaughtCount = 0;
        public int lightningBottleCount = 0;

        // Current active timer stats
        public long activeTime = 0;
        public long lastFishCaughtTime = 0;
        public boolean timerPaused = true;

        // All-time stats
        public int allFishCaughtCount = 0;
        public float allTotalXP = 0.0f;
        public float allTotalValue = 0.0f;
        public Map<Constant, Integer> allRarityCounts = new HashMap<>();
        public Map<Constant, Integer> allVariantCounts = new HashMap<>();
        public Map<Constant, Integer> allFishSizeCounts = new HashMap<>();
        public int allPetCaughtCount = 0;
        public int allShardCaughtCount = 0;
        public int allLightningBottleCount = 0;

        public int timerFishCaughtCount = 0;

        // Equipped Pet
        public int equippedPetSlot = -1;
        public Pet equippedPet = null;

        // Dry streak count
        public int petDryStreak;
        public int shardDryStreak;
        public int lightningBottleDryStreak;
        public Map<Constant, Integer> rarityDryStreak = new HashMap<>();
        public Map<Constant, Integer> variantDryStreak = new HashMap<>();
        public Map<Constant, Integer> fishSizeDryStreak = new HashMap<>();

        // Crew Data
        public List<UUID> crewMembers = new ArrayList<>();
        public boolean isInCrewChat = false;

        // Quest Data
        public Map<Constant, List<QuestHandler.Quest>> activeQuests = new HashMap<>();

        // Stats Data
        public boolean isStatsInitialized = false;

        public ProfileData() {
        }

        public ProfileData(ProfileData prevData) {
            fishCaughtCount = prevData.fishCaughtCount;
            totalXP = prevData.totalXP;
            totalValue = prevData.totalValue;
            variantCounts = new HashMap<>(prevData.variantCounts);
            rarityCounts = new HashMap<>(prevData.rarityCounts);
            fishSizeCounts = new HashMap<>(prevData.fishSizeCounts);
            petCaughtCount = prevData.petCaughtCount;
            shardCaughtCount = prevData.shardCaughtCount;
            lightningBottleCount = prevData.lightningBottleCount;
            activeTime = prevData.activeTime;
            lastFishCaughtTime = prevData.lastFishCaughtTime;
            timerPaused = prevData.timerPaused;
            allFishCaughtCount = prevData.allFishCaughtCount;
            allTotalXP = prevData.allTotalXP;
            allTotalValue = prevData.allTotalValue;
            allRarityCounts = new HashMap<>(prevData.allRarityCounts);
            allVariantCounts = new HashMap<>(prevData.allVariantCounts);
            allFishSizeCounts = new HashMap<>(prevData.allFishSizeCounts);
            allPetCaughtCount = prevData.allPetCaughtCount;
            allShardCaughtCount = prevData.allShardCaughtCount;
            allLightningBottleCount = prevData.allLightningBottleCount;
            timerFishCaughtCount = prevData.timerFishCaughtCount;
            equippedPetSlot = prevData.equippedPetSlot;
            equippedPet = prevData.equippedPet;
            petDryStreak = prevData.petDryStreak;
            shardDryStreak = prevData.shardDryStreak;
            lightningBottleDryStreak = prevData.lightningBottleDryStreak;
            rarityDryStreak = new HashMap<>(prevData.rarityDryStreak);
            variantDryStreak = new HashMap<>(prevData.variantDryStreak);
            fishSizeDryStreak = new HashMap<>(prevData.fishSizeDryStreak);
            crewMembers = new ArrayList<>(prevData.crewMembers);
            isInCrewChat = prevData.isInCrewChat;
            activeQuests = new HashMap<>(prevData.activeQuests);
            isStatsInitialized = prevData.isStatsInitialized;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            }

            return obj instanceof ProfileData oldProfileData
                    && this.fishCaughtCount == oldProfileData.fishCaughtCount
                    && this.totalXP == oldProfileData.totalXP
                    && this.totalValue == oldProfileData.totalValue
//                    && this.variantCounts.equals(oldProfileData.variantCounts)
//                    && this.rarityCounts.equals(oldProfileData.rarityCounts)
//                    && this.fishSizeCounts.equals(oldProfileData.fishSizeCounts)
                    && this.petCaughtCount == oldProfileData.petCaughtCount
                    && this.shardCaughtCount == oldProfileData.shardCaughtCount
                    && this.lightningBottleCount == oldProfileData.lightningBottleCount
                    && this.lastFishCaughtTime == oldProfileData.lastFishCaughtTime
                    && this.timerPaused == oldProfileData.timerPaused
                    && this.allFishCaughtCount == oldProfileData.allFishCaughtCount
                    && this.allTotalXP == oldProfileData.allTotalXP
                    && this.allTotalValue == oldProfileData.allTotalValue
//                    && this.allRarityCounts.equals(oldProfileData.allRarityCounts)
//                    && this.allVariantCounts.equals(oldProfileData.allVariantCounts)
//                    && this.allFishSizeCounts.equals(oldProfileData.allFishSizeCounts)
                    && this.allPetCaughtCount == oldProfileData.allPetCaughtCount
                    && this.allShardCaughtCount == oldProfileData.allShardCaughtCount
                    && this.allLightningBottleCount == oldProfileData.allLightningBottleCount
                    && this.timerFishCaughtCount == oldProfileData.timerFishCaughtCount
                    && this.equippedPetSlot == oldProfileData.equippedPetSlot
                    && this.petDryStreak == oldProfileData.petDryStreak
                    && this.shardDryStreak == oldProfileData.shardDryStreak
                    && this.lightningBottleDryStreak == oldProfileData.lightningBottleDryStreak
                    && this.rarityDryStreak.equals(oldProfileData.rarityDryStreak)
                    && this.variantDryStreak.equals(oldProfileData.variantDryStreak)
                    && this.fishSizeDryStreak.equals(oldProfileData.fishSizeDryStreak)
                    && this.crewMembers.equals(oldProfileData.crewMembers)
                    && this.isInCrewChat == oldProfileData.isInCrewChat
                    && this.activeQuests.equals(oldProfileData.activeQuests);
        }
    }
}
