package net.minusmc.minusbounce.features.module.modules.movement.flys.normal

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode

class DamageFly: FlyMode("Damage", FlyType.NORMAL) {
    override fun handleUpdate() {}
    
    override fun onUpdate() {
        mc.thePlayer.capabilities.isFlying = false
        if (mc.thePlayer.hurtTime <= 0) return
    }

    override fun onRender3D() {}
}