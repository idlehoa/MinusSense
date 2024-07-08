package net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.element.components

import net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.ColorManager
import net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.extensions.animSmooth
import net.minusmc.minusbounce.utils.render.ShaderUtils
import java.awt.Color

class Slider {
    private var smooth = 0F
    private var value = 0F

    fun onDraw(x: Float, y: Float, width: Float, accentColor: Color) {
        smooth = smooth.animSmooth(value, 0.5F)
        ShaderUtils.drawRoundedRect(x - 1F, y - 1F, x + width + 1F, y + 1F, 1F, ColorManager.unusedSlider)
        ShaderUtils.drawRoundedRect(x - 1F, y - 1F, x + width * (smooth / 100F) + 1F, y + 1F, 1F, accentColor)
        ShaderUtils.drawFilledCircle(x + width * (smooth / 100F), y, 5F, Color.white)
        ShaderUtils.drawFilledCircle(x + width * (smooth / 100F), y, 3F, ColorManager.background)
    }

    fun setValue(desired: Float, min: Float, max: Float) {
        value = (desired - min) / (max - min) * 100F
    }
}
