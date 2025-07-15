package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import net.minecraft.client.MinecraftClient;

public class LocationHandler {
    private static LocationHandler INSTANCE = new LocationHandler();

    public static LocationHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationHandler();
        }
        return INSTANCE;
    }

    public Constant getLocation(MinecraftClient minecraftClient, String text, Constant currentLocation) {
        if(minecraftClient.player != null) {
            // Check Dimension
            String dimensionName = minecraftClient.player.getWorld().getRegistryKey().getValue().toString();
            if (!dimensionName.isEmpty()) {
                if (dimensionName.contains("crew")){
                    return Constant.CREW_ISLAND;
                }
            }

            // Check Side Location
            Constant sideLocation = findSideLocation((int) minecraftClient.player.getPos().x, (int) minecraftClient.player.getPos().z);
            if(sideLocation != Constant.UNKNOWN) {
                return sideLocation;
            }

            // Check Normal Locations
            sideLocation = getLocation(text);
            if(sideLocation != Constant.UNKNOWN) {
                return sideLocation;
            }
        }
        return currentLocation;
    }

    private Constant getLocation(String bossText) {
        if(bossText.contains(Constant.CYPRESS_LAKE.TAG.getString())) return Constant.CYPRESS_LAKE;
        else if (bossText.contains(Constant.KENAI_RIVER.TAG.getString())) return Constant.KENAI_RIVER;
        else if (bossText.contains(Constant.LAKE_BIWA.TAG.getString())) return Constant.LAKE_BIWA;
        else if (bossText.contains(Constant.MURRAY_RIVER.TAG.getString())) return Constant.MURRAY_RIVER;
        else if (bossText.contains(Constant.EVERGLADES.TAG.getString())) return Constant.EVERGLADES;
        else if (bossText.contains(Constant.KEY_WEST.TAG.getString())) return Constant.KEY_WEST;
        else if (bossText.contains(Constant.TOLEDO_BEND.TAG.getString())) return Constant.TOLEDO_BEND;
        else if (bossText.contains(Constant.GREAT_LAKES.TAG.getString())) return Constant.GREAT_LAKES;
        else if (bossText.contains(Constant.DANUBE_RIVER.TAG.getString())) return Constant.DANUBE_RIVER;
        else if (bossText.contains(Constant.AMAZON_RIVER.TAG.getString())) return Constant.AMAZON_RIVER;
        else if (bossText.contains(Constant.MEDITERRANEAN_SEA.TAG.getString())) return Constant.MEDITERRANEAN_SEA;
        else if (bossText.contains(Constant.CAPE_COD.TAG.getString())) return Constant.CAPE_COD;
        else if (bossText.contains(Constant.HAWAII.TAG.getString())) return Constant.HAWAII;
        else if (bossText.contains(Constant.CAIRNS.TAG.getString())) return Constant.CAIRNS;
        else return Constant.UNKNOWN;
    }

    private Constant findSideLocation(int pX, int pZ) {
        for (SideLocations location : SideLocations.values()){
            if ((pX >= location.x1 && pX <= location.x2) && (pZ >= location.z1 && pZ <= location.z2)) {
                return location.sidelocation;
            }
        }
        return Constant.UNKNOWN;
    }

    private enum SideLocations {
        SPAWNHUB(95, -58, 145, 38, Constant.SPAWNHUB);

        public final int x1;
        public final int z1;
        public final int x2;
        public final int z2;
        public final Constant sidelocation;

        SideLocations(int x1, int z1, int x2, int z2, Constant sidelocation) {
            this.x1 = x1;
            this.z1 = z1;
            this.x2 = x2;
            this.z2 = z2;
            this.sidelocation = sidelocation;
        }
    }
}
