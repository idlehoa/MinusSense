package net.minusmc.minusbounce.features.module.modules.movement.longjumps.aac

import net.minusmc.minusbounce.features.module.modules.movement.longjumps.LongJumpMode
import net.minusmc.minusbounce.utils.MovementUtils

class AACv1LongJump : LongJumpMode("AACv1") {
	override fun onUpdate() {
		mc.thePlayer.motionY += 0.05999
    	MovementUtils.strafe(MovementUtils.speed * 1.08F)
	}
}
