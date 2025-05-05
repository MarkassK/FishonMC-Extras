package io.github.markassk.fishonmcextras.FOMC;

import io.github.markassk.fishonmcextras.util.ColorHelper;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.UUIDHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Types {
    public static Pet getPet(ItemStack itemStack, String type) {
        return new Pet(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Fish getFish(ItemStack itemStack, String type, String name) {
        return new Fish(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack), name);
    }

    public static Shard getShard(ItemStack itemStack, String type) {
        return new Shard(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Armor getArmor(ItemStack itemStack, String type) {
        return new Armor(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Bait getBait(ItemStack itemStack, String type) {
        return new Bait(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Lure getLure(ItemStack itemStack, String type) {
        return new Lure(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static FishingRod getFishingRod(ItemStack itemStack, String type, String name) {
        return new FishingRod(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack), name);
    }

    public static Line getLine(ItemStack itemStack, String type) {
        return new Line(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Pole getPole(ItemStack itemStack, String type) {
        return new Pole(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static Reel getReel(ItemStack itemStack, String type) {
        return new Reel(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type, ItemStackHelper.getCustomModelData(itemStack));
    }

    public static FOMCItem getFOMCItem(ItemStack itemStack) {
        if(itemStack.get(DataComponentTypes.CUSTOM_DATA) != null && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem")) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")) {
                // Check for types
                return switch (nbtCompound.getString("type")) {
                    case Defaults.ItemTypes.PET -> getPet(itemStack, Defaults.ItemTypes.PET);
                    case Defaults.ItemTypes.SHARD -> getShard(itemStack, Defaults.ItemTypes.SHARD);
                    case Defaults.ItemTypes.ARMOR -> getArmor(itemStack, Defaults.ItemTypes.ARMOR);
                    case Defaults.ItemTypes.BAIT -> getBait(itemStack, Defaults.ItemTypes.BAIT);
                    case Defaults.ItemTypes.LURE -> getLure(itemStack, Defaults.ItemTypes.LURE);
                    case Defaults.ItemTypes.LINE -> getLine(itemStack, Defaults.ItemTypes.LINE);
                    case Defaults.ItemTypes.POLE -> getPole(itemStack, Defaults.ItemTypes.POLE);
                    case Defaults.ItemTypes.REEL -> getReel(itemStack, Defaults.ItemTypes.REEL);
                    default -> null;
                };
            // Fish
            } else if (itemStack.getItem() == Items.COD
                    || itemStack.getItem() == Items.WHITE_DYE
                    || itemStack.getItem() == Items.BLACK_DYE
                    || itemStack.getItem() == Items.GOLD_INGOT
            ) {
                String line = Objects.requireNonNull(itemStack.getComponents().get(DataComponentTypes.LORE)).lines().get(15).getString();
                return getFish(itemStack, Defaults.ItemTypes.FISH, line.substring(line.lastIndexOf(" ") + 1));
            } else if (itemStack.getItem() == Items.FISHING_ROD) {
                return getFishingRod(itemStack, Defaults.ItemTypes.FISHINGROD, itemStack.getName().getString());
            }
        }
        return null;
    }

    public static class Pet extends FOMCItem {
        public final UUID id;
        public final Constant pet;
        public final Constant rarity;
        public final Constant climate;
        public final Constant location;

        public final int lvl;

        public final float currentXp;
        public final float neededXp;

        public final Stat climateStat;
        public final Stat locationStat;

        public final float percentPetRating;

        public final String discovererName;
        public final UUID discoverer;

        public final LocalDate date;

        private Pet(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.pet = Constant.valueOfId(nbtCompound.getString("pet"));
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
            this.climate = Constant.valueOfId(nbtCompound.getString("climate"));
            this.location = Constant.valueOfId(nbtCompound.getString("location"));
            this.lvl = nbtCompound.getInt("level");
            this.currentXp = nbtCompound.getFloat("xp_cur");
            this.neededXp = nbtCompound.getFloat("xp_need");
            this.climateStat = new Stat(nbtCompound, Constant.CLIMATE_BASE);
            this.locationStat = new Stat(nbtCompound, Constant.LOCATION_BASE);
            this.percentPetRating = getPercentPetRating(this.climateStat.percentLuck, this.climateStat.percentScale, this.locationStat.percentLuck, this.locationStat.percentScale);
            this.discovererName = nbtCompound.getString("username");
            this.discoverer = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            this.date = LocalDate.parse(nbtCompound.getString("date"), formatter);
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
            super("pet", -1);
            this.id = null;
            this.pet = pet;
            this.rarity = rarity;
            this.climate = Constant.DEFAULT;
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
            this.date = LocalDate.now();

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
    }

    public static class Fish extends FOMCItem {
        public final UUID id; // id
        public final String fishId; // fish
        public final Constant rarity; // rarity
        public final String scientific; // scientific
        public final Constant variant; // variant

        public final float value; // value
        public final float xp;
        public final String natureId; // nature
        public final Constant location; // location
        public final Constant size; // size
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

        private Fish(NbtCompound nbtCompound, String type, int customModelData, String name) {
            super(type, customModelData);
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.fishId = nbtCompound.getString("fish");
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
            this.scientific = nbtCompound.getString("scientific");
            this.variant = Constant.valueOfId(nbtCompound.getString("variant"));
            this.value = nbtCompound.getFloat("value");
            this.xp = nbtCompound.getFloat("xp");
            this.natureId = nbtCompound.getString("nature");
            this.location = Constant.valueOfId(nbtCompound.getString("location"));
            this.size = Constant.valueOfId(nbtCompound.getString("size"));
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
        public final Constant rarity;

        private Shard(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.climateId = nbtCompound.getString("name");
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
        }

    }

    public static class Armor extends FOMCItem {
        public final List<ArmorBonus> armorBonuses;
        public final UUID id;
        public final int color;
        public final int quality;
        public final boolean identified;
        public final Constant rarity;
        public final String armorPiece;
        public final Constant climate;
        public final UUID crafter;
        public final ArmorStat luck;
        public final ArmorStat scale;
        public final ArmorStat prospect;

        private Armor(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.armorBonuses = List.of(
                    new ArmorBonus(nbtCompound.getList("fish_bonus", NbtElement.LIST_TYPE).getCompound(0)),
                    new ArmorBonus(nbtCompound.getList("fish_bonus", NbtElement.LIST_TYPE).getCompound(1)),
                    new ArmorBonus(nbtCompound.getList("fish_bonus", NbtElement.LIST_TYPE).getCompound(2)),
                    new ArmorBonus(nbtCompound.getList("fish_bonus", NbtElement.LIST_TYPE).getCompound(3)),
                    new ArmorBonus(nbtCompound.getList("fish_bonus", NbtElement.LIST_TYPE).getCompound(4))
            );
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("itemUUID"));
            this.color = ColorHelper.getColorFromNbt(nbtCompound.getString("rgb"));
            this.quality = nbtCompound.getInt("quality");
            this.identified = nbtCompound.getBoolean("identified");
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
            this.armorPiece = nbtCompound.getString("piece");
            this.climate = Constant.valueOfId(nbtCompound.getString("name"));
            this.crafter = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));
            this.luck = new ArmorStat(nbtCompound.getList(nbtCompound.getString("base"), NbtElement.LIST_TYPE).getCompound(0));
            this.scale = new ArmorStat(nbtCompound.getList(nbtCompound.getString("base"), NbtElement.LIST_TYPE).getCompound(1));
            this.prospect = new ArmorStat(nbtCompound.getList(nbtCompound.getString("base"), NbtElement.LIST_TYPE).getCompound(2));
        }

        public static class ArmorBonus {
            public final int tier;
            public final boolean rolled;
            public final int rolls;
            public final boolean unlocked;
            public final float cur;
            public final String id;

            private ArmorBonus(NbtCompound nbtCompound) {
                this.tier = nbtCompound.getInt("tier");
                this.rolled = nbtCompound.getBoolean("rolled");
                this.rolls = nbtCompound.getInt("rolls");
                this.unlocked = nbtCompound.getBoolean("unlocked");
                this.cur = nbtCompound.getFloat("cur");
                this.id = nbtCompound.getString("id");
            }
        }

        public static class ArmorStat {
            public final int amount;
            public final float max;

            private ArmorStat(NbtCompound nbtCompound) {
                this.amount = nbtCompound.getInt("cur");
                this.max = nbtCompound.getFloat("max");
            }
        }
    }

    public static class Bait extends FOMCItem {
        public final String name;
        public int counter;
        public final Constant water;
        public final String intricacy;
        public final List<BaitStats> baitStats;
        public final Constant rarity;

        private Bait(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.name = nbtCompound.getString("name");
            this.counter = nbtCompound.getInt("counter");
            this.water = Constant.valueOfId(nbtCompound.getString("water"));
            this.intricacy = nbtCompound.getString("intricacy");
            NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
            List<NbtCompound> nbtCompoundList = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                nbtCompoundList.add(nbtList.getCompound(i));
            }
            this.baitStats = nbtCompoundList.stream().map(BaitStats::new).toList();
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
        }

        public static class BaitStats {
            public final int cur;
            public final String id;

            private  BaitStats(NbtCompound nbtCompound) {
                this.cur = nbtCompound.getInt("cur");
                this.id = nbtCompound.getString("id");
            }
        }
    }

    public static class Lure extends FOMCItem {
        public final String name;
        public final int totalUses;
        public int counter;
        public final Constant water;
        public final String intricacy;
        public final List<LureStats> lureStats;
        public final Constant rarity;
        public final String size;

        private Lure(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.name = nbtCompound.getString("name");
            this.counter = nbtCompound.getInt("counter");
            this.water = Constant.valueOfId(nbtCompound.getString("water"));
            this.intricacy = nbtCompound.getString("intricacy");
            NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
            List<NbtCompound> nbtCompoundList = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                nbtCompoundList.add(nbtList.getCompound(i));
            }
            this.lureStats = nbtCompoundList.stream().map(LureStats::new).toList();
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
            this.totalUses = nbtCompound.getInt("totalUses");
            this.size = nbtCompound.getString("size");
        }

        public static class LureStats {
            public final int cur;
            public final String id;

            private LureStats(NbtCompound nbtCompound) {
                this.cur = nbtCompound.getInt("cur");
                this.id = nbtCompound.getString("id");
            }
        }
    }

    public static class Line extends FOMCItem {
        public final String name;
        public final UUID id;
        public final Constant water;
        public final List<LineStats> lineStats;
        public final Constant rarity; // is capitalized

        private Line(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.name = nbtCompound.getString("name");
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.water = Constant.valueOfId(nbtCompound.getString("water"));
            NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
            List<NbtCompound> nbtCompoundList = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                nbtCompoundList.add(nbtList.getCompound(i));
            }
            this.lineStats = nbtCompoundList.stream().map(LineStats::new).toList();
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
        }

        public static class LineStats {
            public final int cur;
            public final String id;

            private LineStats(NbtCompound nbtCompound) {
                this.cur = nbtCompound.getInt("cur");
                this.id = nbtCompound.getString("id");
            }
        }
    }

    public static class Pole extends FOMCItem {
        public final String name;
        public final UUID id;
        public final Constant water;
        public final List<PoleStats> poleStats;
        public final Constant rarity; // is capitalized

        private Pole(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.name = nbtCompound.getString("name");
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.water = Constant.valueOfId(nbtCompound.getString("water"));
            NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
            List<NbtCompound> nbtCompoundList = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                nbtCompoundList.add(nbtList.getCompound(i));
            }
            this.poleStats = nbtCompoundList.stream().map(PoleStats::new).toList();
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
        }

        public static class PoleStats {
            public final int cur;
            public final String id;

            private PoleStats(NbtCompound nbtCompound) {
                this.cur = nbtCompound.getInt("cur");
                this.id = nbtCompound.getString("id");
            }
        }
    }

    public static class Reel extends FOMCItem {
        public final String name;
        public final UUID id;
        public final Constant water;
        public final List<ReelStats> reelStats;
        public final Constant rarity; // is capitalized

        private Reel(NbtCompound nbtCompound, String type, int customModelData) {
            super(type, customModelData);
            this.name = nbtCompound.getString("name");
            this.id = UUIDHelper.getUUID(nbtCompound.getIntArray("id"));
            this.water = Constant.valueOfId(nbtCompound.getString("water"));
            NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
            List<NbtCompound> nbtCompoundList = new ArrayList<>();
            for (int i = 0; i < nbtList.size(); i++) {
                nbtCompoundList.add(nbtList.getCompound(i));
            }
            this.reelStats = nbtCompoundList.stream().map(ReelStats::new).toList();
            this.rarity = Constant.valueOfId(nbtCompound.getString("rarity"));
        }

        public static class ReelStats {
            public final int cur;
            public final String id;

            private ReelStats(NbtCompound nbtCompound) {
                this.cur = nbtCompound.getInt("cur");
                this.id = nbtCompound.getString("id");
            }
        }
    }

    public static class FishingRod extends FOMCItem {
        public final String name;
        public final boolean soulboundRod;
        public final String skin;
        public final UUID owner;
        public final List<FOMCItem> tacklebox;
        public final Line line;
        public final Pole pole;
        public final Reel reel;

        private FishingRod(NbtCompound nbtCompound, String type, int customModelData, String name) {
            super(type, customModelData);
            this.name = name;
            this.soulboundRod = nbtCompound.getBoolean("soulbound_rod");
            this.skin = nbtCompound.getString("skin");
            this.owner = UUIDHelper.getUUID(nbtCompound.getIntArray("uuid"));
            NbtList nbtBaitList = (NbtList) nbtCompound.get("tacklebox");
            List<NbtCompound> nbtCompoundBaitList = new ArrayList<>();
            if(nbtBaitList != null) {
                for (int i = 0; i < nbtBaitList.size(); i++) {
                    nbtCompoundBaitList.add(nbtBaitList.getCompound(i));
                }
            }
            this.tacklebox = nbtCompoundBaitList.stream().map(nbtCompound1 -> {
                if(Objects.equals(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data").getString("type"), Defaults.ItemTypes.BAIT)) {
                    return new Bait(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data"), Defaults.ItemTypes.BAIT, nbtCompound1.getCompound("components").getInt("minecraft:custom_model_data"));
                } else if (Objects.equals(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data").getString("type"), Defaults.ItemTypes.LURE)) {
                    return new Lure(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data"), Defaults.ItemTypes.LURE, nbtCompound1.getCompound("components").getInt("minecraft:custom_model_data"));
                }
                return null;
            }).toList();
            NbtList nbtLineList = (NbtList) nbtCompound.get("line");
            List<NbtCompound> nbtCompoundLineList = new ArrayList<>();
            if(nbtLineList != null) {
                for (int i = 0; i < nbtLineList.size(); i++) {
                    nbtCompoundLineList.add(nbtLineList.getCompound(i));
                }
            }
            List<Types.Line> lineList = nbtCompoundLineList.stream().map(nbtCompound1 -> {
                if(Objects.equals(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data").getString("type"), Defaults.ItemTypes.LINE)) {
                    return new Line(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data"), Defaults.ItemTypes.LINE, nbtCompound1.getCompound("components").getInt("minecraft:custom_model_data"));
                }
                return null;
            }).toList();
            this.line = lineList.size() == 1 ? lineList.getFirst() : null;
            NbtList nbtPoleList = (NbtList) nbtCompound.get("pole");
            List<NbtCompound> nbtCompoundPoleList = new ArrayList<>();
            if(nbtPoleList != null) {
                for (int i = 0; i < nbtPoleList.size(); i++) {
                    nbtCompoundPoleList.add(nbtPoleList.getCompound(i));
                }
            }
            List<Types.Pole> poleList = nbtCompoundPoleList.stream().map(nbtCompound1 -> {
                if(Objects.equals(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data").getString("type"), Defaults.ItemTypes.POLE)) {
                    return new Pole(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data"), Defaults.ItemTypes.POLE, nbtCompound1.getCompound("components").getInt("minecraft:custom_model_data"));
                }
                return null;
            }).toList();
            this.pole = poleList.size() == 1 ? poleList.getFirst() : null;
            NbtList nbtReelList = (NbtList) nbtCompound.get("reel");
            List<NbtCompound> nbtCompoundReelList = new ArrayList<>();
            if(nbtReelList != null) {
                for (int i = 0; i < nbtReelList.size(); i++) {
                    nbtCompoundReelList.add(nbtReelList.getCompound(i));
                }
            }
            List<Types.Reel> reelList = nbtCompoundReelList.stream().map(nbtCompound1 -> {
                if(Objects.equals(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data").getString("type"), Defaults.ItemTypes.REEL)) {
                    return new Reel(nbtCompound1.getCompound("components").getCompound("minecraft:custom_data"), Defaults.ItemTypes.REEL, nbtCompound1.getCompound("components").getInt("minecraft:custom_model_data"));
                }
                return null;
            }).toList();
            this.reel = reelList.size() == 1 ? reelList.getFirst() : null;
        }
    }

    public static class FOMCItem {
        public final String type;
        public final int customModelData;
        private FOMCItem(String type, int customModelData) {
            this.type = type;
            this.customModelData = customModelData;
        }
    }
}
