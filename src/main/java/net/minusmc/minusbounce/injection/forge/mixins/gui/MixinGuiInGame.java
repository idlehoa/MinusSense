/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.gui;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.event.Render2DEvent;
import net.minusmc.minusbounce.features.module.modules.render.AntiBlind;
import net.minusmc.minusbounce.features.module.modules.client.HUD;
import net.minusmc.minusbounce.injection.access.StaticStorage;
import net.minusmc.minusbounce.ui.font.AWTFontRenderer;
import net.minusmc.minusbounce.utils.ClassUtils;
import net.minusmc.minusbounce.utils.render.ColorUtils;
import net.minusmc.minusbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.lwjgl.opengl.GL11;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame extends MixinGui {

    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    @Shadow @Final
    protected static ResourceLocation widgetsTexPath;

    @Shadow public GuiPlayerTabOverlay overlayPlayerList;

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    private void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution, CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = MinusBounce.moduleManager.getModule(AntiBlind.class);
        if ((antiBlind.getState() && antiBlind.getScoreBoard().get()) || MinusBounce.moduleManager.getModule(HUD.class).getState())
            callbackInfo.cancel();
    }

    @ModifyConstant(method = "renderScoreboard", constant = @Constant(intValue = 553648127))
    private int fixTextBlending(int original) {
        return -1;
    }

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = MinusBounce.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && antiBlind.getBossHealth().get())
            callbackInfo.cancel();
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        final HUD hud = MinusBounce.moduleManager.getModule(HUD.class);

        if(Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && (hud.getBlackHotbarValue().get() || hud.getAnimHotbarValue().get())) {
            final Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer entityPlayer = (EntityPlayer) mc.getRenderViewEntity();

            boolean blackHB = hud.getBlackHotbarValue().get();
            int middleScreen = sr.getScaledWidth() / 2;
            float posInv = hud.getAnimPos(entityPlayer.inventory.currentItem * 20F);

            GlStateManager.resetColor();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(widgetsTexPath);

            float f = this.zLevel;
            this.zLevel = -90.0F;
            GlStateManager.resetColor();

            if (blackHB) {
                RenderUtils.INSTANCE.originalRoundedRect(middleScreen - 91, sr.getScaledHeight() - 2, middleScreen + 91, sr.getScaledHeight() - 22, 3F, Integer.MIN_VALUE);
                RenderUtils.INSTANCE.originalRoundedRect(middleScreen - 91 + posInv, sr.getScaledHeight() - 2, middleScreen - 91 + posInv + 22, sr.getScaledHeight() - 22, 3F, Integer.MAX_VALUE);
            } else {
                this.drawTexturedModalRect(middleScreen - 91F, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect(middleScreen - 91F + posInv - 1, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }

            this.zLevel = f;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 19 - (blackHB ? 1 : 0);
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }   

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
            MinusBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
            AWTFontRenderer.Companion.garbageCollectionTick();
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderTooltip", at = @At("TAIL"))
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        MinusBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
        AWTFontRenderer.Companion.garbageCollectionTick();
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = MinusBounce.moduleManager.getModule(AntiBlind.class);

        if(antiBlind.getState() && antiBlind.getPumpkinEffect().get())
            callbackInfo.cancel();
    }
}