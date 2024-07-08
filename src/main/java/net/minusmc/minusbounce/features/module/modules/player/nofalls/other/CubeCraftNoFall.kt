package net.minusmc.minusbounce.features.module.modules.player.nofalls.other

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer

class CubeCraftNoFall: NoFallMode("CubeCraft") {
	override fun onUpdate() {
		if (mc.thePlayer.fallDistance > 2F) {
            mc.thePlayer.onGround = true
            mc.netHandler.addToSendQueue(C03PacketPlayer(true))
        }
	}
}