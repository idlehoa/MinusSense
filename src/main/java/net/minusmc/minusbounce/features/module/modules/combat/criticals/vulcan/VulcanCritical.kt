package net.minusmc.minusbounce.features.module.modules.combat.criticals.vulcan

import net.minusmc.minusbounce.event.AttackEvent
import net.minusmc.minusbounce.features.module.modules.combat.criticals.CriticalMode
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class VulcanCritical : CriticalMode("Vulcan") {
    private var attacks = 0
    override fun onEnable() {
        attacks = 0
    }
    override fun onAttack(event: AttackEvent) {
        attacks++
        if (attacks > 7) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.16477328182606651, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.08307781780646721, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0030162615090425808, mc.thePlayer.posZ, false))
            attacks = 0
        }
    }
}