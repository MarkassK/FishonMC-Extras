package io.github.markassk.fishonmcextras.FOMC;

public enum LocationInfo {
    SPAWNHUB("spawnhub", Constant.SUBTROPICAL, Constant.FRESHWATER),
    CYPRESS_LAKE("spawn", Constant.SUBTROPICAL, Constant.FRESHWATER),
    KENAI_RIVER("kenai", Constant.SUBARCTIC, Constant.FRESHWATER),
    LAKE_BIWA("biwa", Constant.SUBTROPICAL, Constant.FRESHWATER),
    MURRAY_RIVER("murray", Constant.SEMI_ARID, Constant.FRESHWATER),
    EVERGLADES("everglades", Constant.SAVANNA, Constant.FRESHWATER),
    KEY_WEST("keywest", Constant.SAVANNA, Constant.SALTWATER),
    TOLEDO_BEND("toledobend", Constant.SUBTROPICAL, Constant.FRESHWATER),
    GREAT_LAKES("greatlakes", Constant.CONTINENTAL, Constant.FRESHWATER),
    DANUBE_RIVER("danube", Constant.CONTINENTAL, Constant.FRESHWATER),
    AMAZON_RIVER("amazon", Constant.RAINFOREST, Constant.FRESHWATER),
    MEDITERRANEAN_SEA("mediterranean", Constant.MEDITERRANEAN, Constant.SALTWATER),
    CAPE_COD("capecod", Constant.OCEANIC, Constant.SALTWATER),
    HAWAII("hawaii", Constant.SAVANNA, Constant.SALTWATER),
    CAIRNS("cairns", Constant.MONSOON, Constant.SALTWATER),
    DEFAULT("", Constant.DEFAULT, Constant.DEFAULT)
    ;

    public final String ID;
    public final Constant CLIMATE;
    public final Constant WATER;

    LocationInfo(String id, Constant climate, Constant water) {
        this.ID = id;
        this.CLIMATE = climate;
        this.WATER = water;
    }

    public static LocationInfo valueOfId(String id) {
        for (LocationInfo c : values()) {
            if (c.ID.equals(id)) {
                return c;
            }
        }
        return DEFAULT;
    }
}
