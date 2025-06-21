package io.github.markassk.fishonmcextras.screens.widget.container;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ContainerCrewWidget extends ClickableWidget {
    private final Identifier panelTexture = Identifier.of(FishOnMCExtras.MOD_ID, "containers/panel_crew");

    public ContainerCrewWidget(int x, int y, Text message) {
        super(x, y, 105, 164, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        try {
            context.getMatrices().translate(0, 0, 0);
            context.drawGuiTexture(RenderLayer::getGuiTextured, panelTexture, this.getX(), this.getY(), this.width, this.height);
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
