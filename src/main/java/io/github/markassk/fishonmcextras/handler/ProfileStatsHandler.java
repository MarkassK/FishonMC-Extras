package io.github.markassk.fishonmcextras.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.adapter.LocalDateTypeAdapter;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProfileStatsHandler {
    private static ProfileStatsHandler INSTANCE = new ProfileStatsHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public ProfileStats profileStats = new ProfileStats();
    public UUID playerUUID;

    public static ProfileStatsHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileStatsHandler();
        }
        return INSTANCE;
    }

    /**
     * Update stats from new Fish
     */
    public void updateStatsOnCatch(Types.Fish fish) {
        // All-time stats
        this.profileStats.allFishCaughtCount++;
        this.profileStats.allTotalXP += fish.xp;
        this.profileStats.allTotalValue += fish.value;
        this.profileStats.allFishSizeCounts.put(fish.sizeId, this.profileStats.allFishSizeCounts.getOrDefault(fish.sizeId, 0) + 1);
        this.profileStats.allVariantCounts.put(fish.variant, this.profileStats.allVariantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileStats.allRarityCounts.put(fish.rarityId, this.profileStats.allRarityCounts.getOrDefault(fish.rarityId, 0) + 1);

        this.profileStats.lastFishCaughtTime = System.currentTimeMillis();
        this.profileStats.timerPaused = false;

        // Session stats
        this.profileStats.fishCaughtCount++;
        this.profileStats.totalXP += fish.xp;
        this.profileStats.totalValue += fish.value;
        this.profileStats.fishSizeCounts.put(fish.sizeId, this.profileStats.fishSizeCounts.getOrDefault(fish.sizeId, 0) + 1);
        this.profileStats.variantCounts.put(fish.variant, this.profileStats.variantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileStats.rarityCounts.put(fish.rarityId, this.profileStats.rarityCounts.getOrDefault(fish.rarityId, 0) + 1);

        this.saveStats();
    }

    public void updateStatsOnCatch(Types.Pet pet) {
        // All-time stats
        this.profileStats.allPetCaughtCount++;

        // Session stats
        this.profileStats.petCaughtCount++;

        this.saveStats();
    }

    public void updateStatsOnCatch(int count) {
        // All-time stats
        this.profileStats.allShardCaughtCount += count;

        // Session stats
        this.profileStats.shardCaughtCount += count;

        this.saveStats();
    }

    public void updatePet(Types.Pet pet, int slot) {
        this.profileStats.equippedPet = pet;
        this.profileStats.equippedPetSlot = slot;
        this.saveStats();
    }

    public void resetPet() {
        this.profileStats.equippedPet = null;
        this.profileStats.equippedPetSlot = -1;
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
                    .create();
            String json = gson.toJson(this.profileStats);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            e.printStackTrace();
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
                        .create();
                this.profileStats = gson.fromJson(json, ProfileStats.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset Stats, but not all-time stats
     */
    public void resetStats() {
        this.profileStats.fishCaughtCount = 0;
        this.profileStats.totalXP = 0.0f;
        this.profileStats.totalValue = 0.0f;
        this.profileStats.variantCounts.clear();
        this.profileStats.rarityCounts.clear();
        this.profileStats.fishSizeCounts.clear();
        this.profileStats.petCaughtCount = 0;
        this.profileStats.shardCaughtCount = 0;

        this.profileStats.activeTime = 0;
        this.profileStats.lastFishCaughtTime = 0;
        this.profileStats.lastUpdateTime = System.currentTimeMillis();
        this.profileStats.timerPaused = true;

        FishCatchHandler.instance().reset();
        this.saveStats();
    }

    public void tickTimer() {
        long currentTime = System.currentTimeMillis();
        // Pause timer when not fishing after x seconds
        long timeSinceLastFish = currentTime - ProfileStatsHandler.instance().profileStats.lastFishCaughtTime;
        if (timeSinceLastFish >= TimeUnit.SECONDS.toMillis(config.fishTracker.autoPauseTimer)) {
            ProfileStatsHandler.instance().profileStats.timerPaused = true;
            this.saveStats();
        }

        long delta = currentTime - ProfileStatsHandler.instance().profileStats.lastUpdateTime;
        ProfileStatsHandler.instance().profileStats.lastUpdateTime = currentTime;

        // Track time when fishing
        if(!ProfileStatsHandler.instance().profileStats.timerPaused) {
            ProfileStatsHandler.instance().profileStats.activeTime += delta;
        }
    }

    public static class ProfileStats {
        // Session stats
        public int fishCaughtCount = 0;
        public float totalXP = 0.0f;
        public float totalValue = 0.0f;
        public Map<String, Integer> variantCounts = new HashMap<>();
        public Map<String, Integer> rarityCounts = new HashMap<>();
        public Map<String, Integer> fishSizeCounts = new HashMap<>();
        public int petCaughtCount = 0;
        public int shardCaughtCount = 0;

        // Current active timer stats
        public long activeTime = 0;
        public long lastFishCaughtTime = 0;
        public long lastUpdateTime = System.currentTimeMillis();
        public boolean timerPaused = true;

        // All-time stats
        public int allFishCaughtCount = 0;
        public float allTotalXP = 0.0f;
        public float allTotalValue = 0.0f;
        public Map<String, Integer> allRarityCounts = new HashMap<>();
        public Map<String, Integer> allVariantCounts = new HashMap<>();
        public Map<String, Integer> allFishSizeCounts = new HashMap<>();
        public int allPetCaughtCount = 0;
        public int allShardCaughtCount = 0;

        // Equipped Pet
        public int equippedPetSlot = -1;
        public Types.Pet equippedPet = null;
    }
}
