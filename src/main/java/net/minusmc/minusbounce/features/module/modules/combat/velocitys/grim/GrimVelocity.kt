package net.minusmc.minusbounce.features.module.modules.combat.velocitys.grim

import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minusmc.minusbounce.value.IntegerValue

class GrimVelocity : VelocityMode("Grim") {
    private var cancelPacket = IntegerValue("CancelPacket", 6, 0, 20)
    private var resetPersec = IntegerValue("ResetPerMin", 10, 0, 30)
    private var grimTCancel = 0
    private var updates = 0

    override fun onEnable() {
        grimTCancel = 0
    }

    override fun onUpdate() {
        updates++

        if (resetPersec.get() > 0) {
            if (updates >= 0 || updates >= resetPersec.get()) {
                updates = 0
                if (grimTCancel > 0) grimTCancel--
            }
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S12PacketEntityVelocity && packet.entityID == mc.thePlayer.entityId) {
            event.cancelEvent()
            grimTCancel = cancelPacket.get()
        }
        if (packet is S32PacketConfirmTransaction && grimTCancel > 0) {
            event.cancelEvent()
            grimTCancel--
        }
    }
}