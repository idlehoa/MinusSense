/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.render;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.injection.forge.mixins.gui.MixinGuiContainer;
import net.minusmc.minusbounce.features.module.modules.client.HUD;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer extends MixinGuiContainer {
    
    @Shadow
    private boolean hasActivePotionEffects;

    @Overwrite
    public void updateActivePotionEffects()
    {
        final HUD hud = MinusBounce.moduleManager.getModule(HUD.class);
        if (!hud.getInvEffectOffset().get()) 
        {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = !this.mc.thePlayer.getActivePotionEffects().isEmpty();
        } 
        else if (!this.mc.thePlayer.getActivePotionEffects().isEmpty())
        {
            this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        }
        else
        {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
    }

}