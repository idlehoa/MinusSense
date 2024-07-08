package net.minusmc.minusbounce.features.module.modules.movement.flys.aac

import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyMode
import net.minusmc.minusbounce.features.module.modules.movement.flys.FlyType
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.value.FloatValue
import org.lwjgl.input.Keyboard
import java.awt.Color

class AAC3312Fly: FlyMode("AAC3.3.12", FlyType.AAC) {
	private val aacMotion = FloatValue("Motion", 10f, 0.1f, 10f)

	override fun resetMotion() {}

	override fun onUpdate() {
		if(mc.thePlayer.posY < -70)
            mc.thePlayer.motionY = aacMotion.get().toDouble()

        mc.timer.timerSpeed = 1f

        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            mc.timer.timerSpeed = 0.2f
            mc.rightClickDelayTimer = 0
        }
	}

	override fun onRender3D() {
		val y = startY + 2.0
		val color = if (mc.thePlayer.entityBoundingBox!!.maxY < y) Color(0, 255, 0, 90) else Color(255, 0, 0, 90)
		RenderUtils.drawPlatform(y, color, 1.0)
		RenderUtils.drawPlatform(-70.0, Color(0, 0, 255, 90), 1.0)
	}
}