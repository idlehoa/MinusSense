/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.utils.ClientUtils

class HideCommand : Command("hide", emptyArray()) {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            when {
                args[1].equals("list", true) -> {
                    chat("§c§lHidden")
                    MinusBounce.moduleManager.modules.filter { !it.array }.forEach {
                        ClientUtils.displayChatMessage("§6> §c${it.name}")
                    }
                    return
                }

                args[1].equals("clear", true) -> {
                    for (module in MinusBounce.moduleManager.modules)
                        module.array = true

                    chat("Cleared hidden modules.")
                    return
                }

                args[1].equals("reset", true) -> {
                    for (module in MinusBounce.moduleManager.modules)
                        module.array = module::class.java.getAnnotation(ModuleInfo::class.java).array

                    chat("Reset hidden modules.")
                    return
                }

                args[1].equals("category", true) -> {
                    if (args.size < 3) {
                        chatSyntax("hide category <name>")
                        return
                    } else if (MinusBounce.moduleManager.modules.find { it.category.displayName.equals(args[2], true) } != null) {
                        MinusBounce.moduleManager.modules.filter { it.category.displayName.equals(args[2], true) }.forEach { it.array = false }
                        chat("All modules in category §7${args[2]}§3 is now §a§lhidden.")
                        return
                    } else {
                        chat("Couldn't find any category named §7${args[2]}§3!")
                        return
                    }
                }

                else -> {
                    // Get module by name
                    val module = MinusBounce.moduleManager.getModule(args[1])

                    if (module == null) {
                        chat("Module §a§l${args[1]}§3 not found.")
                        return
                    }

                    // Find key by name and change
                    module.array = !module.array

                    // Response to user
                    chat("Module §a§l${module.name}§3 is now §a§l${if (module.array) "visible" else "invisible"}§3 on the array list.")
                    playEdit()
                    return
                }
            }
        }

        chatSyntax("hide <module/list/clear/reset/category>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        val moduleName = args[0]
        when (args.size) {
            1 -> {
                val moduleList = MinusBounce.moduleManager.modules
                    .map { it.name }
                    .filter { it.startsWith(moduleName, true) }
                    .toMutableList()

                moduleList.addAll(listOf("category", "list", "clear", "reset").filter { it.startsWith(moduleName, true) })
                return moduleList
            }
            2 -> {
                if (moduleName.equals("category", true))
                    return ModuleCategory.values()
                            .map { it.displayName }
                            .filter { it.startsWith(args[1], true) }
                            .toList()
            }
        }

        return emptyList()
    }

}
