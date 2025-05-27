package io.github.markassk.fishonmcextras.screens.main;

import io.github.markassk.fishonmcextras.config.ConfigConstants;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.TabHandler;
import io.github.markassk.fishonmcextras.screens.debug.DebugScreen;
import io.github.markassk.fishonmcextras.screens.movehud.MoveHudScreen;
import io.github.markassk.fishonmcextras.screens.widget.IconButtonWidget;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        List<Text> textList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);

        context.drawVerticalLine(width / 2, height / 2 - 4 - 24, height / 2 + 4 + 24, 0xFFFFFFFF);
        textList.add(Text.literal("Welcome to FishOnMC-Extras").formatted(Formatting.WHITE));
        textList.add(TabHandler.instance().player);
        textList.add(Text.empty());
        textList.add(Text.literal("Version 0.2.2-beta.1").formatted(Formatting.GRAY));

        textList.forEach(text -> {
            context.drawText(textRenderer, text, width / 2 - 4 - textRenderer.getWidth(text), height / 2 - (textList.size() * (textRenderer.fontHeight + 1)) / 2 + count.getAndIncrement() * (textRenderer.fontHeight + 1), 0xFFFFFF, true);
        });
    }

    private void renderWidgets() {
        List<ClickableWidget> widgets = new ArrayList<>();

        widgets.add(IconButtonWidget.builder(Text.literal("FoE Config"), button -> minecraftClient.setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, minecraftClient.currentScreen).get()))
                .position(width / 2 + 4, height / 2 - 24 - 4)
                .itemIcon(Items.COMMAND_BLOCK.getDefaultStack())
                .width(130)
                .build());

        widgets.add(IconButtonWidget.builder(Text.literal("Move HUD Elements"), button -> minecraftClient.setScreen(new MoveHudScreen(minecraftClient, minecraftClient.currentScreen)))
                .position(width / 2 + 4, height / 2 + 4)
                .itemIcon(Items.STRUCTURE_VOID.getDefaultStack())
                .width(130)
                .build());

        if(ConfigConstants.DEV) {
            widgets.add(net.minecraft.client.gui.widget.ButtonWidget.builder(Text.literal("FoE Debug"), button -> minecraftClient.setScreen(new DebugScreen(minecraftClient, minecraftClient.currentScreen)))
                    .dimensions(width / 2 - 100, height - 20 - 8, 200, 20)
                    .tooltip(Tooltip.of(Text.literal("Open Debug Screen")))
                    .build());
        }

        widgets.forEach(this::addDrawableChild);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
