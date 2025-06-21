package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.screens.widget.IconButtonWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsImportHandler {
    private static StatsImportHandler INSTANCE = new StatsImportHandler();
    private ProfileDataHandler.ProfileData dummyProfileData = new ProfileDataHandler.ProfileData();

    public boolean screenInit = false;
    public boolean isOnScreen = false;

    public static StatsImportHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new StatsImportHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(screenInit && isOwnPage(minecraftClient)) {
            this.createButton(minecraftClient);
            this.screenInit = false;
        }

        if(isOnScreen && minecraftClient.currentScreen != null && !Objects.equals(minecraftClient.currentScreen.getTitle().getString(), "\uEEE4픲")) {
            Screens.getButtons(minecraftClient.currentScreen).forEach(clickableWidget -> {
                if(clickableWidget.getMessage() != null) {
                    if(Objects.equals(clickableWidget.getMessage().getString(), "Import Stats")) {
                        clickableWidget.active = false;
                        clickableWidget.visible = false;
                        isOnScreen = false;
                    }
                }
            });
        }
    }

    private void getData(MinecraftClient minecraftClient) {
        ProfileDataHandler.ProfileData dummyProfileData = new ProfileDataHandler.ProfileData();
        ProfileDataHandler.ProfileData oldProfileData = ProfileDataHandler.instance().profileData;

        AtomicInteger fishCaught = new AtomicInteger(-1);

        for (int i = 0; i < Objects.requireNonNull(minecraftClient.player).currentScreenHandler.slots.size(); i++) {
            ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack();

            if(minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() && itemStack.getItem() == Items.KNOWLEDGE_BOOK && isOwnPage(minecraftClient)) {
                List<Text> loreLines = Objects.requireNonNull(itemStack.get(DataComponentTypes.LORE)).lines();

                loreLines.forEach(lore -> {
                    String loreLine = lore.getString();
                    if (loreLine.contains(Constant.COMMON.TAG.getString())) dummyProfileData.allRarityCounts.put(Constant.COMMON, getValue(loreLine, Constant.COMMON));
                    else if (loreLine.contains(Constant.RARE.TAG.getString())) dummyProfileData.allRarityCounts.put(Constant.RARE, getValue(loreLine, Constant.RARE));
                    else if (loreLine.contains(Constant.EPIC.TAG.getString())) dummyProfileData.allRarityCounts.put(Constant.EPIC, getValue(loreLine, Constant.EPIC));
                    else if (loreLine.contains(Constant.LEGENDARY.TAG.getString())) dummyProfileData.allRarityCounts.put(Constant.LEGENDARY, getValue(loreLine, Constant.LEGENDARY));
                    else if (loreLine.contains(Constant.MYTHICAL.TAG.getString())) dummyProfileData.allRarityCounts.put(Constant.MYTHICAL, getValue(loreLine, Constant.MYTHICAL));
                    else if (loreLine.contains(Constant.BABY.TAG.getString())) dummyProfileData.allFishSizeCounts.put(Constant.BABY, getValue(loreLine, Constant.BABY));
                    else if (loreLine.contains(Constant.JUVENILE.TAG.getString())) dummyProfileData.allFishSizeCounts.put(Constant.JUVENILE, getValue(loreLine, Constant.JUVENILE));
                    else if (loreLine.contains(Constant.ADULT.TAG.getString())) dummyProfileData.allFishSizeCounts.put(Constant.ADULT, getValue(loreLine, Constant.ADULT));
                    else if (loreLine.contains(Constant.LARGE.TAG.getString())) dummyProfileData.allFishSizeCounts.put(Constant.LARGE, getValue(loreLine, Constant.LARGE));
                    else if (loreLine.contains(Constant.GIGANTIC.TAG.getString())) dummyProfileData.allFishSizeCounts.put(Constant.GIGANTIC, getValue(loreLine, Constant.GIGANTIC));
                    else if (loreLine.contains(Constant.ALBINO.TAG.getString())) dummyProfileData.allVariantCounts.put(Constant.ALBINO, getValue(loreLine, Constant.ALBINO));
                    else if (loreLine.contains(Constant.MELANISTIC.TAG.getString())) dummyProfileData.allVariantCounts.put(Constant.MELANISTIC, getValue(loreLine, Constant.MELANISTIC));
                    else if (loreLine.contains(Constant.TROPHY.TAG.getString())) dummyProfileData.allVariantCounts.put(Constant.TROPHY, getValue(loreLine, Constant.TROPHY));
                    else if (loreLine.contains(Constant.FABLED.TAG.getString())) dummyProfileData.allVariantCounts.put(Constant.FABLED, getValue(loreLine, Constant.FABLED));
                    else if (loreLine.contains("ꜰɪꜱʜ ᴄᴀᴜɢʜᴛ")) fishCaught.set(getValue(loreLine));
                });
            }
        }

        if(fishCaught.get() != -1) {
            dummyProfileData.allFishCaughtCount = fishCaught.get();
            dummyProfileData.petDryStreak = Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.petDryStreak), fishCaught.get());
            dummyProfileData.shardDryStreak = Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.shardDryStreak), fishCaught.get());
            dummyProfileData.rarityDryStreak.put(Constant.COMMON, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.rarityDryStreak.getOrDefault(Constant.COMMON, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.rarityDryStreak.put(Constant.RARE, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.rarityDryStreak.getOrDefault(Constant.RARE, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.rarityDryStreak.put(Constant.EPIC, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.rarityDryStreak.getOrDefault(Constant.EPIC, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.rarityDryStreak.put(Constant.LEGENDARY, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.rarityDryStreak.getOrDefault(Constant.LEGENDARY, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.rarityDryStreak.put(Constant.MYTHICAL, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.rarityDryStreak.getOrDefault(Constant.MYTHICAL, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.fishSizeDryStreak.put(Constant.BABY, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.fishSizeDryStreak.getOrDefault(Constant.BABY, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.fishSizeDryStreak.put(Constant.JUVENILE, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.fishSizeDryStreak.getOrDefault(Constant.JUVENILE, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.fishSizeDryStreak.put(Constant.ADULT, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.fishSizeDryStreak.getOrDefault(Constant.ADULT, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.fishSizeDryStreak.put(Constant.LARGE, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.fishSizeDryStreak.getOrDefault(Constant.LARGE, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.fishSizeDryStreak.put(Constant.GIGANTIC, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.fishSizeDryStreak.getOrDefault(Constant.GIGANTIC, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.variantDryStreak.put(Constant.ALBINO, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.variantDryStreak.getOrDefault(Constant.ALBINO, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.variantDryStreak.put(Constant.MELANISTIC, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.variantDryStreak.getOrDefault(Constant.MELANISTIC, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.variantDryStreak.put(Constant.TROPHY, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.variantDryStreak.getOrDefault(Constant.TROPHY, oldProfileData.allFishCaughtCount)), fishCaught.get()));
            dummyProfileData.variantDryStreak.put(Constant.FABLED, Math.min(fishCaught.get() - (oldProfileData.allFishCaughtCount - oldProfileData.variantDryStreak.getOrDefault(Constant.FABLED, oldProfileData.allFishCaughtCount)), fishCaught.get()));

            this.dummyProfileData = dummyProfileData;
        }
    }

    private IconButtonWidget getButton(MinecraftClient minecraftClient) {
        return IconButtonWidget.builder(Text.literal("Import Stats"), button ->
                        StatsImportHandler.instance().onButtonClick(minecraftClient))
                .position(minecraftClient.getWindow().getScaledWidth() / 2 + 100, minecraftClient.getWindow().getScaledHeight() / 2 - 100)
                .tooltip(Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Import your stats into ").formatted(Formatting.WHITE),
                                Text.literal("FoE").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                Text.literal(".\n").formatted(Formatting.WHITE),
                                Text.literal("This will delete your previous all time stats!\n").formatted(Formatting.RED),
                                Text.literal("- The stats are not accurate and could be off by 5.\n- You can change your FoE stats in the config file located in /config/foe/stats.").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )))
                .itemIcon(Items.COMMAND_BLOCK.getDefaultStack())
                .build();
    }


    private void createButton(MinecraftClient minecraftClient) {
        if (minecraftClient.currentScreen != null) {
            Screens.getButtons(minecraftClient.currentScreen).add(getButton(minecraftClient));
        }
    }

    private void saveStats() {
        ProfileDataHandler.instance().profileData.allRarityCounts = dummyProfileData.allRarityCounts;
        ProfileDataHandler.instance().profileData.allFishSizeCounts = dummyProfileData.allFishSizeCounts;
        ProfileDataHandler.instance().profileData.allVariantCounts = dummyProfileData.allVariantCounts;
        ProfileDataHandler.instance().profileData.allFishCaughtCount = dummyProfileData.allFishCaughtCount;
        ProfileDataHandler.instance().profileData.petDryStreak = dummyProfileData.petDryStreak;
        ProfileDataHandler.instance().profileData.shardDryStreak = dummyProfileData.shardDryStreak;
        ProfileDataHandler.instance().profileData.rarityDryStreak = dummyProfileData.rarityDryStreak;
        ProfileDataHandler.instance().profileData.fishSizeDryStreak = dummyProfileData.fishSizeDryStreak;
        ProfileDataHandler.instance().profileData.variantDryStreak = dummyProfileData.variantDryStreak;
        ProfileDataHandler.instance().profileData.isStatsInitialized = true;
        ProfileDataHandler.instance().saveStats();
    }

    public void onButtonClick(MinecraftClient minecraftClient) {
        this.getData(minecraftClient);
        this.saveStats();
    }

    private int toIntFromString(String value) {
        value = value.trim();
        if(value.contains("K")) {
            return (int) (Float.parseFloat(value.substring(0, value.indexOf("K"))) * 1000f);
        } else {
            return Integer.parseInt(value);
        }
    }

    public boolean isOwnPage(MinecraftClient minecraftClient) {
        AtomicBoolean isMe = new AtomicBoolean(false);
        for (int i = 0; i < Objects.requireNonNull(minecraftClient.player).currentScreenHandler.slots.size(); i++) {
            ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack();
            if (minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() && itemStack.getItem() == Items.PLAYER_HEAD && Objects.requireNonNull(itemStack.get(DataComponentTypes.PROFILE)).id().isPresent()) {

                if(Objects.requireNonNull(itemStack.get(DataComponentTypes.PROFILE)).id().get().equals(minecraftClient.player.getUuid())) {

                    isMe.set(true);
                }
            }
        }
        return isMe.get();
    }

    private int getValue(String line, Constant prefix) {
        return toIntFromString(line.substring(line.indexOf(prefix.TAG.getString()) + prefix.TAG.getString().length()));
    }

    private int getValue(String line) {
        return toIntFromString(line.substring(line.indexOf(":") + 1));
    }
}
