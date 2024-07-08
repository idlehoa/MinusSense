package net.minusmc.minusbounce.features.module.modules.player.nofalls.normal

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer
import net.minusmc.minusbounce.event.PacketEvent

class VanillaNoFall: NoFallMode("Vanilla") {
	override fun onPacket(event: PacketEvent) {
		val packet = event.packet
		if (packet is C03PacketPlayer && mc.thePlayer.fallDistance > 2.5f) packet.onGround = true
	}
}