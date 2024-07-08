package net.minusmc.minusbounce.features.module.modules.player.nofalls.other

import net.minecraft.network.play.client.C03PacketPlayer
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode

class VerusNoFall: NoFallMode("Verus") {
	private var needSpoof = false

	override fun onEnable() {
		needSpoof = false
	}

	override fun onDisable() {
		needSpoof = false
	}

	override fun onUpdate() {
		if (mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3F) {
            mc.thePlayer.motionY = 0.0
            mc.thePlayer.motionX *= 0.5
            mc.thePlayer.motionX *= 0.5
            mc.thePlayer.fallDistance = 0F
            needSpoof = true
        }
	}

	override fun onPacket(event: PacketEvent) {
		val packet = event.packet
		if (packet is C03PacketPlayer && needSpoof) {
			packet.onGround = true
            needSpoof = false
		}
	}
}