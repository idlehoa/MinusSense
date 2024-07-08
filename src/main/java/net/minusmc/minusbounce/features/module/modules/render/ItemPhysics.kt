/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.render

import net.minusmc.minusbounce.features.module.*
import net.minusmc.minusbounce.value.FloatValue

@ModuleInfo(name = "ItemPhysics", spacedName = "Item Physics", description = "newton hits", category = ModuleCategory.RENDER)
class ItemPhysics : Module() {
    val itemWeight = FloatValue("Weight", 0.5F, 0F, 1F, "x")
    override val tag: String
        get() = "${itemWeight.get()}"
}
