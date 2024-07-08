package net.minusmc.minusbounce.features.module.modules.movement.flys.normal

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook

class FlagFly: FlyMode("Flag", FlyType.NORMAL) {
    override fun onUpdate() {
        val y1 = if (mc.gameSettings.keyBindJump.isKeyDown) 1.5624 else 0.00000001
        val y2 = if (mc.gameSettings.keyBindSneak.isKeyDown) 0.0624 else 0.00000002
        mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY + y1 - y2, mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true))
        mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY - 6969, mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true))
        mc.thePlayer.setPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 11, mc.thePlayer.posY, mc.thePlayer.posZ + mc.thePlayer.motionZ * 11)
        mc.thePlayer.motionY = 0.0
    }
}
    
