/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets

import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.elements.Target
import net.minusmc.minusbounce.utils.MinecraftInstance
import net.minusmc.minusbounce.utils.render.ColorUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.value.Value
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow

abstract class TargetStyle(val name: String, val targetInstance: Target, val shaderSupport: Boolean): MinecraftInstance() {

    var easingHealth = 0F

    val decimalFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))
    val decimalFormat2 = DecimalFormat("##0.0", DecimalFormatSymbols(Locale.ENGLISH))
    val decimalFormat3 = DecimalFormat("0.#", DecimalFormatSymbols(Locale.ENGLISH))
    val decimalFormat4 = DecimalFormat("###.##")

    val shadowOpaque: Color
        get() = ColorUtils.reAlpha(when (targetInstance.shadowColorMode.get().lowercase()) {
            "background" -> targetInstance.bgColor
            "custom" -> Color(targetInstance.shadowColorRedValue.get(), targetInstance.shadowColorGreenValue.get(), targetInstance.shadowColorBlueValue.get())
            else -> targetInstance.barColor
        }, 1F - targetInstance.animProgress)

    abstract fun drawTarget(entity: EntityPlayer)
    abstract fun getBorder(entity: EntityPlayer?): Border?
    
    open fun updateAnim(targetHealth: Float) {
        if (targetInstance.noAnimValue.get())
            easingHealth = targetHealth
        else
            easingHealth += ((targetHealth - easingHealth) / 2.0F.pow(10.0F - targetInstance.globalAnimSpeed.get())) * RenderUtils.deltaTime
    }

    open fun handleDamage(entity: EntityPlayer) {}

    open fun handleBlur(entity: EntityPlayer) {}
    
    open fun handleShadowCut(entity: EntityPlayer) {}
    open fun handleShadow(entity: EntityPlayer) {}

    /**
     * Get all values of element
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>()

    fun getColor(color: Color) = ColorUtils.reAlpha(color, color.alpha / 255F * (1F - targetInstance.getFadeProgress()))
    fun getColor(color: Int) = getColor(Color(color))

    fun drawMosswareHead(skin: ResourceLocation, width: Int, height: Int) {
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }

    fun drawHead(skin: ResourceLocation, x: Int = 2, y: Int = 2, width: Int, height: Int, alpha: Float = 1F) {
        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)
        glColor4f(1.0F, 1.0F, 1.0F, alpha)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
                64F, 64F)
        glDepthMask(true)
        glDisable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
    }

    fun drawHead(skin: ResourceLocation, x: Float, y: Float, scale: Float, width: Int, height: Int, red: Float, green: Float, blue: Float, alpha: Float = 1F) {
        glPushMatrix()
        glTranslatef(x, y, 0F)
        glScalef(scale, scale, scale)
        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)
        glColor4f(red.coerceIn(0F, 1F), green.coerceIn(0F, 1F), blue.coerceIn(0F, 1F), alpha.coerceIn(0F, 1F))
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(0, 0, 8F, 8F, 8, 8, width, height,
                64F, 64F)
        glDepthMask(true)
        glDisable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
        glPopMatrix()
        glColor4f(1f, 1f, 1f, 1f)
    }
    fun fadeAlpha(alpha: Int) : Int {
        return alpha - (targetInstance.getFadeProgress() * alpha).toInt()
    }

}
