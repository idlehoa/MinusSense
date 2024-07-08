package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.PacketEvent
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C07PacketPlayerDigging

class RedeskyCritical : CriticalMode("Redesky") {
	private var canCrits = true

	override fun onEnable() {
        canCrits = true
    }
	
	override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            if(mc.thePlayer.onGround && canCrits) {
                packet.y += 0.000001
                packet.onGround = false
            }
            if (mc.theWorld.getCollidingBoundingBoxes(
                    mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(
                            0.0, (mc.thePlayer.motionY - 0.08) * 0.98, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                packet.onGround = true
            }
        }
        if(packet is C07PacketPlayerDigging) {
            if(packet.status == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                canCrits = false
            } else if(packet.status == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK || packet.status == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                canCrits = true
            }
        }
	}
}
