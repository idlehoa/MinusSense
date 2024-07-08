/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.features.module.modules.misc.AutoDisable.DisableEvent
import net.minusmc.minusbounce.utils.ClientUtils

class AutoDisableCommand : Command("autodisable", arrayOf("ad")) {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size == 2) {
            when (args[1].lowercase()) {
                "list" -> {
                    chat("§c§lAutoDisable modules:")
                    MinusBounce.moduleManager.modules.filter { it.autoDisables.size > 0 }.forEach {
                        ClientUtils.displayChatMessage("§6> §c${it.name} §7| §a${it.autoDisables.joinToString { d -> d.name.lowercase() }}")
                    }
                    return
                }
                "clear" -> {
                    MinusBounce.moduleManager.modules.filter { it.autoDisables.size > 0 }.forEach {
                        it.autoDisables.clear()
                    }
                    chat("Successfully cleared the AutoDisable list.")
                    return
                }
            }
        }
        else if (args.size > 2) {
            // Get module by name
            val module = MinusBounce.moduleManager.getModule(args[1])

            if (module == null) {
                chat("Module §a§l${args[1]}§3 not found.")
                return
            }

            if (args[2].equals("clear", true)) {
                module.autoDisables.clear()
                chat("Module §a§l${module.name}§3 has been removed from AutoDisable trigger list.")
                playEdit()
                return
            }

            try {
                val disableWhen = DisableEvent.valueOf(args[2].uppercase())

                var added = "will now"
                if (module.autoDisables.contains(disableWhen)) {
                    if (module.autoDisables.remove(disableWhen)) {
                        added = "will no longer"
                    }
                } else {
                    module.autoDisables.add(disableWhen)
                }

                val disableType = when (disableWhen) {
                    DisableEvent.FLAG -> "when you get flagged."
                    DisableEvent.WORLD_CHANGE -> "when you change the world."
                    DisableEvent.GAME_END -> "when the game end."
                }

                // Response to user
                chat("Module §a§l${module.name}§3 $added be disabled $disableType")
                playEdit()
                return
            } catch (e: IllegalArgumentException) {
                chat("§c§lWrong auto disable type!")
                chatSyntax("autodisable <module> <clear/flag/world_change/game_end>")
                return
            }
        }

        chatSyntax("autodisable <module/list> <clear/flag/world_change/game_end>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        val moduleName = args[0]

        return when (args.size) {
            1 -> MinusBounce.moduleManager.modules
                    .map { it.name }
                    .filter { it.startsWith(moduleName, true) }
                    .toList()

            2 -> listOf("clear", "flag", "world_change", "game_end").filter { it.startsWith(args[1], true) }
            else -> emptyList()
        }
    }

}
