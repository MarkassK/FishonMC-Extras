package io.github.markassk.fishonmcextras.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor
    Text getOverlayMessage();
    @Accessor
    int getOverlayRemaining();
    @Accessor
    int getTitleRemainTicks();
    @Accessor
    Text getTitle();
}
