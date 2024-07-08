package net.minusmc.minusbounce.features.module.modules.movement.flys.other

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode

class HawkEyeFly: FlyMode("HawkEye", FlyType.OTHER) {
	override fun onUpdate() {
		mc.thePlayer.motionY = if (mc.thePlayer.motionY <= -0.42) 0.42 else -0.42
	}
}