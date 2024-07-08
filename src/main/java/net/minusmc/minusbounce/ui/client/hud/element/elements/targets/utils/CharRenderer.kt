/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets.utils

import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.render.AnimationUtils
import net.minusmc.minusbounce.utils.MinecraftInstance
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CharRenderer(private val small: Boolean): MinecraftInstance() {
    private var moveY = FloatArray(20)
    private var moveX = FloatArray(20)

    private val numberList = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")

    private val deFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))

    init {
        for (i in 0..19) {
            moveX[i] = 0F
            moveY[i] = 0F
        }
    }

    fun renderChar(
        number: Float,
        orgY: Float,
        initX: Float,
        initY: Float,
        scaleY: Float,
        shadow: Boolean,
        fontSpeed: Float,
        color: Int
    ): Float {
        val reFormat = deFormat.format(number.toDouble()) // string
        val fontRend = if (small) Fonts.font40 else Fonts.font72
        val delta = RenderUtils.deltaTime
        val scaledRes = ScaledResolution(mc)

        var indexY = 0
        var animX = 0F

        val cutY = initY + fontRend.FONT_HEIGHT.toFloat() * (3F / 4F)

        GL11.glEnable(3089)
        RenderUtils.makeScissorBox(0F, orgY + initY - 4F * scaleY, scaledRes.scaledWidth.toFloat(), orgY + cutY - 4F * scaleY)
        for ((indexX, char) in reFormat.toCharArray().withIndex()) {
            moveX[indexX] = AnimationUtils.animate(animX, moveX[indexX], fontSpeed * 0.025F * delta)
            animX = moveX[indexX]

            val pos = numberList.indexOf("$char")
            val expectAnim = (fontRend.FONT_HEIGHT.toFloat() + 2F) * pos
            val expectAnimMin = (fontRend.FONT_HEIGHT.toFloat() + 2F) * (pos - 2)
            val expectAnimMax = (fontRend.FONT_HEIGHT.toFloat() + 2F) * (pos + 2)
            
            if (pos >= 0) {
                moveY[indexY] = AnimationUtils.animate(expectAnim, moveY[indexY], fontSpeed * 0.02F * delta)

                GL11.glTranslatef(0F, initY - moveY[indexY], 0F)
                numberList.forEachIndexed { index, num ->
                    if ((fontRend.FONT_HEIGHT.toFloat() + 2F) * index in expectAnimMin..expectAnimMax) {
                        fontRend.drawString(num, initX + moveX[indexX], (fontRend.FONT_HEIGHT.toFloat() + 2F) * index, color, shadow)
                    }
                }
                GL11.glTranslatef(0F, -initY + moveY[indexY], 0F)
            } else {
                moveY[indexY] = 0F
                fontRend.drawString("$char", initX + moveX[indexX], initY, color, shadow)
            }

            animX += fontRend.getStringWidth("$char")
            indexY++
        }
        GL11.glDisable(3089)

        return animX
    }
}
