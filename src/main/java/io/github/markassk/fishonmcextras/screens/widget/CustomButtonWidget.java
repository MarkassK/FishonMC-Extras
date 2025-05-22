package io.github.markassk.fishonmcextras.screens.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomButtonWidget extends ClickableWidget {
    private final int padding = 4;
    private final int iconSize = 16;
    private final Text text;
    private final ItemStack itemIcon;
    private final String stringIcon;
    private final TextRenderer textRenderer;
    private final ClickCallback clickCallback;

    public static CustomButtonWidget.Builder builder(Text text, ClickCallback onClick) {
        return new CustomButtonWidget.Builder(text, onClick);
    }

    private CustomButtonWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, ItemStack itemIcon, String stringIcon, ClickCallback clickCallback) {
        super(x, y, 0, height, text);
        this.textRenderer = textRenderer;
        this.clickCallback = clickCallback;
        this.text = text;
        this.itemIcon = itemIcon;
        this.stringIcon = stringIcon;
        this.setWidth(width == -1 ? padding * 3 + iconSize + textRenderer.getWidth(text) : width);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int alphaInt = (int) ((60 / 100f) * 255f) << 24;
        // Box
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, this.isHovered() ? alphaInt | 0xFFFFFF : alphaInt);
        context.drawBorder(this.getX(), this.getY(), this.width, this.height, this.hovered ? 0xFFFFAA00 : 0xFFFFFFFF);

        // Button Text
        context.drawText(textRenderer, this.text,
                itemIcon == null && Objects.equals(stringIcon, "") ? this.getX() + padding : this.getX() + padding * 2 + iconSize,
                 this.getY() + this.height / 2 - textRenderer.fontHeight / 2, 0xFFFFFF, true);

        // Icon
        if(itemIcon != null || !Objects.equals(stringIcon, "")) {
            context.drawBorder(this.getX() + padding - 1, this.getY() + (padding * 2 + iconSize) / 2 - iconSize / 2 - 1, iconSize + 2, iconSize + 2, 0xFFFFFFFF);
            if(itemIcon != null) {
                context.drawItem(itemIcon, this.getX() + padding, this.getY() + (padding * 2 + iconSize) / 2 - iconSize / 2);
            } else if (!Objects.equals(stringIcon, "")) {
                context.drawText(textRenderer, this.stringIcon, this.getX() + iconSize / 2 + padding - textRenderer.getWidth(this.stringIcon) / 2, this.getY() + iconSize / 2 + padding - textRenderer.fontHeight / 2, 0xFFFFFF, true);
            }
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.clickCallback.onClick(this);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    public static class Builder {
        private final Text text;
        private final ClickCallback clickCallback;

        @Nullable
        private ItemStack itemIcon = null;
        @Nullable
        private Tooltip tooltip;

        private int x;
        private int y;
        private int width = -1;
        private int height = 24;
        private String stringIcon = "";

        public Builder(Text text, ClickCallback clickCallback) {
            this.text = text;
            this.clickCallback = clickCallback;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder itemIcon(@Nullable ItemStack itemIcon) {
            this.itemIcon = itemIcon;
            return this;
        }

        public Builder stringIcon(String stringIcon) {
            this.stringIcon = stringIcon;
            return this;
        }

        public CustomButtonWidget build() {
            CustomButtonWidget customButtonWidget = new CustomButtonWidget(MinecraftClient.getInstance().textRenderer, this.x, this.y, this.width, this.height, this.text, this.itemIcon, this.stringIcon, this.clickCallback);
            customButtonWidget.setTooltip(this.tooltip);
            return customButtonWidget;
        }
    }

    public interface ClickCallback {
        void onClick(CustomButtonWidget customButtonWidget);
    }
}
