package io.github.markassk.fishonmcextras.hud;

import java.util.HashMap;
import java.util.Map;

public class FishStatsData {
    // Current session fishHUDConfig
    public int fishCaughtCount = 0;
    public float totalXP = 0.0f;
    public float totalValue = 0.0f;
    public Map<String, Integer> variantCounts = new HashMap<>();
    public Map<String, Integer> rarityCounts = new HashMap<>();
    public Map<String, Integer> sizeCounts = new HashMap<>();
    public long activeTime = 0;
    public long lastFishCaughtTime = 0;
    public boolean timerPaused = false;


    // All-time fishHUDConfig
    public int allFishCaughtCount = 0;
    public float allTotalXP = 0.0f;
    public float allTotalValue = 0.0f;
    public Map<String, Integer> allVariantCounts = new HashMap<>();
    public Map<String, Integer> allRarityCounts = new HashMap<>();
    public Map<String, Integer> allSizeCounts = new HashMap<>();

    // Current pet
    public String equippedPet = null;
}
