package net.minusmc.minusbounce.features.module.modules.player.nofalls.other

import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minusmc.minusbounce.utils.misc.NewFallingPlayer
import net.minusmc.minusbounce.value.IntegerValue
import java.util.Timer
import kotlin.concurrent.schedule

class PhaseNoFall: NoFallMode("Phase") {
	private val phaseOffsetValue = IntegerValue("PhaseOffset", 1, 0, 5)

	override fun onUpdate() {
		if (mc.thePlayer.fallDistance > 3 + phaseOffsetValue.get()) {
            val fallPos = NewFallingPlayer(mc.thePlayer).findCollision(5) ?: return
            if (fallPos.y - mc.thePlayer.motionY / 20.0 < mc.thePlayer.posY) {
                mc.timer.timerSpeed = 0.05f
                Timer().schedule(100L) {
                    try {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(fallPos.x.toDouble(), fallPos.y.toDouble(), fallPos.z.toDouble(), true))
                        mc.thePlayer.setPosition(fallPos.x.toDouble(), fallPos.y.toDouble(), fallPos.z.toDouble())
                    } catch (_: Exception) {
                    }
                    mc.timer.timerSpeed = 1f
                }
            }
        }
	}
}