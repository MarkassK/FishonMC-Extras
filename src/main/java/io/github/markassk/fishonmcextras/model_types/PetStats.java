package io.github.markassk.fishonmcextras.model_types;

import io.github.markassk.fishonmcextras.common.handler.PetMergeCalculatorHandler;

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
}
