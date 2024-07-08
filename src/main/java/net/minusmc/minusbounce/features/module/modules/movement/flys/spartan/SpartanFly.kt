package net.minusmc.minusbounce.features.module.modules.movement.flys.spartan

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.utils.timer.TickTimer


class SpartanFly: FlyMode("Spartan", FlyType.SPARTAN) {

	private val spartanTimer = TickTimer()

	override fun onUpdate() {
		mc.thePlayer.motionY = 0.0
        spartanTimer.update()
        if(spartanTimer.hasTimePassed(12)) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 8, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 8, mc.thePlayer.posZ, true))
            spartanTimer.reset()
        }
	}
}