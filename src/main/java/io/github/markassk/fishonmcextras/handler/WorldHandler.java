package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;

public class WorldHandler {
    private static WorldHandler INSTANCE = new WorldHandler();

    public static WorldHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new WorldHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.world != null) {
            this.beforeIterator(minecraftClient);

            minecraftClient.world.getEntities().forEach(entity -> {
                CrewHandler.instance().tickEntities(entity, minecraftClient);
                FishCatchHandler.instance().tickEntities(entity, minecraftClient);
                HiderHandler.instance().tickEntities(entity, minecraftClient);
                PetEquipHandler.instance().tickEntities(entity, minecraftClient);
                FishingRodHandler.instance().tickEntities(entity, minecraftClient);
                OtherPlayerHandler.instance().tickEntities(entity, minecraftClient);
            });

            this.afterIterator(minecraftClient);
        }
    }

    public void beforeIterator(MinecraftClient minecraftClient) {
        FishingRodHandler.instance().beforeTickEntitiess();
        CrewHandler.instance().beforeTickEntitiess();
    }

    public void afterIterator(MinecraftClient minecraftClient) {
        FishingRodHandler.instance().afterTickEntities(minecraftClient);
        CrewHandler.instance().afterTickEntities();
    }
}
