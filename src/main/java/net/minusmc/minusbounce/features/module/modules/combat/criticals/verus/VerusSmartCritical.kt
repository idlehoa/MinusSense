package net.minusmc.minusbounce.features.module.modules.combat.criticals.verus


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class VerusSmartCritical : CriticalMode("VerusSmart") {
	private var counter = 0

	override fun onEnable() {
		counter = 0
	}

	override fun onAttack(event: AttackEvent) {
		val y = mc.thePlayer.posY
		counter++
        if (counter == 1) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, y + 0.001, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, false))
        }
        if (counter >= 5)
            counter = 0
	}
}
