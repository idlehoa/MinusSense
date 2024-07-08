package net.minusmc.minusbounce.ui.client.hud.element.elements

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minusmc.minusbounce.ui.client.hud.element.ElementInfo
import net.minusmc.minusbounce.ui.client.hud.element.Side
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.FloatValue
import net.minusmc.minusbounce.value.FontValue
import net.minusmc.minusbounce.value.IntegerValue
import java.awt.Color

@ElementInfo(name = "SessionInfo") 
class SessionInfo(x: Double = 15.0, y: Double = 10.0, scale: Float = 1F, side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)) : Element(x, y, scale, side) {

    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private val bgredValue = IntegerValue("Bg-R", 255, 0, 255)
    private val bggreenValue = IntegerValue("Bg-G", 255, 0, 255)
    private val bgblueValue = IntegerValue("Bg-B", 255, 0, 255)
    private val bgalphaValue = IntegerValue("Bg-Alpha", 150, 0, 255)

    private val lineValue = BoolValue("Line", true)
    private val redValue = IntegerValue("Line-R", 255, 0, 255)
    private val greenValue = IntegerValue("Line-G", 255, 0, 255)
    private val blueValue = IntegerValue("Line-B", 255, 0, 255)
    private val colorRedValue2 = IntegerValue("Line-R2", 0, 0, 255)
    private val colorGreenValue2 = IntegerValue("Line-G2", 111, 0, 255)
    private val colorBlueValue2 = IntegerValue("Line-B2", 255, 0, 255)

    private val fontValue = FontValue("Font", Fonts.font35)

    override fun drawElement(): Border {
        val fontRenderer = fontValue.get()

        val y2 = fontRenderer.FONT_HEIGHT * 3 + 11.0
        val x2 = 140.0

        val durationInMillis: Long = System.currentTimeMillis() - MinusBounce.playTimeStart
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        val time = String.format("%02dh %02dm %02ds", hour, minute, second)

        RenderUtils.drawRoundedRect(-2f, -2f, x2.toFloat(), y2.toFloat(), radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get()).rgb)
        if(lineValue.get()) {
            RenderUtils.drawGradientSideways(
                    5.44,
                    fontRenderer.FONT_HEIGHT + 2.5 + 0.0,
                    138.0 + -2.44,
                    fontRenderer.FONT_HEIGHT + 2.5 + 1.16,
                    Color(redValue.get(), greenValue.get(), blueValue.get()).rgb,
                    Color(colorRedValue2.get(), colorGreenValue2.get(), colorBlueValue2.get()).rgb
            )
        }
        fontRenderer.drawStringWithShadow("Statistics", x2.toFloat() / 4f, 3f, Color.WHITE.rgb)
        fontRenderer.drawStringWithShadow("Play time: $time", 2f, fontRenderer.FONT_HEIGHT + 8f, Color.WHITE.rgb)
        fontRenderer.drawStringWithShadow("Games played: 0", 3f , fontRenderer.FONT_HEIGHT * 2 + 8f, Color.WHITE.rgb)
        //fontRenderer.drawStringWithShadow("Kills: " + CombatListener.killCounts, 3f , fontRenderer.FONT_HEIGHT * 3 + 8f, Color.WHITE.rgb)
        return Border(-2f, -2f, x2.toFloat(), y2.toFloat())
    }
}