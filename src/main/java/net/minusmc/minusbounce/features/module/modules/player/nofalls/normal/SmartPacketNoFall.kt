package net.minusmc.minusbounce.features.module.modules.player.nofalls.normal

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer

class SmartPacketNoFall: NoFallMode("SmartPacket") {
	override fun onUpdate() {
		if (mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3f) {
            mc.netHandler.addToSendQueue(C03PacketPlayer(true))
            mc.thePlayer.fallDistance = 0f
        }
	}
}