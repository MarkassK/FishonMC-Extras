package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.UUID;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    private Text injectRender(PlayerListHud instance, PlayerListEntry entry) {
        MutableText text = instance.getPlayerName(entry).copy();

        if(LoadingHandler.instance().isOnServer && Objects.equals(entry.getProfile().getId(), UUID.fromString("b5a9bbb7-42b4-4a6a-9ebe-bdf6697c8ee0"))) {
            text = Text.literal("\uE00B ").formatted(Formatting.WHITE).append(Text.literal("DannyPX").withColor(0x00AF0E));
        }

        if(config.crewTracker.showCrewTag && LoadingHandler.instance().isOnServer && ProfileDataHandler.instance().profileData.crewMembers.contains(entry.getProfile().getId())) {
            return Text.literal("\uE00A ").append(text);
        } else {
            return text;
        }

    }
}
