/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.misc.sound

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.utils.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound : TipSoundPlayer
    var disableSound : TipSoundPlayer

    init {
        val enableSoundFile = File(MinusBounce.fileManager.soundsDir, "enable.wav")
        val disableSoundFile = File(MinusBounce.fileManager.soundsDir, "disable.wav")

        if (!enableSoundFile.exists())
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/minusbounce/sound/enable.wav")
        if (!disableSoundFile.exists())
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/minusbounce/sound/disable.wav")

        enableSound = TipSoundPlayer(enableSoundFile)
        disableSound = TipSoundPlayer(disableSoundFile)
    }
}
