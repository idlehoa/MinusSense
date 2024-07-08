/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.Listenable
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.event.TickEvent
import net.minusmc.minusbounce.utils.timer.MSTimer

class PacketUtils : MinecraftInstance(), Listenable {
    @EventTarget
    fun onPacket(event: PacketEvent) {
        handlePacket(event.packet)
    }

    @EventTarget
    fun onTick(event: TickEvent?) {
        if (packetTimer.hasTimePassed(1000L)) {
            avgInBound = inBound
            avgOutBound = outBound
            outBound = 0
            inBound = outBound
            packetTimer.reset()
        }
        if (mc.thePlayer == null || mc.theWorld == null) {
            //reset all checks
            wdVL = 0
            transCount = 0
            wdTimer.reset()
        } else if (wdTimer.hasTimePassed(100L)) { // watchdog active when the transaction poll rate reaches about 100ms/packet.
            wdVL += if (transCount > 0) 1 else -1
            transCount = 0
            if (wdVL > 10) wdVL = 10
            if (wdVL < 0) wdVL = 0
            wdTimer.reset()
        }
    }

    /**
     * @return wow
     */
    override fun handleEvents(): Boolean {
        return true
    }

    companion object {
        var inBound = 0
        var outBound = 0
        var avgInBound = 0
        var avgOutBound = 0
        private val packets = ArrayList<Packet<INetHandlerPlayServer>>()
        private val packetTimer = MSTimer()
        private val wdTimer = MSTimer()
        private var transCount = 0
        private var wdVL = 0
        private fun isInventoryAction(action: Short): Boolean {
            return action > 0 && action < 100
        }

        val isWatchdogActive: Boolean
            get() = wdVL >= 8

        fun handlePacket(packet: Packet<*>) {
            if (packet.javaClass.getSimpleName().startsWith("C")) outBound++ else if (packet.javaClass.getSimpleName()
                    .startsWith("S")
            ) inBound++
            if (packet is S32PacketConfirmTransaction) {
                if (!isInventoryAction(packet.actionNumber)) transCount++
            }
        }

        /*
     * This code is from UnlegitMC/FDPClient. Please credit them when using this code in your repository.
     */
        fun sendPacketNoEvent(packet: Packet<INetHandlerPlayServer>) {
            packets.add(packet)
            mc.netHandler.addToSendQueue(packet)
        }

        fun handleSendPacket(packet: Packet<*>): Boolean {
            if (packets.contains(packet)) {
                packets.remove(packet)
                handlePacket(packet) // make sure not to skip silent packets.
                return true
            }
            return false
        }
    }
}
