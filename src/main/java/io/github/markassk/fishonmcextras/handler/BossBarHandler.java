package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;

import java.util.Map;
import java.util.UUID;

public class BossBarHandler {
    private static BossBarHandler INSTANCE = new BossBarHandler();

    public String time = "";
    public String weather = "";
    public String timeSuffix = "";
    public String temperature = "";

    public static BossBarHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BossBarHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) (minecraftClient.inGameHud.getBossBarHud())).getBossBars();

        if(!bossBars.isEmpty()) {
            bossBars.forEach(((uuid, clientBossBar) -> {
                if(clientBossBar.getName().getString().contains("\uF039") && LoadingHandler.instance().isLoadingDone) {
                    String bossText = clientBossBar.getName().getString();
                    if(bossText.contains(":")) {
                        time = bossText.substring(bossText.indexOf(":") - 2, bossText.indexOf(":") + 3).trim();
                        weather = bossText.substring(bossText.indexOf(":") - 4, bossText.indexOf(":") - 2).trim();
                        timeSuffix = bossText.substring(bossText.indexOf(":") + 3, bossText.indexOf(":") + 5);
                        temperature = bossText.contains("PM") ? bossText.substring(bossText.lastIndexOf("PM") + 3, bossText.lastIndexOf("°")) : bossText.substring(bossText.lastIndexOf("AM") + 3, bossText.lastIndexOf("°"));
                    }
                }
            }));
        }
    }

}
