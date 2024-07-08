/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook

class PosLookInstance {
    private var x = 0.0
    private var y = 0.0
    private var z = 0.0
    private var yaw = 0f
    private var pitch = 0f

    constructor()
    constructor(a: Double, b: Double, c: Double, d: Float, e: Float) {
        x = a
        y = b
        z = c
        yaw = d
        pitch = e
    }

    fun reset() {
        set(0.0, 0.0, 0.0, 0f, 0f)
    }

    fun set(packet: S08PacketPlayerPosLook) {
        set(packet.x, packet.y, packet.z, packet.yaw, packet.pitch)
    }

    operator fun set(a: Double, b: Double, c: Double, d: Float, e: Float) {
        x = a
        y = b
        z = c
        yaw = d
        pitch = e
    }

    fun equalFlag(packet: C06PacketPlayerPosLook?): Boolean {
        return packet != null && !packet.onGround && packet.x == x && packet.y == y && packet.z == z && packet.yaw == yaw && packet.pitch == pitch
    }
}
