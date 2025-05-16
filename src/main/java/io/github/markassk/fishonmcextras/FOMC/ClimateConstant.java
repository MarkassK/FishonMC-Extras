package io.github.markassk.fishonmcextras.FOMC;

import io.github.markassk.fishonmcextras.FOMC.Types.Defaults;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;

public enum ClimateConstant {
    // Climate
    SUBTROPICAL("subtropical", TextHelper.concat(
            Text.literal("S").withColor(0x4FB07A),
            Text.literal("u").withColor(0x4FB683),
            Text.literal("b").withColor(0x4EBC8D),
            Text.literal("t").withColor(0x4EC296),
            Text.literal("r").withColor(0x4DC8A0),
            Text.literal("o").withColor(0x4DCEA9),
            Text.literal("p").withColor(0x4CD6B2),
            Text.literal("i").withColor(0x4BDEBB),
            Text.literal("c").withColor(0x49E7C4),
            Text.literal("a").withColor(0x48EFCD),
            Text.literal("l").withColor(0x47F7D6)), Defaults.DEFAULT_COLOR),
    SUBARCTIC("subarctic", TextHelper.concat(
            Text.literal("S").withColor(0x53A1C1),
            Text.literal("u").withColor(0x64AAC8),
            Text.literal("b").withColor(0x75B3CF),
            Text.literal("a").withColor(0x86BBD5),
            Text.literal("r").withColor(0x97C4DC),
            Text.literal("c").withColor(0x97C1D8),
            Text.literal("t").withColor(0x98BED3),
            Text.literal("i").withColor(0x98BACF),
            Text.literal("c").withColor(0x98B7CA)), Defaults.DEFAULT_COLOR),
    SEMI_ARID("semi-arid", TextHelper.concat(
            Text.literal("S").withColor(0xE6902E),
            Text.literal("e").withColor(0xE59833),
            Text.literal("m").withColor(0xE5A038),
            Text.literal("i").withColor(0xE4A73C),
            Text.literal("-").withColor(0xE3AF41),
            Text.literal("A").withColor(0xE3B14C),
            Text.literal("r").withColor(0xE4B357),
            Text.literal("i").withColor(0xE4B562),
            Text.literal("d").withColor(0xE4B76D)), Defaults.DEFAULT_COLOR),
    SAVANNA("savanna", TextHelper.concat(
            Text.literal("S").withColor(0xBAC153),
            Text.literal("a").withColor(0xC8CB5A),
            Text.literal("v").withColor(0xD7D661),
            Text.literal("a").withColor(0xE5E068),
            Text.literal("n").withColor(0xE4DF6F),
            Text.literal("n").withColor(0xE3DE77),
            Text.literal("a").withColor(0xE2DD7E)), Defaults.DEFAULT_COLOR),
    CONTINENTAL("continental", TextHelper.concat(
            Text.literal("C").withColor(0xA4A9AB),
            Text.literal("o").withColor(0xABB2B2),
            Text.literal("n").withColor(0xB2BAB9),
            Text.literal("t").withColor(0xB8C3BF),
            Text.literal("i").withColor(0xBFCBC6),
            Text.literal("n").withColor(0xC6D4CD),
            Text.literal("e").withColor(0xCCD9D2),
            Text.literal("n").withColor(0xD2DDD8),
            Text.literal("t").withColor(0xD9E2DD),
            Text.literal("a").withColor(0xDFE6E3),
            Text.literal("l").withColor(0xE5EBE8)), Defaults.DEFAULT_COLOR),
    RAINFOREST("rainforest", TextHelper.concat(
            Text.literal("R").withColor(0x569579),
            Text.literal("a").withColor(0x4C9E7A),
            Text.literal("i").withColor(0x42A87B),
            Text.literal("n").withColor(0x39B17C),
            Text.literal("f").withColor(0x2FBA7D),
            Text.literal("o").withColor(0x2AC27F),
            Text.literal("r").withColor(0x2AC983),
            Text.literal("e").withColor(0x2AD086),
            Text.literal("s").withColor(0x2AD68A),
            Text.literal("t").withColor(0x2ADD8E)), Defaults.DEFAULT_COLOR),
    MEDITERRANEAN("mediterranean", TextHelper.concat(
            Text.literal("M").withColor(0x80C4EF),
            Text.literal("e").withColor(0x85C6EF),
            Text.literal("d").withColor(0x8AC8EF),
            Text.literal("i").withColor(0x8FCAEF),
            Text.literal("t").withColor(0x94CCEE),
            Text.literal("e").withColor(0x99CEEE),
            Text.literal("r").withColor(0x9ED0EE),
            Text.literal("r").withColor(0xA1D1EF),
            Text.literal("a").withColor(0xA4D3F0),
            Text.literal("n").withColor(0xA7D4F1),
            Text.literal("e").withColor(0xAAD5F1),
            Text.literal("a").withColor(0xADD7F2),
            Text.literal("n").withColor(0xB0D8F3)), Defaults.DEFAULT_COLOR),
    OCEANIC("oceanic", TextHelper.concat(
            Text.literal("O").withColor(0x397FAC),
            Text.literal("c").withColor(0x3A85B4),
            Text.literal("e").withColor(0x3C8CBD),
            Text.literal("a").withColor(0x3D92C5),
            Text.literal("n").withColor(0x3995CF),
            Text.literal("i").withColor(0x3599D9),
            Text.literal("c").withColor(0x319CE3)), Defaults.DEFAULT_COLOR),
    MONSOON("monsoon", TextHelper.concat(
            Text.literal("M").withColor(0x6141DF),
            Text.literal("o").withColor(0x654FE0),
            Text.literal("n").withColor(0x6A5CE0),
            Text.literal("s").withColor(0x6E6AE1),
            Text.literal("o").withColor(0x7278E1),
            Text.literal("o").withColor(0x7785E2),
            Text.literal("n").withColor(0x7B93E2)), Defaults.DEFAULT_COLOR),
    DEFAULT("default", Text.empty(), Defaults.DEFAULT_COLOR)
    ;

    public final String ID;
    public final Text TAG;
    public final int COLOR;

    ClimateConstant(String id, Text tag, int color) {
        this.ID = id;
        this.TAG = tag;
        this.COLOR = color;
    }

    public static ClimateConstant valueOfId(String id) {
        for (ClimateConstant c : values()) {
            if (c.ID.equals(id.toLowerCase())) {
                return c;
            }
        }
        return DEFAULT;
    }

    public static ClimateConstant valueOfTag(String tag) {
        for (ClimateConstant c : values()) {
            if (c.TAG.getString().equals(tag)) {
                return c;
            }
        }
        return DEFAULT;
    }

    public static int colorOfId(String id) {
        for (ClimateConstant c : values()) {
            if (c.ID.equals(id.toLowerCase())) {
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
