/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.command

import net.minusmc.minusbounce.utils.ClassUtils
import net.minusmc.minusbounce.utils.ClientUtils

class CommandManager {
    val commands = mutableListOf<Command>()
    var latestAutoComplete: Array<String> = emptyArray()

    var prefix = '.'

    /**
     * Register all default commands
     */
    fun registerCommands() {
        ClassUtils.resolvePackage("${this.javaClass.`package`.name}.commands", Command::class.java)
            .forEach(this::registerCommand)
    }

    /**
     * Execute command by given [input]
     */
    fun executeCommands(input: String) {
        for (command in commands) {
            val args = input.split(" ").toTypedArray()

            if (args[0].equals(prefix.toString() + command.command, ignoreCase = true)) {
                command.execute(args)
                return
            }

            for (alias in command.alias) {
                if (!args[0].equals(prefix.toString() + alias, ignoreCase = true))
                    continue

                command.execute(args)
                return
            }
        }

        ClientUtils.displayChatMessage("§cCommand not found. Type ${prefix}help to view all commands.")
    }

    /**
     * Updates the [latestAutoComplete] array based on the provided [input].
     *
     * @param input text that should be used to check for auto completions.
     * @author NurMarvin
     */
    fun autoComplete(input: String): Boolean {
        this.latestAutoComplete = this.getCompletions(input) ?: emptyArray()
        return input.startsWith(this.prefix) && this.latestAutoComplete.isNotEmpty()
    }

    /**
     * Returns the auto completions for [input].
     *
     * @param input text that should be used to check for auto completions.
     * @author NurMarvin
     */
    private fun getCompletions(input: String): Array<String>? {
        if (input.isNotEmpty() && input.toCharArray()[0] == this.prefix) {
            val args = input.split(" ")

            return if (args.size > 1) {
                val command = getCommand(args[0].substring(1))
                val tabCompletions = command?.tabComplete(args.drop(1).toTypedArray())

                tabCompletions?.toTypedArray()
            } else {
                val rawInput = input.substring(1)
                commands
                    .filter {
                        it.command.startsWith(rawInput, true)
                            || it.alias.any { alias -> alias.startsWith(rawInput, true) }
                    }
                    .map {
                        val alias: String = if (it.command.startsWith(rawInput, true))
                            it.command
                        else {
                            it.alias.first { alias -> alias.startsWith(rawInput, true) }
                        }

                        this.prefix + alias
                    }
                    .toTypedArray()
            }
        }
        return null
    }

    /**
     * Get command instance by given [name]
     */
    private fun getCommand(name: String): Command? {
        return commands.find {
            it.command.equals(name, ignoreCase = true)
                || it.alias.any { alias -> alias.equals(name, true) }
        }
    }

    /**
     * Register [command] by just adding it to the commands registry
     */
    private fun registerCommand(command: Class<out Command>) = commands.add(command.newInstance())
    fun registerCommand(command: Command) = commands.add(command)

    /**
     * Unregister [command] by just removing it from the commands registry
     */
    fun unregisterCommand(command: Command?) = commands.remove(command)
}
