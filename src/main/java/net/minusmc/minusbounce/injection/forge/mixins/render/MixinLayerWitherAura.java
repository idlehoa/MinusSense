/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerWitherAura.class)
public class MixinLayerWitherAura {
    @Inject(method = "doRenderLayer(Lnet/minecraft/entity/boss/EntityWither;FFFFFFF)V", at = @At("TAIL"))
    private void fixDepth(CallbackInfo ci) {
        GlStateManager.depthMask(true);
    }
}
