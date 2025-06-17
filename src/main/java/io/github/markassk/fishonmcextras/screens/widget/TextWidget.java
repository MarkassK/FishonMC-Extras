package io.github.markassk.fishonmcextras.screens.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class TextWidget extends ClickableWidget {
    private final TextRenderer textRenderer;
    private final int color;
    private final boolean shadow;

    public TextWidget(int x, int y, Text message, int color, boolean shadow) {
        super(x, y, 0, 0, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.color = color;
        this.shadow = shadow;
        this.setWidth(textRenderer.getWidth(message));
        this.setHeight(textRenderer.fontHeight);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawText(this.textRenderer, this.getMessage(), this.getX(), this.getY(), this.color, this.shadow);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}
