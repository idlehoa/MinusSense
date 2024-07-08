/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.file

import java.io.File
import java.io.IOException

abstract class FileConfig(val file: File) {
    @Throws(IOException::class)
    abstract fun loadConfig()

    @Throws(IOException::class)
    abstract fun saveConfig()

    @Throws(IOException::class)
    fun createConfig() {
        file.createNewFile()
    }

    fun hasConfig(): Boolean = file.exists()
}
