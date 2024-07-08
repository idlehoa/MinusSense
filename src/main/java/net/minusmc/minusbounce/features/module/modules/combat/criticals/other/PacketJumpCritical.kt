package net.minusmc.minusbounce.features.module.modules.combat.criticals.other

import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class PacketJumpCritical : CriticalMode("PacketJump") {
	override fun onAttack(event: AttackEvent) {
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.41999998688698, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.7531999805212, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 1.00133597911214, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 1.16610926093821, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 1.24918707874468, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 1.1707870772188, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 1.0155550727022, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.78502770378924, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.4807108763317, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.10408037809304, mc.thePlayer.posZ, false))
		mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posZ, mc.thePlayer.posY + 0.0, mc.thePlayer.posZ, true))
	}
}
