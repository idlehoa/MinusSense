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
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.utils.CharRenderer
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.extensions.darker
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.render.Stencil
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.FloatValue
import org.lwjgl.opengl.GL11

class Chill(inst: Target): TargetStyle("Chill", inst, true) {

    private val chillFontSpeed = FloatValue("FontSpeed", 0.5F, 0.01F, 1F)
    private val chillRoundValue = BoolValue("RoundedBar", true)

    private val numberRenderer = CharRenderer(false)

    private var calcScaleX = 0F
    private var calcScaleY = 0F
    private var calcTranslateX = 0F
    private var calcTranslateY = 0F

    fun updateData(calcTranslateX: Float, calcTranslateY: Float, calcScaleX: Float, calcScaleY: Float) {
        this.calcTranslateX = calcTranslateX
        this.calcTranslateY = calcTranslateY
        this.calcScaleX = calcScaleX
        this.calcScaleY = calcScaleY
    }

    override fun drawTarget(entity: EntityPlayer) {
        updateAnim(entity.health)

        val name = entity.name
        val health = entity.health
        val tWidth = (45F + Fonts.font40.getStringWidth(name).coerceAtLeast(Fonts.font72.getStringWidth(decimalFormat.format(health)))).coerceAtLeast(120F)
        val playerInfo = mc.netHandler.getPlayerInfo(entity.uniqueID)

        // background
        RenderUtils.drawRoundedRect(0F, 0F, tWidth, 48F, 7F, targetInstance.bgColor.rgb)
        GlStateManager.resetColor()
        GL11.glColor4f(1F, 1F, 1F, 1F)
        
        // head
        if (playerInfo != null) {
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderUtils.fastRoundedRect(4F, 4F, 34F, 34F, 7F)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 4, 4, 30, 30, 1F - targetInstance.getFadeProgress())
            Stencil.dispose()
        }

        GlStateManager.resetColor()
        GL11.glColor4f(1F, 1F, 1F, 1F)

        // name + health
        Fonts.font40.drawString(name, 38F, 6F, getColor(-1).rgb)
        numberRenderer.renderChar(
            health,
            calcTranslateY,
            38F,
            17F,
            calcScaleY,
            false,
            chillFontSpeed.get(),
            getColor(-1).rgb
        )

        // health bar
        RenderUtils.drawRoundedRect(4F, 38F, tWidth - 4F, 44F, 3F, targetInstance.barColor.darker(0.5F).rgb)

        Stencil.write(false)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        RenderUtils.fastRoundedRect(4F, 38F, tWidth - 4F, 44F, 3F)
        GL11.glDisable(GL11.GL_BLEND)
        Stencil.erase(true)
        if (chillRoundValue.get())
            RenderUtils.customRounded(4F, 38F, 4F + (easingHealth / entity.maxHealth) * (tWidth - 8F), 44F, 0F, 3F, 3F, 0F, targetInstance.barColor.rgb)
        else
            RenderUtils.drawRect(4F, 38F, 4F + (easingHealth / entity.maxHealth) * (tWidth - 8F), 44F, targetInstance.barColor.rgb)
        Stencil.dispose()
    }

    override fun handleBlur(entity: EntityPlayer) {
        val tWidth = (45F + Fonts.font40.getStringWidth(entity.name).coerceAtLeast(Fonts.font72.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.fastRoundedRect(0F, 0F, tWidth, 48F, 7F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    override fun handleShadowCut(entity: EntityPlayer) = handleBlur(entity)

    override fun handleShadow(entity: EntityPlayer) {
        val tWidth = (45F + Fonts.font40.getStringWidth(entity.name).coerceAtLeast(Fonts.font72.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        RenderUtils.originalRoundedRect(0F, 0F, tWidth, 48F, 7F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        entity ?: return Border(0F, 0F, 120F, 48F)
        val tWidth = (45F + Fonts.font40.getStringWidth(entity.name).coerceAtLeast(Fonts.font72.getStringWidth(decimalFormat.format(entity.health)))).coerceAtLeast(120F)
        return Border(0F, 0F, tWidth, 48F)
    }

}
