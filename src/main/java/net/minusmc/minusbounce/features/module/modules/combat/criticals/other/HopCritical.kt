package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent

class HopCritical : CriticalMode("Hop") {
	override fun onAttack(event: AttackEvent) {
		mc.thePlayer.motionY = 0.1
        mc.thePlayer.fallDistance = 0.1f
        mc.thePlayer.onGround = false
	}
}
