package net.minusmc.minusbounce.features.module.modules.combat.criticals.other


import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minusmc.minusbounce.event.AttackEvent

class VisualCritical : CriticalMode("Visual") {
	override fun onAttack(event: AttackEvent) {
        mc.thePlayer.onCriticalHit(criticals.entity)
	}
}
