package net.minusmc.minusbounce.features.module.modules.combat.velocitys.intave

import net.minusmc.minusbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minecraft.client.settings.GameSettings

class OldIntaveVelocity : VelocityMode("OldIntave") {
    private var velocityInput = false
    private var jumped = 0

    override fun onUpdate() {
        if (mc.thePlayer.hurtTime == 9) {
            if (++jumped % 2 == 0 && mc.thePlayer.onGround && mc.thePlayer.isSprinting && mc.currentScreen == null) {
                mc.gameSettings.keyBindJump.pressed = true
                jumped = 0
            }
        } else {
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump)
            velocityInput = false
        }
    }
}