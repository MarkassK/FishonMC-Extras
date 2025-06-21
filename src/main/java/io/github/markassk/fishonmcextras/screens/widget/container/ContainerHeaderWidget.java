package io.github.markassk.fishonmcextras.screens.widget.container;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ContainerHeaderWidget extends ClickableWidget {
    private final Identifier headerTexture = Identifier.of(FishOnMCExtras.MOD_ID, "containers/panel_header");

    public ContainerHeaderWidget(int x, int y, Text message) {
        super(x, y, 174, 28, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        try {
            context.getMatrices().translate(0, 0, 0);
            context.drawGuiTexture(RenderLayer::getGuiTextured, headerTexture, this.getX(), this.getY(), this.width, this.height);
        } finally {
            context.getMatrices().pop();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}
