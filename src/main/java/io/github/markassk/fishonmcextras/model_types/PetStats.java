package io.github.markassk.fishonmcextras.model_types;

import io.github.markassk.fishonmcextras.handler.PetMergeCalculatorHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class PetStats {
    private final String name;
    private final String rarity;
    private final float lLuck;
    private final float lScale;
    private final float cLuck;
    private final float cScale;
    private final float lLuckPercent;
    private final float lScalePercent;
    private final float cLuckPercent;
    private final float cScalePercent;
    public String hiddenRating;

    public PetStats(String name, String rarity, float lLuck, float lScale, float cLuck, float cScale, float lLuckPercent, float lScalePercent, float cLuckPercent, float cScalePercent) {
        this.name = name;
        this.rarity = rarity;
        this.lLuck = lLuck;
        this.lScale = lScale;
        this.cLuck = cLuck;
        this.cScale = cScale;
        this.lLuckPercent = lLuckPercent;
        this.lScalePercent = lScalePercent;
        this.cLuckPercent = cLuckPercent;
        this.cScalePercent = cScalePercent;
    }

    public PetStats(String name, String rarity, float lLuck, float lScale, float cLuck, float cScale, float lLuckPercent, float lScalePercent, float cLuckPercent, float cScalePercent, String hiddenRating) {
        this.name = name;
        this.rarity = rarity;
        this.lLuck = lLuck;
        this.lScale = lScale;
        this.cLuck = cLuck;
        this.cScale = cScale;
        this.lLuckPercent = lLuckPercent;
        this.lScalePercent = lScalePercent;
        this.cLuckPercent = cLuckPercent;
        this.cScalePercent = cScalePercent;
        this.hiddenRating = hiddenRating;
    }


    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public float getlLuck() {
        return lLuck;
    }

    public float getlScale() {
        return lScale;
    }

    public float getcLuck() {
        return cLuck;
    }

    public float getcScale() {
        return cScale;
    }

    public float getlLuckPercent() {
        return lLuckPercent;
    }

    public float getlScalePercent() {
        return lScalePercent;
    }

    public float getcLuckPercent() {
        return cLuckPercent;
    }

    public float getcScalePercent() {
        return cScalePercent;
    }

    public float getTotalPercent() {
        return (lLuckPercent + lScalePercent + cLuckPercent + cScalePercent) / 4f;
    }

    public String getRatingValue() {
        return PetMergeCalculatorHandler.ratingValue(this.getTotalPercent() * 100);
    }

    public Object[] getRatingString() {
        return PetMergeCalculatorHandler.ratingString(this.getRatingValue());
    }

    public static PetStats getStats(NbtCompound compound, String rating) {
        return new PetStats(
                TextHelper.capitalize(compound.getString("pet")),
                compound.getString("rarity"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getDouble("percent_max"),
                (float) compound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getDouble("percent_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getDouble("percent_max"),
                (float) compound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getDouble("percent_max"),
                rating
        );
    }

    public boolean hasRoundingError() {
        boolean one = !TextHelper.fmt((getcLuck() + getcScale() + getlLuck() + getlScale()) / PetMergeCalculatorHandler.rarityMultiplier(getRarity()), 2).equals(TextHelper.fmt(getTotalPercent() * 100, 2));
        boolean two = hiddenRating != null && !PetMergeCalculatorHandler.ratingValue(this.getTotalPercent() * 100).equals(this.hiddenRating);
        System.out.println(one);
        System.out.println(two);
        return one && two;
    }
}
