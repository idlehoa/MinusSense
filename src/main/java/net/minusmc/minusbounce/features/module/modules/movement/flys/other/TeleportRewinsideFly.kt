package net.minusmc.minusbounce.features.module.modules.movement.flys.other

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minecraft.util.Vec3
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

import kotlin.math.sin
import kotlin.math.cos

class TeleportRewinsideFly: FlyMode("TeleportRewinside", FlyType.OTHER) {
    override fun onUpdate() {
        val vectorStart = Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
        val yaw = Math.toRadians(-mc.thePlayer.rotationYaw.toDouble())
        val pitch = Math.toRadians(-mc.thePlayer.rotationPitch.toDouble())
        val length = 9.9
        val vectorEnd = Vec3(sin(yaw) * cos(pitch) * length + vectorStart.xCoord, sin(pitch) * length + vectorStart.yCoord, cos(yaw) * cos(pitch) * length + vectorStart.zCoord)
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(vectorEnd.xCoord, mc.thePlayer.posY + 2, vectorEnd.zCoord, true))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(vectorStart.xCoord, mc.thePlayer.posY + 2, vectorStart.zCoord, true))
        mc.thePlayer.motionY = 0.0
    }

}