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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProfileStatsHandler {
    private static ProfileStatsHandler INSTANCE = new ProfileStatsHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private boolean isSavedAfterTimer = false;

    public ProfileStats profileStats = new ProfileStats();
    public long lastUpdateTime = System.currentTimeMillis();
    public UUID playerUUID = UUID.randomUUID();

    public static ProfileStatsHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileStatsHandler();
        }
        return INSTANCE;
    }

    public void onJoinServer(PlayerEntity player) {
        ProfileStatsHandler.instance().playerUUID = player.getUuid();
        ProfileStatsHandler.instance().loadStats();
    }

    /**
     * Update stats from new Fish
     */
    public void updateStatsOnCatch(Types.Fish fish) {
        // All-time stats
        this.profileStats.allFishCaughtCount++;
        this.profileStats.allTotalXP += fish.xp;
        this.profileStats.allTotalValue += fish.value;
        this.profileStats.allFishSizeCounts.put(fish.size, this.profileStats.allFishSizeCounts.getOrDefault(fish.size, 0) + 1);
        this.profileStats.allVariantCounts.put(fish.variant, this.profileStats.allVariantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileStats.allRarityCounts.put(fish.rarity, this.profileStats.allRarityCounts.getOrDefault(fish.rarity, 0) + 1);

        this.profileStats.lastFishCaughtTime = System.currentTimeMillis();
        this.profileStats.timerPaused = false;

        // Session stats
        this.profileStats.fishCaughtCount++;
        this.profileStats.totalXP += fish.xp;
        this.profileStats.totalValue += fish.value;
        this.profileStats.fishSizeCounts.put(fish.size, this.profileStats.fishSizeCounts.getOrDefault(fish.size, 0) + 1);
        this.profileStats.variantCounts.put(fish.variant, this.profileStats.variantCounts.getOrDefault(fish.variant, 0) + 1);
        this.profileStats.rarityCounts.put(fish.rarity, this.profileStats.rarityCounts.getOrDefault(fish.rarity, 0) + 1);

        this.profileStats.fishSizeDryStreak.put(fish.size, this.profileStats.allFishCaughtCount);
        this.profileStats.variantDryStreak.put(fish.variant, this.profileStats.allFishCaughtCount);
        this.profileStats.rarityDryStreak.put(fish.rarity, this.profileStats.allFishCaughtCount);

        this.isSavedAfterTimer = false;
        this.saveStats();
    }

    public void updateStatsOnCatch() {
        // All-time stats
        this.profileStats.allPetCaughtCount++;

        // Session stats
        this.profileStats.petCaughtCount++;

        this.profileStats.petDryStreak = this.profileStats.allFishCaughtCount;

        this.saveStats();
    }

    public void updateStatsOnCatch(int count) {
        // All-time stats
        this.profileStats.allShardCaughtCount += count;

        // Session stats
        this.profileStats.shardCaughtCount += count;

        this.profileStats.shardDryStreak = this.profileStats.allFishCaughtCount;

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
            String json = gson.toJson(this.profileStats);
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
                this.profileStats = gson.fromJson(json, ProfileStats.class);
            }
        } catch (IOException e) {
            FishOnMCExtras.LOGGER.error(e.getMessage());
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
        this.profileStats.timerPaused = true;

        this.profileStats.rarityDryStreak.clear();
        this.profileStats.fishSizeDryStreak.clear();
        this.profileStats.variantDryStreak.clear();

        FishCatchHandler.instance().reset();
        this.saveStats();
    }

    public void tickTimer() {
        long currentTime = System.currentTimeMillis();
        // Pause timer when not fishing after x seconds
        long timeSinceLastFish = currentTime - ProfileStatsHandler.instance().profileStats.lastFishCaughtTime;
        if (timeSinceLastFish >= TimeUnit.SECONDS.toMillis(config.fishTracker.autoPauseTimer)) {
            ProfileStatsHandler.instance().profileStats.timerPaused = true;
            if(!isSavedAfterTimer) {
                this.isSavedAfterTimer = true;
                this.saveStats();
            }
        }

        long delta = currentTime - ProfileStatsHandler.instance().lastUpdateTime;
        ProfileStatsHandler.instance().lastUpdateTime = currentTime;

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

        // Equipped Pet
        public int equippedPetSlot = -1;
        public Types.Pet equippedPet = null;

        // Dry streak count
        public int petDryStreak;
        public int shardDryStreak;
        public Map<Constant, Integer> rarityDryStreak = new HashMap<>();
        public Map<Constant, Integer> variantDryStreak = new HashMap<>();
        public Map<Constant, Integer> fishSizeDryStreak = new HashMap<>();
    }
}
