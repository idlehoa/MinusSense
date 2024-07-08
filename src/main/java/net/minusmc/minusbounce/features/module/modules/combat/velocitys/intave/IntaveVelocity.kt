package net.minusmc.minusbounce.features.module.modules.combat.velocitys.intave

import net.minusmc.minusbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minusmc.minusbounce.event.PacketEvent
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minusmc.minusbounce.utils.timer.MSTimer

class IntaveVelocity : VelocityMode("Intave") {
    private var velocityInput = false
    private val velocityTimer = MSTimer()

    override fun onUpdate() {
        if (mc.thePlayer.hurtTime > 1 && velocityInput) {
            mc.thePlayer.motionX *= 0.62
            mc.thePlayer.motionZ *= 0.62
        }
        if (velocityInput && (mc.thePlayer.hurtTime < 7 || mc.thePlayer.onGround) && velocityTimer.hasTimePassed(60)) 
            velocityInput = false
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S12PacketEntityVelocity) velocityInput = true
    }
}