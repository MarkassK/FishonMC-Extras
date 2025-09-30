package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Fish;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.packet.PacketHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.Objects;

public class FishCatchHandler  {
    private static FishCatchHandler INSTANCE = new FishCatchHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private Text title = Text.empty();
    private Text subtitle = Text.empty();
    private final List<UUID> trackFishList = new ArrayList<>();
    private boolean fishFound = false;
    private boolean preCheck = false;
    private boolean isFull = false;
    private long fishCaughtTime = 0L;
    private boolean hasUsedRod = false;

    public long lastTimeUsedRod = 0L;

    public static FishCatchHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishCatchHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player == null || !LoadingHandler.instance().isLoadingDone || minecraftClient.world == null) {
            return;
        }

        if(minecraftClient.player.fishHook != null && !hasUsedRod) {
            hasUsedRod = true;
        } else if (hasUsedRod && minecraftClient.player.fishHook == null) {
            hasUsedRod = false;
            this.lastTimeUsedRod = System.currentTimeMillis();
        }

        if(this.preCheck) {
            this.updateTrackedFish(minecraftClient.player);
            this.preCheck = false;
            FishOnMCExtras.LOGGER.info("[FoE] Tracked Fish: {}", this.trackFishList.size());
        }

        if(this.fishFound) {
            if (System.currentTimeMillis() - this.fishCaughtTime < 2000L) {
                if (!this.isFull) {
                    for (int i = minecraftClient.player.getInventory().main.size() - 1; i >= 0; i--) {
                        ItemStack stack = minecraftClient.player.getInventory().main.get(i);
                        if(stack.isEmpty()) {
                            continue;
                        }
                        this.processStack(stack, minecraftClient);
                        
                    }
                }

                if(FullInventoryHandler.instance().slotsLeft == 0) {
                    this.isFull = true;
                }
                
                

            } else {
                this.fishFound = false;
                this.isFull = false;
                this.updateTrackedFish(minecraftClient.player);   
                FishOnMCExtras.LOGGER.error("[FoE] Fish not found");
            }
        }

        ProfileDataHandler.instance().tickTimer();
    }

    public void tickEntities(Entity entity, MinecraftClient minecraftClient) {
        if(this.fishFound && this.isFull) {
            if(entity instanceof ItemEntity itemEntity) {
                ItemStack stack =  itemEntity.getStack();
                if(stack.isEmpty()) {
                    return;
                }
                this.processStack(stack, minecraftClient);
            }
        }
    }

    public void onJoinServer() {
        this.preCheck = true;
    }

    public void onLeaveServer() {
        this.fishFound = false;
    }

    public void catchTitle(Text title) {
        if(title.getString().length() != 1 || title.equals(Text.empty())) {
            return;
        }

        if(isFish(title.getString().charAt(0))) {
            this.title = title;
            this.fishFound = true;
            this.fishCaughtTime = System.currentTimeMillis();
        }
    }

    public void catchSubtitle(Text title) {
        if(title.getString().contains(Constant.COMMON.TAG.getString())
                || title.getString().contains(Constant.RARE.TAG.getString())
                || title.getString().contains(Constant.EPIC.TAG.getString())
                || title.getString().contains(Constant.LEGENDARY.TAG.getString())
                || title.getString().contains(Constant.MYTHICAL.TAG.getString())
                || title.getString().contains(Constant.SPECIAL.TAG.getString())
        ) {
            this.subtitle = title;
        }
    }

    public void reset() {
        LoadingHandler.instance().isLoadingDone = false;
        if(MinecraftClient.getInstance().player != null) {
            this.updateTrackedFish(MinecraftClient.getInstance().player);
        }
    }

    public boolean onReceiveMessage(Text text) {
        if(text.getString().startsWith("PET DROP! You pulled")) {
            ProfileDataHandler.instance().updatePetCaughtStatsOnCatch();
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Pet");
        }

        if(text.getString().startsWith("RARE CATCH! You pulled") && text.getString().contains("Shard")) {
            ProfileDataHandler.instance().updateShardCaughtStatsOnCatch(1);
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Shard");
        }

        if(text.getString().startsWith("RARE CATCH! You pulled") && text.getString().contains("Lightning in a Bottle")) {
            ProfileDataHandler.instance().updateLightningBottleCaughtStatsOnCatch();
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Shard");
        }
        
        return false; // Don't suppress any messages
    }

    private boolean isFish(char character) {
        return (int) character > 0xE000 && (int) character < 0xE999;
    }

    private void processStack(ItemStack stack, MinecraftClient minecraftClient) {
        Fish fish = Fish.getFish(stack);
        if (fish != null
                && minecraftClient.player != null
                && Objects.equals(fish.catcher, minecraftClient.player.getUuid())
                && !trackFishList.contains(fish.id)
                && this.subtitle.getString().contains(stack.getName().getString())) {
            FishOnMCExtras.LOGGER.info("[FoE] Tracking {}", stack.getName().getString());

            if(config.fishTracker.fishTrackerToggles.otherToggles.useNewTitle) {
                this.sendToTitleHud(fish, this.title, this.subtitle);
            }

            ProfileDataHandler.instance().updateStatsOnCatch(fish);
            ProfileDataHandler.instance().updateStatsOnCatch();
            QuestHandler.instance().updateQuest(fish);
            PetEquipHandler.instance().updatePet(minecraftClient.player);

            if (config.contestTracker.shouldShowFullContest() && config.contestTracker.refreshOnContestPB) {
                // Check if caught fish is for contest and refresh if it's heavier
                var typecheck = ContestHandler.instance().type.replace("Heaviest", "").trim().toLowerCase();
                ContestHandler contestHandler = ContestHandler.instance();
                
                // Check if we are in the right location for the contest
                boolean locationMatches = Objects.equals(
                    Objects.requireNonNull(Constant.valueOfTag(contestHandler.location)) == Constant.SPAWNHUB 
                        ? Constant.CYPRESS_LAKE.ID 
                        : Objects.requireNonNull(Constant.valueOfTag(contestHandler.location).ID), 
                    BossBarHandler.instance().currentLocation.ID
                );
                
                if (contestHandler.isContest && typecheck.contains(fish.groupId.toLowerCase())
                        && locationMatches && (fish.weight > contestHandler.biggestFish)) {
                    ContestHandler.instance().biggestFish = fish.weight;
                    ContestHandler.instance().setRefreshReason("personal_best");
                    minecraftClient.player.networkHandler.sendChatCommand("contest");
                    
                    // Send packet to notify other players of contest PB
                    if(config.contestTracker.recieveLocalPBs) {
                        PacketHandler.CONTEST_PB_PACKET.sendContestPBPacket(fish.groupId, minecraftClient.player.getName().getString(), fish.weight, ScoreboardHandler.instance().level);
                    }
                    
                    FishOnMCExtras.LOGGER.info("[FoE] Refreshed Contest Stats - New heaviest fish: {} lbs",
                            fish.weight);
                }
            }

            this.fishFound = false;
            this.isFull = false;
            this.updateTrackedFish(minecraftClient.player);
            this.title = Text.empty();
            this.subtitle = Text.empty();
            
        }
        
    }

    private void updateTrackedFish(PlayerEntity player) {
        trackFishList.clear();
        for (int i = player.getInventory().main.size() - 1; i >= 0; i--) {
            ItemStack stack = player.getInventory().main.get(i);

            if(stack.isEmpty()) {
                continue;
            }

            Fish fish = Fish.getFish(stack);
            if (fish != null
                    && Objects.equals(fish.catcher, player.getUuid())
                    && !trackFishList.contains(fish.id)) {
                trackFishList.add(fish.id);
            }
        }
    }

    private void sendToTitleHud(Fish fish, Text icon, Text name) {
        // Send to TitleHud
        List<Text> title = new ArrayList<>();
        title.add(icon.copy().formatted(Formatting.WHITE));
        title.add(Text.empty());
        title.add(name);
        title.add(fish.size.TAG);
        if(FullInventoryHandler.instance().slotsLeft == 0) {
            title.add(Text.literal("Inventory Full!").formatted(Formatting.RED));
        }
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
