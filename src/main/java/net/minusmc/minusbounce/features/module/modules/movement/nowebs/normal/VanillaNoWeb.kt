package net.minusmc.minusbounce.features.module.modules.movement.nowebs.normal

import net.minusmc.minusbounce.features.module.modules.movement.nowebs.NoWebMode

class VanillaNoWeb: NoWebMode("Vanilla") {
	override fun onUpdate() {
		mc.thePlayer.isInWeb = false
    }
}