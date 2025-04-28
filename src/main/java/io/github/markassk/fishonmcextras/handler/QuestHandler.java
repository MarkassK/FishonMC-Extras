package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Quests;
import io.github.markassk.fishonmcextras.FOMC.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class QuestHandler {
    private static QuestHandler INSTANCE = new QuestHandler();

    public static QuestHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestHandler();
        }
        return INSTANCE;
    }

    private static boolean questMenuState = false;
    private final Map<Constant, Map<Integer, Quests>> activeQuests = new HashMap<>();

    public static void setQuestMenuState(boolean state){
        questMenuState = state;
    }

    public void tick(MinecraftClient minecraftClient){
        // Tick only if quest menu is open
        // Update active quests
        if (questMenuState && minecraftClient.player != null) {
            // Scan quest menu for data about the quest
            for (int i = 12; i < 25; i++) {
                ComponentMap components = minecraftClient.player.currentScreenHandler.getSlot(i).getStack().getComponents();
                if (components != null) {
                    //System.out.println(i + "Slot " + components);
                } else{
                    System.out.println("Quest Slot " + i + " NULL.");
                    continue;
                }

                // Correspond background slot to "real slot"
                int slot = switch (i) {
                    case 12 -> 1;
                    case 13 -> 2;
                    case 14 -> 3;
                    case 20 -> 4;
                    case 21 -> 5;
                    case 22 -> 6;
                    case 23 -> 7;
                    case 24 -> 8;
                    default -> 0;
                };


                if (slot != 0) {
                    LoreComponent lore = components.get(DataComponentTypes.LORE);
                    assert lore != null;
                    List<Text> lines = lore.lines();

                    int progress = 0;
                    int needed = 0;
                    String fish = "";

                    // TODO Stop crash, "contains" check lines to pull progress, needed, fish
                    // Consider switching all instances of the "fish" variable to "Types.Fish", currently String as it's easier for updateQuest, also when pulling quests dont need to worry about whether its rarity or size were catching.
                    for (Text line : lines){
                        String raw = line.getString();

                        System.out.println(raw);
                    }

                    Constant location = LocationHandler.instance().currentLocation;
                    Map<Integer, Quests> questsAtLocation = activeQuests.computeIfAbsent(location, k -> new HashMap<>());

                    Quests newQuest = new Quests(location, slot, fish, progress, needed);
                    questsAtLocation.put(slot, newQuest);


                }
            }
        }
    }

    public void updateQuest(Types.Fish fish) {
        Constant location = LocationHandler.instance().currentLocation;
        Map<Integer, Quests> questsAtLocation = activeQuests.get(location);

        if (questsAtLocation != null) {
            for (Quests quest : questsAtLocation.values()) {
                String neededFish = quest.getFishType();
                if (quest.getLocation() == location &&
                        Objects.equals(neededFish, Objects.requireNonNull(fish.size).ID) ||
                        Objects.equals(neededFish, Objects.requireNonNull(fish.rarity).ID)) {
                    quest.incrementProgress();

                    // TODO add update to send to Quest HUD (Doesnt exist)
                }
            }
        }
    }
}
