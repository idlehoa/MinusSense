package net.minusmc.minusbounce.features.module.modules.movement.speeds.vulcan

import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedMode
import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedType
import net.minusmc.minusbounce.utils.MovementUtils

class VulcanYPort2Speed: SpeedMode("VulcanYPort2", SpeedType.VULCAN) {
	
    private var wasTimer = false
    private var portSwitcher = 0
  
    override fun onEnable() {
        wasTimer = true
        mc.timer.timerSpeed = 1.0f
        portSwitcher = 0
    }
    
    override fun onDisable() {
        wasTimer = false
        mc.timer.timerSpeed = 1.0f
        portSwitcher = 0
    }

    override fun onUpdate() {
        if (wasTimer) {
            mc.timer.timerSpeed = 1.0f
            wasTimer = false
        }
        if (portSwitcher > 1) {
            mc.thePlayer.motionY = -0.2784
            mc.timer.timerSpeed = 1.5f
            wasTimer = true
            if(portSwitcher > 1) {
                portSwitcher = 0
            }
        }
        if (mc.thePlayer.onGround && MovementUtils.isMoving) {
            mc.thePlayer.jump()
            MovementUtils.strafe()
            if(portSwitcher >= 1) {
                mc.thePlayer.motionY = 0.2
                mc.timer.timerSpeed = 1.5f
            }
            portSwitcher++
        }else if(MovementUtils.speed < 0.225){
            MovementUtils.strafe(0.225f)
        }
    }
}