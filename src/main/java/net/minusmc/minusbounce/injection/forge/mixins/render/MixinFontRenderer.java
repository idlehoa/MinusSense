/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.render;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.event.TextEvent;
import net.minusmc.minusbounce.ui.font.GameFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Shadow protected abstract void resetStyles();

    @Inject(method = "drawString(Ljava/lang/String;FFIZ)I",
        at = @At(
            value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I",
            ordinal = 0, shift = At.Shift.AFTER
        )
    )
    private void resetStyle(CallbackInfoReturnable<Integer> ci) {
        this.resetStyles();
    }

    @ModifyVariable(method = "renderString", at = @At("HEAD"), require = 1, ordinal = 0)
    private String renderString(final String string) {
        if (string == null)
            return null;
        if (MinusBounce.eventManager == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        MinusBounce.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), require = 1, ordinal = 0)
    private String getStringWidth(final String string) {
        if (string == null)
            return null;
        if (MinusBounce.eventManager == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        MinusBounce.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

}