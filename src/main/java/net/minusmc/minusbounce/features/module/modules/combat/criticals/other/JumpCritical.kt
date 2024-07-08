package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent

class JumpCritical : CriticalMode("Jump") {
	override fun onAttack(event: AttackEvent) {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.motionY = criticals.jumpHeightValue.get().toDouble()
		} else {
			mc.thePlayer.motionY -= criticals.downYValue.get()
		}
	}
}
