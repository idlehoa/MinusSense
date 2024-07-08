/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.file.configs

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.file.FileConfig
import net.minusmc.minusbounce.ui.client.hud.Config
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class HudConfig(file: File?) : FileConfig(file!!) {
    override fun loadConfig() {
        MinusBounce.hud.clearElements()
        MinusBounce.hud = Config(FileUtils.readFileToString(file)).toHUD()
    }

    override fun saveConfig() {
        val printWriter = PrintWriter(FileWriter(file))
        printWriter.println(Config(MinusBounce.hud).toJson())
        printWriter.close()
    }
}
