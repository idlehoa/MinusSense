/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.file.configs

import com.google.gson.*
import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.features.module.modules.misc.AutoDisable.DisableEvent
import net.minusmc.minusbounce.file.FileConfig
import net.minusmc.minusbounce.file.FileManager
import java.io.*

class ModulesConfig(file: File?) : FileConfig(file!!) {
    /**
     * Load config from file
     *
     * @throws IOException
     */
    override fun loadConfig() {
        val jsonElement = JsonParser().parse(BufferedReader(FileReader(file)))
        if (jsonElement is JsonNull) return
        val entryIterator: Iterator<Map.Entry<String, JsonElement>> =
            jsonElement.getAsJsonObject().entrySet().iterator()
        while (entryIterator.hasNext()) {
            val (key, value) = entryIterator.next()
            val module = MinusBounce.moduleManager.getModule(key)
            if (module != null) {
                val jsonModule = value as JsonObject
                module.state = jsonModule["State"].asBoolean
                module.keyBind = jsonModule["KeyBind"].asInt
                if (jsonModule.has("Array")) module.array = jsonModule["Array"].asBoolean
                if (jsonModule.has("AutoDisable")) {
                    module.autoDisables.clear()
                    try {
                        val jsonAD = jsonModule.getAsJsonArray("AutoDisable")
                        if (jsonAD.size() > 0) for (i in 0 until jsonAD.size()) {
                            try {
                                val disableEvent = DisableEvent.valueOf(jsonAD[i].asString)
                                module.autoDisables.add(disableEvent)
                            } catch (e: Exception) {
                                // nothing
                            }
                        }
                    } catch (e: Exception) {
                        //nothing.
                    }
                }
            }
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    override fun saveConfig() {
        val jsonObject = JsonObject()
        for (module in MinusBounce.moduleManager.modules) {
            val jsonMod = JsonObject()
            jsonMod.addProperty("State", module.state)
            jsonMod.addProperty("KeyBind", module.keyBind)
            jsonMod.addProperty("Array", module.array)
            val jsonAD = JsonArray()
            for (e in module.autoDisables) {
                jsonAD.add(JsonPrimitive(e.toString()))
            }
            jsonMod.add("AutoDisable", jsonAD)
            jsonObject.add(module.name, jsonMod)
        }
        val printWriter = PrintWriter(FileWriter(file))
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject))
        printWriter.close()
    }
}
