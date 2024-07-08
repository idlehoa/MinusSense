/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 *
 * This code belongs to WYSI-Foundation. Please give credits when using this in your repository.
 */
package net.minusmc.minusbounce.features.command.commands

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.command.Command
import net.minusmc.minusbounce.features.module.modules.combat.AntiBot
import net.minusmc.minusbounce.utils.misc.StringUtils
import net.minusmc.minusbounce.utils.render.ColorUtils
import java.util.*

class FriendCommand : Command("friend", arrayOf("friends", "f")) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            val friendsConfig = MinusBounce.fileManager.friendsConfig

            when {
                args[1].equals("add", ignoreCase = true) -> {
                    if (args.size > 2) {
                        val name = args[2]

                        if (name.isEmpty()) {
                            chat("The name is empty.")
                            return
                        }

                        if (name == mc.thePlayer.name) {
                            chat("You can't add self-friend")
                            return
                        }

                        if (if (args.size > 3) friendsConfig.addFriend(name, StringUtils.toCompleteString(args, 3)) else friendsConfig.addFriend(name)) {
                            MinusBounce.fileManager.saveConfig(friendsConfig)
                            chat("§a§l$name§3 was added to your friend list.")
                            playEdit()
                        } else
                            chat("The name is already in the list.")
                        return
                    }
                    chatSyntax("friend add <name> [alias]")
                    return
                }

                args[1].equals("addall", ignoreCase = true) -> {
                    if (args.size == 3) {
                        val regex = args[2]
                        val coloredRegex = ColorUtils.translateAlternateColorCodes(regex)

                        var added : Int = 0

                        mc.theWorld.playerEntities
                                .filter { !AntiBot.isBot(it) && it.displayName.formattedText.contains(coloredRegex, false) }
                                .forEach {
                                    if (friendsConfig.addFriend(it.name))
                                        added++
                                }

                        chat("Added §a§l$added §3players matching the same regex to your friend list.")
                        playEdit()
                        return
                    }
                    chatSyntax("friend addall <colored regex>")
                    return
                }

                args[1].equals("removeall", ignoreCase = true) -> {
                    if (args.size == 3) {
                        val regex = args[2]

                        var remove : Int = 0

                        friendsConfig.friends
                                .map { it.playerName }
                                .filter { it.contains(regex, false) }
                                .forEach {
                                    if (friendsConfig.removeFriend(it))
                                        remove++
                                }

                        chat("Removed §a§l$remove §3players matching the same regex from your friend list.")
                        playEdit()
                        return
                    }
                    chatSyntax("friend removeall <regex>")
                    return
                }

                args[1].equals("remove", ignoreCase = true) -> {
                    if (args.size > 2) {
                        val name = args[2]

                        if (friendsConfig.removeFriend(name)) {
                            MinusBounce.fileManager.saveConfig(friendsConfig)
                            chat("§a§l$name§3 was removed from your friend list.")
                            playEdit()
                        } else
                            chat("This name is not in the list.")
                        return
                    }
                    chatSyntax("friend remove <name>")
                    return
                }

                args[1].equals("clear", ignoreCase = true) -> {
                    val friends = friendsConfig.friends.size
                    friendsConfig.clearFriends()
                    MinusBounce.fileManager.saveConfig(friendsConfig)
                    chat("Removed $friends friend(s).")
                    return
                }

                args[1].equals("list", ignoreCase = true) -> {
                    chat("Your Friends:")

                    for (friend in friendsConfig.friends)
                        chat("§7> §a§l${friend.playerName} §c(§7§l${friend.alias}§c)")

                    chat("You have §c${friendsConfig.friends.size}§3 friends.")
                    return
                }
            }
        }

        chatSyntax("friend <add/addall/removeall/list/clear>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("add", "addall", "remove", "removeall", "list", "clear").filter { it.startsWith(args[0], true) }
            2 -> {
                when (args[0].lowercase(Locale.getDefault())) {
                    "add" -> {
                        return mc.theWorld.playerEntities
                                .map { it.name }
                                .filter { it.startsWith(args[1], true) }
                    }
                    "remove" -> {
                        return MinusBounce.fileManager.friendsConfig.friends
                                .map { it.playerName }
                                .filter { it.startsWith(args[1], true) }
                    }
                }
                return emptyList()
            }
            else -> emptyList()
        }
    }
}