package io.github.markassk.fishonmcextras.screens.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchBarKeyWordWidget extends TextFieldWidget {
    private boolean specialFocus = false;
    private final List<Text> hoverInfo;
    private TextRenderer textRenderer;

    public SearchBarKeyWordWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, List<Text> hoverInfo) {
        super(textRenderer, x, y, width, height, null, text);
        this.hoverInfo = hoverInfo;
        this.textRenderer = textRenderer;
    }

    @Override
    public void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.renderWidget(drawContext, mouseX, mouseY, delta);
        // Hover Info
        if(this.isHovered() && this.isFocused()) {
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 320);

                int padding = 4;
                int lineHeight = textRenderer.fontHeight + 1;
                int length = hoverInfo.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
                int lines = hoverInfo.size() * lineHeight;

                drawContext.fill(this.getX() + this.width / 2 - length / 2 - padding, this.getBottom(), this.getX() + this.width / 2 + length / 2 + padding, this.getBottom() + padding * 2 + lines,0xFF000000);
                drawContext.drawBorder(this.getX() + this.width / 2 - length / 2 - padding, this.getBottom(), padding * 2 + length, padding * 2 + lines, 0xFFFFAA00);

                AtomicInteger count = new AtomicInteger(0);
                hoverInfo.forEach(text -> {
                    drawContext.drawText(textRenderer, text, this.getX() + this.width / 2 - length / 2, this.getBottom() + padding + count.getAndIncrement() * lineHeight, 0xFFFFFF, true);
                });
            } finally {
                drawContext.getMatrices().pop();
            }
        }

        // Special Focus
        if(this.isSpecialFocus()) {
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 300);
                int PADDING = 2;
                if(this.isFocused()) {
                    drawContext.drawBorder(this.getX(), this.getY(), this.width, this.height, 0xFFFFAA00);
                }
                drawContext.drawBorder(this.getX() + PADDING, this.getY() + PADDING, this.width - PADDING * 2, this.height - PADDING * 2, 0xFFFFAA00);
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }

    public boolean isSpecialFocus() {
        return specialFocus;
    }

    public void setSpecialFocus(boolean specialFocus) {
        this.specialFocus = specialFocus;
    }
}
