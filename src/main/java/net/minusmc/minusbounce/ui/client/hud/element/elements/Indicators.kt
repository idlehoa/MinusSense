package net.minusmc.minusbounce.ui.client.hud.element.elements

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.MathHelper
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minusmc.minusbounce.ui.client.hud.element.ElementInfo
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.AnimationHelper
import net.minusmc.minusbounce.utils.render.ColorUtils.LiquidSlowly
import net.minusmc.minusbounce.utils.render.ColorUtils.fade
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.timer.TimeUtils
import net.minusmc.minusbounce.value.FloatValue
import net.minusmc.minusbounce.value.IntegerValue
import net.minusmc.minusbounce.value.ListValue
import java.awt.Color
import kotlin.math.sqrt

@ElementInfo(name = "Indicators")
class Indicators : Element() {
    private val colorModeValue =
        ListValue("Color", arrayOf("Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade"), "Custom")
    private val saturationValue = FloatValue("Saturation", 1f, 0f, 1f)
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)
    private val mixerSecondsValue = IntegerValue("Seconds", 2, 1, 10)

    //fix jitter
    private var indx = IntegerValue("noting", 120, 0, 1000)
    private var indy = IntegerValue("noting2", 80, 0, 1000)
    private var armorBarWidth = 0.0
    private var hurttimeBarWidth = 0.0
    private var bpsBarWidth = 0.0
    private var healthBarWidth = 0.0
    var x2 = indx.get()
    private var y3 = indy.get()
    private var timeHelper = TimeUtils()
    private var sr = ScaledResolution(mc)
    val scaledWidth = sr.scaledWidth.toFloat()
    val scaledHeight = sr.scaledHeight.toFloat()
    override fun drawElement(): Border {
        val prevZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ
        val prevX = mc.thePlayer.posX - mc.thePlayer.prevPosX
        val lastDist = sqrt(prevX * prevX + prevZ * prevZ)
        val currSpeed = lastDist * 15.3571428571 / 4
        val xX = scaledWidth / 2.0f - x2
        val yX = scaledHeight / 2.0f + y3
        RenderUtils.drawRect(xX + 4.5, yX + 196.5 - 405, xX + 100.5, yX + 246.5 - 408, Color(11, 11, 11, 255).rgb)
        RenderUtils.drawRect(xX + 5, yX + 198 - 405, xX + 100, yX + 246 - 408, Color(28, 28, 28, 255).rgb)
        RenderUtils.drawRect(xX + 5, yX + 198 - 405, xX + 100, yX + 208 - 408, Color(21, 19, 20, 255).rgb)
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 210 - 406).toDouble(),
            (xX + 95).toDouble(),
            yX + 213.5 - 406,
            Color(41, 41, 41, 255).rgb
        )
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 219 - 406).toDouble(),
            (xX + 95).toDouble(),
            yX + 222.5 - 406,
            Color(41, 41, 41, 255).rgb
        )
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 228 - 406).toDouble(),
            (xX + 95).toDouble(),
            yX + 231.5 - 406,
            Color(41, 41, 41, 255).rgb
        )
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 237 - 406).toDouble(),
            (xX + 95).toDouble(),
            yX + 240.5 - 406,
            Color(41, 41, 41, 255).rgb
        )
        RenderUtils.drawRect(xX + 5, yX + 197 - 405, xX + 100, yX + 198 - 405, color!!.rgb)
        Fonts.fontSFUI35.drawString("Indicators", xX + 37, yX + 202 - 406, -1)

        // armor
        val armorValue = mc.thePlayer.totalArmorValue.toFloat()
        var armorPercentage = (armorValue / 20).toDouble()
        armorPercentage = MathHelper.clamp_double(armorPercentage, 0.0, 1.0)
        val armorWidth = 51 * armorPercentage
        armorBarWidth = AnimationHelper.animate(armorWidth, armorBarWidth, 0.0229999852180481)
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 210 - 406).toDouble(),
            xX + 44 + armorBarWidth,
            yX + 213.5 - 406,
            color!!.rgb
        )
        Fonts.fontSFUI35.drawString("Armor", xX + 8, yX + 211 - 406, -1)

        // HurtTime
        val hurttimePercentage = MathHelper.clamp_double(mc.thePlayer.hurtTime.toDouble(), 0.0, 0.6)
        val hurttimeWidth = 51.0 * hurttimePercentage
        hurttimeBarWidth = AnimationHelper.animate(hurttimeWidth, hurttimeBarWidth, 0.0429999852180481)
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 219 - 406).toDouble(),
            xX + 44 + hurttimeBarWidth,
            yX + 222.5 - 406,
            color!!.rgb
        )
        Fonts.fontSFUI35.drawString("HurtTime", xX + 8, yX + 220 - 406, -1)

        // HurtTime
        val bpsPercentage = MathHelper.clamp_double(currSpeed, 0.0, 1.0)
        val bpsBarWidth = 51.0 * bpsPercentage
        this.bpsBarWidth = AnimationHelper.animate(bpsBarWidth, this.bpsBarWidth, 0.0329999852180481)
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 228 - 406).toDouble(),
            xX + 44 + this.bpsBarWidth,
            yX + 231.5 - 406,
            color!!.rgb
        )
        Fonts.fontSFUI35.drawString("BPS", xX + 8, yX + 229 - 406, -1)

        // HurtTime
        val health = mc.thePlayer.health
        var hpPercentage = (health / mc.thePlayer.maxHealth).toDouble()
        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0)
        val hpWidth = 51.0 * hpPercentage
        if (timeHelper.hasReached(15.0)) {
            healthBarWidth = AnimationHelper.animate(hpWidth, healthBarWidth, 0.2029999852180481)
            timeHelper.reset()
        }
        RenderUtils.drawRect(
            (xX + 44).toDouble(),
            (yX + 237 - 406).toDouble(),
            xX + 44 + healthBarWidth,
            yX + 240.5 - 406,
            color!!.rgb
        )
        Fonts.fontSFUI35.drawString("HP", xX + 8, yX + 238 - 406, -1)
        return Border(xX + 5, yX + 198 - 405, xX + 100, yX + 246 - 408)
    }

    val color: Color?
        get() {
            when (colorModeValue.get()) {
                "Custom" -> return Color(rRed.get(), rGreen.get(), rBlue.get())
                "Rainbow" -> return Color(
                    RenderUtils.getRainbowOpaque(
                        mixerSecondsValue.get(),
                        saturationValue.get(),
                        brightnessValue.get(),
                        0
                    )
                )

                "Sky" -> return RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get())
                "LiquidSlowly" -> return LiquidSlowly(
                    System.nanoTime(),
                    0,
                    saturationValue.get(),
                    brightnessValue.get()
                )
                "Fade" -> return fade(Color(rRed.get(), rGreen.get(), rBlue.get()), 0, 100)
            }
            return null
        }

    companion object {
        var rRed = IntegerValue("Red", 0, 0, 255)
        var rGreen = IntegerValue("Green", 0, 0, 255)
        var rBlue = IntegerValue("Blue", 0, 0, 255)
    }
}
