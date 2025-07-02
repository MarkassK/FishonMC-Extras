package io.github.markassk.fishonmcextras.FOMC.Types;

import me.enderkill98.proxlib.ProxPacketIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Defaults {
    public static final String EMPTY_STRING = "";
    public static final int DEFAULT_COLOR = 0xFFFFFF;

    public static class ItemTypes {
        public static final String PET = "pet";
        public static final String FISH = "fish";
        public static final String SHARD = "armorShard";
        public static final String ARMOR = "armor";
        public static final String BAIT = "bait";
        public static final String LURE = "lure";
        public static final String LINE = "line";
        public static final String POLE = "pole";
        public static final String REEL = "reel";
        public static final String FISHINGROD = "fishing_rod";
        public static final String CRAFTINGCOMPONENT = "craftingComponent";
    }

    public static final Map<String, String> foeDevs = Map.of(
            "b5a9bbb7-42b4-4a6a-9ebe-bdf6697c8ee0", "DannyPX"
    );
}
