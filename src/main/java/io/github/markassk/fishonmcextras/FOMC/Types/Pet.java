package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.ClimateConstant;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Pet extends FOMCItem {
    public final UUID id;
    public final Constant pet;
    public final ClimateConstant climate;
    public final Constant location;

    public final int lvl;

    public final float currentXp;
    public final float neededXp;

    public final Stat climateStat;
    public final Stat locationStat;

    public final float percentPetRating;

    public final String discovererName;
    public final UUID discoverer;

    public final String date;

    private Pet(NbtCompound nbtCompound, String type) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
        this.pet = Constant.valueOfId(nbtCompound.getString("pet"));
        this.climate = ClimateConstant.valueOfId(nbtCompound.getString("climate"));
        this.location = Constant.valueOfId(nbtCompound.getString("location"));
        this.lvl = nbtCompound.getInt("level");
        this.currentXp = nbtCompound.getFloat("xp_cur");
        this.neededXp = nbtCompound.getFloat("xp_need");
        this.climateStat = new Stat(nbtCompound, Constant.CLIMATE_BASE);
        this.locationStat = new Stat(nbtCompound, Constant.LOCATION_BASE);
        this.percentPetRating = getPercentPetRating(this.climateStat.percentLuck, this.climateStat.percentScale, this.locationStat.percentLuck, this.locationStat.percentScale);
        this.discovererName = nbtCompound.getString("username");
        this.discoverer = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));

        this.date = nbtCompound.getString("date");
    }

    public Pet(
            Constant pet,
            Constant rarity,
            float cMaxLuck,
            float cMaxScale,
            float cPercentLuck,
            float cPercentScale,
            float lMaxLuck,
            float lMaxScale,
            float lPercentLuck,
            float lPercentScale
    ) {
        super("pet", rarity);
        this.id = UUID.randomUUID();
        this.pet = pet;
        this.climate = ClimateConstant.DEFAULT;
        this.location = Constant.DEFAULT;
        this.lvl = 100;
        this.currentXp = 0;
        this.neededXp = 0;
        this.climateStat = new Stat(
                Constant.CLIMATE_BASE.ID,
                cMaxLuck,
                cMaxScale,
                cPercentLuck,
                cPercentScale
        );
        this.locationStat = new Stat(
                Constant.LOCATION_BASE.ID,
                lMaxLuck,
                lMaxScale,
                lPercentLuck,
                lPercentScale
        );
        this.percentPetRating = getPercentPetRating(climateStat.percentLuck, climateStat.percentScale, locationStat.percentLuck, locationStat.percentScale);
        this.discovererName = "";
        this.discoverer = null;
        this.date = LocalDate.now().toString();

    }

    public static class Stat {
        public final String id;
        public final float currentLuck;
        public final float currentScale;
        public final float maxLuck;
        public final float maxScale;
        public final float percentLuck;
        public final float percentScale;

        private Stat(NbtCompound nbtCompound, Constant base) {
            switch (base) {
                case Constant.CLIMATE_BASE -> {
                    this.id = nbtCompound.getString("climate");
                    this.currentLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur");
                    this.currentScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur");
                    this.maxLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max");
                    this.maxScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max");
                    this.percentLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getFloat("percent_max");
                    this.percentScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getFloat("percent_max");
                }
                case Constant.LOCATION_BASE -> {
                    this.id = nbtCompound.getString("location");
                    this.currentLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur");
                    this.currentScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur");
                    this.maxLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max");
                    this.maxScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max");
                    this.percentLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getFloat("percent_max");
                    this.percentScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getFloat("percent_max");
                }
                default -> {
                    this.id = Defaults.EMPTY_STRING;
                    this.currentScale = 0f;
                    this.maxLuck = 0f;
                    this.maxScale = 0f;
                    this.percentLuck = 0f;
                    this.percentScale = 0f;
                    this.currentLuck = 0f;
                }
            }
        }

        private Stat(
                String id,
                float maxLuck,
                float maxScale,
                float percentLuck,
                float percentScale
        ) {
            this.id = id;
            this.currentLuck = 0f;
            this.currentScale = 0f;
            this.maxLuck = maxLuck;
            this.maxScale = maxScale;
            this.percentLuck = percentLuck;
            this.percentScale = percentScale;
        }
    }

    private static float getPercentPetRating(float climateLuck, float climateScale, float locationLuck, float locationScale) {
        return (climateLuck + climateScale + locationLuck + locationScale) / 4;
    }

    public static Constant getConstantFromPercent(float value) {
        float ceilValue = Math.round(value * 100f);
        if (ceilValue <= 20) return Constant.SICKLY;
        else if (ceilValue < 30) return Constant.BAD;
        else if (ceilValue < 40) return Constant.BELOW_AVERAGE;
        else if (ceilValue < 50) return Constant.AVERAGE;
        else if (ceilValue < 60) return Constant.GOOD;
        else if (ceilValue < 80) return Constant.GREAT;
        else if (ceilValue < 90) return Constant.EXCELLENT;
        else if (ceilValue < 100) return Constant.AMAZING;
        else if (ceilValue <= 101) return Constant.PERFECT;
        return Constant.DEFAULT;
    }

    public static Constant getConstantFromLine(Text line) {
        if(line.getString().contains(Constant.SICKLY.TAG.getString())) return Constant.SICKLY;
        else if (line.getString().contains(Constant.BAD.TAG.getString())) return Constant.BAD;
        else if (line.getString().contains(Constant.BELOW_AVERAGE.TAG.getString())) return Constant.BELOW_AVERAGE;
        else if (line.getString().contains(Constant.AVERAGE.TAG.getString())) return Constant.AVERAGE;
        else if (line.getString().contains(Constant.GOOD.TAG.getString())) return Constant.GOOD;
        else if (line.getString().contains(Constant.GREAT.TAG.getString())) return Constant.GREAT;
        else if (line.getString().contains(Constant.EXCELLENT.TAG.getString())) return Constant.EXCELLENT;
        else if (line.getString().contains(Constant.AMAZING.TAG.getString())) return Constant.AMAZING;
        else if (line.getString().contains(Constant.PERFECT.TAG.getString())) return Constant.PERFECT;
        return Constant.DEFAULT;
    }

    public static Pet getPet(ItemStack itemStack, String type) {
        return new Pet(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type);
    }
}
