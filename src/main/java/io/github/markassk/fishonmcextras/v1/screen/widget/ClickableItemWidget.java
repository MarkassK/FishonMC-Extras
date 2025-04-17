package io.github.markassk.fishonmcextras.v1.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;

public class ClickableItemWidget extends ClickableWidget {
    private final ItemStack itemStack;
    private final TextRenderer textRenderer;

    public ClickableItemWidget(int x, int y, TextRenderer textRenderer, ItemStack itemStack) {
        super(x, y, 16, 16, null);
        this.itemStack = itemStack;
        this.textRenderer = textRenderer;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render Tooltip
        if(isHovered()) {
            context.fill(getX(), getY(), getX() + width, getY() + height, 0x36FFFFFF);
            context.drawItemTooltip(this.textRenderer, itemStack, mouseX, mouseY);
        }

        context.drawItem(itemStack, this.getX(), this.getY());
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
