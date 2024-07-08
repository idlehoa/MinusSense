package net.minusmc.minusbounce.utils

import net.minecraft.entity.EntityLivingBase
import java.awt.Color
import java.text.NumberFormat
import kotlin.math.ceil

object Colors {
    fun getColor(color: Color): Int {
        return getColor(color.red, color.green, color.blue, color.alpha)
    }

    fun getColor(brightness: Int): Int {
        return getColor(brightness, brightness, brightness, 255)
    }

    fun getColor(brightness: Int, alpha: Int): Int {
        return getColor(brightness, brightness, brightness, alpha)
    }

    fun getColor(red: Int, green: Int, blue: Int): Int {
        return getColor(red, green, blue, 255)
    }

    fun getColor(red: Int, green: Int, blue: Int, alpha: Int): Int {
        var color = 0
        color = color or (alpha shl 24)
        color = color or (red shl 16)
        color = color or (green shl 8)
        color = color or blue
        return color
    }

    fun getHealthColor(entityLivingBase: EntityLivingBase): Color {
        val health = entityLivingBase.health
        val fractions = floatArrayOf(0.0f, 0.15f, 0.55f, 0.7f, 0.9f)
        val colors = arrayOf(Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN)
        val progress = health / entityLivingBase.maxHealth
        return if (health >= 0.0f) blendColors(fractions, colors, progress)!!.brighter() else colors[0]
    }

    fun blendColors(fractions: FloatArray?, colors: Array<Color>?, progress: Float): Color? {
        requireNotNull(fractions) { "Fractions can't be null" }
        requireNotNull(colors) { "Colours can't be null" }
        require(fractions.size == colors.size) { "Fractions and colours must have equal number of elements" }
        val indicies = getFractionIndicies(fractions, progress)
        val range = floatArrayOf(fractions[indicies[0]], fractions[indicies[1]])
        val colorRange = arrayOf(colors[indicies[0]], colors[indicies[1]])
        val max = range[1] - range[0]
        val value = progress - range[0]
        val weight = value / max
        return blend(colorRange[0], colorRange[1], (1.0f - weight).toDouble())
    }

    fun getIncremental(`val`: Double, inc: Double): Double {
        val one = 1.0 / inc
        return Math.round(`val` * one) / one
    }

    fun getFractionIndicies(fractions: FloatArray, progress: Float): IntArray {
        val range = IntArray(2)
        var startPoint: Int
        startPoint = 0
        while (startPoint < fractions.size && fractions[startPoint] <= progress) {
            startPoint++
        }
        if (startPoint >= fractions.size) {
            startPoint = fractions.size - 1
        }
        range[0] = startPoint - 1
        range[1] = startPoint
        return range
    }

    fun astolfoRainbow(delay: Int, offset: Int, index: Int): Color {
        var rainbowDelay = ceil((System.currentTimeMillis() + (delay * index).toLong()).toDouble()) / offset
        return Color.getHSBColor(if ((360.0.also { rainbowDelay %= it } / 360.0).toFloat()
                .toDouble() < 0.5) -(rainbowDelay / 360.0).toFloat() else (rainbowDelay / 360.0).toFloat(), 0.5f, 1.0f)
    }

    fun blend(color1: Color, color2: Color, ratio: Double): Color? {
        val r = ratio.toFloat()
        val ir = 1.0f - r
        val rgb1 = FloatArray(3)
        val rgb2 = FloatArray(3)
        color1.getColorComponents(rgb1)
        color2.getColorComponents(rgb2)
        var red = rgb1[0] * r + rgb2[0] * ir
        var green = rgb1[1] * r + rgb2[1] * ir
        var blue = rgb1[2] * r + rgb2[2] * ir
        if (red < 0.0f) {
            red = 0.0f
        } else if (red > 255.0f) {
            red = 255.0f
        }
        if (green < 0.0f) {
            green = 0.0f
        } else if (green > 255.0f) {
            green = 255.0f
        }
        if (blue < 0.0f) {
            blue = 0.0f
        } else if (blue > 255.0f) {
            blue = 255.0f
        }
        var color: Color? = null
        try {
            color = Color(red, green, blue)
        } catch (exp: IllegalArgumentException) {
            val nf = NumberFormat.getNumberInstance()
            println(nf.format(red.toDouble()) + "; " + nf.format(green.toDouble()) + "; " + nf.format(blue.toDouble()))
            exp.printStackTrace()
        }
        return color
    }
}
