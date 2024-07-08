package net.minusmc.minusbounce.features.module.modules.combat.velocitys.normal

import net.minusmc.minusbounce.MinusBounce.combatManager
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.utils.MinecraftInstance.Companion.mc
import net.minusmc.minusbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minusmc.minusbounce.value.BoolValue

class JumpReset : VelocityMode("JumpReset") {
    private val onMouse = BoolValue("onMouseDown", false)
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.hurtTime > 8 && mc.thePlayer.onGround && combatManager.inCombat && !mc.thePlayer.isBurning) {
            if (!onMouse.get() || mc.gameSettings.keyBindAttack.isKeyDown) {
                mc.thePlayer.jump()
            }
        }
    }
}
