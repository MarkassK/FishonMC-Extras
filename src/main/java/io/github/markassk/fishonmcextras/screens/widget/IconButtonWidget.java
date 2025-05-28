package io.github.markassk.fishonmcextras.screens.widget;

import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IconButtonWidget extends ClickableWidget {
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private final int padding = 4;
    private final int iconSize = 16;
    private Text text;
    private final ItemStack itemIcon;
    private final String stringIcon;
    private final TextRenderer textRenderer;
    private final boolean isLoader;
    private final ClickCallback clickCallback;

    public static IconButtonWidget.Builder builder(Text text, ClickCallback onClick) {
        return new IconButtonWidget.Builder(text, onClick);
    }

    private IconButtonWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, ItemStack itemIcon, String stringIcon, boolean isLoader, ClickCallback clickCallback) {
        super(x, y, 0, height, text);
        this.textRenderer = textRenderer;
        this.clickCallback = clickCallback;
        this.text = text;
        this.itemIcon = itemIcon;
        this.stringIcon = stringIcon;
        this.setWidth(width == -1 ? padding * 3 + iconSize + textRenderer.getWidth(text) : width);
        this.isLoader = isLoader;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int alphaInt = (int) ((60 / 100f) * 255f) << 24;
        // Box
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, this.isHovered() ? alphaInt | 0xFFFFFF : alphaInt);
        if(ThemingHandler.instance().currentThemeType == Theming.ThemeType.OFF) {
            context.drawBorder(this.getX(), this.getY(), this.width, this.height, this.hovered ? 0xFFFFAA00 : 0xFFFFFFFF);
        }

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

        if(ThemingHandler.instance().currentThemeType != Theming.ThemeType.OFF) {
            Theming theme = ThemingHandler.instance().currentTheme;
            int colorOverlay = config.theme.colorOverlay;
            int alphaOverlay = (int) 255f << 24;

            // Corners
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_LEFT, this.getX() - 8, this.getY() - 8, 16, 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_RIGHT, this.getX() + this.width - 8, this.getY() - 8, 16, 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_LEFT, this.getX() - 8, this.getY() + this.height - 8, 16, 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_RIGHT, this.getX() + this.width - 8, this.getY() + this.height - 8, 16, 16, alphaOverlay | colorOverlay);

            // Sides
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_LEFT, this.getX() - 8, this.getY() + 8, 16, this.height - 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_RIGHT, this.getX() + this.width - 8, this.getY() + 8, 16, this.height - 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP, this.getX() + 8, this.getY() - 8, this.width - 16, 16, alphaOverlay | colorOverlay);
            context.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM, this.getX() + 8, this.getY() + this.height - 8, this.width - 16, 16, alphaOverlay | colorOverlay);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.clickCallback.onClick(this);
        if(isLoader) this.text = Text.literal("Loading...").formatted(Formatting.GRAY);
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
        private boolean isLoader = false;

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

        public Builder isLoader(boolean isLoader) {
            this.isLoader = isLoader;
            return this;
        }

        public IconButtonWidget build() {
            IconButtonWidget iconButtonWidget = new IconButtonWidget(MinecraftClient.getInstance().textRenderer, this.x, this.y, this.width, this.height, this.text, this.itemIcon, this.stringIcon, this.isLoader ,this.clickCallback);
            iconButtonWidget.setTooltip(this.tooltip);
            return iconButtonWidget;
        }
    }

    public interface ClickCallback {
        void onClick(IconButtonWidget iconButtonWidget);
    }
}
