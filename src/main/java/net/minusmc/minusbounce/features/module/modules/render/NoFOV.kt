/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.render

import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.value.FloatValue

@ModuleInfo(name = "NoFOV", spacedName = "No FOV", description = "Disables FOV changes caused by speed effect, etc.", category = ModuleCategory.RENDER)
class NoFOV : Module() {
    val fovValue = FloatValue("FOV", 1f, 0f, 1.5f, "x")
}
