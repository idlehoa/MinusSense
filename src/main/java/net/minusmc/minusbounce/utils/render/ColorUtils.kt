/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.render

import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.opengl.GL11.glColor4f
import java.awt.Color
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.max

object ColorUtils {

    private val startTime = System.currentTimeMillis()
    private val COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]")

    @JvmField
    val hexColors = IntArray(16)

    init {
        repeat(16) { i ->
            val baseColor = (i shr 3 and 1) * 85

            val red = (i shr 2 and 1) * 170 + baseColor + if (i == 6) 85 else 0
            val green = (i shr 1 and 1) * 170 + baseColor
            val blue = (i and 1) * 170 + baseColor

            hexColors[i] = red and 255 shl 16 or (green and 255 shl 8) or (blue and 255)
        }
    }

    @JvmStatic
    fun stripColor(input: String?): String? {
        return COLOR_PATTERN.matcher(input ?: return null).replaceAll("")
    }

    fun interpolate(oldValue: Double, newValue: Double, interpolationValue: Double): Double {
        return oldValue + (newValue - oldValue) * interpolationValue
    }

    @JvmStatic
    fun interpolateInt(oldValue: Int, newValue: Int, interpolationValue: Double): Int {
        return interpolate(oldValue.toDouble(), newValue.toDouble(), interpolationValue.toFloat().toDouble()).toInt()
    }

    @JvmStatic
    fun interpolateColorC(color1: Color, color2: Color, amount: Float): Color {
        var amount = amount
        amount = Math.min(1f, Math.max(0f, amount))
        return Color(
                interpolateInt(color1.red, color2.red, amount.toDouble()),
                interpolateInt(color1.green, color2.green, amount.toDouble()),
                interpolateInt(color1.blue, color2.blue, amount.toDouble()),
                interpolateInt(color1.alpha, color2.alpha, amount.toDouble()
                )
        )
    }

    @JvmStatic
    fun translateAlternateColorCodes(textToTranslate: String): String {
        val chars = textToTranslate.toCharArray()

        for (i in 0 until chars.size - 1) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".contains(chars[i + 1], true)) {
                chars[i] = '§'
                chars[i + 1] = Character.toLowerCase(chars[i + 1])
            }
        }

        return String(chars)
    }

    fun randomMagicText(text: String): String {
        val stringBuilder = StringBuilder()
        val allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"

        for (c in text.toCharArray()) {
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                val index = Random().nextInt(allowedCharacters.length)
                stringBuilder.append(allowedCharacters.toCharArray()[index])
            }
        }

        return stringBuilder.toString()
    }

    @JvmStatic
    fun rainbow(): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + 400000L) / 10000000000F % 1, 1F, 1F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255f * 1F, currentColor.blue / 255F * 1F, currentColor.alpha / 255F)
    }

    // TODO: Use kotlin optional argument feature

    @JvmStatic
    fun rainbow(offset: Long): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 1F, 1F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255F * 1F, currentColor.blue / 255F * 1F,
            currentColor.alpha / 255F)
    }

    @JvmStatic
    fun rainbow(alpha: Float) = rainbow(400000L, alpha)

    @JvmStatic
    fun rainbow(alpha: Int) = rainbow(400000L, alpha / 255)

    @JvmStatic
    fun rainbow(offset: Long, alpha: Int) = rainbow(offset, alpha.toFloat() / 255)

    @JvmStatic
    fun rainbow(offset: Long, alpha: Float): Color {
        val currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 1F, 1F))
        return Color(currentColor.red / 255F * 1F, currentColor.green / 255f * 1F, currentColor.blue / 255F * 1F, alpha)
    }

    @JvmStatic
    fun LiquidSlowly(time: Long, count: Int, qd: Float, sq: Float): Color? {
        val color = Color(Color.HSBtoRGB((time.toFloat() + count * -3000000f) / 2 / 1.0E9f, qd, sq))
        return Color(color.red / 255.0f * 1, color.green / 255.0f * 1, color.blue / 255.0f * 1, color.alpha / 255.0f)
    }

    @JvmStatic
    fun TwoRainbow(offset: Long, alpha: Float): Color {
        var currentColor = Color(Color.HSBtoRGB((System.nanoTime() + offset) / 8.9999999E10F % 1, 0.75F, 0.8F))
        return Color(currentColor.getRed() / 255.0F * 1.0F, currentColor.getGreen() / 255.0F * 1.0F, currentColor.getBlue() / 255.0F * 1.0F, alpha)
    }

    @JvmStatic
    fun fade(color: Color, index: Int, count: Int): Color {
        val hsb = FloatArray(3)
        Color.RGBtoHSB(color.red, color.green, color.blue, hsb)
        var brightness =
            abs(((System.currentTimeMillis() % 2000L).toFloat() / 1000.0f + index.toFloat() / count.toFloat() * 2.0f) % 2.0f - 1.0f)
        brightness = 0.5f + 0.5f * brightness
        hsb[2] = brightness % 2.0f
        return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]))
    }

    fun getColor(hueoffset: Float, saturation: Float, brightness: Float): Int {
        val speed = 4500f
        val hue = System.currentTimeMillis() % speed.toInt() / speed
        return Color.HSBtoRGB(hue - hueoffset / 54, saturation, brightness)
    }

    @JvmStatic
    fun setColour(colour: Int) {
        val a = (colour shr 24 and 0xFF) / 255.0f
        val r = (colour shr 16 and 0xFF) / 255.0f
        val g = (colour shr 8 and 0xFF) / 255.0f
        val b = (colour and 0xFF) / 255.0f
        glColor4f(r, g, b, a)
    }
    @JvmStatic
    fun getColor(n: Int): String? {
        if (n != 1) {
            if (n == 2) {
                return "\u00a7a"
            }
            if (n == 3) {
                return "\u00a73"
            }
            if (n == 4) {
                return "\u00a74"
            }
            if (n >= 5) {
                return "\u00a7e"
            }
        }
        return "\u00a7f"
    }

    @JvmStatic
    fun hoverColor(color: Color?, hover: Int): Color {
        val r = color!!.red - (hover * 2)
        val g = color.green - (hover * 2)
        val b = color.blue - (hover * 2)
        return Color(max(r.toDouble(), 0.0).toInt(), max(g.toDouble(), 0.0).toInt(), max(b.toDouble(), 0.0).toInt(), color.alpha)
    }

    @JvmStatic
    fun reAlpha(color: Color, alpha: Int): Color = Color(color.red, color.green, color.blue, alpha.coerceIn(0, 255))

    @JvmStatic
    fun reAlpha(color: Color, alpha: Float): Color = Color(color.red / 255F, color.green / 255F, color.blue / 255F, alpha.coerceIn(0F, 1F))

    @JvmStatic
    fun getOppositeColor(color: Color): Color = Color(255 - color.red, 255 - color.green, 255 - color.blue, color.alpha)

    @JvmStatic
    fun modifyAlpha(col: Color?, alpha: Int) = Color(col!!.red, col.green, col.blue, alpha)

    fun colorCode(code: String, alpha: Int = 255): Color = when (code.lowercase()) {
        "0" -> Color(0, 0, 0, alpha)
        "1" -> Color(0, 0, 170, alpha)
        "2" -> Color(0, 170, 0, alpha)
        "3" -> Color(0, 170, 170, alpha)
        "4" -> Color(170, 0, 0, alpha)
        "5" -> Color(170, 0, 170, alpha)
        "6" -> Color(255, 170, 0, alpha)
        "7" -> Color(170, 170, 170, alpha)
        "8" -> Color(85, 85, 85, alpha)
        "9" -> Color(85, 85, 255, alpha)
        "a" -> Color(85, 255, 85, alpha)
        "b" -> Color(85, 255, 255, alpha)
        "c" -> Color(255, 85, 85, alpha)
        "d" -> Color(255, 85, 255, alpha)
        "e" -> Color(255, 255, 85, alpha)
        else -> Color(255, 255, 255, alpha)
    }

    fun getGradientOffset(one: Color, two: Color, offset: Double, alpha: Int): Color {
        val offset = if (offset > 1) {
            val off = offset.toInt()
            if (off % 2 == 0) offset else 1 - offset
        } else offset

        val inverse_percent = 1 - offset

        val redPart = one.red * inverse_percent + two.red * offset
        val greenPart = one.green * inverse_percent + two.green * offset
        val bluePart = one.blue * inverse_percent + two.blue * offset

        return Color(redPart.toInt(), greenPart.toInt(), bluePart.toInt(), alpha)
    }
}
