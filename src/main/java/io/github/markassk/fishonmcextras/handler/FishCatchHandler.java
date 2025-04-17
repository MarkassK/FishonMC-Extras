package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;

public class FishCatchHandler  {
    private static FishCatchHandler INSTANCE = new FishCatchHandler();

    private final List<Types.Fish> trackedFishes = new ArrayList<>();
    private final List<Types.Pet> trackedPets = new ArrayList<>();
    private int trackedShards = 0;
    private long lastTimeUsedRod = 0;
    private boolean hasUsedRod = false;

    public boolean isDoneScanning = false;

    public static FishCatchHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishCatchHandler();
        }
        return INSTANCE;
    }

    public void tick(PlayerEntity player) {
        if(player.fishHook != null && !hasUsedRod) {
            hasUsedRod = true;
        } else if (hasUsedRod) {
            hasUsedRod = false;
            lastTimeUsedRod = System.currentTimeMillis();
        }


        if(!isDoneScanning) {
            // Pre scan inventory for old fishes
            scanInventoryBackground(player);
        } else {
            if(System.currentTimeMillis() - lastTimeUsedRod < 1000) {
                // Scan Inventory for new fishes
                scanInventory(player);
            } else {
                // Scan Inventory in the background for changes when not fishing
                scanInventoryBackground(player);
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
        isDoneScanning = false;
        trackedFishes.clear();
        trackedPets.clear();
        trackedShards = 0;
    }

    private void scanInventoryBackground(PlayerEntity player) {
        int shardCount = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);

            if(stack.getItem() == Items.FISHING_ROD) {
                isDoneScanning = true;
                System.out.println("[FoE] Scan Done");
            }

            if(Types.getFOMCItem(stack) instanceof Types.Fish fish && fish.catcher.equals(player.getUuid())) {
                trackedFishes.add(fish);
            } else if (Types.getFOMCItem(stack) instanceof  Types.Pet pet && pet.discoverer.equals(player.getUuid())) {
                trackedPets.add(pet);
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
                if(!containsFish(trackedFishes, fish)) {
                    trackedFishes.add(fish);
                    ProfileStatsHandler.instance().updateStatsOnCatch(fish);
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Pet pet && pet.discoverer.equals(player.getUuid())) {
                if(!containsPet(trackedPets, pet)) {
                    trackedPets.add(pet);
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

    private boolean containsFish(final List<Types.Fish> fishList, final Types.Fish fishToCompare){
        return fishList.stream().map(fish -> fish.id).anyMatch(fishToCompare.id::equals);
    }

    private boolean containsPet(final List<Types.Pet> petList, final Types.Pet petToCompare) {
        return petList.stream().map(pet -> pet.id).anyMatch(petToCompare.id::equals);
    }


}
