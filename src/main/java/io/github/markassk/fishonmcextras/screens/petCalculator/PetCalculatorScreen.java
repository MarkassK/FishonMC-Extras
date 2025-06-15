package io.github.markassk.fishonmcextras.screens.petCalculator;

import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.handler.PetCalculatorHandler;
import io.github.markassk.fishonmcextras.handler.screens.petCalculator.PetCalculatorScreenHandler;
import io.github.markassk.fishonmcextras.screens.widget.ClickablePetWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PetCalculatorScreen extends Screen {
    private final PlayerEntity player;
    private final Screen parent;

    private final int lineHeight = 2;
    private final int padding = 4;
    private final int maxPetsPerColumn = 9;
    private final int boxWidth = 120;
    private final int boxHeight = (16 + lineHeight) * maxPetsPerColumn - lineHeight;

    List<ItemStack> petListStack;

    public PetCalculatorScreen(PlayerEntity player, Screen parent) {
        super(Text.literal("Pet Merge Calculator"));
        this.player = player;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        petListStack = new ArrayList<>();
        this.player.getInventory().main.forEach(stack -> {
            Pet pet = Pet.getPet(stack);
            if(pet != null) {
                this.petListStack.add(stack);
            }
        }) ;

        this.renderWidgets();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Assemble all text lines
        HashMap<String, List<Text>> textListHashMap = PetCalculatorScreenHandler.instance().assemblePetText();

        int columns = (int) Math.ceil((double) petListStack.size() / 9);


        renderBox(context, PetCalculatorHandler.PetList.LEFT, columns);
        renderBox(context, PetCalculatorHandler.PetList.RIGHT, columns);
        renderBox(context, PetCalculatorHandler.PetList.MIDDLE, columns);

        renderText(context, PetCalculatorHandler.PetList.LEFT, columns, textListHashMap.get("leftPet"));
        renderText(context, PetCalculatorHandler.PetList.RIGHT, columns, textListHashMap.get("rightPet"));
        renderText(context, PetCalculatorHandler.PetList.MIDDLE, columns, textListHashMap.get("calculatedPet"));
    }

    private void renderBox(DrawContext context, PetCalculatorHandler.PetList petList, int columns) {
        int translate = 0;

        final int translation = padding + (16 + padding) * columns + boxWidth;
        switch (petList) {
            case LEFT -> translate -= translation;
            case RIGHT -> translate += translation;
            default -> {
            }
        }

        context.fill(
                width / 2 - boxWidth / 2 + translate, height / 2 - boxHeight / 2, width / 2 + boxWidth / 2 + translate, height / 2 + boxHeight / 2, 0x48AAAAAA
        );
    }

    private void renderText(DrawContext context, PetCalculatorHandler.PetList petList, int columns, List<Text> textList) {
        int translate = 0;
        AtomicInteger count = new AtomicInteger(0);

        final int translation = padding + (16 + padding) * columns + boxWidth;
        switch (petList) {
            case LEFT -> translate -= translation;
            case RIGHT -> translate += translation;
            default -> {
            }
        }

        int finalTranslate = translate;
        textList.forEach(text -> context.drawText(textRenderer, text, width / 2 + finalTranslate - textRenderer.getWidth(text) / 2, height / 2 - boxHeight / 2 + padding + (count.getAndIncrement() * (textRenderer.fontHeight + lineHeight)), 0xFFFFFF, true));
    }

    private void renderWidgets() {
        List<ClickablePetWidget> clickablePetWidgets = new ArrayList<>();
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger column = new AtomicInteger(0);
        int listHeight = petListStack.size() > maxPetsPerColumn ? boxHeight : (16 + lineHeight) * petListStack.size() - lineHeight;
        petListStack.forEach(petStack -> {
            if(row.get() == 9) {
                row.set(0);
                column.getAndIncrement();
            }
            // LEFT
            clickablePetWidgets.add(new ClickablePetWidget(width / 2 - boxWidth / 2 - 16 - padding - (16 + padding) * column.get(), height / 2 - listHeight / 2 + (16 + lineHeight) * row.get(), textRenderer, petStack, PetCalculatorHandler.PetList.LEFT, row.get() + column.get() * 9));

            // RIGHT
            clickablePetWidgets.add(new ClickablePetWidget(width / 2 + boxWidth / 2 + padding + (16 + padding) * column.get(), height / 2 - listHeight / 2 + (16 + lineHeight) * row.get(), textRenderer, petStack, PetCalculatorHandler.PetList.RIGHT, row.get() + column.get() * 9));

            row.getAndIncrement();
        });

        clickablePetWidgets.forEach(this::addDrawableChild);
    }

    @Override
    public void close() {

        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
        PetCalculatorHandler.instance().reset();
    }
}
