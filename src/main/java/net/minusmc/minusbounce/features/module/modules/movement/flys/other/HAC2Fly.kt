package net.minusmc.minusbounce.features.module.modules.movement.flys.other

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode

class HAC2Fly: FlyMode("HAC2", FlyType.OTHER) {
	override fun onUpdate() {
		mc.thePlayer.motionX *= 0.8
        mc.thePlayer.motionZ *= 0.8
	}
}