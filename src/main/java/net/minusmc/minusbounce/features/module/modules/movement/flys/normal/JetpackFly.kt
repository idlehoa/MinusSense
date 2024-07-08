package net.minusmc.minusbounce.features.module.modules.movement.flys.normal

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minecraft.util.EnumParticleTypes

class JetpackFly: FlyMode("Jetpack", FlyType.NORMAL) {
    override fun onUpdate() {
        if(mc.gameSettings.keyBindJump.isKeyDown) {
            mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.FLAME.particleID, mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ, -mc.thePlayer.motionX, -0.5, -mc.thePlayer.motionZ)
            mc.thePlayer.motionY += 0.15
            mc.thePlayer.motionX *= 1.1
            mc.thePlayer.motionZ *= 1.1
        }
    }
}