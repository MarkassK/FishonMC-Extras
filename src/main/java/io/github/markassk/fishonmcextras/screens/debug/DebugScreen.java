package io.github.markassk.fishonmcextras.screens.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DebugScreen extends Screen {
    private final MinecraftClient minecraftClient;
    private final Screen parent;
    private final AtomicInteger count = new AtomicInteger(0);
    private DebugScreenHandler.HandlerType handler = DebugScreenHandler.HandlerType.EXAMPLE;

    public DebugScreen(MinecraftClient minecraftClient, Screen parent) {
        super(Text.literal("FoE Debug Screen"));
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

        List<Text> textList = DebugScreenHandler.instance().assembleDebugText(handler);

        context.getMatrices().push();
        try {
            // Get screen size
            int screenWidth = minecraftClient.getWindow().getScaledWidth();
            int screenHeight = minecraftClient.getWindow().getScaledHeight();

            // Calculate base positions relative to screen size
            int baseX = (int) (screenWidth * 0.5f);
            int baseY = (int) (screenHeight * 0.5f);

            // Scaling setup
            int fontSize = 8;
            float scale = fontSize / 10.0f;
            context.getMatrices().scale(scale, scale, 1f);

            int lineSpacing = 2;
            int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));

            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);
            AtomicInteger countLine = new AtomicInteger(0);

            int maxLines = 30;
            int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
            int columns = (int) Math.ceil((double) textList.size() / maxLines);
            int heightTextList = columns > 1 ? maxLines * lineHeight : textList.size() * lineHeight;
            int columnStart = (int) (Math.ceil(columns / 2f) - columns);
            AtomicInteger columnCount = new AtomicInteger(columnStart);


            if((columns & 1) == 0) {
                // even
                textList.forEach(text -> {
                    if(countLine.get() == maxLines) {
                        countLine.set(0);
                        columnCount.getAndIncrement();
                    }
                    context.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2 + columnCount.get() * maxLength + maxLength / 2, scaledY - heightTextList / 2 + (countLine.getAndIncrement() * lineHeight), 0xFFFFFF, true);
                });
            } else {
                // odd
                textList.forEach(text -> {
                    if(countLine.get() == maxLines) {
                        countLine.set(0);
                        columnCount.getAndIncrement();
                    }
                    context.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2 + columnCount.get() * maxLength, scaledY - heightTextList / 2 + (countLine.getAndIncrement() * lineHeight), 0xFFFFFF, true);
                });
            }

            int heightHandlerList = DebugScreenHandler.HandlerType.values().length * lineHeight;
            AtomicInteger countHandler = new AtomicInteger(0);
            Arrays.stream(DebugScreenHandler.HandlerType.values()).toList().forEach(handlerType -> context.drawText(
                    textRenderer,
                    handler == handlerType ? Text.literal(handlerType.name).formatted(Formatting.BOLD, Formatting.YELLOW) : Text.literal(handlerType.name),
                    0,
                    scaledY - heightHandlerList / 2 + (countHandler.getAndIncrement() * lineHeight),
                    0xFFFFFF, true
            ));

        } finally {
            context.getMatrices().pop();
        }
    }

    private void renderWidgets() {
        List<ButtonWidget> buttonWidgets = new ArrayList<>();

        buttonWidgets.add(ButtonWidget.builder(Text.literal("Next Handler"), button -> {
                    if(count.incrementAndGet() == DebugScreenHandler.HandlerType.values().length) {
                        count.set(0);
                    }
                    handler = DebugScreenHandler.HandlerType.valueOfId(count.get());
                })
                .dimensions(width / 2 - 100, 40, 200, 20)
                .build());

        buttonWidgets.forEach(this::addDrawableChild);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
