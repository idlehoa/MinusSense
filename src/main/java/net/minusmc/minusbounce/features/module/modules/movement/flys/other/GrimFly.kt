package net.minusmc.minusbounce.features.module.modules.movement.flys.other

import net.minusmc.minusbounce.event.PacketEvent
import net.minecraft.network.play.client.C03PacketPlayer
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minecraft.network.play.server.S27PacketExplosion

class GrimFly: FlyMode("Grim", FlyType.OTHER) {

    private var velocityPacket = false

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (velocityPacket && packet is C03PacketPlayer) {
            packet.x = mc.thePlayer.posX + 1000.0
            packet.z = mc.thePlayer.posZ + 1000.0
        }
        if (packet is S27PacketExplosion) velocityPacket = true
    }
}