package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class TPHopCritical : CriticalMode("TPHop") {
	override fun onAttack(event: AttackEvent) {
		val y = mc.thePlayer.posY
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, y + 0.02, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, y + 0.01, mc.thePlayer.posZ, false))
        mc.thePlayer.setPosition(mc.thePlayer.posX, y + 0.01, mc.thePlayer.posZ)
	}
}
