/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import net.minecraft.block.*
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minusmc.minusbounce.utils.block.BlockUtils.floorVec3
import net.minusmc.minusbounce.utils.block.BlockUtils.getBlock

class AStarCustomPathFinder(startVec3: Vec3, endVec3: Vec3) {
    private val startVec3: Vec3
    private val endVec3: Vec3
    var path = ArrayList<Vec3>()
        private set
    private val hubs = ArrayList<Hub>()
    private val hubsToWork = ArrayList<Hub>()
    private val minDistanceSquared = 9.0
    private val nearest = true

    init {
        this.startVec3 = floorVec3(startVec3.addVector(0.0, 0.0, 0.0))
        this.endVec3 = floorVec3(endVec3.addVector(0.0, 0.0, 0.0))
    }

    @JvmOverloads
    fun compute(loops: Int = 1000, depth: Int = 4) {
        path.clear()
        hubsToWork.clear()
        val initPath = ArrayList<Vec3>()
        initPath.add(startVec3)
        hubsToWork.add(Hub(startVec3, null, initPath, startVec3.squareDistanceTo(endVec3), 0.0, 0.0))
        search@ for (i in 0 until loops) {
            hubsToWork.sortWith(CompareHub())
            var j = 0
            if (hubsToWork.size == 0) {
                break
            }
            for (hub in ArrayList<Hub>(hubsToWork)) {
                j++
                if (j > depth) {
                    break
                } else {
                    hubsToWork.remove(hub)
                    hubs.add(hub)
                    for (direction in flatCardinalDirections) {
                        val loc = floorVec3(hub.loc!!.add(direction))
                        if (checkPositionValidity(loc, false)) {
                            if (addHub(hub, loc, 0.0)) {
                                break@search
                            }
                        }
                    }
                    val loc1 = floorVec3(hub.loc!!.addVector(0.0, 1.0, 0.0))
                    if (checkPositionValidity(loc1, false)) {
                        if (addHub(hub, loc1, 0.0)) {
                            break@search
                        }
                    }
                    val loc2 = floorVec3(hub.loc!!.addVector(0.0, -1.0, 0.0))
                    if (checkPositionValidity(loc2, false)) {
                        if (addHub(hub, loc2, 0.0)) {
                            break@search
                        }
                    }
                }
            }
        }
        if (nearest) {
            hubs.sortWith(CompareHub())
            path = hubs[0].path
        }
    }

    fun isHubExisting(loc: Vec3): Hub? {
        for (hub in hubs) {
            if (hub.loc!!.xCoord == loc.xCoord && hub.loc!!.yCoord == loc.yCoord && hub.loc!!.zCoord == loc.zCoord) {
                return hub
            }
        }
        for (hub in hubsToWork) {
            if (hub.loc!!.xCoord == loc.xCoord && hub.loc!!.yCoord == loc.yCoord && hub.loc!!.zCoord == loc.zCoord) {
                return hub
            }
        }
        return null
    }

    fun addHub(parent: Hub?, loc: Vec3, cost: Double): Boolean {
        val existingHub = isHubExisting(loc)
        var totalCost = cost
        if (parent != null) {
            totalCost += parent.totalCost
        }
        if (existingHub == null) {
            if (loc.xCoord == endVec3.xCoord && loc.yCoord == endVec3.yCoord && loc.zCoord == endVec3.zCoord || minDistanceSquared != 0.0 && loc.squareDistanceTo(
                    endVec3
                ) <= minDistanceSquared
            ) {
                path.clear()
                path = parent!!.path
                path.add(loc)
                return true
            } else {
                val path = ArrayList(parent!!.path)
                path.add(loc)
                hubsToWork.add(Hub(loc, parent, path, loc.squareDistanceTo(endVec3), cost, totalCost))
            }
        } else if (existingHub.cost > cost) {
            val path = ArrayList(parent!!.path)
            path.add(loc)
            existingHub.loc = loc
            existingHub.parent = parent
            existingHub.path = path
            existingHub.squareDistanceToFromTarget = loc.squareDistanceTo(endVec3)
            existingHub.cost = cost
            existingHub.totalCost = totalCost
        }
        return false
    }

    inner class Hub(
        loc: Vec3?,
        parent: Hub?,
        path: ArrayList<Vec3>,
        squareDistanceToFromTarget: Double,
        cost: Double,
        totalCost: Double
    ) {
        var loc: Vec3? = null
        var parent: Hub? = null
        var path: ArrayList<Vec3>
        var squareDistanceToFromTarget: Double
        var cost: Double
        var totalCost: Double

        init {
            this.loc = loc
            this.parent = parent
            this.path = path
            this.squareDistanceToFromTarget = squareDistanceToFromTarget
            this.cost = cost
            this.totalCost = totalCost
        }
    }

    inner class CompareHub : Comparator<Hub> {
        override fun compare(o1: Hub, o2: Hub): Int {
            return (o1.squareDistanceToFromTarget + o1.totalCost - (o2.squareDistanceToFromTarget + o2.totalCost)).toInt()
        }
    }

    companion object {
        private val flatCardinalDirections = arrayOf(
            Vec3(1.0, 0.0, 0.0),
            Vec3(-1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
            Vec3(0.0, 0.0, -1.0)
        )

        fun checkPositionValidity(loc: Vec3, checkGround: Boolean): Boolean {
            return checkPositionValidity(loc.xCoord.toInt(), loc.yCoord.toInt(), loc.zCoord.toInt(), checkGround)
        }

        fun checkPositionValidity(x: Int, y: Int, z: Int, checkGround: Boolean): Boolean {
            val block1 = BlockPos(x, y, z)
            val block2 = BlockPos(x, y + 1, z)
            val block3 = BlockPos(x, y - 1, z)
            return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(
                block3
            )
        }

        private fun isBlockSolid(blockPos: BlockPos): Boolean {
            val block = getBlock(blockPos) ?: return false
            return block.isFullBlock ||
                    block is BlockSlab ||
                    block is BlockStairs ||
                    block is BlockCactus ||
                    block is BlockChest ||
                    block is BlockEnderChest ||
                    block is BlockSkull ||
                    block is BlockPane ||
                    block is BlockFence ||
                    block is BlockWall ||
                    block is BlockGlass ||
                    block is BlockPistonBase ||
                    block is BlockPistonExtension ||
                    block is BlockPistonMoving ||
                    block is BlockStainedGlass ||
                    block is BlockTrapDoor
        }

        private fun isSafeToWalkOn(blockPos: BlockPos): Boolean {
            val block = getBlock(blockPos) ?: return false
            return block !is BlockFence &&
                    block !is BlockWall
        }
    }
}
