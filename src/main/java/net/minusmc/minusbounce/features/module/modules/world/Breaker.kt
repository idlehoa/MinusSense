/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.world

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.Render3DEvent
import net.minusmc.minusbounce.event.Render2DEvent
import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.event.WorldEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.features.module.modules.combat.KillAura
import net.minusmc.minusbounce.features.module.modules.player.AutoTool
import net.minusmc.minusbounce.ui.client.hud.element.elements.Notification
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.RotationUtils
import net.minusmc.minusbounce.utils.block.BlockUtils.getBlock
import net.minusmc.minusbounce.utils.block.BlockUtils.getBlockName
import net.minusmc.minusbounce.utils.block.BlockUtils.getCenterDistance
import net.minusmc.minusbounce.utils.block.BlockUtils.isFullBlock
import net.minusmc.minusbounce.utils.extensions.getBlock
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.timer.MSTimer
import net.minusmc.minusbounce.value.*
import net.minecraft.block.Block
import net.minecraft.block.BlockAir
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import java.awt.Color

@ModuleInfo(name = "Breaker", description = "Destroys selected blocks around you. (aka. IDNuker)", category = ModuleCategory.WORLD)
object Breaker : Module() {

    /**
     * SETTINGS
     */

    private val blockValue = BlockValue("Block", 26)
    private val ignoreFirstBlockValue = BoolValue("IgnoreFirstDetection", false)
    private val resetOnWorldValue = BoolValue("ResetOnWorldChange", false) { ignoreFirstBlockValue.get() }
    private val renderValue = ListValue("Render-Mode", arrayOf("Box", "Outline", "2D", "None"), "Box")
    private val throughWallsValue = ListValue("ThroughWalls", arrayOf("None", "Raycast", "Around"), "None")
    private val rangeValue = FloatValue("Range", 5F, 1F, 7F, "m")
    private val actionValue = ListValue("Action", arrayOf("Destroy", "Use"), "Destroy")
    private val instantValue = BoolValue("Instant", false)
    private val switchValue = IntegerValue("SwitchDelay", 250, 0, 1000, "ms")
    private val coolDownValue = IntegerValue("Cooldown-Seconds", 15, 0, 60)
    private val swingValue = BoolValue("Swing", true)
    private val rotationsValue = BoolValue("Rotations", true)
    private val surroundingsValue = BoolValue("Surroundings", true)
    private val hypixelValue = BoolValue("Hypixel", false)
    private val noHitValue = BoolValue("NoAura", false)
    private val toggleResetCDValue = BoolValue("ResetCoolDownWhenToggled", false)

    /**
     * VALUES
     */

    private var firstPos: BlockPos? = null
    private var firstPosBed: BlockPos? = null
    private var pos: BlockPos? = null
    private var oldPos: BlockPos? = null
    private var blockHitDelay = 0
    private val switchTimer = MSTimer()
    private val coolDownTimer = MSTimer()
    private var currentDamage = 0F

    private var lastWorld: WorldClient? = null

