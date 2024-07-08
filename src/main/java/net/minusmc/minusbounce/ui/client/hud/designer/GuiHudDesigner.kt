/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.designer

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import kotlin.math.min

class GuiHudDesigner : GuiScreen() {

    private var editorPanel = EditorPanel(this, 2, 2)

    var selectedElement: Element? = null
    private var buttonAction = false
    private var wheel = 0
    
    override fun initGui() {
        Keyboard.enableRepeatEvents(true)
        editorPanel = EditorPanel(this, width / 2, height / 2)
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        MinusBounce.hud.render(true)
        MinusBounce.hud.handleMouseMove(mouseX, mouseY)

        if (!MinusBounce.hud.elements.contains(selectedElement))
            selectedElement = null

        wheel = Mouse.getDWheel()

        editorPanel.drawPanel(mouseX, mouseY, wheel)
        
    if (wheel != 0) {
         for (element in MinusBounce.hud.elements) {
            if (element.isInBorder(mouseX / element.scale - element.renderX,
                             mouseY / element.scale - element.renderY)) {
                element.scale += if (wheel > 0) 0.05f else -0.05f
                 
                 if (Keyboard.isKeyDown(200)) {
                      wheel = 1
                 } else if (Keyboard.isKeyDown(208)) {
                      wheel = -1
                 } 
                 break
            }
         }
    }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        if (buttonAction) {
            buttonAction = false
            return
        }

        MinusBounce.hud.handleMouseClick(mouseX, mouseY, mouseButton)

        if (!(mouseX >= editorPanel.x && mouseX <= editorPanel.x + editorPanel.width && mouseY >= editorPanel.y &&
                        mouseY <= editorPanel.y + min(editorPanel.realHeight, 200))) {
            selectedElement = null
            editorPanel.create = false
        }

        if (mouseButton == 0) {
            for (element in MinusBounce.hud.elements) {
                if (element.isInBorder(mouseX / element.scale - element.renderX, mouseY / element.scale - element.renderY)) {
                    selectedElement = element
                    break
                }
            }
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        MinusBounce.hud.handleMouseReleased()
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
        MinusBounce.fileManager.saveConfig(MinusBounce.fileManager.hudConfig)

        super.onGuiClosed()
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        when (keyCode) {
        	Keyboard.KEY_UP -> wheel = 1
            
            Keyboard.KEY_DOWN -> wheel = -1
            
            Keyboard.KEY_DELETE -> if (selectedElement != null)
                MinusBounce.hud.removeElement(selectedElement!!)

            Keyboard.KEY_ESCAPE -> {
                selectedElement = null
                editorPanel.create = false
            }

            else -> MinusBounce.hud.handleKey(typedChar, keyCode)
        }

        super.keyTyped(typedChar, keyCode)
    }
}
