package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class Packet2Critical : CriticalMode("Packet2") {
	override fun onAttack(event: AttackEvent) {
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.09858, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.04114514, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.025, mc.thePlayer.posZ, false))
	}
}
