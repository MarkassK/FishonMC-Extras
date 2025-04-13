package io.github.markassk.fishonmcextras.handler;

import com.google.gson.Gson;
import com.mojang.serialization.JsonOps;
import io.github.markassk.fishonmcextras.model_types.PetStats;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PetMergeCalculatorHandler {
    private static PetMergeCalculatorHandler INSTANCE = new PetMergeCalculatorHandler();
    public ItemStack[] selectedPets = {null, null};
    public PetStats petOne;
    public PetStats petTwo;
    public PetStats calculatedPet = null;
    public int[] index = {-1, -1};

    public static PetMergeCalculatorHandler instance() {
        if(INSTANCE == null) {
            INSTANCE = new PetMergeCalculatorHandler();
        }
        return INSTANCE;
    }

    public void setPet(ItemStack pet, int list) {
        selectedPets[list] = pet;
        update();
    }

    public void setIndex(int list, int index) {
        this.index[list] = index;
    }

    public void reset() {
        selectedPets = new ItemStack[]{null, null};
        petOne = null;
        petTwo = null;
        this.index = new int[]{-1, -1};
    }

    private void update() {
        if(selectedPets[0] != null) {
            NbtCompound componentOne = getNbt(selectedPets[0]);

            // Pet one data
            assert componentOne != null;
            petOne = PetStats.getStats(componentOne, getRatingFromJson(petToJson(selectedPets[0])));
        }

        if(selectedPets[1] != null) {
            NbtCompound componentTwo = getNbt(selectedPets[1]);

            // Pet two data
            assert componentTwo != null;
            petTwo = PetStats.getStats(componentTwo, getRatingFromJson(petToJson(selectedPets[1])));
        }

        if (selectedPets[0] != null && selectedPets[1] != null) {
            this.calculatedPet = calculatePet(petOne, petTwo);
        }
    }

    private PetStats calculatePet(PetStats petOne, PetStats petTwo) {
        if(Objects.equals(petOne.getRarity(), petTwo.getRarity())) {
            String petResultRarity = rarityUpgrade(petOne.getRarity());
            float petMultiplier = rarityMultiplier(petOne.getRarity());
            float petResultMultiplier = rarityMultiplier(rarityUpgrade(petOne.getRarity()));

            float petResultlLuck = (petOne.getlLuck() / petMultiplier + petTwo.getlLuck() / petMultiplier) / 2 * petResultMultiplier;
            float petResultlScale = (petOne.getlScale() / petMultiplier + petTwo.getlScale() / petMultiplier) / 2 * petResultMultiplier;
            float petResultcLuck = (petOne.getcLuck() / petMultiplier + petTwo.getcLuck() / petMultiplier) / 2 * petResultMultiplier;
            float petResultcScale = (petOne.getcScale() / petMultiplier + petTwo.getcScale() / petMultiplier) / 2 * petResultMultiplier;
            float petResultlLuckPercent = (petOne.getlLuckPercent() + petTwo.getlLuckPercent()) / 2;
            float petResultlScalePercent = (petOne.getlScalePercent() + petTwo.getlScalePercent()) / 2;
            float petResultcLuckPercent = (petOne.getcLuckPercent() + petTwo.getcLuckPercent()) / 2;
            float petResultcScalePercent = (petOne.getcScalePercent() + petTwo.getcScalePercent()) / 2;

            return new PetStats(
                    Objects.equals(petOne.getName(), petTwo.getName()) ? petOne.getName() : petOne.getName() + " + " + petTwo.getName(),
                    petResultRarity,
                    petResultlLuck,
                    petResultlScale,
                    petResultcLuck,
                    petResultcScale,
                    petResultlLuckPercent,
                    petResultlScalePercent,
                    petResultcLuckPercent,
                    petResultcScalePercent
            );
        }
        return null;
    }

    public static String ratingValue(float value) {
        BigDecimal decimal = new BigDecimal(Float.toString(value));
        decimal = decimal.setScale(2, RoundingMode.HALF_EVEN);
        if (decimal.floatValue() <= 20) return "Sickly";
        else if (decimal.floatValue() < 30) return "Bad";
        else if (decimal.floatValue() < 40) return "Below Average";
        else if (decimal.floatValue() < 50) return "Average";
        else if (decimal.floatValue() < 60) return "Good";
        else if (decimal.floatValue() < 80) return "Great";
        else if (decimal.floatValue() < 90) return "Excellent";
        else if (decimal.floatValue() < 100) return "Amazing";
        else if (decimal.floatValue() < 101) return "Perfect";
        return "Wrong Rarity Selected";
    }

    public static String getRatingFromJson(String json) {
        if(json.contains("ѕɪᴄᴋʟʏ")) return "Sickly";
        else if(json.contains("ʙᴀᴅ")) return "Bad";
        else if(json.contains("ʙᴇʟᴏᴡ ᴀᴠᴇʀᴀɢᴇ")) return "Below Average";
        else if(json.contains("ᴀᴠᴇʀᴀɢᴇ")) return "Average";
        else if(json.contains("ɢᴏᴏᴅ")) return "Good";
        else if(json.contains("ɢʀᴇᴀᴛ")) return "Great";
        else if(json.contains("ᴇxᴄᴇʟʟᴇɴᴛ")) return "Excellent";
        else if(json.contains("ᴀᴍᴀᴢɪɴɢ")) return "Amazing";
        else if(json.contains("ᴘᴇʀꜰᴇᴄᴛ")) return "Perfect";
        return "";
    }

    public static Object[] ratingString(String value) {
        return switch (value) {
            case "Sickly" -> new Object[]{"ѕɪᴄᴋʟʏ", 0xFF74403B};
            case "Bad" -> new Object[]{"ʙᴀᴅ", 0xFFFF5555};
            case "Below Average" -> new Object[]{"ʙᴇʟᴏᴡ ᴀᴠᴇʀᴀɢᴇ", 0xFFFCFC54};
            case "Average" -> new Object[]{"ᴀᴠᴇʀᴀɢᴇ", 0xFFFCA800};
            case "Good" -> new Object[]{"ɢᴏᴏᴅ", 0xFF54FC54};
            case "Great" -> new Object[]{"ɢʀᴇᴀᴛ", 0xFF00A800};
            case "Excellent" -> new Object[]{"ᴇxᴄᴇʟʟᴇɴᴛ", 0xFF54FCFC};
            case "Amazing" -> new Object[]{"ᴀᴍᴀᴢɪɴɢ", 0xFFFC54FC};
            case "Perfect \uD83D\uDCAF" -> new Object[]{"ᴘᴇʀꜰᴇᴄᴛ", 0xFFA800A8};
            default -> new Object[]{"", 0xFFFFFFFF};
        };
    }

    public static String rarityString(String rarity) {
        return switch (rarity) {
            case "common" -> "\uf033";
            case "rare" -> "\uf034";
            case "epic" -> "\uf035";
            case "legendary" -> "\uf036";
            case "mythical" -> "\uf037";
            default -> "";
        };
    }

    public static String rarityUpgrade(String rarity) {
        return switch (rarity) {
            case "common" -> "rare";
            case "rare" -> "epic";
            case "epic" -> "legendary";
            case "legendary" -> "mythical";
            default -> "";
        };
    }

    public static float rarityMultiplier(String value) {
        return switch (value) {
            case "common" -> 1f;
            case "rare" -> 2f;
            case "epic" -> 3f;
            case "legendary" -> 5f;
            case "mythical" -> 7.5f;
            default -> 1;
        };
    }

    private NbtCompound getNbt(ItemStack itemStack) {
        NbtComponent component = itemStack.get(DataComponentTypes.CUSTOM_DATA);
        return component != null ? component.getNbt() : null;
    }

    public static String petToJson(ItemStack itemStack) {
        Gson gson = new Gson();
        return gson.toJson(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, itemStack).getOrThrow());
    }
}
