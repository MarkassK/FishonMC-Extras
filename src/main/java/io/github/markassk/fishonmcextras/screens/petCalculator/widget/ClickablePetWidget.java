package io.github.markassk.fishonmcextras.screens.petCalculator.widget;

import io.github.markassk.fishonmcextras.handler.PetCalculatorHandler;
import io.github.markassk.fishonmcextras.screens.widget.ClickableItemWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ClickablePetWidget extends ClickableItemWidget {
    private final int index;
    private final PetCalculatorHandler.PetList list;
    private final ItemStack pet;

    public ClickablePetWidget(int x, int y, TextRenderer textRenderer, ItemStack itemStack, PetCalculatorHandler.PetList petList, int index) {
        super(x, y, textRenderer, itemStack);
        this.index = index;
        this.list = petList;
        this.pet = itemStack;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        if(PetCalculatorHandler.instance().selectedIndex[list.id] == this.index) {
            context.drawBorder(getX(), getY(), width, height, 0xFFFFD700);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        PetCalculatorHandler.instance().setIndex(this.list, this.index);
        PetCalculatorHandler.instance().setPet(this.pet, this.list);
    }
}
