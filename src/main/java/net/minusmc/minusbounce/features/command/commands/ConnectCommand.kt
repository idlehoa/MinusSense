/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.multiplayer.GuiConnecting
import net.minecraft.client.multiplayer.ServerData
import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.ui.client.GuiMainMenu

class ConnectCommand : Command("connect", emptyArray()) {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size == 3 && args[2].equals("silent", true)) {
            chat("Connecting to §a§l${args[1]} §7(Silent mode)")
            mc.displayGuiScreen(GuiConnecting(GuiMultiplayer(GuiMainMenu()), mc, ServerData("", args[1], false)))
        } else if (args.size == 2) {
            chat("Connecting to §a§l${args[1]}")
            mc.theWorld.sendQuittingDisconnectingPacket()
            mc.displayGuiScreen(GuiConnecting(GuiMultiplayer(GuiMainMenu()), mc, ServerData("", args[1], false)))
        } else
            chatSyntax("connect <ip:port> (silent)")
    }

}
