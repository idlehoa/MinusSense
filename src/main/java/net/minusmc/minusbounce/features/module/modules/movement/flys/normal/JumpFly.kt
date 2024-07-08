package net.minusmc.minusbounce.features.module.modules.movement.flys.normal

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minusmc.minusbounce.event.BlockBBEvent
import net.minecraft.block.BlockAir
import net.minecraft.util.AxisAlignedBB

class JumpFly: FlyMode("Jump", FlyType.NORMAL) {
    override fun onUpdate() {
        if (mc.thePlayer.onGround) mc.thePlayer.jump()
    }
    override fun onBlockBB(event: BlockBBEvent) {
        if (event.block is BlockAir && event.y < startY)
            event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(), (event.x + 1).toDouble(), mc.thePlayer.posY, (event.z + 1).toDouble())
    }
}