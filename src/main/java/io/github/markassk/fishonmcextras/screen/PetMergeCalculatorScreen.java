package io.github.markassk.fishonmcextras.screen;

import io.github.markassk.fishonmcextras.FishOnMCExtrasClient;
import io.github.markassk.fishonmcextras.model_types.PetStats;
import io.github.markassk.fishonmcextras.handler.PetMergeCalculatorHandler;
import io.github.markassk.fishonmcextras.screen.widget.ClickablePetItemWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class PetMergeCalculatorScreen extends Screen {
    List<ItemStack> petList;
    PlayerEntity player;
    Screen parent;
    int x = 50;
    int y = 0;
    int padding = 10;
    FishOnMCExtrasConfig config = FishOnMCExtrasClient.CONFIG;

    public PetMergeCalculatorScreen(PlayerEntity player, Screen parent) {
        super(Text.literal("Pet Merge Calculator"));
        this.player = player;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.petList = new ArrayList<>();

        this.player.getInventory().main.forEach(stack -> {
            if (stack.getItem() == Items.PLAYER_HEAD && stack.getName().getString().contains(" Pet") && stack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                this.petList.add(stack);
            }
        });

        renderWidgets();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int columns = (int) Math.ceil((double) petList.size() / 9) - 1;
        AtomicInteger count = new AtomicInteger();

        // Pet One
        context.fill(
                ((width / 2) - x + padding) - x - (columns * 18) - 16 * 3 - 2,
                (height / 2) - (9 * 18 / 2),
                ((width / 2) + x - padding) - x - (columns * 18) - 16 * 3 - 2,
                (height / 2) + (9 * 18 / 2),
                0x48AAAAAA);
        Text petOneText = Text.literal("Pet one").formatted(Formatting.BOLD, Formatting.GOLD);
        context.drawText(
                textRenderer,
                petOneText,
                width / 2 - x - textRenderer.getWidth(petOneText) / 2 - 50 - (columns * 18),
                height / 2 - (9 * 18 / 2) + 6,
                0xFFFFFFFF,
                true
        );
        if (PetMergeCalculatorHandler.instance().petOne != null) {
            PetStats petOne = PetMergeCalculatorHandler.instance().petOne;
            renderPetText(context, petOne, columns, 0);
        }

        count.set(0);

        // Pet Two
        context.fill(
                ((width / 2) - x + padding) + x + (columns * 18) + 16 * 3 + 2,
                (height / 2) - (9 * 18 / 2),
                ((width / 2) + x - padding) + x + (columns * 18) + 16 * 3 + 2,
                (height / 2) + (9 * 18 / 2),
                0x48AAAAAA);
        Text petTwoText = Text.literal("Pet two").formatted(Formatting.BOLD, Formatting.GOLD);
        context.drawText(
                textRenderer,
                petTwoText,
                width / 2 + x - textRenderer.getWidth(petTwoText) / 2 + 50 + (columns * 18),
                height / 2 - (9 * 18 / 2) + 6,
                0xFFFFFFFF,
                true
        );
        if (PetMergeCalculatorHandler.instance().petTwo != null) {
            PetStats petTwo = PetMergeCalculatorHandler.instance().petTwo;
            renderPetText(context, petTwo, columns, 2);
        }

        count.set(0);

        // Result
        context.fill(
                (width / 2) - x + padding,
                (height / 2) - (9 * 18 / 2),
                (width / 2) + x - padding,
                (height / 2) + (9 * 18 / 2),
                0x48AAAAAA);
        Text petResultText = Text.literal("Result").formatted(Formatting.BOLD, Formatting.GOLD);
        context.drawText(
                textRenderer,
                petResultText,
                width / 2 - textRenderer.getWidth(petResultText) / 2,
                height / 2 - (9 * 18 / 2) + 6,
                0xFFFFFFFF,
                true
        );
        if(PetMergeCalculatorHandler.instance().petOne == null || PetMergeCalculatorHandler.instance().petTwo == null){
        } else {
            if (PetMergeCalculatorHandler.instance().petTwo != null && PetMergeCalculatorHandler.instance().petOne != null && !Objects.equals(PetMergeCalculatorHandler.instance().petOne.getRarity(), PetMergeCalculatorHandler.instance().petTwo.getRarity())) {
                Text warning = Text.literal("Cannot merge pets of different rarity!").formatted(Formatting.DARK_RED);
                context.drawText(textRenderer, warning, width / 2 - textRenderer.getWidth(warning) / 2, height / 2 + (9 * 18) / 2 + padding, 0xFFFFFFFF, true);
            } else if(!Objects.equals(PetMergeCalculatorHandler.instance().petOne.getName(), PetMergeCalculatorHandler.instance().petTwo.getName())) {
                Text warning = Text.literal("Cannot merge pets of different pet types!").formatted(Formatting.DARK_RED);
                context.drawText(textRenderer, warning, width / 2 - textRenderer.getWidth(warning) / 2, height / 2 + (9 * 18) / 2 + padding, 0xFFFFFFFF, true);
            } else if (PetMergeCalculatorHandler.instance().calculatedPet != null) {
                PetStats calculatedPet = PetMergeCalculatorHandler.instance().calculatedPet;
                renderPetText(context, calculatedPet, columns, 1);
            }
        }
        if(!config.petTooltipToggles.showAccuratePercentage) {
            Text accurateWarning = Text.literal("Accurate Percentages setting is forced to be enabled when merging")
                    .formatted(Formatting.RED, Formatting.ITALIC);
            context.drawText(
                    textRenderer,
                    accurateWarning,
                    width / 2 - (textRenderer.getWidth(accurateWarning) / 2),
                    height / 2 - (9 * 18 / 2) - 26,
                    0xFFFFFFFF,
                    true
            );
        }

        Text titleText = Text.literal("Pet Merge Calculator")
                .formatted(Formatting.WHITE, Formatting.BOLD);
        context.drawText(
                textRenderer,
                titleText,
                width / 2 - (textRenderer.getWidth(titleText) / 2),
                height / 2 - (9 * 18 / 2) - 14,
                0xFFFFFFFF,
                true);
    }

    // WIP
    private void renderPetText(DrawContext context, PetStats petStat, int petColumns, int column) {
        List<Text> petTextList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        Text luckText = Text.literal("ʟᴜᴄᴋ: ").withColor(0xFF7ED7C1);
        Text scaleText = Text.literal("ѕᴄᴀʟᴇ: ").withColor(0xFF4B86EE);

        petTextList.add(Text.literal(petStat.getName()));
        petTextList.add(Text.literal(PetMergeCalculatorHandler.rarityString(petStat.getRarity())));
        petTextList.add(Text.empty());
        petTextList.add(Text.literal("Climate").formatted(Formatting.BOLD));
        petTextList.add(TextHelper.concat(
                luckText,
                Text.literal(TextHelper.fmt(petStat.getcLuck())),
                Text.literal(" " + TextHelper.fmt(petStat.getcLuckPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%").formatted(Formatting.GRAY)
        ));
        petTextList.add(TextHelper.concat(
                scaleText,
                Text.literal(TextHelper.fmt(petStat.getcScale())),
                Text.literal(" " + TextHelper.fmt(petStat.getcScalePercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%").formatted(Formatting.GRAY)
        ));
        petTextList.add(Text.empty());
        petTextList.add(Text.literal("Location").formatted(Formatting.BOLD));
        petTextList.add(TextHelper.concat(
                luckText,
                Text.literal(TextHelper.fmt(petStat.getlLuck())),
                Text.literal(" " + TextHelper.fmt(petStat.getlLuckPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%").formatted(Formatting.GRAY)
        ));
        petTextList.add(TextHelper.concat(
                scaleText,
                Text.literal(TextHelper.fmt(petStat.getlScale())),
                Text.literal(" " + TextHelper.fmt(petStat.getlScalePercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%").formatted(Formatting.GRAY)
        ));
        petTextList.add(Text.empty());
        petTextList.add(Text.literal("Rating").formatted(Formatting.BOLD));
        Object[] ratingString = petStat.hasRoundingError() ? PetMergeCalculatorHandler.ratingString(petStat.hiddenRating) : petStat.getRatingString();
        petTextList.add(Text.literal(ratingString[0] + "").withColor((Integer) ratingString[1]));
        petTextList.add(Text.literal(TextHelper.fmt(petStat.getTotalPercent() * 100, config.petTooltipToggles.percentageDecimalPlaces) + "%").formatted(Formatting.GRAY));

        switch (column) {
            case 0:
                petTextList.forEach(text -> context.drawText(textRenderer, text, width / 2 - x - textRenderer.getWidth(text) / 2 - 50 - (petColumns * 18), height / 2 - (9 * 18 / 2) + 18 + (count.getAndIncrement() * 10), 0xFFFFFFFF, true));
                break;
            case 1:
                petTextList.forEach(text -> context.drawText(textRenderer, text, width / 2 - textRenderer.getWidth(text) / 2, height / 2 - (9 * 18 / 2) + 18 + (count.getAndIncrement() * 10), 0xFFFFFFFF, true));
                break;
            case 2:
                petTextList.forEach(text -> context.drawText(textRenderer, text, width / 2 + x - textRenderer.getWidth(text) / 2 + 50 + (petColumns * 18), height / 2 - (9 * 18 / 2) + 18 + (count.getAndIncrement() * 10), 0xFFFFFFFF, true));
                break;
            default:
                break;
        }
    }

    private void renderWidgets() {
        List<ClickablePetItemWidget> clickablePetItemWidgets = new ArrayList<>();
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger column = new AtomicInteger(0);
        int middleHeight = petList.size() > 9 ? (9 * 18) / 2 : (petList.size() * 18) / 2;

        petList.forEach(itemStack -> {
            if (row.get() == 9) {
                row.set(0);
                column.getAndIncrement();
            }
            clickablePetItemWidgets.add(new ClickablePetItemWidget(width / 2 - x - 8 - (column.get() * 18), height / 2 + y - middleHeight + (row.get() * 18), textRenderer, itemStack, 0, row.get() + (column.get() * 9)));
            row.getAndIncrement();
        });

        row.set(0);
        column.set(0);

        petList.forEach(itemStack -> {
            if (row.get() == 9) {
                row.set(0);
                column.getAndIncrement();
            }
            clickablePetItemWidgets.add(new ClickablePetItemWidget(width / 2 + x - 8 + (column.get() * 18), height / 2 + y - middleHeight + (row.get() * 18), textRenderer, itemStack, 1, row.get() + (column.get() * 9)));
            row.getAndIncrement();
        });

        clickablePetItemWidgets.forEach(this::addDrawableChild);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
        PetMergeCalculatorHandler.instance().reset();
    }
}
