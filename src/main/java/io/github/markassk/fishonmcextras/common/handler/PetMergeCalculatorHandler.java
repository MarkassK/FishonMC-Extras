package io.github.markassk.fishonmcextras.common.handler;

import com.google.gson.Gson;
import com.mojang.serialization.JsonOps;
import io.github.markassk.fishonmcextras.common.PetStats;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

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
            Gson gson = new Gson();

            System.out.println(gson.toJson(NbtCompound.CODEC.encodeStart(JsonOps.INSTANCE, componentOne).getOrThrow()));

            // Pet one data
            assert componentOne != null;
            petOne = getStats(componentOne);

            // Pet One - [STDOUT]: {"level":100,"lbase":[{"min":5,"cur_max":20,"id":"toledobend_luck","max":25,"percent_max":0.84,"cur":20},{"min":5,"cur_max":23,"id":"toledobend_scale","max":25,"percent_max":0.92,"cur":23}],"location":"toledobend","xp_cur":12000.0,"rarity":"common","date":"12/26/2024","id":[412308345,1123962446,-2032311133,-953008819],"username":"{MINECRAFT_USERNAME}","updatedStats":1,"cbase":[{"min":5,"cur_max":23,"id":"subtropical_luck","max":25,"percent_max":0.92,"cur":23},{"min":5,"cur_max":18,"id":"subtropical_scale","max":25,"percent_max":0.72,"cur":18}],"climate":"subtropical","xp_need":100000.0,"uuid":[-1247167561,1119111786,-1631666698,1769770720],"pet":"duck","type":"pet"}

            // Pet Two - [STDOUT]: {"level":100,"lbase":[{"min":5,"cur_max":25,"id":"toledobend_luck","max":25,"percent_max":1.0,"cur":25},{"min":5,"cur_max":25,"id":"toledobend_scale","max":25,"percent_max":1.0,"cur":25}],"location":"toledobend","xp_cur":107500.0,"rarity":"common","date":"01/26/2025","max_stack_size":1,"id":[-1955563180,-962115005,-1394308568,-179744566],"username":"ItsMeDjeff","updatedStats":1,"cbase":[{"min":5,"cur_max":14,"id":"subtropical_luck","max":25,"percent_max":0.56,"cur":14},{"min":5,"cur_max":25,"id":"subtropical_scale","max":25,"percent_max":1.0,"cur":25}],"climate":"subtropical","xp_need":100000.0,"uuid":[372878717,-1193916841,-1658116853,449039815],"pet":"duck","type":"pet"}
        }

        if(selectedPets[1] != null) {
            NbtCompound componentTwo = getNbt(selectedPets[1]);

            // Pet two data
            assert componentTwo != null;
            petTwo = getStats(componentTwo);
        }

        if (selectedPets[0] != null && selectedPets[1] != null) {
            this.calculatedPet = calculatePet(petOne, petTwo);
        }
    }

    private PetStats getStats(NbtCompound compound) {
        return new PetStats(
                capitalize(compound.getString("pet")),
                compound.getString("rarity"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getDouble("percent_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getDouble("percent_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getDouble("percent_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getDouble("percent_max")
        );
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
        float ceilValue = Math.round(value);
        if (ceilValue < 10) return "Awful";
        else if (ceilValue < 20) return "Bad";
        else if (ceilValue < 35) return "Below Average";
        else if (ceilValue < 50) return "Average";
        else if (ceilValue < 60) return "Good";
        else if (ceilValue < 80) return "Great";
        else if (ceilValue < 90) return "Excellent";
        else if (ceilValue < 100) return "Amazing";
        else if (ceilValue < 101) return "Perfect";
        return "Wrong Rarity Selected";
    }

    public static Object[] ratingString(String value) {
        return switch (value) {
            case "Awful" -> new Object[]{"ᴀᴡꜰᴜʟ", 0xFFAA0000};
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

    public static String capitalize(String str) {
        if(str == null || str.length()<=1) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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
}
