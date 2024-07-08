/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.font

import org.lwjgl.opengl.GL11

data class CachedFont(val displayList: Int, var lastUsage: Long, var deleted: Boolean = false) {
    protected fun finalize() {
        if (!deleted)
            GL11.glDeleteLists(displayList, 1)
    }
}
