package io.github.markassk.fishonmcextras.screens.widget.container;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ContainerButtonWidget extends ClickableWidget {
    private final Identifier buttonTexture = Identifier.of(FishOnMCExtras.MOD_ID, "containers/button");
    private final Identifier buttonHoverTexture = Identifier.of(FishOnMCExtras.MOD_ID, "containers/button_hover");
    private final TextRenderer textRenderer;
    private final ClickCallback clickCallback;


    public ContainerButtonWidget(int x, int y, Text message, @Nullable Tooltip tooltip, ClickCallback clickCallback) {
        super(x, y, 22, 22, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.setTooltip(tooltip);
        this.clickCallback = clickCallback;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        try {
            context.getMatrices().translate(0, 0, 5);
            if(this.isHovered() || this.isFocused()) context.drawGuiTexture(RenderLayer::getGuiTextured, buttonHoverTexture, this.getX(), this.getY(), this.width, this.height);
            else context.drawGuiTexture(RenderLayer::getGuiTextured, buttonTexture, this.getX(), this.getY(), this.width, this.height);

            context.drawText(textRenderer, this.getMessage(), this.getX() + this.width / 2 - this.textRenderer.getWidth(this.getMessage())/ 2, this.getY() + this.height / 2 - this.textRenderer.fontHeight / 2, 0xFFFFFF, true);
        } finally {
            context.getMatrices().pop();
        }


    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.clickCallback.onClick(this);
        this.setMessage(Text.literal("...").formatted(Formatting.GRAY));
    }

    public interface ClickCallback {
        void onClick(ContainerButtonWidget iconButtonWidget);
    }
}
