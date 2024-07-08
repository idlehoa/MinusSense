package net.minusmc.minusbounce.features.module.modules.player.nofalls.other

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minusmc.minusbounce.utils.timer.TickTimer

class SpartanNoFall: NoFallMode("Spartan") {
	private val spartanTimer = TickTimer()

	override fun onUpdate() {
		spartanTimer.update()

        if (mc.thePlayer.fallDistance > 1.5F && spartanTimer.hasTimePassed(10)) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 10, mc.thePlayer.posZ, true))
            spartanTimer.reset()
        }
	}
}