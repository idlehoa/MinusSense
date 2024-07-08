package net.minusmc.minusbounce.rpc

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import net.minusmc.minusbounce.MinusBounce
import net.ccbluex.liquidbounce.ui.client.gui.GuiMainMenu
import net.minusmc.minusbounce.utils.ClientUtils
import net.minusmc.minusbounce.utils.MinecraftInstance
import net.minusmc.minusbounce.utils.ServerUtils
import net.minusmc.minusbounce.utils.HttpUtils
import net.minecraft.client.gui.GuiMultiplayer
import org.json.JSONObject
import java.time.OffsetDateTime
import kotlin.concurrent.thread

object CrossSineRPC : MinecraftInstance(){

    private val ipcClient = IPCClient(1218820754552918096)
    private val timestamp = OffsetDateTime.now()
    private var running = false


    fun run() {
        ipcClient.setListener(object : IPCListener {
            override fun onReady(client: IPCClient?) {
                running = true
                thread {
                    while (running) {
                        update()
                        try {
                            Thread.sleep(1000L)
                        } catch (ignored: InterruptedException) {
                        }
                    }
                }
            }

            override fun onClose(client: IPCClient?, json: JSONObject?) {
                running = false
            }
        })
        try {
            ipcClient.connect()
        } catch (e: Exception) {
            ClientUtils.logError("DiscordRPC failed to start")
        } catch (e: RuntimeException) {
            ClientUtils.logError("DiscordRPC failed to start")
        }
    }

    private fun update() {
        val builder = RichPresence.Builder()
        builder.setStartTimestamp(timestamp)

        builder.setLargeImage("https://github.com/idlehoa/minusskid/blob/main/src/main/resources/assets/minecraft/minusbounce/big.png", "MinusSense")

        if (mc.currentScreen is GuiMainMenu) {
            builder.setDetails("MainMenu")
        }
        else
        if (mc.currentScreen is GuiMultiplayer) {
            builder.setDetails("Selecting Server")
        }
        else
        if (mc.theWorld != null && mc.theWorld.isRemote) {
            builder.setDetails("Playing : ${ServerUtils.getRemoteIp()}")
        }
        else
            builder.setDetails(mc.session.username + " is best player")

        builder.setState("Version : Build ${MinusBounce.CLIENT_VERSION}")
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.sendRichPresence(builder.build())
    }

    fun stop() {
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.close()
    }
}