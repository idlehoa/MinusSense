/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.item

import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack

class ArmorPart(val itemStack: ItemStack, val slot: Int) {
    val armorType = (itemStack.item as ItemArmor).armorType
}
