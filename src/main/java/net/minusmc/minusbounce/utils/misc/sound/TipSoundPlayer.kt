/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.misc.sound

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl

class TipSoundPlayer(private val file: File) {
    fun asyncPlay(volume: Float) {
        val thread: Thread = object : Thread() {
            override fun run() {
                playSound(volume / 100f)
            }
        }
        thread.start()
    }

    fun playSound(volume: Float) {
        try {
            val audioInputStream = AudioSystem.getAudioInputStream(file)
            val clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            val controller = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            val range = controller.maximum - controller.minimum
            val value = range * volume + controller.minimum
            controller.setValue(value)
            clip.start()
        } catch (ex: Exception) {
            println("Error with playing sound.")
            ex.printStackTrace()
        }
    }
}
