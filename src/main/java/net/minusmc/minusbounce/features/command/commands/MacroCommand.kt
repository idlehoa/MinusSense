/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.features.special.MacroManager
import net.minusmc.minusbounce.utils.ClientUtils
import net.minusmc.minusbounce.utils.misc.StringUtils
import org.lwjgl.input.Keyboard

class MacroCommand : Command("macro", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 2) {
            val key = Keyboard.getKeyIndex(args[2].uppercase())
            if (key == 0) {
                chat("§c§lKeybind doesn't exist, or not allowed.")
                chatSyntax("macro <list/clear/add/remove>")
                return
            }
            when (args[1].lowercase()) {
                "add" -> {
                    if (args.size < 4) {
                        chatSyntax("macro add <key name> <message>")
                        return
                    }
                    val message = StringUtils.toCompleteString(args, 3)
                    val existed = MacroManager.macroMapping.containsKey(key)
                    MacroManager.addMacro(key, message)
                    MinusBounce.fileManager.saveConfig(MinusBounce.fileManager.valuesConfig)
                    if (existed)
                        chat("§a§lSuccessfully changed macro in key §7${Keyboard.getKeyName(key)} to §r$message.")
                    else
                        chat("§a§lSuccessfully added §r$message §a§lto key §7${Keyboard.getKeyName(key)}.")
                    playEdit()
                    return
                }
                "remove" -> {
                    if (MacroManager.macroMapping.containsKey(key)) {
                        val lastMessage = MacroManager.macroMapping[key]
                        MacroManager.removeMacro(key)
                        MinusBounce.fileManager.saveConfig(MinusBounce.fileManager.valuesConfig)
                        chat("§a§lSuccessfully removed the macro §r$lastMessage §a§lfrom §7${Keyboard.getKeyName(key)}.")
                        playEdit()
                        return
                    }
                    chat("§c§lThere's no macro bound to this key.")
                    chatSyntax("macro remove <key name>")
                    return
                }
            }
        }
        if (args.size == 2) {
            when (args[1].lowercase()) {
                "list" -> {
                    chat("§6§lMacros:")
                    MacroManager.macroMapping.forEach {
                        ClientUtils.displayChatMessage("§6> §c${Keyboard.getKeyName(it.key)}: §r${it.value}")
                    }
                    return
                }
                "clear" -> {
                    MacroManager.macroMapping.clear()
                    playEdit()
                    MinusBounce.fileManager.saveConfig(MinusBounce.fileManager.valuesConfig)
                    chat("§a§lSuccessfully cleared macro list.")
                    return
                }
                "add" -> {
                    chatSyntax("macro add <key name> <message>")
                    return
                }
                "remove" -> {
                    chatSyntax("macro remove <key name>")
                    return
                }
            }
        }

        chatSyntax("macro <list/clear/add/remove>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("add", "remove", "list", "clear")
                .filter { it.startsWith(args[0], true) }
            else -> emptyList()
        }
    }
}
