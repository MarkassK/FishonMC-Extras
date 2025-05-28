package io.github.markassk.fishonmcextras.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ChatScreenHandler;
import io.github.markassk.fishonmcextras.handler.CrewHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Shadow @Final private TextRenderer textRenderer;
    @Shadow private String text;
    @Unique
    private static int xCoord = 0;
    @Unique
    private static int yCoord = 0;
    @Unique
    private static boolean ticker = false;

    @ModifyArgs(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"))
    private void getLocals(Args args) {
        xCoord = args.get(2);
        yCoord = args.get(3);
        args.set(1, ticker ? "_" : "");
    }

    @ModifyVariable(method = "renderWidget", at = @At("STORE"), ordinal = 1)
    private boolean getbl2(boolean bl2) {
        ticker = bl2;
        return true;
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void injectMarker(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

        if(ChatScreenHandler.instance().screenInit
                && ProfileDataHandler.instance().profileData.isInCrewChat && !this.text.startsWith("/")
                && config.crewTracker.crewChatLocation == CrewHandler.CrewChatLocation.IN_CHAT) {
            Text marker = Text.literal("ɪɴ ᴄʀᴇᴡ ᴄʜᴀᴛ").formatted(Formatting.GREEN, Formatting.ITALIC);
            context.drawText(this.textRenderer, marker, 16 + xCoord, yCoord - 1, ((int) 150f << 24) | 0xFFFFFF, true);
        }
    }
}
