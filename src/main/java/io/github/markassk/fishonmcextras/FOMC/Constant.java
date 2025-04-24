package io.github.markassk.fishonmcextras.FOMC;


import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.markassk.fishonmcextras.FOMC.Defaults.EMPTY_STRING;

public enum Constant {
    // Fish Size
    BABY("baby", Text.literal("ʙᴀʙʏ").withColor(0x468CE7), 0x468CE7),
    JUVENILE("juvenile", Text.literal("ᴊᴜᴠᴇɴɪʟᴇ").withColor(0x22EA08), 0x22EA08),
    ADULT("adult", Text.literal("ᴀᴅᴜʟᴛ").withColor(0x1C7DA0), 0x1C7DA0),
    LARGE("large", Text.literal("ʟᴀʀɢᴇ").withColor(0xFF9000), 0xFF9000),
    GIGANTIC("gigantic", Text.literal("ɢɪɢᴀɴᴛɪᴄ").withColor(0xAF3333), 0xAF3333),

    // Rarity
    COMMON("common", Text.literal("\uF033").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    RARE("rare",
            Text.literal("\uF034").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    EPIC("epic", Text.literal("\uF035").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    LEGENDARY("legendary", Text.literal("\uF036").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    MYTHICAL("mythical", Text.literal("\uF037").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    SPECIAL("special", Text.literal("\uF092").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),

    // Location
    CYPRESS_LAKE("spawn", Text.literal(""), Defaults.DEFAULT_COLOR),
    KENAI_RIVER("kenai", Text.literal(""), Defaults.DEFAULT_COLOR),
    LAKE_BIWA("biwa", Text.literal(""), Defaults.DEFAULT_COLOR),
    MURRAY_RIVER("murray", Text.literal(""), Defaults.DEFAULT_COLOR),
    EVERGLADES("everglades", Text.literal(""), Defaults.DEFAULT_COLOR),
    KEY_WEST("keywest", Text.literal(""), Defaults.DEFAULT_COLOR),
    TOLEDO_BEND("toledobend", Text.literal(""), Defaults.DEFAULT_COLOR),
    GREAT_LAKES("greatlakes", Text.literal(""), Defaults.DEFAULT_COLOR),
    DANUBE_RIVER("danube", Text.literal(""), Defaults.DEFAULT_COLOR),
    AMAZON_RIVER("amazon", Text.literal(""), Defaults.DEFAULT_COLOR),
    MEDITERRANEAN_SEA("mediterranean", Text.literal(""), Defaults.DEFAULT_COLOR),
    CAPE_COD("capecod", Text.literal(""), Defaults.DEFAULT_COLOR),
    HAWAII("hawaii", Text.literal(""), Defaults.DEFAULT_COLOR),
    CAIRNS("cairns", Text.literal(""), Defaults.DEFAULT_COLOR),

    // Pet Base
    LOCATION_BASE("lbase", Text.empty(), Defaults.DEFAULT_COLOR),
    CLIMATE_BASE("cbase", Text.empty(), Defaults.DEFAULT_COLOR),

    // Climate
    SUBTROPICAL("", Text.literal(""), Defaults.DEFAULT_COLOR),
    SUBARCTIC("", Text.literal(""), Defaults.DEFAULT_COLOR),
    SEMI_ARID("", Text.literal(""), Defaults.DEFAULT_COLOR),
    SAVANNA("", Text.literal(""), Defaults.DEFAULT_COLOR),
    CONTINENTAL("", Text.literal(""), Defaults.DEFAULT_COLOR),
    RAINFOREST("", Text.literal(""), Defaults.DEFAULT_COLOR),
    MEDITERRANEAN("", Text.literal(""), Defaults.DEFAULT_COLOR),
    OCEANIC("", Text.literal(""), Defaults.DEFAULT_COLOR),
    MONSOON("", Text.literal(""), Defaults.DEFAULT_COLOR),

    // Variants
    NORMAL("normal", Text.empty(), Defaults.DEFAULT_COLOR),
    ALBINO("albino", Text.literal("\uF041").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    MELANISTIC("melanistic", Text.literal("\uF042").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    TROPHY("trophy", Text.literal("\uF043").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
    FABLED("fabled", Text.literal("\uF044").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),

    // Pet Rating
    SICKLY(EMPTY_STRING, Text.literal("ѕɪᴄᴋʟʏ").withColor(0xFF74403B), 0xFF74403B),
    BAD(EMPTY_STRING, Text.literal("ʙᴀᴅ").withColor(0xFFFF5555), 0xFFFF5555),
    BELOW_AVERAGE(EMPTY_STRING, Text.literal("ʙᴇʟᴏᴡ ᴀᴠᴇʀᴀɢᴇ").withColor(0xFFFCFC54), 0xFFFCFC54),
    AVERAGE(EMPTY_STRING, Text.literal("ᴀᴠᴇʀᴀɢᴇ").withColor(0xFFFCA800), 0xFFFCA800),
    GOOD(EMPTY_STRING, Text.literal("ɢᴏᴏᴅ").withColor(0xFF54FC54), 0xFF54FC54),
    GREAT(EMPTY_STRING, Text.literal("ɢʀᴇᴀᴛ").withColor(0xFF00A800), 0xFF00A800),
    EXCELLENT(EMPTY_STRING, Text.literal("ᴇxᴄᴇʟʟᴇɴᴛ").withColor(0xFF54FCFC), 0xFF54FCFC),
    AMAZING(EMPTY_STRING, Text.literal("ᴀᴍᴀᴢɪɴɢ").withColor(0xFFFC54FC), 0xFFFC54FC),
    PERFECT(EMPTY_STRING, Text.literal("ᴘᴇʀꜰᴇᴄᴛ").withColor(0xFFA800A8), 0xFFA800A8),

    // Pets
    BULLFROG("bullfrog", Text.literal(""), Defaults.DEFAULT_COLOR),
    BEAR("bear", Text.literal(""), Defaults.DEFAULT_COLOR),
    FOX("fox", Text.literal(""), Defaults.DEFAULT_COLOR),
    KANGAROO("kangaroo", Text.literal(""), Defaults.DEFAULT_COLOR),
    MARSH_RABBIT("marshrabbit", Text.literal(""), Defaults.DEFAULT_COLOR),
    SEA_TURTLE("seaturtle", Text.literal(""), Defaults.DEFAULT_COLOR),
    DUCK("duck", Text.literal(""), Defaults.DEFAULT_COLOR),
    EAGLE("eagle", Text.literal(""), Defaults.DEFAULT_COLOR),
    WOLF("wolf", Text.literal(""), Defaults.DEFAULT_COLOR),
    CAPYBARA("capybara", Text.literal(""), Defaults.DEFAULT_COLOR),
    LYNX("lynx", Text.literal(""), Defaults.DEFAULT_COLOR),
    SHARK("shark", Text.literal(""), Defaults.DEFAULT_COLOR),
    DOLPHIN("dolphin", Text.literal(""), Defaults.DEFAULT_COLOR),
    KOALA("koala", Text.literal(""), Defaults.DEFAULT_COLOR),

    DEFAULT(EMPTY_STRING, Text.empty(), Defaults.DEFAULT_COLOR);

    public final String ID;
    public final Text TAG;
    public final int COLOR;

    private Constant(String id, Text tag, int color) {
        this.ID = id;
        this.TAG = tag;
        this.COLOR = color;
    }

    public static Constant valueOfId(String id) {
        for (Constant c : values()) {
            if (c.ID.equals(id)) {
                return c;
            }
        }
        return null;
    }

    public static int colorOfId(String id) {
        for (Constant c : values()) {
            if (c.ID.equals(id)) {
                return c.COLOR;
            }
        }
        return Defaults.DEFAULT_COLOR;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
