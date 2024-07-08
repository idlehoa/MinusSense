package net.minusmc.minusbounce.features.module.modules.misc

import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.UpdateEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.utils.misc.RandomUtils
import net.minusmc.minusbounce.utils.timer.MSTimer
import net.minusmc.minusbounce.utils.timer.TimeUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.IntegerValue
import net.minusmc.minusbounce.value.TextValue
import kotlin.random.Random

@ModuleInfo(name = "Spammer", description = "Spams the chat with a given message.", category = ModuleCategory.MISC)
class Spammer: Module() {

    private val maxDelayValue: IntegerValue = object: IntegerValue("MaxDelay", 1000, 0, 5000, "ms") {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minDelayValue.get()

            if (i > newValue) set(i)
        }
    }

    private val minDelayValue: IntegerValue = object: IntegerValue("MinDelay", 500, 0, 5000, "ms") {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxDelayValue.get()

            if (i < newValue) set(i)
        }
    }

    private val messageValue = TextValue("Message", "Example text")
    private val customValue = BoolValue("Custom", false)

    private val msTimer = MSTimer()
    private var delay: Long = 0

    override fun onEnable() {
        delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get())
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (msTimer.hasTimePassed(delay)) {
        	val randomString = " >" + RandomUtils.randomString(5 + Random.nextInt(5)) + "<"
        	val message = if (customValue.get()) replace(messageValue.get()) else messageValue.get()
            mc.thePlayer.sendChatMessage(message + randomString)
            msTimer.reset()
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get())
        }
    }

    private fun replace(messageRaw: String): String {
        var message = messageRaw

        while(message.contains("%f"))
            message = message.substring(0, message.indexOf("%f")) + Random.nextFloat() + message.substring(message.indexOf("%f") + "%f".length)

        while(message.contains("%i"))
            message = message.substring(0, message.indexOf("%i")) + Random.nextInt(10000) + message.substring(message.indexOf("%i") + "%i".length)

        while(message.contains("%s"))
            message = message.substring(0, message.indexOf("%s")) + RandomUtils.randomString(Random.nextInt(8) + 1) + message.substring(message.indexOf("%s") + "%s".length)

        while(message.contains("%ss"))
            message = message.substring(0, message.indexOf("%ss")) + RandomUtils.randomString(Random.nextInt(4) + 1) + message.substring(message.indexOf("%ss") + "%ss".length)

        while(message.contains("%ls"))
            message = message.substring(0, message.indexOf("%ls")) + RandomUtils.randomString(Random.nextInt(15) + 1) + message.substring(message.indexOf("%ls") + "%ls".length)
        return message
    }

}
