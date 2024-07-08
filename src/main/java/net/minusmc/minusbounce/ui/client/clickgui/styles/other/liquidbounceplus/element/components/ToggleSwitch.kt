package net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.element.components

import net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.extensions.animLinear
import net.minusmc.minusbounce.utils.render.BlendUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.render.ShaderUtils
import java.awt.Color

class ToggleSwitch {
    private var smooth = 0F
    var state = false
    
    fun onDraw(x: Float, y: Float, width: Float, height: Float, bgColor: Color, accentColor: Color) {
        smooth = smooth.animLinear((if (state) 0.2F else -0.2F) * RenderUtils.deltaTime * 0.045F, 0F, 1F)
        val borderColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(Color(160, 160, 160), accentColor), smooth)
        val mainColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(bgColor, accentColor), smooth)
        val switchColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(Color(160, 160, 160), bgColor), smooth)

        ShaderUtils.drawRoundedRect(x - 0.5F, y - 0.5F, x + width + 0.5F, y + height + 0.5F, (height + 1F) / 2F, borderColor!!)
        ShaderUtils.drawRoundedRect(x, y, x + width, y + height, height / 2F, mainColor!!)
        ShaderUtils.drawFilledCircle(x + (1F - smooth) * (2F + (height - 4F) / 2F) + smooth * (width - 2F - (height - 4F) / 2F), y + 2F + (height - 4F) / 2F, (height - 4F) / 2F, switchColor!!)
    }
}