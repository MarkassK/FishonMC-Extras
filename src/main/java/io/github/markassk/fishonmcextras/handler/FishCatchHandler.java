package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.FishStrings;
import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class FishCatchHandler  {
    private static FishCatchHandler INSTANCE = new FishCatchHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private final List<UUID> trackedFishes = new ArrayList<>();
    private final List<UUID> trackedPets = new ArrayList<>();
    private int trackedShards = 0;
    private boolean hasUsedRod = false;
    private boolean isDone = false;

    public long lastTimeUsedRod = 0;

    public static FishCatchHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishCatchHandler();
        }
        return INSTANCE;
    }

    public void init() {
        isDone = false;
    }

    public void tick(MinecraftClient minecraftClient) {
        assert minecraftClient.player != null;
        if(minecraftClient.player.fishHook != null && !hasUsedRod) {
            hasUsedRod = true;
        } else if (hasUsedRod && minecraftClient.player.fishHook == null) {
            hasUsedRod = false;
            this.lastTimeUsedRod = System.currentTimeMillis();
        }


        if(!LoadingHandler.instance().isLoadingDone || !isDone) {
            // Pre scan inventory for old fishes
            scanInventoryBackground(minecraftClient.player);
        } else {
            if(System.currentTimeMillis() - lastTimeUsedRod < 2000) {
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

        if(LoadingHandler.instance().isLoadingDone) {
            isDone = true;
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

                    // Send to TitleHud
                    if(config.fishTracker.fishTrackerToggles.otherToggles.useNewTitle) {
                        sendToTitleHud(stack, fish);
                    }
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Pet pet && pet.discoverer.equals(player.getUuid())) {
                if(!trackedPets.contains(pet.id)) {
                    trackedPets.add(pet.id);
                    ProfileStatsHandler.instance().updateStatsOnCatch();
                }
            } else if (Types.getFOMCItem(stack) instanceof Types.Shard) {
                shardCount += stack.getCount();
            }
        }

        if(shardCount > trackedShards) {
            ProfileStatsHandler.instance().updateStatsOnCatch(1);
            trackedShards = shardCount;
        }
    }

    private void sendToTitleHud(ItemStack stack, Types.Fish fish) {
        // Send to TitleHud
        List<Text> title = new ArrayList<>();
        if(FishStrings.valueOfId(fish.fishId) != null) {
            title.add(Text.literal(Objects.requireNonNull(FishStrings.valueOfId(fish.fishId)).CHARACTER).formatted(Formatting.WHITE));
            title.add(Text.empty());
        }
        assert fish.variant != null;
        if(fish.variant != Constant.NORMAL) {
            title.add(fish.variant.TAG);
        }
        assert fish.rarity != null;
        title.add(TextHelper.concat(
                fish.rarity.TAG,
                Text.literal(" "),
                stack.getName()
        ));
        assert fish.size != null;
        title.add(fish.size.TAG);
        List<Text> subtitle = new ArrayList<>();
        if(config.fishTracker.fishTrackerToggles.otherToggles.showStatsOnCatch) {
            subtitle.add(Text.literal("ᴡᴇɪɢʜᴛ").formatted(Formatting.BOLD).withColor(0xFFFFFF));
            subtitle.add(TextHelper.concat(
                    Text.literal(TextHelper.fmt(fish.weight, 2)),
                    Text.literal("ʟʙ").withColor(0xAAAAAA),
                    Text.literal(" (").withColor(0x555555),
                    Text.literal(TextHelper.fmt(fish.weight * 0.453592f, 2)),
                    Text.literal("ᴋɢ").withColor(0xAAAAAA),
                    Text.literal(")").withColor(0x555555)
            ).withColor(0xFFFFFF));
            subtitle.add(Text.literal("ʟᴇɴɢᴛʜ").formatted(Formatting.BOLD).withColor(0xFFFFFF));
            subtitle.add(TextHelper.concat(
                    Text.literal(TextHelper.fmt(fish.length, 2)),
                    Text.literal("ɪɴ").withColor(0xAAAAAA),
                    Text.literal(" (").withColor(0x555555),
                    Text.literal(TextHelper.fmt(fish.length * 2.54f, 2)),
                    Text.literal("ᴄᴍ").withColor(0xAAAAAA),
                    Text.literal(")").withColor(0x555555)
            ).withColor(0xFFFFFF));
        }

        TitleHandler.instance().setTitleHud(title, config.fishTracker.fishTrackerToggles.otherToggles.showStatsOnCatchTime * 1000L, MinecraftClient.getInstance(), subtitle);
    }
}
