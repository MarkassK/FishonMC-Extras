package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    private Text injectRender(PlayerListHud instance, PlayerListEntry entry) {
        if(ProfileDataHandler.instance().profileData.crewMembers.contains(entry.getProfile().getId())) {
            return instance.getPlayerName(entry).copy().append(Text.literal(" ⭐").formatted(Formatting.GREEN));
        } else {
            return instance.getPlayerName(entry);
        }

    }
}
