package io.github.markassk.fishonmcextras.screens.widget.timer;

import io.github.markassk.fishonmcextras.screens.widget.TextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class TimerWidget extends ClickableWidget {
    protected final TextRenderer textRenderer;
    private final ClickCallback clickCallback;

    public TimerWidget(int x, int y) {
        super(x, y, 0, 0, null);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.setHeight(textRenderer.fontHeight);
        this.clickCallback = null;
    }

    public TimerWidget(int x, int y, ClickCallback clickCallback) {
        super(x, y, 0, 0, null);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.setHeight(textRenderer.fontHeight);
        this.clickCallback = clickCallback;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if(clickCallback != null) {
            this.clickCallback.onClick(this);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(clickCallback != null) {
            this.clickCallback.onClick(this);
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    public interface ClickCallback {
        void onClick(TimerWidget iconButtonWidget);
    }
}
