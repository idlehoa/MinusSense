package net.minusmc.minusbounce.features.module.modules.combat.velocitys.grim

import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minusmc.minusbounce.value.IntegerValue

class OldHeroMCVelocity : VelocityMode("OldHeroMC") {
    private var cancelPacket = IntegerValue("CancelPacket", 6, 0, 20)
    private var resetPersec = IntegerValue("ResetPerMin", 10, 0, 30)

    private var maxMotionRangeValue: IntegerValue = object: IntegerValue("MaxMotionRange", -500, -1000, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = minMotionRangeValue.get()
            if (v > newValue) set(v)
        }
    }

    private var minMotionRangeValue: IntegerValue = object: IntegerValue("MinMotionRange", -500, -1000, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = maxMotionRangeValue.get()
            if (v < newValue) set(v)
        }
    }

    private var grimTCancel = 0
    private var updates = 0

    override fun onEnable() {
        grimTCancel = 0
    }

    override fun onUpdate() {
        updates++

        if (resetPersec.get() > 0 && (updates >= 0 || updates >= resetPersec.get())) {
            updates = 0
            if (grimTCancel > 0) grimTCancel--
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S12PacketEntityVelocity && packet.entityID == mc.thePlayer.entityId) {
            if (packet.motionX < minMotionRangeValue.get() || packet.motionX > maxMotionRangeValue.get() || packet.motionZ < minMotionRangeValue.get() || packet.motionZ > maxMotionRangeValue.get()) {
                event.cancelEvent()
                grimTCancel = cancelPacket.get()
            }
        }
        if (packet is S32PacketConfirmTransaction && grimTCancel > 0) {
            event.cancelEvent()
            grimTCancel--
        }
    }
}