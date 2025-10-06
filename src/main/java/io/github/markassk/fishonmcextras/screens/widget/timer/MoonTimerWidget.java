package io.github.markassk.fishonmcextras.screens.widget.timer;

import io.github.markassk.fishonmcextras.handler.TimerHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.TimeUnit;

public class MoonTimerWidget extends TimerWidget {
    public MoonTimerWidget(int x, int y) {
        super(x, y);
    }

    public MoonTimerWidget(int x, int y, ClickCallback clickCallback) {
        super(x, y, clickCallback);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        long hours = TimeUnit.MILLISECONDS.toHours(TimerHandler.instance().moonTimer);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(TimerHandler.instance().moonTimer) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(TimerHandler.instance().moonTimer) % 60;
        Text time = Text.literal(String.format("%02d:%02d:%02d", hours, minutes, seconds)).formatted(Formatting.GRAY);

        context.drawText(textRenderer, time, this.getX() - textRenderer.getWidth(time), this.getY(), 0xFFFFFF, true);

        long total = 160L * 60L * 1000L; // 160 minutes between starts
        float percentage = (float) TimerHandler.instance().moonTimer / (float) total;
        int dashes = 14;
        int grayDashes = Math.round(dashes * percentage);
        int greenDashes = dashes - grayDashes;

        Text bar = TextHelper.concat(
                Text.literal("|").formatted(Formatting.WHITE),
                Text.literal(" ".repeat(greenDashes)).formatted(Formatting.STRIKETHROUGH, Formatting.GREEN),
                Text.literal(" ".repeat(grayDashes)).formatted(Formatting.STRIKETHROUGH, Formatting.GRAY),
                Text.literal("|").formatted(Formatting.WHITE)
        );
        context.drawText(textRenderer, bar, this.getX() - textRenderer.getWidth(bar), this.getY() + textRenderer.fontHeight, 0xFFFFFF, true);
    }
}
