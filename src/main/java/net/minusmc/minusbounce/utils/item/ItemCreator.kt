/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import net.minecraft.util.ResourceLocation
import java.util.*
import java.util.regex.Pattern
import kotlin.math.min

/**
 * @author MCModding4K
 */
object ItemCreator {
    /**
     * Allows you to create a item using the item json
     *
     * @param itemArguments arguments of item
     * @return created item
     * @author MCModding4K
     */
    fun createItem(itemArguments: String): ItemStack? {
        var itemArguments = itemArguments
        return try {
            itemArguments = itemArguments.replace('&', '§')
            var item: Item? = Item()
            var args: Array<String>? = null
            var i = 1
            var j = 0
            for (mode in 0..min(12.0, (itemArguments.length - 2).toDouble()).toInt()) {
                args = itemArguments.substring(mode).split(Pattern.quote(" ").toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val resourcelocation = ResourceLocation(args[0])
                item = Item.itemRegistry.getObject(resourcelocation)
                if (item != null) break
            }
            if (item == null) return null
            if (args!!.size >= 2 && args!![1].matches("\\d+".toRegex())) i = args[1].toInt()
            if (args!!.size >= 3 && args[2].matches("\\d+".toRegex())) j = args[2].toInt()
            val itemstack = ItemStack(item, i, j)
            if (args.size >= 4) {
                val NBT = StringBuilder()
                for (nbtcount in 3 until args.size) NBT.append(" ").append(args[nbtcount])
                itemstack.tagCompound = JsonToNBT.getTagFromJson(NBT.toString())
            }
            itemstack
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}
