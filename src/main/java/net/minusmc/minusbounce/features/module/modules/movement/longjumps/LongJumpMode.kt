package net.minusmc.minusbounce.features.module.modules.movement.longjumps

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.event.JumpEvent
import net.minusmc.minusbounce.event.MoveEvent
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.event.StepEvent
import net.minusmc.minusbounce.features.module.modules.movement.LongJump
import net.minusmc.minusbounce.utils.ClassUtils
import net.minusmc.minusbounce.utils.MinecraftInstance
import net.minusmc.minusbounce.value.Value

abstract class LongJumpMode(val modeName: String): MinecraftInstance() {
    protected var startY = 0.0

	protected val longjump: LongJump
		get() = MinusBounce.moduleManager[LongJump::class.java]!!

	open val values: List<Value<*>>
		get() = ClassUtils.getValues(this.javaClass, this)

	// NCP
	open fun resetMotion() {}

	open fun onEnable() {}
	open fun onDisable() {}
    open fun onUpdate() {}
    open fun onUpdateSpecial() {}
    open fun onPacket(event: PacketEvent) {}
    open fun onMove(event: MoveEvent) {}
    open fun onJump(event: JumpEvent) {}
    open fun onStep(event: StepEvent) {}
}
