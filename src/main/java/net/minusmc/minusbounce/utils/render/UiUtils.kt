/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.render

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.util.ResourceLocation
import net.minusmc.minusbounce.utils.render.RenderUtils.drawRoundedRect
import net.minusmc.minusbounce.utils.render.RenderUtils.glColor
import net.minusmc.minusbounce.utils.render.Stencil.dispose
import net.minusmc.minusbounce.utils.render.Stencil.erase
import net.minusmc.minusbounce.utils.render.Stencil.write
import org.lwjgl.opengl.GL11
import java.awt.Color

object UiUtils {
    fun width(): Int {
        return ScaledResolution(Minecraft.getMinecraft()).scaledWidth
    }

    fun height(): Int {
        return ScaledResolution(Minecraft.getMinecraft()).scaledHeight
    }

    fun anima(target: Int, speed: Int): Int {
        var a = 0
        if (a < target) {
            a += speed
        }
        if (a > target) {
            a -= speed
        }
        return a
    }

    private fun clamp(t: Float, x: Float, y: Float): Float {
        if (t < x) return x
        return if (t > y) y else t
    }

    fun drawImage(image: ResourceLocation?, x: Int, y: Int, width: Int, height: Int) {
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
        GL11.glDisable(2929)
        GL11.glEnable(3042)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        Minecraft.getMinecraft().textureManager.bindTexture(image)
        Gui.drawModalRectWithCustomSizedTexture(
            x,
            y, 0.0f, 0.0f, width, height, width.toFloat(), height.toFloat()
        )
        GL11.glDepthMask(true)
        GL11.glDisable(3042)
        GL11.glEnable(2929)
    }

    fun drawImage(image: ResourceLocation?, x: Int, y: Int, width: Int, height: Int, color: Color) {
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
        GL11.glDisable(2929)
        GL11.glEnable(3042)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GL11.glColor4f(
            (color.red.toFloat() / 255.0f),
            (color.blue.toFloat() / 255.0f), (color.red.toFloat() / 255.0f), 1.0f
        )
        Minecraft.getMinecraft().textureManager.bindTexture(image)
        Gui.drawModalRectWithCustomSizedTexture(
            x,
            y, 0.0f, 0.0f, width, height, width.toFloat(), height.toFloat()
        )
        GL11.glDepthMask(true)
        GL11.glDisable(3042)
        GL11.glEnable(2929)
    }

