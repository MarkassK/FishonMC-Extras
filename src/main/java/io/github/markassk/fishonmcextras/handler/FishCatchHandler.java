package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.*;

public class FishCatchHandler  {
    private static FishCatchHandler INSTANCE = new FishCatchHandler();

    private final List<UUID> trackedFishes = new ArrayList<>();
    private final List<UUID> trackedPets = new ArrayList<>();
    private int trackedShards = 0;
    private long lastTimeUsedRod = 0;
    private boolean hasUsedRod = false;

    public static FishCatchHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishCatchHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        assert minecraftClient.player != null;
        if(minecraftClient.player.fishHook != null && !hasUsedRod) {
            hasUsedRod = true;
        } else if (hasUsedRod) {
            hasUsedRod = false;
            lastTimeUsedRod = System.currentTimeMillis();
        }


        if(!LoadingHandler.instance().isLoadingDone) {
            // Pre scan inventory for old fishes
            scanInventoryBackground(minecraftClient.player);
        } else {
            if(System.currentTimeMillis() - lastTimeUsedRod < 1000) {
                // Scan Inventory for new fishes
                scanInventory(minecraftClient.player);
            } else {
                // Scan Inventory in the background for changes when not fishing
                scanInventoryBackground(minecraftClient.player);
            }
        }

        // Ticking while in server
        ProfileStatsHandler.instance().tickTimer();
    }

    public void onJoinServer(PlayerEntity player) {
        ProfileStatsHandler.instance().playerUUID = player.getUuid();
        ProfileStatsHandler.instance().loadStats();
        trackedFishes.clear();
        trackedPets.clear();
        trackedShards = 0;
    }

    public void onLeaveServer() {
        ProfileStatsHandler.instance().saveStats();
    }

    public void reset() {
        LoadingHandler.instance().isLoadingDone = false;
        trackedFishes.clear();
        trackedPets.clear();
        trackedShards = 0;
    }

    private void scanInventoryBackground(PlayerEntity player) {
        int shardCount = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);

            if(Types.getFOMCItem(stack) instanceof Types.Fish fish && fish.catcher.equals(player.getUuid())) {
                if(!trackedFishes.contains(fish.id)) {
                    trackedFishes.add(fish.id);
                }
            } else if (Types.getFOMCItem(stack) instanceof  Types.Pet pet && pet.discoverer.equals(player.getUuid())) {
                if(!trackedPets.contains(pet.id)) {
                    trackedPets.add(pet.id);
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Shard) {
                shardCount += stack.getCount();
            }
        }

        if(trackedShards != shardCount) {
            trackedShards = shardCount;
        }
    }

    private void scanInventory(PlayerEntity player) {
        int shardCount = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);

            if(Types.getFOMCItem(stack) instanceof Types.Fish fish && fish.catcher.equals(player.getUuid())) {
                if(!trackedFishes.contains(fish.id)) {
                    trackedFishes.add(fish.id);
                    ProfileStatsHandler.instance().updateStatsOnCatch(fish);

                    // Update stats on Equipped Pet
                    PetEquipHandler.instance().updatePet(player);
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Pet pet && pet.discoverer.equals(player.getUuid())) {
                if(!trackedPets.contains(pet.id)) {
                    trackedPets.add(pet.id);
                    ProfileStatsHandler.instance().updateStatsOnCatch(pet);
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Shard) {
                shardCount += stack.getCount();
            }
        }

        if(shardCount > trackedShards) {
            ProfileStatsHandler.instance().updateStatsOnCatch(shardCount - trackedShards);
            trackedShards = shardCount;
        }
    }
}
