/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.misc

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minusmc.minusbounce.utils.MinecraftInstance

class FallingPlayer(
    private var x: Double,
    private var y: Double,
    private var z: Double,
    private var motionX: Double,
    private var motionY: Double,
    private var motionZ: Double,
    private val yaw: Float,
    private var strafe: Float,
    private var forward: Float
) :
    MinecraftInstance() {
    constructor(player: EntityPlayer) : this(
        player.posX,
        player.posY,
        player.posZ,
        player.motionX,
        player.motionY,
        player.motionZ,
        player.rotationYaw,
        player.moveStrafing,
        player.moveForward
    )

    private fun calculateForTick() {
        strafe *= 0.98f
        forward *= 0.98f
        var v = strafe * strafe + forward * forward
        if (v >= 0.0001f) {
            v = MathHelper.sqrt_float(v)
            if (v < 1.0f) {
                v = 1.0f
            }
            v = mc.thePlayer.jumpMovementFactor / v
            strafe = strafe * v
            forward = forward * v
            val f1 = MathHelper.sin(yaw * Math.PI.toFloat() / 180.0f)
            val f2 = MathHelper.cos(yaw * Math.PI.toFloat() / 180.0f)
            motionX += (strafe * f2 - forward * f1).toDouble()
            motionZ += (forward * f2 + strafe * f1).toDouble()
        }
        motionY -= 0.08
        motionX *= 0.91
        motionY *= 0.9800000190734863
        motionY *= 0.91
        motionZ *= 0.91
        x += motionX
        y += motionY
        z += motionZ
    }

    fun findCollision(ticks: Int): CollisionResult? {
        for (i in 0 until ticks) {
            val start = Vec3(x, y, z)
            calculateForTick()
            val end = Vec3(x, y, z)
            var raytracedBlock: BlockPos?
            val w = mc.thePlayer.width / 2f
            if (rayTrace(start, end).also { raytracedBlock = it } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(w.toDouble(), 0.0, w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(-w.toDouble(), 0.0, w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(w.toDouble(), 0.0, -w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(-w.toDouble(), 0.0, -w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(w.toDouble(), 0.0, (w / 2f).toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector(-w.toDouble(), 0.0, (w / 2f).toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector((w / 2f).toDouble(), 0.0, w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
            if (rayTrace(start.addVector((w / 2f).toDouble(), 0.0, -w.toDouble()), end).also {
                    raytracedBlock = it
                } != null) return CollisionResult(raytracedBlock, i)
        }
        return null
    }

    private fun rayTrace(start: Vec3, end: Vec3): BlockPos? {
        val result = mc.theWorld.rayTraceBlocks(start, end, true)
        return if (result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && result.sideHit == EnumFacing.UP) {
            result.blockPos
        } else null
    }

    class CollisionResult(val pos: BlockPos?, val tick: Int)
}
