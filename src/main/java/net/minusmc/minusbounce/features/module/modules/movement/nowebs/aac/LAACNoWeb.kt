package net.minusmc.minusbounce.features.module.modules.movement.nowebs.aac

import net.minusmc.minusbounce.features.module.modules.movement.nowebs.NoWebMode

class LAACNoWeb: NoWebMode("LAAC") {
	override fun onUpdate() {
		mc.thePlayer.jumpMovementFactor = if (mc.thePlayer.movementInput.moveStrafe != 0f) 1.0f else 1.21f

        if (!mc.gameSettings.keyBindSneak.isKeyDown)
            mc.thePlayer.motionY = 0.0

        if (mc.thePlayer.onGround)
            mc.thePlayer.jump()
    }
}