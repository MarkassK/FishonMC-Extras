package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TitleHandler {
    private static TitleHandler INSTANCE = new TitleHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public long showedAt = 0L;
    public List<Text> title = new ArrayList<>();
    public long time = 5000L;
    public List<Text> subtitle = new ArrayList<>();

    public static TitleHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new TitleHandler();
        }
        return INSTANCE;
    }

    public void setTitleHud(List<Text> title, long time, MinecraftClient minecraftClient) {
        if (config.titlePopup.useNewTitleSystem) {
            minecraftClient.inGameHud.setTitle(Text.empty());
            minecraftClient.inGameHud.setSubtitle(Text.empty());
        }
        this.showedAt = System.currentTimeMillis();
        this.title = title;
        this.time = time;
        this.subtitle = new ArrayList<>();
    }

    public void setTitleHud(List<Text> title, long time, MinecraftClient minecraftClient, List<Text> subtitle) {
        if (config.titlePopup.useNewTitleSystem) {
            minecraftClient.inGameHud.setTitle(Text.empty());
            minecraftClient.inGameHud.setSubtitle(Text.empty());
        }
        this.showedAt = System.currentTimeMillis();
        Collections.reverse(title);
        this.title = title;
        this.time = time;
        this.subtitle = subtitle;
    }
}
