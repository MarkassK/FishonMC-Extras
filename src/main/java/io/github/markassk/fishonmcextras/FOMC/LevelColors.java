package io.github.markassk.fishonmcextras.FOMC;

public enum LevelColors {
    LEVEL1(0, 4, 0x8A897E),
    LEVEL2(5, 9, 0x868A6E),
    LEVEL3(10, 14, 0x868A6E),
    LEVEL4(15, 19, 0x868A6E),
    LEVEL5(20, 34, 0xBBCF53),
    LEVEL6(35, 49, 0xE8D41E),
    LEVEL7(50, 64, 0xE8A81E),
    LEVEL8(65, 79, 0x7FD223),
    LEVEL9(80, 94, 0x23F133),
    LEVEL10(95, 109, 0x1EE8B2),
    LEVEL11(110, 124, 0x1EB6E8),
    LEVEL12(125, 139, 0x1958E9),
    LEVEL13(140, 154, 0x411DE8),
    LEVEL14(155, 169, 0x9B1EE8),
    LEVEL15(170, 184, 0xBE12D2),
    LEVEL16(185, 199, 0xE81ED4),
    LEVEL17(200, 209, 0xE81E61),
    LEVEL18(210, 219, 0xAC1648),
    LEVEL19(220, 229, 0x83232B),
    LEVEL20(230, 239, 0x71316B),
    LEVEL21(240, 249, 0xA00950),
    LEVEL22(250, 259, 0xD03EC2),
    LEVEL23(260, 269, 0xE675DB),
    LEVEL24(270, 279, 0xDE9DD8),
    LEVEL25(280, 289, 0xE4C1E1),
    LEVEL26(290, 299, 0xE1C9DF),
    LEVEL27(300, Integer.MAX_VALUE, 0xFFFEFA),
    DEFAULT(Integer.MAX_VALUE, Integer.MAX_VALUE, 0xFFFFFF);

    public final int startLvl;
    public final int endLvl;
    public final int color;

    LevelColors(int startLvl, int endLvl, int color) {
        this.startLvl = startLvl;
        this.endLvl = endLvl;
        this.color = color;
    }

    public static LevelColors valueOfLvl(int lvl) {
        for (LevelColors c : values()) {
            if (lvl >= c.startLvl && lvl <= c.endLvl) {
                return c;
            }
        }
        return DEFAULT;
    }
}
