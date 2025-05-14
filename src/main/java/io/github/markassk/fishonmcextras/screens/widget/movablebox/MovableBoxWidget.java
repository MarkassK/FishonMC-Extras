package io.github.markassk.fishonmcextras.screens.widget.movablebox;

import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.RotationAxis;

public class MovableBoxWidget extends ClickableWidget {
    private final int padding = 8;
    private final TextRenderer textRenderer;
    private final CallbackCoordinates callbackCoordinates;
    private final float scale;
    private final int lines;
    private final Text text;
    private final boolean rightAlignment;
    private final int minWidth;
    private final int maxWidth;
    private final int minHeight;
    private final int maxHeight;
    private final int maxLength;

    private double deltaX = 0;
    private double deltaY = 0;
    private int originalX;
    private int originalY;
    private int xPercent;
    private int yPercent;

    public MovableBoxWidget(TextRenderer textRenderer, int xPercent, int yPercent, boolean rightAlignment, Text text, int fontSize, int lines, int maxLength, CallbackCoordinates callbackCoordinates) {
        super(1, 1,  1, 1, null);

        this.text = text;
        this.rightAlignment = rightAlignment;
        this.textRenderer = textRenderer;
        this.callbackCoordinates = callbackCoordinates;
        this.scale = fontSize / 10f;
        this.lines = lines;
        this.maxLength = maxLength;
        this.xPercent = xPercent;
        this.yPercent = yPercent;

        this.setWidth((int) ((padding * 2 + this.maxLength) * this.scale));
        this.setHeight((int) ((padding * 2 + (textRenderer.fontHeight + 2f) * this.lines) * this.scale));

        this.minWidth = 0;
        this.maxWidth = MinecraftClient.getInstance().getWindow().getScaledWidth() - this.width;
        this.minHeight = 24;
        this.maxHeight = MinecraftClient.getInstance().getWindow().getScaledHeight() - this.height;

        float xCoord = ((rightAlignment ? 100 - xPercent : xPercent) / 100f * (this.maxWidth - this.minWidth + this.width)) - (rightAlignment ? this.width : 0);
        float yCoord = yPercent / 100f * (this.maxHeight - this.minHeight) + this.minHeight;
        this.setX((int) xCoord);
        this.setY((int) yCoord);
        this.originalX = getX();
        this.originalY = getY();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int alphaInt = (int) ((40 / 100f) * 255f) << 24;

        context.fill(getX(), getY(), getX() + width, getY() + height, this.isHovered() ? alphaInt | 0xFFFFFF : alphaInt);
        context.fill(rightAlignment ? getX() + width - 1 : getX(), getY(), rightAlignment ? getX() + width : getX() + 1, getY() + height, 0xFFFFFFFF);
        context.drawText(textRenderer, text, getX() + width / 2 - textRenderer.getWidth(text) / 2, getY() + height / 2 - textRenderer.fontHeight, 0xFFFFFF, true);
        context.drawText(textRenderer,
                TextHelper.concat(
                        Text.literal("X: ").formatted(Formatting.GRAY),
                        Text.literal(xPercent + "% ").formatted(Formatting.YELLOW),
                        Text.literal("Y: ").formatted(Formatting.GRAY),
                        Text.literal(yPercent + "%").formatted(Formatting.YELLOW)

                ),
                getX() + width / 2 - textRenderer.getWidth(text) / 2, getY() + height / 2 + 1, 0xFFFFFF, true);

        context.getMatrices().push();
        try {
            context.getMatrices().translate((rightAlignment ? getX() + width: getX()), getY(), 0.0f);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rightAlignment ? 90.0F : -90.0F));
            Text alignmentText = Text.literal(rightAlignment ? "ʀɪɢʜᴛ" : "ʟᴇꜰᴛ").formatted(Formatting.GRAY, Formatting.ITALIC);
            context.drawText(textRenderer, alignmentText, rightAlignment ? height / 2 - textRenderer.getWidth(alignmentText) / 2 : - height / 2 - textRenderer.getWidth(alignmentText) / 2, 0, 0xFFFFFF, true);

        } finally {
            context.getMatrices().pop();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);

        this.deltaX += deltaX;
        this.deltaY += deltaY;

        this.setX(Math.clamp(originalX + (int) this.deltaX, this.minWidth, this.maxWidth));
        this.setY(Math.clamp(originalY + (int) this.deltaY, this.minHeight, this.maxHeight));
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.originalX = getX();
        this.originalY = getY();
        this.deltaX = 0;
        this.deltaY = 0;

        float percentageX = rightAlignment ? 100 - ((float) (getX() - minWidth + width) / (maxWidth + width - minWidth) * 100) : (float) (getX() - minWidth) / (maxWidth + width - minWidth) * 100;
        float percentageY = (float) (getY() - minHeight) / (maxHeight - minHeight) * 100;

        this.xPercent = (int) percentageX;
        this.yPercent = (int) percentageY;

        // Send percentages back
        callbackCoordinates.onRelease((int) percentageX, (int) percentageY);
    }
}
