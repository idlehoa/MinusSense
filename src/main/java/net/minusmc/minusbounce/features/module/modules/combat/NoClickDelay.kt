package net.minusmc.minusbounce.features.module.modules.combat

import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo

@ModuleInfo(name = "NoClickDelay", description = "No click delay.", category = ModuleCategory.COMBAT)
class NoClickDelay : Module() {

    fun onUpdate(event: UpdateEvent) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return

            mc.leftClickCounter = 0
        }
    }
}