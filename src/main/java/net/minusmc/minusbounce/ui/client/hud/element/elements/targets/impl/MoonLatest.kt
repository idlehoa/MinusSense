/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets.impl

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.elements.Target
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.extensions.darker
import net.minusmc.minusbounce.utils.render.BlendUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.abs

class MoonLatest(inst: Target): TargetStyle("MoonLatest", inst, true) {

    private var lastTarget: EntityPlayer? = null

    override fun drawTarget(entity: EntityPlayer) {

        val font = Fonts.fontSFUI35
        val healthString = "${decimalFormat2.format(entity.health)} "

        if (entity != lastTarget || easingHealth < 0 || easingHealth > entity.maxHealth ||
            abs(easingHealth - entity.health) < 0.01) {
            easingHealth = entity.health
        }
        val width = (38 + Fonts.fontSFUI40.getStringWidth(entity.name))
                .coerceAtLeast(118)
                .toFloat()

        // Draw rect box
        RenderUtils.drawRect(0F, 0F, width, 32F, targetInstance.bgColor.rgb)

        // Health bar
        val barLength = 69F * (entity.health / entity.maxHealth).coerceIn(0F, 1F)
        RenderUtils.drawRect(37F, 25.5F, 45F + 69F, 26.5F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth).darker(0.3F)).rgb)
        RenderUtils.drawRect(37F, 25.5F, 45F + barLength, 26.5F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth)).rgb)

        // Draw rect 1
        RenderUtils.drawRect(0F, 0F, width, 1F, RenderUtils.skyRainbow(0, 1f,1f))

        // Armor bar
        if (entity.totalArmorValue != 0) {
            RenderUtils.drawRect(37F, 28.5F, 30f + (entity.totalArmorValue) * 4.2F, 29.5F, Color(36,77,255).rgb) // Draw armor bar
        }

        updateAnim(entity.health)
        // Name
        Fonts.fontSFUI40.drawString(entity.name, 37, 3, getColor(-1).rgb)

        // HP
        GL11.glPushMatrix()
        GL11.glScalef(1F,1F,1F)
        font.drawStringWithShadow(healthString + "HP", 37F, 17.5F, Color(255,255,255).rgb)
        GL11.glPopMatrix()

        GlStateManager.resetColor()
        RenderUtils.drawEntityOnScreen(18, 28, 12, entity)

        lastTarget = entity
    }

    override fun handleBlur(entity: EntityPlayer) {
        val width = (38 + Fonts.fontSFUI40.getStringWidth(entity.name))
                        .coerceAtLeast(118)
                        .toFloat()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.quickDrawRect(0F, 0F, width, 32F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

    }

    override fun handleShadowCut(entity: EntityPlayer) = handleBlur(entity)

    override fun handleShadow(entity: EntityPlayer) {
        val width = (38 + Fonts.fontSFUI40.getStringWidth(entity.name))
                        .coerceAtLeast(118)
                        .toFloat()

        RenderUtils.newDrawRect(0F, 0F, width, 32F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        entity ?: return Border(0F, 0F, 118F, 32F)
        val width = (38 + Fonts.fontSFUI40.getStringWidth(entity.name))
                        .coerceAtLeast(118)
                        .toFloat()
        return Border(0F, 0F, width, 32F)
    }

}
