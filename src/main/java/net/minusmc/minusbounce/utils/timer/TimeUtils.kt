/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.timer

import net.minecraft.util.MathHelper
import net.minusmc.minusbounce.utils.misc.RandomUtils.nextInt

class TimeUtils {
    private var lastMS = 0L
    private var previousTime: Long

    init {
        previousTime = -1L
    }

    private val currentMS: Long
        private get() = System.nanoTime() / 1000000L

    fun hasReached(milliseconds: Double): Boolean {
        return if ((currentMS - lastMS).toDouble() >= milliseconds) {
            true
        } else false
    }

    fun delay(milliSec: Float): Boolean {
        return if ((time - lastMS).toFloat() >= milliSec) {
            true
        } else false
    }

    var time: Long
        get() = System.nanoTime() / 1000000L
        set(time) {
            lastMS = time
        }

    fun hasTimeElapsed(time: Long): Boolean {
        return System.currentTimeMillis() - lastMS > time
    }

    fun check(milliseconds: Float): Boolean {
        return System.currentTimeMillis() - previousTime >= milliseconds
    }

    fun delay(milliseconds: Double): Boolean {
        return MathHelper.clamp_float((currentMS - lastMS).toFloat(), 0f, milliseconds.toFloat()) >= milliseconds
    }

    fun reset() {
        previousTime = System.currentTimeMillis()
        lastMS = currentMS
    }

    fun time(): Long {
        return System.nanoTime() / 1000000L - lastMS
    }

    fun delay(nextDelay: Long): Boolean {
        return System.currentTimeMillis() - lastMS >= nextDelay
    }

    fun delay(nextDelay: Float, reset: Boolean): Boolean {
        if (System.currentTimeMillis() - lastMS >= nextDelay) {
            if (reset) {
                reset()
            }
            return true
        }
        return false
    }

    companion object {
        fun randomDelay(minDelay: Int, maxDelay: Int): Long {
            return nextInt(minDelay, maxDelay).toLong()
        }

        fun randomClickDelay(minCPS: Int, maxCPS: Int): Long {
            return (Math.random() * (1000 / minCPS - 1000 / maxCPS + 1) + 1000 / maxCPS).toLong()
        }
    }
}