    override fun onEnable() {
        if (toggleResetCDValue.get()) coolDownTimer.reset()
        firstPos = null
        firstPosBed = null
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        if (event.worldClient != lastWorld && resetOnWorldValue.get()) {
            firstPos = null
            firstPosBed = null
        }
        lastWorld = event.worldClient
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (noHitValue.get()) {
            val killAura = MinusBounce.moduleManager.getModule(KillAura::class.java) as KillAura

            if (killAura.state && killAura.target != null)
                return
        }

        val targetId = blockValue.get()

        if (pos == null || Block.getIdFromBlock(getBlock(pos)) != targetId ||
                getCenterDistance(pos!!) > rangeValue.get())
            pos = find(targetId)

        // Reset current breaking when there is no target block
        if (pos == null) {
            currentDamage = 0F
            return
        }

        var currentPos = pos ?: return
        var rotations = RotationUtils.faceBlock(currentPos) ?: return

        // Surroundings
        var surroundings = false

        if (surroundingsValue.get()) {
            val eyes = mc.thePlayer.getPositionEyes(1F)
            val blockPos = mc.theWorld.rayTraceBlocks(eyes, rotations.vec, false,
                    false, true).blockPos

            if (blockPos != null && blockPos.getBlock() !is BlockAir) {
                if (currentPos.x != blockPos.x || currentPos.y != blockPos.y || currentPos.z != blockPos.z)
                    surroundings = true

                pos = blockPos
                currentPos = pos ?: return
                rotations = RotationUtils.faceBlock(currentPos) ?: return
            }
        }

        val b = Block.getIdFromBlock(getBlock(currentPos)) == targetId
        if (hypixelValue.get()) {
            if (b) {
                val blockPos = currentPos.up()
                if (getBlock(blockPos) !is BlockAir) {
                    if (currentPos.x != blockPos.x || currentPos.y != blockPos.y || currentPos.z != blockPos.z)
                        surroundings = true

                    pos = blockPos
                    currentPos = pos ?: return
                    rotations = RotationUtils.faceBlock(currentPos) ?: return
                }
            }
        }

        // Reset switch timer when position changed
        if (oldPos != null && oldPos != currentPos) {
            currentDamage = 0F
            switchTimer.reset()
        }

        oldPos = currentPos

        if (!switchTimer.hasTimePassed(switchValue.get().toLong()) || (coolDownValue.get() > 0 && !coolDownTimer.hasTimePassed(coolDownValue.get().toLong() * 1000L)))
            return

        // Block hit delay
        if (blockHitDelay > 0) {
            blockHitDelay--
            return
        }

        // Face block
        if (rotationsValue.get())
            RotationUtils.setTargetRot(rotations.rotation)

        when {
            // Destory block
            actionValue.get().equals("destroy", true) || surroundings -> {
                // Auto Tool
                val autoTool = MinusBounce.moduleManager[AutoTool::class.java] as AutoTool
                if (autoTool.state)
                    autoTool.switchSlot(currentPos)

                // Break block
                if (instantValue.get()) {
                    // CivBreak style block breaking
                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                            currentPos, EnumFacing.DOWN))

                    if (swingValue.get())
                        mc.thePlayer.swingItem()

                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            currentPos, EnumFacing.DOWN))
                    currentDamage = 0F
                    if (!surroundingsValue.get()) coolDownTimer.reset()
                    return
                }

                // Minecraft block breaking
                val block = currentPos.getBlock() ?: return

                if (currentDamage == 0F) {
                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                            currentPos, EnumFacing.DOWN))

                    if (mc.thePlayer.capabilities.isCreativeMode ||
                            block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos) >= 1.0F) {
                        if (swingValue.get())
                            mc.thePlayer.swingItem()
                        mc.playerController.onPlayerDestroyBlock(pos, EnumFacing.DOWN)

                        currentDamage = 0F
                        pos = null
                        if (!surroundingsValue.get()) coolDownTimer.reset()
                        return
                    }
                }

                if (swingValue.get())
                    mc.thePlayer.swingItem()

                currentDamage += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, currentPos)
                mc.theWorld.sendBlockBreakProgress(mc.thePlayer.entityId, currentPos, (currentDamage * 10F).toInt() - 1)

                if (currentDamage >= 1F) {
                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            currentPos, EnumFacing.DOWN))
                    mc.playerController.onPlayerDestroyBlock(currentPos, EnumFacing.DOWN)
                    blockHitDelay = 4
                    currentDamage = 0F
                    pos = null
                    if (!surroundingsValue.get()) coolDownTimer.reset()
                }
            }

            // Use block
            actionValue.get().equals("use", true) -> if (mc.playerController.onPlayerRightClick(
                            mc.thePlayer, mc.theWorld, mc.thePlayer.heldItem, pos, EnumFacing.DOWN,
                            Vec3(currentPos.x.toDouble(), currentPos.y.toDouble(), currentPos.z.toDouble()))) {
                if (swingValue.get())
                    mc.thePlayer.swingItem()

                blockHitDelay = 4
                currentDamage = 0F
                pos = null
                coolDownTimer.reset()
            }
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        when (renderValue.get().lowercase()) {
            "box" -> RenderUtils.drawBlockBox(pos ?: return, if (!coolDownTimer.hasTimePassed(coolDownValue.get().toLong() * 1000L)) Color.DARK_GRAY else Color.RED, false)
            "outline" -> RenderUtils.drawBlockBox(pos ?: return, if (!coolDownTimer.hasTimePassed(coolDownValue.get().toLong() * 1000L)) Color.DARK_GRAY else Color.RED, true)
            "2d" -> RenderUtils.draw2D(pos ?: return, if (!coolDownTimer.hasTimePassed(coolDownValue.get().toLong() * 1000L)) Color.DARK_GRAY.rgb else Color.RED.rgb, Color.BLACK.rgb)
        }
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val sc = ScaledResolution(mc)
        if (coolDownValue.get() > 0 && !coolDownTimer.hasTimePassed(coolDownValue.get().toLong() * 1000L)) {
            val timeLeft = "Cooldown: ${(coolDownTimer.hasTimeLeft(coolDownValue.get().toLong() * 1000L) / 1000L).toInt()}s"
            val strWidth = Fonts.minecraftFont.getStringWidth(timeLeft)

            Fonts.minecraftFont.drawString(
                timeLeft,
                sc.scaledWidth / 2 - strWidth / 2 - 1,
                sc.scaledHeight / 2 - 70,
                0x000000
            )
            Fonts.minecraftFont.drawString(
                timeLeft,
                sc.scaledWidth / 2 - strWidth / 2 + 1,
                sc.scaledHeight / 2 - 70,
                0x000000
            )
            Fonts.minecraftFont.drawString(
                timeLeft,
                sc.scaledWidth / 2 - strWidth / 2,
                sc.scaledHeight / 2 - 69,
                0x000000
            )
            Fonts.minecraftFont.drawString(
                timeLeft,
                sc.scaledWidth / 2 - strWidth / 2,
                sc.scaledHeight / 2 - 71,
                0x000000
            )
            Fonts.minecraftFont.drawString(timeLeft, sc.scaledWidth / 2 - strWidth / 2, sc.scaledHeight / 2 - 70, -1)
        }
    }

    /**
     * Find new target block by targetID
     */
    /*private fun find(targetID: Int) =
        searchBlocks(rangeValue.get().toInt() + 1).filter {
                    Block.getIdFromBlock(it.value) == targetID && getCenterDistance(it.key) <= rangeValue.get()
                            && (isHitable(it.key) || surroundingsValue.get())
                }.minByOrNull { getCenterDistance(it.key) }?.key*/

    //Removed triple iteration of blocks to improve speed
    /**
     * Find new target block by [targetID]
     */
    private fun find(targetID: Int): BlockPos? {
        val radius = rangeValue.get().toInt() + 1

        var nearestBlockDistance = Double.MAX_VALUE
        var nearestBlock: BlockPos? = null

        for (x in radius downTo -radius + 1) {
            for (y in radius downTo -radius + 1) {
                for (z in radius downTo -radius + 1) {
                    val blockPos = BlockPos(mc.thePlayer.posX.toInt() + x, mc.thePlayer.posY.toInt() + y,
                            mc.thePlayer.posZ.toInt() + z)
                    val block = getBlock(blockPos) ?: continue

                    if (Block.getIdFromBlock(block) != targetID) continue

                    val distance = getCenterDistance(blockPos)
                    if (distance > rangeValue.get()) continue
                    if (nearestBlockDistance < distance) continue
                    if (!isHitable(blockPos) && !surroundingsValue.get()) continue

                    nearestBlockDistance = distance
                    nearestBlock = blockPos
                }
            }
        }

        if (ignoreFirstBlockValue.get() && nearestBlock != null) {
            if (firstPos == null) {
                firstPos = nearestBlock
                MinusBounce.hud.addNotification(
                    Notification(
                        "Found first ${getBlockName(targetID)} block at ${nearestBlock.x} ${nearestBlock.y} ${nearestBlock.z}",
                        Notification.Type.SUCCESS
                    )
                )
            }
            if (targetID == 26 && firstPos != null && firstPosBed == null) { // bed
                firstPosBed = when (true) {
                    (getBlock(firstPos!!.east()) != null && Block.getIdFromBlock(getBlock(firstPos!!.east())!!) == 26) -> firstPos!!.east()
                    (getBlock(firstPos!!.west()) != null && Block.getIdFromBlock(getBlock(firstPos!!.west())!!) == 26) -> firstPos!!.west()
                    (getBlock(firstPos!!.south()) != null && Block.getIdFromBlock(getBlock(firstPos!!.south())!!) == 26) -> firstPos!!.south()
                    (getBlock(firstPos!!.north()) != null && Block.getIdFromBlock(getBlock(firstPos!!.north())!!) == 26) -> firstPos!!.north()
                    else -> null
                }
                if (firstPosBed != null)
                    MinusBounce.hud.addNotification(
                        Notification(
                            "Found second Bed block at ${firstPosBed!!.x} ${firstPosBed!!.y} ${firstPosBed!!.z}",
                            Notification.Type.SUCCESS
                        )
                    )
            }
        }
        return if (ignoreFirstBlockValue.get() && (firstPos == nearestBlock || firstPosBed == nearestBlock)) null else nearestBlock
    }

    /**
     * Check if block is hitable (or allowed to hit through walls)
     */
    private fun isHitable(blockPos: BlockPos): Boolean {
        return when (throughWallsValue.get().lowercase()) {
            "raycast" -> {
                val eyesPos = Vec3(mc.thePlayer.posX, mc.thePlayer.entityBoundingBox.minY +
                        mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
                val movingObjectPosition = mc.theWorld.rayTraceBlocks(eyesPos,
                        Vec3(blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5), false,
                        true, false)

                movingObjectPosition != null && movingObjectPosition.blockPos == blockPos
            }
            "around" -> !isFullBlock(blockPos.down()) || !isFullBlock(blockPos.up()) || !isFullBlock(blockPos.north())
                    || !isFullBlock(blockPos.east()) || !isFullBlock(blockPos.south()) || !isFullBlock(blockPos.west())
            else -> true
        }
    }

    override val tag: String
        get() = getBlockName(blockValue.get())
}