    fun drawRoundRect(d: Double, e: Double, g: Double, h: Double, color: Int) {
        drawRect(d + 1, e, g - 1, h, color)
        drawRect(d, e + 1, d + 1, h - 1, color)
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color)
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color)
        drawRect(g - 1, e + 1, g - 0.5, e + 0.5, color)
        drawRect(g - 1, e + 1, g, h - 1, color)
        drawRect(d + 1, h - 1, d + 0.5, h - 0.5, color)
        drawRect(g - 1, h - 1, g - 0.5, h - 0.5, color)
    }

    fun drawRoundRectWithRect(d: Double, e: Double, g: Double, h: Double, color: Int) {
        drawRect(d + 1, e, g - 1, h, color)
        drawRect(d, e + 1, d + 1, h - 1, color)
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color)
        drawRect(d + 1, e + 1, d + 0.5, e + 0.5, color)
        drawRect(g - 1, e + 1, g - 0.5, e + 0.5, color)
        drawRect(g - 1, e + 1, g, h - 1, color)
        drawRect(d + 1, h - 1, d + 0.5, h - 0.5, color)
        drawRect(g - 1, h - 1, g - 0.5, h - 0.5, color)
    }

    fun startGlScissor(x: Int, y: Int, width: Int, height: Int) {
        val mc = Minecraft.getMinecraft()
        var scaleFactor = 1
        var k = mc.gameSettings.guiScale
        if (k == 0) {
            k = 1000
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor
        }
        GL11.glPushMatrix()
        GL11.glEnable(3089)
        GL11.glScissor(
            (x * scaleFactor),
            (mc.displayHeight - (y + height) * scaleFactor), (width * scaleFactor), (height * scaleFactor)
        )
    }

    fun stopGlScissor() {
        GL11.glDisable(3089)
        GL11.glPopMatrix()
    }

    fun drawGradient(x: Double, y: Double, x2: Double, y2: Double, col1: Int, col2: Int) {
        val f = (col1 shr 24 and 255).toFloat() / 255.0f
        val f1 = (col1 shr 16 and 255).toFloat() / 255.0f
        val f2 = (col1 shr 8 and 255).toFloat() / 255.0f
        val f3 = (col1 and 255).toFloat() / 255.0f
        val f4 = (col2 shr 24 and 255).toFloat() / 255.0f
        val f5 = (col2 shr 16 and 255).toFloat() / 255.0f
        val f6 = (col2 shr 8 and 255).toFloat() / 255.0f
        val f7 = (col2 and 255).toFloat() / 255.0f
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        GL11.glPushMatrix()
        GL11.glBegin(7)
        GL11.glColor4f(f1, f2, f3, f)
        GL11.glVertex2d(x2, y)
        GL11.glVertex2d(x, y)
        GL11.glColor4f(f5, f6, f7, f4)
        GL11.glVertex2d(x, y2)
        GL11.glVertex2d(x2, y2)
        GL11.glEnd()
        GL11.glPopMatrix()
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
    }

    fun drawGradientSideways(left: Double, top: Double, right: Double, bottom: Double, col1: Int, col2: Int) {
        val f = (col1 shr 24 and 255).toFloat() / 255.0f
        val f1 = (col1 shr 16 and 255).toFloat() / 255.0f
        val f2 = (col1 shr 8 and 255).toFloat() / 255.0f
        val f3 = (col1 and 255).toFloat() / 255.0f
        val f4 = (col2 shr 24 and 255).toFloat() / 255.0f
        val f5 = (col2 shr 16 and 255).toFloat() / 255.0f
        val f6 = (col2 shr 8 and 255).toFloat() / 255.0f
        val f7 = (col2 and 255).toFloat() / 255.0f
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        GL11.glPushMatrix()
        GL11.glBegin(7)
        GL11.glColor4f(f1, f2, f3, f)
        GL11.glVertex2d(left, top)
        GL11.glVertex2d(left, bottom)
        GL11.glColor4f(f5, f6, f7, f4)
        GL11.glVertex2d(right, bottom)
        GL11.glVertex2d(right, top)
        GL11.glEnd()
        GL11.glPopMatrix()
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
    }

    fun outlineRect(x: Double, y: Double, x1: Double, y1: Double, width: Double, internalColor: Int, borderColor: Int) {
        drawRect(x + width, y + width, x1 - width, y1 - width, internalColor)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        drawRect(x + width, y, x1 - width, y + width, borderColor)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        drawRect(x, y, x + width, y1, borderColor)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        drawRect(x1 - width, y, x1, y1, borderColor)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        drawRect(x + width, y1 - width, x1 - width, y1, borderColor)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun fastShadowRoundedRect(
        x: Float,
        y: Float,
        x2: Float,
        y2: Float,
        rad: Float,
        width: Float,
        r: Float,
        g: Float,
        b: Float,
        al: Float
    ) {
        write(true)
        drawRoundedRect(x, y, x2, y2, rad, Color(r, g, b, al).rgb)
        erase(false)
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glShadeModel(GL11.GL_SMOOTH)
        GL11.glColor4f(r, g, b, al)
        GL11.glBegin(GL11.GL_QUAD_STRIP)
        GL11.glVertex2f(x + width / 2f, y + width / 2f)
        GL11.glColor4f(r, g, b, 0f)
        GL11.glVertex2f(x - width, y - width)
        GL11.glColor4f(r, g, b, al)
        GL11.glVertex2f(x2 - width / 2f, y + width / 2f)
        GL11.glColor4f(r, g, b, 0f)
        GL11.glVertex2f(x2 + width, y - width)
        GL11.glColor4f(r, g, b, al)
        GL11.glVertex2f(x2 - width / 2f, y2 - width / 2f)
        GL11.glColor4f(r, g, b, 0f)
        GL11.glVertex2f(x2 + width, y2 + width)
        GL11.glColor4f(r, g, b, al)
        GL11.glVertex2f(x + width / 2f, y2 - width / 2f)
        GL11.glColor4f(r, g, b, 0f)
        GL11.glVertex2f(x - width, y2 + width)
        GL11.glColor4f(r, g, b, al)
        GL11.glVertex2f(x + width / 2f, y + width / 2f)
        GL11.glColor4f(r, g, b, 0f)
        GL11.glVertex2f(x - width, y - width)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glShadeModel(7424)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        GL11.glPopMatrix()
        dispose()
    }

    fun fastShadowRoundedRect(x: Float, y: Float, x2: Float, y2: Float, rad: Float, width: Float, color: Color) {
        fastShadowRoundedRect(
            x,
            y,
            x2,
            y2,
            rad,
            width,
            color.red / 255.0f,
            color.green / 255.0f,
            color.blue / 255.0f,
            color.alpha / 255.0f
        )
    }

    fun drawRect(x: Double, y: Double, x2: Double, y2: Double, color: Int) {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glPushMatrix()
        glColor(Color(color))
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2d(x2, y)
        GL11.glVertex2d(x, y)
        GL11.glVertex2d(x, y2)
        GL11.glVertex2d(x2, y2)
        GL11.glEnd()
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
    }

    fun drawBorder(x: Float, y: Float, x2: Float, y2: Float, strength: Float, color: Int) {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glPushMatrix()
        glColor(Color(color))
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2f(x - strength, y - strength)
        GL11.glVertex2f(x2 + strength, y - strength)
        GL11.glVertex2f(x2 + strength, y + strength)
        GL11.glVertex2f(x - strength, y + strength)
        GL11.glVertex2f(x - strength, y2 - strength)
        GL11.glVertex2f(x2 + strength, y2 - strength)
        GL11.glVertex2f(x2 + strength, y2 + strength)
        GL11.glVertex2f(x - strength, y2 + strength)
        GL11.glVertex2f(x - strength, y + strength)
        GL11.glVertex2f(x + strength, y + strength)
        GL11.glVertex2f(x + strength, y2 - strength)
        GL11.glVertex2f(x - strength, y2 - strength)
        GL11.glVertex2f(x2 - strength, y + strength)
        GL11.glVertex2f(x2 + strength, y + strength)
        GL11.glVertex2f(x2 + strength, y2 - strength)
        GL11.glVertex2f(x2 - strength, y2 - strength)
        GL11.glEnd()
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
    }
}
