package io.github.markassk.fishonmcextras.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.adapter.FOMCConstantTypeAdapter;
import io.github.markassk.fishonmcextras.adapter.LocalDateTypeAdapter;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProfileDataHandler {
    private static ProfileDataHandler INSTANCE = new ProfileDataHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private boolean isSavedAfterTimer = false;

    public ProfileData profileData = new ProfileData();
    public long lastUpdateTime = System.currentTimeMillis();
    public UUID playerUUID = UUID.randomUUID();

    public static ProfileDataHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileDataHandler();
        }
        return INSTANCE;
    }

    public void onJoinServer(PlayerEntity player) {
        ProfileDataHandler.instance().playerUUID = player.getUuid();
        ProfileDataHandler.instance().loadStats();
    }

    /**
     * Update stats from new Fish
     */
    public void updateStatsOnCatch(Types.Fish fish) {
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
        this.saveStats();
    }

    public void updateStatsOnCatch() {
        // All-time stats
        this.profileData.allPetCaughtCount++;

        // Session stats
        this.profileData.petCaughtCount++;

        this.profileData.petDryStreak = this.profileData.allFishCaughtCount;

        this.saveStats();
    }

    public void updateStatsOnCatch(int count) {
        // All-time stats
        this.profileData.allShardCaughtCount += count;

        // Session stats
        this.profileData.shardCaughtCount += count;

        this.profileData.shardDryStreak = this.profileData.allFishCaughtCount;

        this.saveStats();
    }

    public void updatePet(Types.Pet pet, int slot) {
        this.profileData.equippedPet = pet;
        this.profileData.equippedPetSlot = slot;
        this.saveStats();
    }

    public void resetPet() {
        this.profileData.equippedPet = null;
        this.profileData.equippedPetSlot = -1;
        this.saveStats();
    }

    /**
     * Save Stats to disk
     */
    public void saveStats() {
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            Path subDir = configDir.resolve("foe");
            Path statsDir = subDir.resolve("stats");
            Files.createDirectories(statsDir);
            Path filePath = statsDir.resolve(playerUUID.toString() + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .registerTypeAdapter(Constant.class, new FOMCConstantTypeAdapter())
                    .create();
            String json = gson.toJson(this.profileData);
            Files.writeString(filePath, json);
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
                Gson gson = new GsonBuilder().setPrettyPrinting()
                        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                        .registerTypeAdapter(Constant.class, new FOMCConstantTypeAdapter())
                        .create();
                this.profileData = gson.fromJson(json, ProfileData.class);
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

        this.profileData.timerFishCaughtCount = 0;

        this.profileData.activeTime = 0;
        this.profileData.lastFishCaughtTime = 0;
        this.profileData.timerPaused = true;

        this.profileData.rarityDryStreak.clear();
        this.profileData.fishSizeDryStreak.clear();
        this.profileData.variantDryStreak.clear();

        FishCatchHandler.instance().reset();
        this.saveStats();
    }

    public void resetTimer() {
        this.profileData.timerFishCaughtCount = 0;
        this.profileData.activeTime = 0;
        this.saveStats();
    }

    public void tickTimer() {
        long currentTime = System.currentTimeMillis();
        // Pause timer when not fishing after x seconds
        long timeSinceLastFish = currentTime - ProfileDataHandler.instance().profileData.lastFishCaughtTime;
        if (timeSinceLastFish >= TimeUnit.SECONDS.toMillis(config.fishTracker.autoPauseTimer)) {
            ProfileDataHandler.instance().profileData.timerPaused = true;
            if(!isSavedAfterTimer) {
                this.isSavedAfterTimer = true;
                this.saveStats();
            }
        }

        long delta = currentTime - ProfileDataHandler.instance().lastUpdateTime;
        ProfileDataHandler.instance().lastUpdateTime = currentTime;

        // Track time when fishing
        if(!ProfileDataHandler.instance().profileData.timerPaused) {
            ProfileDataHandler.instance().profileData.activeTime += delta;
        }
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

        public int timerFishCaughtCount = 0;

        // Equipped Pet
        public int equippedPetSlot = -1;
        public Types.Pet equippedPet = null;

        // Dry streak count
        public int petDryStreak;
        public int shardDryStreak;
        public Map<Constant, Integer> rarityDryStreak = new HashMap<>();
        public Map<Constant, Integer> variantDryStreak = new HashMap<>();
        public Map<Constant, Integer> fishSizeDryStreak = new HashMap<>();

        // Crew Data
        public List<UUID> crewMembers = new ArrayList<>();
        public CrewHandler.CrewState crewState = CrewHandler.CrewState.NOTINITIALIZED;

        // Quest Data
        public Map<Constant, List<QuestHandler.Quest>> activeQuests = new HashMap<>();

        // Stats Data
        public boolean isStatsInitialized = false;
    }
}
