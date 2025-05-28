package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class PetCalculatorHandler {
    private static PetCalculatorHandler INSTANCE = new PetCalculatorHandler();

    public ItemStack[] selectedPetStacks = {null, null};
    public Pet[] selectedPet = {null, null};
    public Text calculatedPetName = Text.empty();
    public Pet calculatedPet = null;
    public int[] selectedIndex = {-1, -1};

    public static PetCalculatorHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PetCalculatorHandler();
        }
        return INSTANCE;
    }

    public void setPet(ItemStack pet, PetList side) {
        selectedPetStacks[side.id] = pet;

        selectedPet[side.id] = Pet.getPet(pet);
        this.update();
    }

    public void setIndex(PetList side, int index) {
        this.selectedIndex[side.id] = index;
    }

    private void update() {
        if(selectedPet[PetList.LEFT.id] != null && selectedPet[PetList.RIGHT.id] != null) {
            this.calculatedPet = this.calculatePet();
            this.calculatedPetName = selectedPetStacks[PetList.LEFT.id].getName();
        }
    }

    public void reset() {
        this.selectedPetStacks = new ItemStack[]{null, null};
        this.selectedPet = new Pet[]{null, null};
        this.calculatedPet = null;
        this.calculatedPetName = Text.empty();
        this.selectedIndex = new int[]{-1, -1};
    }

    private Pet calculatePet() {
        if(
                Objects.equals(selectedPet[PetList.LEFT.id].rarity.ID, selectedPet[PetList.RIGHT.id].rarity.ID)
                && Objects.equals(selectedPet[PetList.LEFT.id].pet.ID, selectedPet[PetList.RIGHT.id].pet.ID)
        ) {

            Pet leftPet = selectedPet[PetList.LEFT.id];
            Pet rightPet = selectedPet[PetList.RIGHT.id];

            Constant pet = leftPet.pet;
            Constant rarity = getUpgradedRarity(leftPet.rarity);

            float rarityMultiplierBefore = getRarityMultiplier(leftPet.rarity);
            float rarityMultiplierAfter = getRarityMultiplier(rarity);

            float cMaxLuck = (leftPet.climateStat.maxLuck / rarityMultiplierBefore +
                    rightPet.climateStat.maxLuck / rarityMultiplierBefore) / 2 * rarityMultiplierAfter;
            float cMaxScale = (leftPet.climateStat.maxScale / rarityMultiplierBefore +
                    rightPet.climateStat.maxScale / rarityMultiplierBefore) / 2 * rarityMultiplierAfter;
            float lMaxLuck = (leftPet.locationStat.maxLuck / rarityMultiplierBefore +
                    rightPet.locationStat.maxLuck / rarityMultiplierBefore) / 2 * rarityMultiplierAfter;
            float lMaxScale = (leftPet.locationStat.maxScale / rarityMultiplierBefore +
                    rightPet.locationStat.maxScale / rarityMultiplierBefore) / 2 * rarityMultiplierAfter;
            float cPercentLuck = (leftPet.climateStat.percentLuck + rightPet.climateStat.percentLuck) / 2;
            float cPercentScale = (leftPet.climateStat.percentScale + rightPet.climateStat.percentScale) / 2;
            float lPercentLuck = (leftPet.locationStat.percentLuck + rightPet.locationStat.percentLuck) / 2;
            float lPercentScale = (leftPet.locationStat.percentScale + rightPet.locationStat.percentScale) / 2;

            return new Pet(pet, rarity, cMaxLuck, cMaxScale, cPercentLuck, cPercentScale, lMaxLuck, lMaxScale, lPercentLuck, lPercentScale);
        } else {
            return null;
        }
    }

    private Constant getUpgradedRarity(Constant rarity) {
        return switch (rarity) {
            case COMMON -> Constant.RARE;
            case RARE -> Constant.EPIC;
            case EPIC -> Constant.LEGENDARY;
            case LEGENDARY -> Constant.MYTHICAL;
            default -> Constant.DEFAULT;
        };
    }

    private float getRarityMultiplier(Constant rarity) {
        return switch (rarity) {
            case COMMON -> 1f;
            case RARE -> 2f;
            case EPIC -> 3f;
            case LEGENDARY -> 5f;
            case MYTHICAL -> 7.5f;
            default -> 1;
        };
    }

    public enum PetList {
        LEFT(0),
        RIGHT(1),
        MIDDLE(2);

        public final int id;

        PetList(int side) {
            this.id = side;
        }
    }
}
