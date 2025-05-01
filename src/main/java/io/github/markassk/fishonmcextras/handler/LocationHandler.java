package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LocationBoundingBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class LocationHandler {
    private static LocationHandler INSTANCE = new LocationHandler();

    public Constant currentLocation = Constant.DEFAULT;

    public static LocationHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        assert minecraftClient.player != null;

        String dimensionName = minecraftClient.player.getWorld().getRegistryKey().getValue().toString();
        if (!dimensionName.isEmpty()) {
            if (dimensionName.contains("crew")){
                currentLocation = Constant.CREW_ISLAND;
                return;
            }
        }

        Vec3d position = minecraftClient.player.getPos();
        int playerX = (int) position.x;
        int playerZ = (int) position.z;

        String location = findClosestLocation(playerX, playerZ);

        currentLocation = Constant.valueOfId(location);
    }

    // pX, pY : Player
    private String findClosestLocation(int pX, int pZ) {
        for (LocationBoundingBox location : LocationBoundingBox.values()){
            if ((pX >= location.x1 && pX <= location.x2) && (pZ >= location.z1 && pZ <= location.z2)) {
                return location.name().toLowerCase();
            }
        }
        return null;
    }
}