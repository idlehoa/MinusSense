package net.minusmc.minusbounce.utils

import java.awt.Color
import java.util.function.Supplier
import kotlin.math.abs

enum class Palette(private val colorSupplier: Supplier<Color>) {
    GREEN(Supplier { Color(0, 255, 138) }),
    WHITE(Supplier { Color.WHITE }),
    PURPLE(Supplier { Color(198, 139, 255) }),
    DARK_PURPLE(Supplier { Color(133, 46, 215) }),
    BLUE(Supplier { Color(116, 202, 255) });

    val color: Color
        get() = colorSupplier.get()

    companion object {
        fun fade(color: Color): Color {
            return fade(color, 2, 100, 2f)
        }

        fun fade(color: Color, index: Int, count: Int, speed: Float): Color {
            val hsb = FloatArray(3)
            Color.RGBtoHSB(color.red, color.green, color.blue, hsb)
            var brightness =
                abs((((System.currentTimeMillis() % 10000L).toFloat() / 1000 + index.toFloat() / count.toFloat() * 2.0f) % 2.0f - 1).toDouble())
                    .toFloat()
            brightness = 0.1f + 0.9f * brightness
            hsb[2] = brightness % 2.0f
            return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]))
        }

        fun fade2(color: Color, index: Int, count: Int): Color {
            val hsb = FloatArray(3)
            Color.RGBtoHSB(color.red, color.green, color.blue, hsb)
            var brightness =
                abs((((System.currentTimeMillis() % 2000L).toFloat() / 1000.0f + index.toFloat() / count.toFloat() * 2.0f) % 2.0f - 1.0f).toDouble())
                    .toFloat()
            brightness = 0.5f + 0.5f * brightness
            hsb[2] = brightness % 2.0f
            return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]))
        }
    }
}
