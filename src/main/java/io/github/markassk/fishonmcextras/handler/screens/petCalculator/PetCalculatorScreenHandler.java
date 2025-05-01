package io.github.markassk.fishonmcextras.handler.screens.petCalculator;

import io.github.markassk.fishonmcextras.FOMC.Types;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.PetCalculatorHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetCalculatorScreenHandler {
    private static PetCalculatorScreenHandler INSTANCE = new PetCalculatorScreenHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public static PetCalculatorScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PetCalculatorScreenHandler();
        }
        return INSTANCE;
    }

    public HashMap<String, List<Text>> assemblePetText() {
        HashMap<String, List<Text>> assembledTextLists = new HashMap<>();
        List<Text> leftPet = new ArrayList<>();
        List<Text> rightPet = new ArrayList<>();
        List<Text> calculatedPet = new ArrayList<>();

        // Left Pet
        leftPet.add(Text.literal("ʟᴇꜰᴛ ᴘᴇᴛ").formatted(Formatting.YELLOW, Formatting.BOLD));
        if(PetCalculatorHandler.instance().selectedPet[PetCalculatorHandler.PetList.LEFT.id] != null) {
            Types.Pet pet = PetCalculatorHandler.instance().selectedPet[PetCalculatorHandler.PetList.LEFT.id];
            ItemStack petStack = PetCalculatorHandler.instance().selectedPetStacks[PetCalculatorHandler.PetList.LEFT.id];
            leftPet.addAll(assembleColumn(pet, petStack));
        }

        // Right pet
        rightPet.add(Text.literal("ʀɪɢʜᴛ ᴘᴇᴛ").formatted(Formatting.YELLOW, Formatting.BOLD));
        if(PetCalculatorHandler.instance().selectedPet[PetCalculatorHandler.PetList.RIGHT.id] != null) {
            Types.Pet pet = PetCalculatorHandler.instance().selectedPet[PetCalculatorHandler.PetList.RIGHT.id];
            ItemStack petStack = PetCalculatorHandler.instance().selectedPetStacks[PetCalculatorHandler.PetList.RIGHT.id];
            rightPet.addAll(assembleColumn(pet, petStack));
        }

        // Calculated Pet
        calculatedPet.add(Text.literal("ᴄᴀʟᴄᴜʟᴀᴛᴇᴅ ᴘᴇᴛ").formatted(Formatting.YELLOW, Formatting.BOLD));
        if(PetCalculatorHandler.instance().calculatedPet != null) {
            Types.Pet pet = PetCalculatorHandler.instance().calculatedPet;
            ItemStack petStack = PetCalculatorHandler.instance().selectedPetStacks[PetCalculatorHandler.PetList.RIGHT.id];
            calculatedPet.addAll(assembleColumn(pet, petStack));
        }

        assembledTextLists.put("leftPet", leftPet);
        assembledTextLists.put("rightPet", rightPet);
        assembledTextLists.put("calculatedPet", calculatedPet);
        return assembledTextLists;
    }

    private List<Text> assembleColumn(Types.Pet pet, ItemStack petStack) {
        List<Text> textPet = new ArrayList<>();

        textPet.add(petStack.getName());
        assert pet.rarity != null;
        textPet.add(pet.rarity.TAG);
        textPet.add(Text.empty());
        textPet.add(Text.literal("ᴄʟɪᴍᴀᴛᴇ ѕᴛᴀᴛ").formatted(Formatting.GRAY, Formatting.BOLD));
        textPet.add(TextHelper.concat(
                Text.literal("ʟᴜᴄᴋ: ").withColor(0xFF7ED7C1),
                Text.literal(TextHelper.fmt(pet.climateStat.maxLuck)),
                Text.literal(" (").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(pet.climateStat.percentLuck * 100, config.petTooltip.decimalPlaces)).formatted(Formatting.GRAY),
                Text.literal("%)").formatted(Formatting.GRAY)
        ));
        textPet.add(TextHelper.concat(
                Text.literal("ѕᴄᴀʟᴇ: ").withColor(0xFF4B86EE),
                Text.literal(TextHelper.fmt(pet.climateStat.maxScale)),
                Text.literal(" (").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(pet.climateStat.percentScale * 100, config.petTooltip.decimalPlaces)).formatted(Formatting.GRAY),
                Text.literal("%)").formatted(Formatting.GRAY)
        ));
        textPet.add(Text.empty());
        textPet.add(Text.literal("ʟᴏᴄᴀᴛɪᴏɴ ѕᴛᴀᴛ").formatted(Formatting.GRAY, Formatting.BOLD));
        textPet.add(TextHelper.concat(
                Text.literal("ʟᴜᴄᴋ: ").withColor(0xFF7ED7C1),
                Text.literal(TextHelper.fmt(pet.locationStat.maxLuck)),
                Text.literal(" (").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(pet.locationStat.percentLuck * 100, config.petTooltip.decimalPlaces)).formatted(Formatting.GRAY),
                Text.literal("%)").formatted(Formatting.GRAY)
        ));
        textPet.add(TextHelper.concat(
                Text.literal("ѕᴄᴀʟᴇ: ").withColor(0xFF4B86EE),
                Text.literal(TextHelper.fmt(pet.locationStat.maxScale)),
                Text.literal(" (").formatted(Formatting.GRAY),
                Text.literal(TextHelper.fmt(pet.locationStat.percentScale * 100, config.petTooltip.decimalPlaces)).formatted(Formatting.GRAY),
                Text.literal("%)").formatted(Formatting.GRAY)
        ));
        textPet.add(Text.empty());
        textPet.add(Text.literal("ᴘᴇᴛ ʀᴀᴛɪɴɢ").formatted(Formatting.GRAY, Formatting.BOLD));
        textPet.add(TextHelper.concat(
                Types.Pet.getConstantFromPercent(pet.percentPetRating).TAG,
                Text.literal(" ("),
                Text.literal(TextHelper.fmt(pet.percentPetRating * 100, config.petTooltip.decimalPlaces)),
                Text.literal("%)")
        ).withColor(Types.Pet.getConstantFromPercent(pet.percentPetRating).COLOR));

        return textPet;
    }
}
