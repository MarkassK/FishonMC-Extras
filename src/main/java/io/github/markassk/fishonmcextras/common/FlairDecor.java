package io.github.markassk.fishonmcextras.common;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.util.Identifier;

public class FlairDecor {
    public final Identifier GUI_FLAIR_TOP_LEFT;
    public final Identifier GUI_FLAIR_TOP_RIGHT;
    public final Identifier GUI_FLAIR_BOTTOM_LEFT;
    public final Identifier GUI_FLAIR_BOTTOM_RIGHT;

    public FlairDecor(FlairTopLeft flairTopLeft, FlairTopRight flairTopRight, FlairBottomLeft flairBottomLeft, FlairBottomRight flairBottomRight) {
        GUI_FLAIR_TOP_LEFT = Identifier.of(FishOnMCExtras.MOD_ID, "flair/top_left/" + flairTopLeft.ID);
        GUI_FLAIR_TOP_RIGHT = Identifier.of(FishOnMCExtras.MOD_ID, "flair/top_right/" + flairTopRight.ID);
        GUI_FLAIR_BOTTOM_LEFT = Identifier.of(FishOnMCExtras.MOD_ID, "flair/bottom_left/" + flairBottomLeft.ID);
        GUI_FLAIR_BOTTOM_RIGHT = Identifier.of(FishOnMCExtras.MOD_ID, "flair/bottom_right/" + flairBottomRight.ID);
    }

    public enum FlairTopLeft {
        Off("none"),
        Heart("heart"),
        Grass_Rat("grass_rat"),
        Grass_Duck("grass_duck"),
        Cat_Tree("cat_tree"),
        Cat("cat");

        public final String ID;
        FlairTopLeft(String id) {
            this.ID = id;
        }
    }

    public enum FlairTopRight {
        Off("none"),
        Heart("heart"),
        FoE_Sign("foe_sign"),
        Tape("tape"),
        Grass_Tree("grass_tree"),
        Sand_Crab("sand_crab"),
        Grass_Fox("grass_fox"),
        Cat_Tree("cat_tree");

        public final String ID;
        FlairTopRight(String id) {
            this.ID = id;
        }
    }

    public enum FlairBottomLeft {
        Off("none"),
        Heart("heart"),
        Crow_Idle("crow_idle"),
        Bat("bat"),
        Flower_Pot_1("flower_pot_1"),
        Cat_Tree("cat_tree");

        public final String ID;
        FlairBottomLeft(String id) {
            this.ID = id;
        }
    }

    public enum FlairBottomRight {
        Off("none"),
        Heart("heart"),
        Chicken_Idle("chicken_idle"),
        Flower_Pot_1("flower_pot_1"),
        Vines_1("vines_1"),
        Cat_Tree("cat_tree");

        public final String ID;
        FlairBottomRight(String id) {
            this.ID = id;
        }
    }
}
