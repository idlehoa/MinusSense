/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.item

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

//import static net.minusmc.minusbounce.utils.item.ItemUtils.getEnchantmentCount;
class ArmorComparator : Comparator<ArmorPiece> {
    override fun compare(o1: ArmorPiece, o2: ArmorPiece): Int {
        // For damage reduction it is better if it is smaller, so it has to be inverted
        // The decimal values have to be rounded since in double math equals is inaccurate
        // For example 1.03 - 0.41 = 0.6200000000000001 and (1.03 - 0.41) == 0.62 would be false
        val compare = java.lang.Double.compare(round(getThresholdedDamageReduction(o2.itemStack).toDouble(), 3),round(getThresholdedDamageReduction(o1.itemStack).toDouble(), 3))

        // If both armor pieces have the exact same damage, compare enchantments
        if (compare == 0) {
            val otherEnchantmentCmp = java.lang.Double.compare(round(getEnchantmentThreshold(o1.itemStack).toDouble(), 3),round(getEnchantmentThreshold(o2.itemStack).toDouble(), 3))

            // If both have the same enchantment threshold, prefer the item with more enchantments
            if (otherEnchantmentCmp == 0) {
                val enchantmentCountCmp = Integer.compare(ItemUtils.getEnchantmentCount(o1.itemStack),ItemUtils.getEnchantmentCount(o2.itemStack))
                if (enchantmentCountCmp != 0) return enchantmentCountCmp

                // Then durability...
                val o1a = o1.itemStack.item as ItemArmor
                val o2a = o2.itemStack.item as ItemArmor
                val durabilityCmp = Integer.compare(o1a.armorMaterial.getDurability(o1a.armorType),o2a.armorMaterial.getDurability(o2a.armorType))
                return if (durabilityCmp != 0) {
                    durabilityCmp
                } else Integer.compare(o1a.armorMaterial.enchantability, o2a.armorMaterial.enchantability)

                // Last comparision: Enchantability
            }
            return otherEnchantmentCmp
        }
        return compare
    }

    private fun getThresholdedDamageReduction(itemStack: ItemStack): Float {
        val item = itemStack.item as ItemArmor
        return getDamageReduction(item.armorMaterial.getDamageReductionAmount(item.armorType), 0) * (1 - getThresholdedEnchantmentDamageReduction(itemStack))
    }

    private fun getDamageReduction(defensePoints: Int, toughness: Int): Float {
        return (1 - min(20.0, max((defensePoints / 5.0f).toDouble(), (defensePoints - 1 / (2 + toughness / 4.0f)).toDouble())) / 25.0f).toFloat()
    }

    private fun getThresholdedEnchantmentDamageReduction(itemStack: ItemStack): Float {
        var sum = 0.0f
        for (i in DAMAGE_REDUCTION_ENCHANTMENTS.indices) {
            sum += ItemUtils.getEnchantment(itemStack, DAMAGE_REDUCTION_ENCHANTMENTS[i]) * ENCHANTMENT_FACTORS[i] * ENCHANTMENT_DAMAGE_REDUCTION_FACTOR[i]
        }
        return sum
    }

    private fun getEnchantmentThreshold(itemStack: ItemStack): Float {
        var sum = 0.0f
        for (i in OTHER_ENCHANTMENTS.indices) {
            sum += ItemUtils.getEnchantment(itemStack, OTHER_ENCHANTMENTS[i]) * OTHER_ENCHANTMENT_FACTORS[i]
        }
        return sum
    }

    companion object {
        private val DAMAGE_REDUCTION_ENCHANTMENTS = arrayOf(
            Enchantment.protection,
            Enchantment.projectileProtection,
            Enchantment.fireProtection,
            Enchantment.blastProtection
        )
        private val ENCHANTMENT_FACTORS = floatArrayOf(1.5f, 0.4f, 0.39f, 0.38f)
        private val ENCHANTMENT_DAMAGE_REDUCTION_FACTOR = floatArrayOf(0.04f, 0.08f, 0.15f, 0.08f)
        private val OTHER_ENCHANTMENTS = arrayOf(
            Enchantment.featherFalling,
            Enchantment.thorns,
            Enchantment.respiration,
            Enchantment.aquaAffinity,
            Enchantment.unbreaking
        )
        private val OTHER_ENCHANTMENT_FACTORS = floatArrayOf(3.0f, 1.0f, 0.1f, 0.05f, 0.01f)

        /**
         * Rounds a double. From https://stackoverflow.com/a/2808648/9140494
         *
         * @param value  the value to be rounded
         * @param places Decimal places
         * @return The rounded value
         */
        fun round(value: Double, places: Int): Double {
            require(places >= 0)
            var bd = BigDecimal.valueOf(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }
    }
}
