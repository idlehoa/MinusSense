/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.altmanager.menus

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.event.SessionEvent
import net.minusmc.minusbounce.ui.client.altmanager.GuiAltManager
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import net.minecraft.util.Session
import org.lwjgl.input.Keyboard
import java.io.IOException

class GuiChangeName(private val prevGui: GuiAltManager) : GuiScreen() {
    private var name: GuiTextField? = null
    private var status: String? = null
    override fun initGui() {
        Keyboard.enableRepeatEvents(true)
        buttonList.add(GuiButton(1, width / 2 - 100, height / 4 + 96, "Change"))
        buttonList.add(GuiButton(0, width / 2 - 100, height / 4 + 120, "Back"))
        name = GuiTextField(2, Fonts.font40, width / 2 - 100, 60, 200, 20)
        name!!.isFocused = true
        name!!.text = mc.getSession().username
        name!!.maxStringLength = 16
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)
        RenderUtils.drawRect(30F, 30F, width - 30F, height - 30F, Int.MIN_VALUE)
        Fonts.font40.drawCenteredString("Change Name", width / 2.0f, 34f, 0xffffff)
        Fonts.font40.drawCenteredString(
            (if (status == null) "" else status)!!,
            width / 2.0f,
            height / 4.0f + 84,
            0xffffff
        )
        name!!.drawTextBox()
        if (name!!.text.isEmpty() && !name!!.isFocused) Fonts.font40.drawCenteredString(
            "§7Username",
            width / 2.0f - 74,
            66f,
            0xffffff
        )
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    @Throws(IOException::class)
    public override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(prevGui)
            1 -> {
                if (name!!.text.isEmpty()) {
                    status = "§cEnter a name!"
                    return
                }
                if (!name!!.text.equals(mc.getSession().username, ignoreCase = true)) {
                    status = "§cJust change the upper and lower case!"
                    return
                }
                mc.session = Session(
                    name!!.text,
                    mc.getSession().playerID,
                    mc.getSession().token,
                    mc.getSession().sessionType.name
                )
                MinusBounce.eventManager.callEvent(SessionEvent())
                status = "§aChanged name to §7" + name!!.text + "§c."
                prevGui.status = status as String
                mc.displayGuiScreen(prevGui)
            }
        }
    }

    @Throws(IOException::class)
    public override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            mc.displayGuiScreen(prevGui)
            return
        }
        if (name!!.isFocused) name!!.textboxKeyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    @Throws(IOException::class)
    public override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        name!!.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun updateScreen() {
        name!!.updateCursorCounter()
        super.updateScreen()
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
        super.onGuiClosed()
    }
}
