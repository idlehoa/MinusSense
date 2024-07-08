/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.special

import net.minusmc.minusbounce.utils.MinecraftInstance

object UUIDSpoofer : MinecraftInstance() {
    var spoofId: String? = null

    @JvmStatic
    fun getUUID(): String = (if (spoofId == null) mc.session.playerID else spoofId!!).replace("-", "")
}
