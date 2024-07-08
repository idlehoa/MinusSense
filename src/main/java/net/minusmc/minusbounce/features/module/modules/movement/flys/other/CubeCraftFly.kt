package net.minusmc.minusbounce.features.module.modules.movement.flys.other

import net.minusmc.minusbounce.event.MoveEvent
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.utils.timer.TickTimer
import kotlin.math.cos
import kotlin.math.sin

class CubeCraftFly: FlyMode("CubeCraft", FlyType.OTHER) {
	private val tickTimer = TickTimer()

    override fun onUpdate() {
        mc.timer.timerSpeed = 0.6f
        tickTimer.update()
    }

    override fun onMove(event: MoveEvent) {
        val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
        if (tickTimer.hasTimePassed(2)) {
            event.x = -sin(yaw) * 2.4
            event.z = cos(yaw) * 2.4
            tickTimer.reset()
        } else {
            event.x = -sin(yaw) * 0.2
            event.z = cos(yaw) * 0.2
        }
    }
}