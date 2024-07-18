package net.minusmc.minusbounce.features.module.modules.player.nofalls.normal

import net.minecraft.block.BlockLiquid
import net.minecraft.util.AxisAlignedBB
import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode
import net.minusmc.minusbounce.utils.block.BlockUtils.collideBlock
import net.minusmc.minusbounce.utils.misc.FallingPlayer


class LegitNoFall: NoFallMode("Legit") {
    private var working = false
    override fun onUpdate() {
        if (collideBlock(mc.thePlayer!!.entityBoundingBox, fun(block: Any?) = block is BlockLiquid) ||
            collideBlock(AxisAlignedBB(mc.thePlayer!!.entityBoundingBox.maxX, mc.thePlayer!!.entityBoundingBox.maxY, mc.thePlayer!!.entityBoundingBox.maxZ, mc.thePlayer!!.entityBoundingBox.minX, mc.thePlayer!!.entityBoundingBox.minY - 0.01, mc.thePlayer!!.entityBoundingBox.minZ), fun(block: Any?) = block is BlockLiquid))
            return

        val thePlayer = mc.thePlayer ?: return
        if (mc.thePlayer.fallDistance > 3) {
            val fallingPlayer = FallingPlayer(
                thePlayer.posX,
                thePlayer.posY,
                thePlayer.posZ,
                thePlayer.motionX,
                thePlayer.motionY,
                thePlayer.motionZ,
                thePlayer.rotationYaw,
                thePlayer.moveStrafing,
                thePlayer.moveForward
            )

            if (fallingPlayer.findCollision(1) != null) {
                working = true
            }
        }
        if (working && mc.thePlayer.onGround) {
            mc.gameSettings.keyBindSneak.pressed = false
            working = false
        }
        if (working) mc.gameSettings.keyBindSneak.pressed = true
    }

    override fun onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false
    }
}