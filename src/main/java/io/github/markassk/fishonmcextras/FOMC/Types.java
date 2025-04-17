package io.github.markassk.fishonmcextras.FOMC;

import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Types {
    public static Pet getPet(ItemStack itemStack, String type) {
        return new Pet(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type);
    }

    public static Fish getFish(ItemStack itemStack, String type, String name) {
        return new Fish(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, name);
    }

    public static Shard getShard(ItemStack itemStack, String type) {
        return new Shard(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type);
    }

    public static FOMCItem getFOMCItem(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")) {
                // Check for types
                switch (nbtCompound.getString("type")) {
                    case Constants.Identifier.ItemTypes.PET -> {
                        return getPet(itemStack, Constants.Identifier.ItemTypes.PET);
                    }
                    case Constants.Identifier.ItemTypes.SHARD -> {
                        return getShard(itemStack, Constants.Identifier.ItemTypes.SHARD);
                    }
                    default -> {
                        return null;
                    }
                }
            // Fish
            } else if (itemStack.getItem() == Items.COD
                    || itemStack.getItem() == Items.WHITE_DYE
                    || itemStack.getItem() == Items.BLACK_DYE
                    || itemStack.getItem() == Items.GOLD_INGOT
            ) {
                String line = Objects.requireNonNull(itemStack.getComponents().get(DataComponentTypes.LORE)).lines().get(15).getString();
                return getFish(itemStack, Constants.Identifier.ItemTypes.FISH, line.substring(line.lastIndexOf(" ") + 1));
            }
        }
        return null;
    }

    public static class Pet extends FOMCItem {
        public final UUID id;
        public final String petId;
        public final String rarityId;

        public final int lvl;

        public final float currentXp;
        public final float neededXp;

        public final Stat climateStat;
        public final Stat locationStat;

        public final String tagPetRating;
        public final float percentPetRating;

        public final String discovererName;
        public final UUID discoverer;

        public final LocalDate date;

        private Pet(NbtCompound nbtCompound, String type) {
            super(type);
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.petId = nbtCompound.getString("pet");
            this.rarityId = nbtCompound.getString("rarity");
            this.lvl = nbtCompound.getInt("level");
            this.currentXp = nbtCompound.getFloat("xp_cur");
            this.neededXp = nbtCompound.getFloat("xp_need");
            this.climateStat = new Stat(nbtCompound, Constants.Identifier.CLIMATE_BASE);
            this.locationStat = new Stat(nbtCompound, Constants.Identifier.LOCATION_BASE);
            this.percentPetRating = getPercentPetRating(this.climateStat.currentLuck, this.climateStat.currentScale, this.locationStat.currentLuck, this.locationStat.currentScale);
            this.tagPetRating = getTagFromPercent(this.percentPetRating * 100);
            this.discovererName = nbtCompound.getString("username");
            this.discoverer = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            this.date = LocalDate.parse(nbtCompound.getString("date"), formatter);
        }

        public static class Stat {
            public final String id;
            public final float currentLuck;
            public final float currentScale;
            public final float maxLuck;
            public final float maxScale;
            public final float percentLuck;
            public final float percentScale;

            private Stat(NbtCompound nbtCompound, String base) {
                switch (base) {
                    case Constants.Identifier.CLIMATE_BASE -> {
                        this.id = nbtCompound.getString("climate");
                        this.currentLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur");
                        this.currentScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur");
                        this.maxLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max");
                        this.maxScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max");
                        this.percentLuck = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("percent_max");
                        this.percentScale = nbtCompound.getList("cbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("percent_max");
                    }
                    case Constants.Identifier.LOCATION_BASE -> {
                        this.id = nbtCompound.getString("location");
                        this.currentLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur");
                        this.currentScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur");
                        this.maxLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("cur_max");
                        this.maxScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("cur_max");
                        this.percentLuck = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(0).getInt("percent_max");
                        this.percentScale = nbtCompound.getList("lbase", NbtElement.COMPOUND_TYPE).getCompound(1).getInt("percent_max");
                    }
                    default -> {
                        this.id = Constants.Identifier.DEFAULT;
                        this.currentScale = 0f;
                        this.maxLuck = 0f;
                        this.maxScale = 0f;
                        this.percentLuck = 0f;
                        this.percentScale = 0f;
                        this.currentLuck = 0f;
                    }
                }
            }
        }

        private static float getPercentPetRating(float climateLuck, float climateScale, float locationLuck, float locationScale) {
            return climateLuck + climateScale + locationLuck + locationScale;
        }

        public static String getTagFromPercent(float value) {
            float ceilValue = Math.round(value);
            if (ceilValue < 10) return Constants.Tag.AWFUL;
            else if (ceilValue < 20) return Constants.Tag.BAD;
            else if (ceilValue < 35) return Constants.Tag.BELOW_AVERAGE;
            else if (ceilValue < 50) return Constants.Tag.AVERAGE;
            else if (ceilValue < 60) return Constants.Tag.GOOD;
            else if (ceilValue < 80) return Constants.Tag.GREAT;
            else if (ceilValue < 90) return Constants.Tag.EXCELLENT;
            else if (ceilValue < 100) return Constants.Tag.AMAZING;
            else if (ceilValue < 101) return Constants.Tag.PERFECT;
            return "Wrong Rarity Selected";
        }
    }

    public static class Fish extends FOMCItem {
        public final UUID id; // id
        public final String fishId; // fish
        public final String rarityId; // rarity
        public final String scientific; // scientific
        public final String variant; // variant

        public final float value; // value
        public final float xp;
        public final String natureId; // nature
        public final String locationId; // location
        public final String sizeId; // size
        public final String sex; // sex
        public final float weight; // weight in lb
        public final float length; // length in in

        public final String groupId; // group
        public final String lifestyleId; // lifestyle
        public final String ecosystem; // ecosystem
        public final String migrationId; // migration

        public final String catcherName; // Tooltip Name
        public final UUID catcher; // catcher
        public final LocalDate date; // date
        public final String rodName; // rod

        private Fish(NbtCompound nbtCompound, String type, String name) {
            super(type);
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.fishId = nbtCompound.getString("fish");
            this.rarityId = nbtCompound.getString("rarity");
            this.scientific = nbtCompound.getString("scientific");
            this.variant = nbtCompound.getString("variant");
            this.value = nbtCompound.getFloat("value");
            this.xp = nbtCompound.getFloat("xp");
            this.natureId = nbtCompound.getString("nature");
            this.locationId = nbtCompound.getString("location");
            this.sizeId = nbtCompound.getString("size");
            this.sex = nbtCompound.getString("sex");
            this.weight = nbtCompound.getFloat("weight");
            this.length = nbtCompound.getFloat("length");
            this.groupId = nbtCompound.getString("group");
            this.lifestyleId = nbtCompound.getString("lifestyle");
            this.ecosystem = nbtCompound.getString("native");
            this.migrationId = nbtCompound.getString("migration");
            this.catcherName = name;
            this.catcher = UUIDHelper.getUUID(nbtCompound.getIntArray("catcher"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            this.date = LocalDate.parse(nbtCompound.getString("date"), formatter);
            this.rodName = nbtCompound.getString("rod");
        }
    }

    public static class Shard extends FOMCItem {
        public final String climateId;
        public final String rarityId;

        private Shard(NbtCompound nbtCompound, String type) {
            super(type);
            this.climateId = nbtCompound.getString("name");
            this.rarityId = nbtCompound.getString("rarity");
        }

    }

    public static class FOMCItem {
        public final String type;
        private FOMCItem(String type) {
            this.type = type;
        }
    }
}
