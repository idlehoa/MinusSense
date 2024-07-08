/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import com.google.gson.JsonObject
import io.netty.util.concurrent.Future
import net.minecraft.client.settings.GameSettings
import net.minecraft.network.NetworkManager
import net.minecraft.network.login.client.C01PacketEncryptionResponse
import net.minecraft.network.login.server.S01PacketEncryptionRequest
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.security.PublicKey
import javax.crypto.SecretKey

object ClientUtils : MinecraftInstance() {
    val logger = LogManager.getLogger("MinusBounce")
    private var fastRenderField: Field? = null

    init {
        try {
            fastRenderField = GameSettings::class.java.getDeclaredField("ofFastRender")
            if (!fastRenderField!!.isAccessible()) fastRenderField!!.setAccessible(true)
        } catch (ignored: NoSuchFieldException) {
        }
    }

    fun disableFastRender() {
        try {
            if (fastRenderField != null) {
                if (!fastRenderField!!.isAccessible) fastRenderField!!.setAccessible(true)
                fastRenderField!!.setBoolean(mc.gameSettings, false)
            }
        } catch (ignored: IllegalAccessException) {
        }
    }

    fun sendEncryption(
        networkManager: NetworkManager,
        secretKey: SecretKey?,
        publicKey: PublicKey?,
        encryptionRequest: S01PacketEncryptionRequest
    ) {
        networkManager.sendPacket(C01PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.verifyToken),
            { p_operationComplete_1_: Future<in Void?>? ->
                networkManager.enableEncryption(
                    secretKey
                )
            })
    }

    fun displayChatMessage(message: String) {
        if (mc.thePlayer == null) {
            logger.info("(MCChat)$message")
            return
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("text", message)
        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()))
    }
}
