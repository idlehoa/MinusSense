/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.misc

import net.minusmc.minusbounce.features.module.*
import net.minusmc.minusbounce.value.*
import net.minusmc.minusbounce.event.*
import kotlin.math.floor

@ModuleInfo(name = "SpinBot", spacedName = "Spin Bot", description = "Client-sided spin bot like CS:GO hacks.", category = ModuleCategory.MISC)
class SpinBot : Module() {
    private val yawMode = ListValue("Yaw", arrayOf("Static", "Offset", "Random", "Jitter", "Spin", "None"), "Offset")
    val pitchMode = ListValue("Pitch", arrayOf("Static", "Offset", "Random", "Jitter", "None"), "Offset")
    private val staticOffsetyaw = FloatValue("Static/Offset-Yaw", 0F, -180F, 180F, "°")
    private val staticOffsetpitch = FloatValue("Static/Offset-Pitch", 0F, -90F, 90F, "°")
    private val yawJitterTimer = IntegerValue("YawJitterTimer", 1, 1, 40, " tick(s)")
    private val pitchJitterTimer = IntegerValue("PitchJitterTimer", 1, 1, 40, " tick(s)")
    private val yawSpinSpeed = FloatValue("YawSpinSpeed", 5F, -90F, 90F, "°")

    var pitch = 0F
    private var lastSpin = 0F
    private var yawTimer = 0
    private var pitchTimer = 0

    override fun onDisable() {
        pitch = -4.9531336E7f
        lastSpin = 0.0f
        yawTimer = 0
        pitchTimer = 0
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        mc.thePlayer ?: return
        
        if (!yawMode.get().equals("none", true)) {
            var yaw = 0F
            when (yawMode.get().lowercase()) {
                "static" -> yaw = staticOffsetyaw.get()
                "offset" -> yaw = mc.thePlayer.rotationYaw + staticOffsetyaw.get()
                "random" -> yaw = floor(Math.random() * 360.0 - 180.0).toFloat()
                "jitter" -> {
                    yaw = if (yawTimer++ % (yawJitterTimer.get() * 2) >= yawJitterTimer.get())
                        mc.thePlayer.rotationYaw
                    else
                        mc.thePlayer.rotationYaw - 180F
                }
                "spin" -> {
                    lastSpin += yawSpinSpeed.get()
                    yaw = lastSpin
                }
            }
            mc.thePlayer.renderYawOffset = yaw
            mc.thePlayer.rotationYawHead = yaw
            lastSpin = yaw
        }
        when (pitchMode.get().lowercase()) {
            "static" -> pitch = staticOffsetpitch.get()
            "offset" -> pitch = mc.thePlayer.rotationPitch + staticOffsetpitch.get()
            "random" -> pitch = floor(Math.random() * 180.0 - 90.0).toFloat()
            "jitter" -> {
                pitch = if (pitchTimer++ % (pitchJitterTimer.get() * 2) >= pitchJitterTimer.get())
                    90F
                else
                    -90F
            }
        }
    }

    override val tag: String
        get() = "${yawMode.get()}, ${pitchMode.get()}"
}
