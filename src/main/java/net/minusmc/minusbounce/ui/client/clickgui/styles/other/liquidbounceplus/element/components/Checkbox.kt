package net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.element.components

import net.minusmc.minusbounce.ui.client.clickgui.styles.other.liquidbounceplus.extensions.animLinear
import net.minusmc.minusbounce.utils.render.BlendUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.render.ShaderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color

class Checkbox {
    private var smooth = 0F
    var state = false

    fun onDraw(x: Float, y: Float, width: Float, height: Float, bgColor: Color, accentColor: Color) {
        smooth = smooth.animLinear((if (state) 0.2F else -0.2F) * RenderUtils.deltaTime * 0.045F, 0F, 1F)
        val borderColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(Color(160, 160, 160), accentColor), smooth)
        val mainColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(bgColor, accentColor), smooth)

        ShaderUtils.drawRoundedRect(x - 0.5F, y - 0.5F, x + width + 0.5F, y + width + 0.5F, 3F, borderColor!!)
        ShaderUtils.drawRoundedRect(x, y, x + width, y + width, 3F, mainColor!!)
        GL11.glColor4f(bgColor.red / 255F, bgColor.green / 255F, bgColor.blue / 255F, 1F)
//        RenderUtils.drawLine(x + width / 4F, y + width / 2F, x + width / 2.15F, y + width / 4F * 3F, 2F)
//        RenderUtils.drawLine(x + width / 2.15F, y + width / 4F * 3F, x + width / 3.95F * 3F, y + width / 3F, 2F)
        GL11.glColor4f(1F, 1F, 1F, 1F)
    }
}