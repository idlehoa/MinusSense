/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.utils.misc.StringUtils

class SayCommand : Command("say", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            mc.thePlayer.sendChatMessage(StringUtils.toCompleteString(args, 1))
            chat("Message was sent to the chat.")
            return
        }
        chatSyntax("say <message...>")
    }
}
