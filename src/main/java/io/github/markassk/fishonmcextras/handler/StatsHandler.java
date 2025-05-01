package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsHandler {
    private static StatsHandler INSTANCE = new StatsHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private ProfileDataHandler.ProfileData dummyProfileData = new ProfileDataHandler.ProfileData();

    public static StatsHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new StatsHandler();
        }
        return INSTANCE;
    }

    private void getData(MinecraftClient minecraftClient) {
        ProfileDataHandler.ProfileData dummyProfileData = new ProfileDataHandler.ProfileData();

        AtomicInteger fishCaught = new AtomicInteger(-1);

        for (int i = 0; i < Objects.requireNonNull(minecraftClient.player).currentScreenHandler.slots.size(); i++) {
            ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack();

            if(minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() && itemStack.getItem() == Items.KNOWLEDGE_BOOK) {
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
                    else if (loreLine.contains("ꜰɪꜱʜ ᴄᴀᴜɢʜᴛ")) fishCaught.set(getValue(loreLine, ":"));
                });
            }
        }

        if(fishCaught.get() != -1) {
            dummyProfileData.allFishCaughtCount = fishCaught.get();
            dummyProfileData.petDryStreak = fishCaught.get();
            dummyProfileData.shardDryStreak = fishCaught.get();
            dummyProfileData.rarityDryStreak.put(Constant.COMMON, fishCaught.get());
            dummyProfileData.rarityDryStreak.put(Constant.RARE, fishCaught.get());
            dummyProfileData.rarityDryStreak.put(Constant.EPIC, fishCaught.get());
            dummyProfileData.rarityDryStreak.put(Constant.LEGENDARY, fishCaught.get());
            dummyProfileData.rarityDryStreak.put(Constant.MYTHICAL, fishCaught.get());
            dummyProfileData.fishSizeDryStreak.put(Constant.BABY, fishCaught.get());
            dummyProfileData.fishSizeDryStreak.put(Constant.JUVENILE, fishCaught.get());
            dummyProfileData.fishSizeDryStreak.put(Constant.ADULT, fishCaught.get());
            dummyProfileData.fishSizeDryStreak.put(Constant.LARGE, fishCaught.get());
            dummyProfileData.fishSizeDryStreak.put(Constant.GIGANTIC, fishCaught.get());
            dummyProfileData.variantDryStreak.put(Constant.ALBINO, fishCaught.get());
            dummyProfileData.variantDryStreak.put(Constant.MELANISTIC, fishCaught.get());
            dummyProfileData.variantDryStreak.put(Constant.TROPHY, fishCaught.get());
            dummyProfileData.variantDryStreak.put(Constant.FABLED, fishCaught.get());

            this.dummyProfileData = dummyProfileData;
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
        ProfileDataHandler.instance().profileData.allPetCaughtCount = 0;
        ProfileDataHandler.instance().profileData.allShardCaughtCount = 0;
        ProfileDataHandler.instance().saveStats();
    }

    public void onButtonClick(MinecraftClient minecraftClient) {
        this.getData(minecraftClient);
        this.saveStats();
    }

    private int toIntFromString(String value) {
        value = value.trim();
        if(value.contains("K")) {
            System.out.println(value);
            return (int) (Float.parseFloat(value.substring(0, value.indexOf("K"))) * 1000f);
        } else {
            return Integer.parseInt(value);
        }
    }

    private int getValue(String line, Constant prefix) {
        return toIntFromString(line.substring(line.indexOf(prefix.TAG.getString()) + prefix.TAG.getString().length()));
    }

    private int getValue(String line, String prefix) {
        return toIntFromString(line.substring(line.indexOf(prefix) + 1));
    }
}
