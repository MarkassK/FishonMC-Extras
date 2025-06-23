package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Fish;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class QuestHandler {
    private static QuestHandler INSTANCE = new QuestHandler();
    private boolean hasInitialized = false;

    public Map<Constant, List<Quest>> activeQuests = new HashMap<>();
    public boolean questMenuState = false;

    public static QuestHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient){
        if (questMenuState && minecraftClient.player != null) {
            List<Quest> quests = new ArrayList<>();
            AtomicInteger slot = new AtomicInteger(1);

            // Check if screen has 8 shulkers. Otherwise, don't do anything.
            // Due to selecting quest screen is using same header.
            AtomicInteger countSafety = new AtomicInteger(0);
            for (int i = 0; i < minecraftClient.player.currentScreenHandler.slots.size(); i++) {
                ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack().copy();
                if(minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() ) {
                    if(
                            itemStack.getItem() == Items.GREEN_SHULKER_BOX
                                    || itemStack.getItem() == Items.RED_SHULKER_BOX
                                    || itemStack.getItem() == Items.ORANGE_SHULKER_BOX
                                    || itemStack.getItem() == Items.YELLOW_SHULKER_BOX
                                    || itemStack.getItem() == Items.WHITE_SHULKER_BOX
                                    || itemStack.getItem() == Items.GRAY_SHULKER_BOX
                    ) {
                        countSafety.incrementAndGet();
                    }
                }
            }

            if(countSafety.get() == 8) {
                for (int i = 0; i < minecraftClient.player.currentScreenHandler.slots.size(); i++) {
                    ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack().copy();
                    if(minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() ) {
                        if(itemStack.getItem() == Items.RED_SHULKER_BOX || itemStack.getItem() == Items.YELLOW_SHULKER_BOX || itemStack.getItem() == Items.ORANGE_SHULKER_BOX) {
                            // Started Quest
                            quests.add(getStartedQuest(itemStack, slot.getAndIncrement()));
                        } else if (itemStack.getItem() == Items.WHITE_SHULKER_BOX) {
                            // Not Started Qyest
                            quests.add(new Quest(slot.getAndIncrement()));
                        } else if (itemStack.getItem() == Items.GREEN_SHULKER_BOX) {
                            // Finished Quest
                            quests.add(new Quest(true, slot.getAndIncrement()));
                        }
                    }
                }
            }

            if(!quests.isEmpty()) {
                if(BossBarHandler.instance().currentLocation == Constant.SPAWNHUB) {
                    activeQuests.put(Constant.CYPRESS_LAKE, quests);
                } else {
                    activeQuests.put(BossBarHandler.instance().currentLocation, quests);
                }
            }
        }

        if(!hasInitialized && LoadingHandler.instance().isLoadingDone) {
            hasInitialized = true;
            this.activeQuests = ProfileDataHandler.instance().profileData.activeQuests;
        }
    }

    public void updateQuest(Fish fish) {
        List<Quest> currentLocationQuests = BossBarHandler.instance().currentLocation == Constant.SPAWNHUB ? this.activeQuests.get(Constant.CYPRESS_LAKE) : this.activeQuests.get(BossBarHandler.instance().currentLocation);
        if(currentLocationQuests != null) {
            currentLocationQuests.forEach(quest -> {
                if(quest.goal == fish.rarity || quest.goal == fish.size) {
                    quest.incrementProgress();
                }
            });
        }
        onScreenClose();
    }

    public void onScreenClose() {
        ProfileDataHandler.instance().profileData.activeQuests = this.activeQuests;
        ProfileDataHandler.instance().saveStats();
    }

    private Quest getStartedQuest(ItemStack itemStack, int slot) {
        List<Text> loreLines = Objects.requireNonNull(itemStack.get(DataComponentTypes.LORE)).lines();
        AtomicReference<String> goal = new AtomicReference<>();
        AtomicReference<String> progress = new AtomicReference<>();
        AtomicReference<String> needed = new AtomicReference<>();

        loreLines.forEach(lore -> {
            String loreLine = lore.getString();
            if(loreLine.contains("Catch")) {
                goal.set(loreLine.substring(loreLine.indexOf("x") + 2, loreLine.indexOf("fish") - 1).trim());
            } else if (loreLine.contains("ɪɴ ᴘʀᴏɢʀᴇss ")) {
                progress.set(loreLine.substring(loreLine.indexOf("(") + 1, loreLine.indexOf("/")).trim());
                needed.set(loreLine.substring(loreLine.indexOf("/") + 1, loreLine.indexOf(")")).trim());
            }
        });

        return new Quest(Constant.valueOfId(goal.get()), Integer.parseInt(progress.get()), Integer.parseInt(needed.get()), slot);
    }

    public boolean isQuestInitialized() {
        return BossBarHandler.instance().currentLocation == Constant.SPAWNHUB ? activeQuests.containsKey(Constant.CYPRESS_LAKE) : activeQuests.containsKey(BossBarHandler.instance().currentLocation);
    }

    public static class Quest {
        public Constant goal;
        public int progress;
        public int needed;
        private boolean questDone;
        public final boolean isStarted;
        public final int slot;

        public Quest(Constant goal, int progress, int needed, int slot){
            this.goal = goal;
            this.progress = progress;
            this.needed = needed;
            this.isStarted = true;
            this.slot = slot;
        }

        public Quest(int slot) {
            this.isStarted = false;
            this.slot = slot;
        }

        public Quest(boolean questDone, int slot) {
            this.questDone = questDone;
            this.isStarted = true;
            this.slot = slot;
        }

        public void incrementProgress() {
            this.progress++;
            this.questDone = this.needed > 0 && this.progress >= this.needed;
        }

        public boolean questDone() {
            return (this.needed > 0 && this.progress >= this.needed) || questDone;
        }
    }
}
