/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.misc

import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S45PacketTitle
import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.PacketEvent
import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.event.WorldEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.ui.client.hud.element.elements.Notification
import net.minusmc.minusbounce.utils.PacketUtils
import net.minusmc.minusbounce.utils.timer.MSTimer
import net.minusmc.minusbounce.value.IntegerValue
import net.minusmc.minusbounce.value.TextValue

@ModuleInfo(name = "AutoLogin", spacedName = "Auto Login", description = "Automatically login into some servers for you.", category = ModuleCategory.MISC)
class AutoLogin : Module() {

	private val password = TextValue("Password", "example@01")
	private val regRegex = TextValue("Register-Regex", "/register")
	private val loginRegex = TextValue("Login-Regex", "/login")
	private val regCmd = TextValue("Register-Cmd", "/register %p %p")
	private val loginCmd = TextValue("Login-Cmd", "/login %p")

	private val delayValue = IntegerValue("Delay", 5000, 0, 5000, "ms")

	private val loginPackets = arrayListOf<C01PacketChatMessage>()
	private val registerPackets = arrayListOf<C01PacketChatMessage>()
	private val regTimer = MSTimer()
	private val logTimer = MSTimer()

	override fun onEnable() = resetEverything()

	@EventTarget
	fun onWorld(event: WorldEvent) = resetEverything()

	@EventTarget
	fun onUpdate(event: UpdateEvent) {
		if (registerPackets.isEmpty())
			regTimer.reset()
		else if (regTimer.hasTimePassed(delayValue.get().toLong())) {
			for (packet in registerPackets)
				PacketUtils.sendPacketNoEvent(packet)
			MinusBounce.hud.addNotification(Notification("Successfully registered.", Notification.Type.SUCCESS))
			registerPackets.clear()
			regTimer.reset()
		}

		if (loginPackets.isEmpty())
			logTimer.reset()
		else if (logTimer.hasTimePassed(delayValue.get().toLong())) {
			for (packet in loginPackets)
				PacketUtils.sendPacketNoEvent(packet)
			MinusBounce.hud.addNotification(Notification("Successfully logined.", Notification.Type.SUCCESS))
			loginPackets.clear()
			logTimer.reset()
		}
	}

    @EventTarget
    fun onPacket(event: PacketEvent) {
		if (mc.thePlayer == null)
			return

		val packet = event.packet

    	if (packet is S45PacketTitle) {
            val messageOrigin = packet.message ?: return
            val message: String = messageOrigin.unformattedText

    		if (message.contains(loginRegex.get(), true))
    			sendLogin(loginCmd.get().replace("%p", password.get(), true))

    		if (message.contains(regRegex.get(), true))
    			sendRegister(regCmd.get().replace("%p", password.get(), true))
    	}

    	if (packet is S02PacketChat) {
            val message: String = packet.chatComponent.unformattedText

    		if (message.contains(loginRegex.get(), true))
    			sendLogin(loginCmd.get().replace("%p", password.get(), true))

    		if (message.contains(regRegex.get(), true))
    			sendRegister(regCmd.get().replace("%p", password.get(), true))
    	}
    }

	private fun sendLogin(str: String) = loginPackets.add(C01PacketChatMessage(str))
	private fun sendRegister(str: String) = registerPackets.add(C01PacketChatMessage(str))

	private fun resetEverything() {
		registerPackets.clear()
		loginPackets.clear()
		regTimer.reset()
		logTimer.reset()
	}

    override val tag: String
        get() = password.get()

}
