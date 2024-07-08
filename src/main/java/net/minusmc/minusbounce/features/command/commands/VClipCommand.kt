/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.ui.client.hud.element.elements.Notification
import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.command.Command

class VClipCommand : Command("vclip", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            try {
                val y = args[1].toDouble()
                val entity = if(mc.thePlayer.isRiding) mc.thePlayer.ridingEntity else mc.thePlayer

                entity.setPosition(entity.posX, entity.posY + y, entity.posZ)
                chat("Teleported ${args[1].toDouble()} blocks.")
                MinusBounce.hud.addNotification(Notification("Teleported ${args[1].toDouble()} blocks.", Notification.Type.SUCCESS))
            } catch (ex: NumberFormatException) {
                chatSyntaxError()
            }

            return
        }

        chatSyntax("vclip <value>")
    }
}
