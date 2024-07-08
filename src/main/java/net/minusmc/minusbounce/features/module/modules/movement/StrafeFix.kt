package net.minusmc.minusbounce.features.module.modules.movement

import net.minusmc.minusbounce.event.StrafeEvent
import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.utils.RotationUtils
import net.minecraft.util.MathHelper
import net.minusmc.minusbounce.utils.MouseUtils
import net.minusmc.minusbounce.value.FloatValue
@ModuleInfo(name = "StrafeFix", spacedName = "Auto Tool", description = "strafefix", category = ModuleCategory.PLAYER)
class StrafeFix : Module() {

    private val silentFixVaule = BoolValue("Silent", true)


    private var silentFix = false
    private var doFix = false
    private var isOverwrited = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!isOverwrited) {
            silentFix = silentFixVaule.get()
            doFix = true
        }
    }

    override fun onDisable() {
        doFix = false
    }

    fun applyForceStrafe(isSilent: Boolean, runStrafeFix: Boolean) {
        silentFix = isSilent
        doFix = runStrafeFix
        isOverwrited = true
    }

    fun updateOverwrite() {
        isOverwrited = false
        doFix = state
        silentFix = silentFixVaule.get()
    }

    fun runStrafeFixLoop(isSilent: Boolean, event: StrafeEvent) {
        if (event.isCancelled) {
            return
        }
        val (yaw) = RotationUtils.targetRotation ?: return
        var strafe = event.strafe
        var forward = event.forward
        var friction = event.friction
        var factor = strafe * strafe + forward * forward

        var angleDiff = ((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - yaw - 22.5f - 135.0f) + 180.0).toDouble() / (45.0).toDouble()).toInt()
        //alert("Diff: " + angleDiff + " friction: " + friction + " factor: " + factor);
        var calcYaw = if(isSilent) { yaw + 45.0f * angleDiff.toFloat() } else yaw

        var calcMoveDir = Math.max(Math.abs(strafe), Math.abs(forward)).toFloat()
        calcMoveDir = calcMoveDir * calcMoveDir
        var calcMultiplier = MathHelper.sqrt_float(calcMoveDir / Math.min(1.0f, calcMoveDir * 2.0f))

        if (isSilent) {
            when (angleDiff) {
                1, 3, 5, 7, 9 -> {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier
                    }
                }
            }
        }
        if (factor >= 1.0E-4F) {
            factor = MathHelper.sqrt_float(factor)

            if (factor < 1.0F) {
                factor = 1.0F
            }

            factor = friction / factor
            strafe *= factor
            forward *= factor

            val yawSin = MathHelper.sin((calcYaw * Math.PI / 180F).toFloat())
            val yawCos = MathHelper.cos((calcYaw * Math.PI / 180F).toFloat())

            mc.thePlayer.motionX += strafe * yawCos - forward * yawSin
            mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin
        }
        event.cancelEvent()
    }
}