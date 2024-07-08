package net.minusmc.minusbounce.features.module.modules.player.nofalls.other

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.utils.PacketUtils

class MedusaNoFall: NoFallMode("Medusa") {
    override fun onPacket(event: PacketEvent) {
        if (mc.thePlayer.fallDistance > 2.3F) {
            event.cancelEvent()
            PacketUtils.sendPacketNoEvent(C03PacketPlayer(true))
            mc.thePlayer.fallDistance = 0F
        }
    }
}