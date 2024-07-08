/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import javax.vecmath.Vector3d
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

object PathUtils : MinecraftInstance() {
    fun findTeleportPath(current: EntityLivingBase, target: EntityLivingBase, dashDistance: Double): ArrayList<Vec3> {
        val curX = current.posX
        val curY = current.posY
        val curZ = current.posZ
        val tpX = target.posX
        val tpY = target.posY
        val tpZ = target.posZ
        var topFrom = Vec3(curX, curY, curZ)
        val to = Vec3(tpX, tpY, tpZ)
        if (!canPassThrow(BlockPos(topFrom))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0)
        }
        val pathfinder = AStarCustomPathFinder(topFrom, to)
        pathfinder.compute()
        var i = 0
        var lastLoc: Vec3? = null
        var lastDashLoc: Vec3? = null
        val path = ArrayList<Vec3>()
        val pathFinderPath = pathfinder.path
        for (pathElm in pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5))
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5))
                lastDashLoc = pathElm
            } else {
                var canContinue = true
                if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance * dashDistance) {
                    canContinue = false
                } else {
                    val smallX = min(lastDashLoc!!.xCoord, pathElm.xCoord)
                    val smallY = min(lastDashLoc.yCoord, pathElm.yCoord)
                    val smallZ = min(lastDashLoc.zCoord, pathElm.zCoord)
                    val bigX = max(lastDashLoc.xCoord, pathElm.xCoord)
                    val bigY = max(lastDashLoc.yCoord, pathElm.yCoord)
                    val bigZ = max(lastDashLoc.zCoord, pathElm.zCoord)
                    var x = smallX.toInt()
                    cordsLoop@ while (x <= bigX) {
                        var y = smallY.toInt()
                        while (y <= bigY) {
                            var z = smallZ.toInt()
                            while (z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false
                                    break@cordsLoop
                                }
                                z++
                            }
                            y++
                        }
                        x++
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc!!.addVector(0.5, 0.0, 0.5))
                    lastDashLoc = lastLoc
                }
            }
            lastLoc = pathElm
            i++
        }
        return path
    }

    private fun canPassThrow(pos: BlockPos): Boolean {
        val block = Minecraft.getMinecraft().theWorld.getBlockState(BlockPos(pos.x, pos.y, pos.z)).block
        return block.material === Material.air || block.material === Material.plants || block.material === Material.vine || block === Blocks.ladder || block === Blocks.water || block === Blocks.flowing_water || block === Blocks.wall_sign || block === Blocks.standing_sign
    }

    fun findBlinkPath(tpX: Double, tpY: Double, tpZ: Double): List<Vector3d> {
        val positions: MutableList<Vector3d> = ArrayList()
        var curX = mc.thePlayer.posX
        var curY = mc.thePlayer.posY
        var curZ = mc.thePlayer.posZ
        var distance = abs(curX - tpX) + abs(curY - tpY) + abs(curZ - tpZ)
        var count = 0
        while (distance > 0.0) {
            distance = abs(curX - tpX) + abs(curY - tpY) + abs(curZ - tpZ)
            val diffX = curX - tpX
            val diffY = curY - tpY
            val diffZ = curZ - tpZ
            val offset = if (count and 1 == 0) 0.4 else 0.1
            val minX = min(abs(diffX), offset)
            if (diffX < 0.0) curX += minX
            if (diffX > 0.0) curX -= minX
            val minY = min(abs(diffY), 0.25)
            if (diffY < 0.0) curY += minY
            if (diffY > 0.0) curY -= minY
            val minZ = min(abs(diffZ), offset)
            if (diffZ < 0.0) curZ += minZ
            if (diffZ > 0.0) curZ -= minZ
            positions.add(Vector3d(curX, curY, curZ))
            count++
        }
        return positions
    }

    fun findPath(tpX: Double, tpY: Double, tpZ: Double, offset: Double): List<Vector3d> {
        val positions: MutableList<Vector3d> = ArrayList()
        val steps = ceil(getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ) / offset)
        val dX = tpX - mc.thePlayer.posX
        val dY = tpY - mc.thePlayer.posY
        val dZ = tpZ - mc.thePlayer.posZ
        var d = 1.0
        while (d <= steps) {
            positions.add(
                Vector3d(
                    mc.thePlayer.posX + dX * d / steps,
                    mc.thePlayer.posY + dY * d / steps,
                    mc.thePlayer.posZ + dZ * d / steps
                )
            )
            ++d
        }
        return positions
    }

    private fun getDistance(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
        val xDiff = x1 - x2
        val yDiff = y1 - y2
        val zDiff = z1 - z2
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff).toDouble()
    }
}
