/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.file.configs

import com.google.gson.*
import me.liuli.elixir.account.CrackedAccount
import me.liuli.elixir.account.MinecraftAccount
import me.liuli.elixir.manage.AccountSerializer
import net.minusmc.minusbounce.file.FileManager
import net.minusmc.minusbounce.file.FileConfig

import java.io.*
import java.util.ArrayList

class AccountsConfig(file: File): FileConfig(file) {
    val accounts = ArrayList<MinecraftAccount>()

    override fun loadConfig() {
        clearAccounts()

        val jsonElement = JsonParser().parse(BufferedReader(FileReader(file)))

        if (jsonElement is JsonNull) return

        for (accountElement in jsonElement.getAsJsonArray()) {
            val accountObject = accountElement.getAsJsonObject()

            try{
                accounts.add(AccountSerializer.fromJson(accountObject))
            } catch (e: Exception) {
                if (e !is JsonSyntaxException && e !is IllegalAccessException) return
                // Import old account format

                val name = accountObject.get("name")
                val password = accountObject.get("password")
                val inGameName = accountObject.get("inGameName")

                if (!(inGameName.isJsonNull && password.isJsonNull)) {
                    val crackedAccount = CrackedAccount()
                    crackedAccount.name = name.asString
                    accounts.add(crackedAccount)
                }
            }
        }
    }

    override fun saveConfig() {
        val jsonArray = JsonArray()

        for (minecraftAccount in accounts) {
            jsonArray.add(AccountSerializer.toJson(minecraftAccount))
        }

        val printWriter = PrintWriter(FileWriter(file))
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonArray))
        printWriter.close()
    }

    fun addCrackedAccount(name: String) {
        val crackedAccount = CrackedAccount()
        crackedAccount.name = name

        if (accountExists(crackedAccount))
            return

        accounts.add(crackedAccount)
    }

    fun addAccount(account: MinecraftAccount) = accounts.add(account)

    fun removeAccount(selectedSlot: Int) = accounts.removeAt(selectedSlot)

    fun removeAccount(account: MinecraftAccount) = accounts.remove(account)

    fun accountExists(newAccount: MinecraftAccount): Boolean {
        for (minecraftAccount in accounts)
            if (minecraftAccount::class.java.name.equals(newAccount::class.java.name) && minecraftAccount.name == newAccount.name)
                return true
        return false
    }

    private fun clearAccounts() = accounts.clear()
}
    