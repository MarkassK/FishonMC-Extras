package io.github.markassk.fishonmcextras.screens.main;

import io.github.markassk.fishonmcextras.config.ConfigConstants;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.screens.debug.DebugScreen;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends Screen {
    private final MinecraftClient minecraftClient;
    private final Screen parent;

    public MainScreen(MinecraftClient minecraftClient, Screen parent) {
        super(Text.literal("FoE Main Screen"));
        this.minecraftClient = minecraftClient;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.renderWidgets();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int translationDev = ConfigConstants.DEV ? 14 : 0;

        context.drawText(textRenderer, Text.literal("Welcome to FishOnMC-Extras").formatted(Formatting.WHITE), width / 2 - textRenderer.getWidth(Text.literal("Welcome to FishOnMC-Extras")) / 2, height / 2 - translationDev - 16, 0xFFFFFF, true);
        context.drawText(textRenderer, Text.literal("Version 0.2.0-hotfix.3").formatted(Formatting.GRAY), width / 2 - textRenderer.getWidth(Text.literal("Version 0.2.0-hotfix.3")) / 2, height / 2 + translationDev + 26, 0xFFFFFF, true);
    }

    private void renderWidgets() {
        List<ButtonWidget> buttonWidgets = new ArrayList<>();
        int translationDev = ConfigConstants.DEV ? 14 : 0;

        buttonWidgets.add(ButtonWidget.builder(Text.literal("FoE Config"), button -> minecraftClient.setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, minecraftClient.currentScreen).get()))
                .dimensions(width / 2 - 100, height / 2 - translationDev, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Configure FoE")))
                .build());

        if(ConfigConstants.DEV) {
            buttonWidgets.add(ButtonWidget.builder(Text.literal("FoE Debug"), button -> minecraftClient.setScreen(new DebugScreen(minecraftClient, minecraftClient.currentScreen)))
                    .dimensions(width / 2 - 100, height / 2 + translationDev, 200, 20)
                    .tooltip(Tooltip.of(Text.literal("Open Debug Screen")))
                    .build());
        }

        buttonWidgets.forEach(this::addDrawableChild);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
