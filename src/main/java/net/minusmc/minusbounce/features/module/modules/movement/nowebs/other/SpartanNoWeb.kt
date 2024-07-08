package net.minusmc.minusbounce.features.module.modules.movement.nowebs.other

import net.minusmc.minusbounce.features.module.modules.movement.nowebs.NoWebMode
import net.minusmc.minusbounce.utils.MovementUtils

class SpartanNoWeb: NoWebMode("Spartan") {
    private var usedTimer = false
    
    override fun onUpdate() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }

        MovementUtils.strafe(0.27F)
        mc.timer.timerSpeed = 3.7F
        if (!mc.gameSettings.keyBindSneak.isKeyDown) mc.thePlayer.motionY = 0.0
        if (mc.thePlayer.ticksExisted % 2 == 0) mc.timer.timerSpeed = 1.7F
        if (mc.thePlayer.ticksExisted % 40 == 0) mc.timer.timerSpeed = 3F
        usedTimer = true
    }
}