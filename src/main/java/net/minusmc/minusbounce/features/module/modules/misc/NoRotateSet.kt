/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.misc

import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.utils.RotationUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook

@ModuleInfo(name = "NoRotateSet", spacedName = "No Rotate Set", description = "Prevents the server from rotating your head.", category = ModuleCategory.MISC)
class NoRotateSet : Module() {

    private val confirmValue = BoolValue("Confirm", true)
    private val illegalRotationValue = BoolValue("ConfirmIllegalRotation", false)
    private val noZeroValue = BoolValue("NoZero", false)

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        mc.thePlayer ?: return

        if (packet is S08PacketPlayerPosLook) {
            if (noZeroValue.get() && packet.getYaw() == 0F && packet.getPitch() == 0F)
                return

            if (illegalRotationValue.get() || packet.getPitch() <= 90 && packet.getPitch() >= -90 &&
                    RotationUtils.serverRotation != null && packet.getYaw() != RotationUtils.serverRotation!!.yaw &&
                    packet.getPitch() != RotationUtils.serverRotation!!.pitch) {

                if (confirmValue.get())
                    mc.netHandler.addToSendQueue(C05PacketPlayerLook(packet.getYaw(), packet.getPitch(), mc.thePlayer.onGround))
            }

            packet.yaw = mc.thePlayer.rotationYaw
            packet.pitch = mc.thePlayer.rotationPitch
        }
    }

}
