package net.minusmc.minusbounce.features.module.modules.movement.longjumps.redesky

import net.minusmc.minusbounce.features.module.modules.movement.longjumps.LongJumpMode

class RedeskyMakiLongJump : LongJumpMode("RedeskyMaki") {
	override fun onUpdate() {
		mc.thePlayer.jumpMovementFactor = 0.15f
        mc.thePlayer.motionY += 0.05
	}
}
