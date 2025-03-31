package io.github.markassk.fishonmcextras.screen.widget;

import io.github.markassk.fishonmcextras.handler.PetMergeCalculatorHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ClickablePetItemWidget extends ClickableItemWidget {
    private final int index;
    private final int list;
    private final ItemStack pet;

    public ClickablePetItemWidget(int x, int y, TextRenderer textRenderer, ItemStack itemStack, int list, int index) {
        super(x, y, textRenderer, itemStack);
        this.index = index;
        this.list = list;
        this.pet = itemStack;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        if(PetMergeCalculatorHandler.instance().index[list] == this.index) {
            context.drawBorder(getX(), getY(), width, height, 0xFFFFD700);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        PetMergeCalculatorHandler.instance().setIndex(list, index);
        PetMergeCalculatorHandler.instance().setPet(this.pet, list);
    }
}
