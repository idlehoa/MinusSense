package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent

class MotionJumpCritical : CriticalMode("MotionJump") {
	override fun onAttack(event: AttackEvent) {
		mc.thePlayer.motionY = 0.41999998688698
	}
}
