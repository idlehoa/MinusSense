/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    @Throws(IOException::class)
    fun unpackFile(file: File?, name: String?) {
        val fos = file?.let { FileOutputStream(it) }
        IOUtils.copy(FileUtils::class.java.getClassLoader().getResourceAsStream(name), fos)
        fos!!.close()
    }
}
