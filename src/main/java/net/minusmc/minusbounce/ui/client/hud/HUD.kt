/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minusmc.minusbounce.ui.client.hud.designer.GuiHudDesigner
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minusmc.minusbounce.ui.client.hud.element.elements.*
import net.minusmc.minusbounce.utils.ClassUtils
import net.minusmc.minusbounce.utils.ClientUtils
import net.minusmc.minusbounce.utils.MinecraftInstance
import org.lwjgl.opengl.GL11
import kotlin.math.max
import kotlin.math.min

open class HUD : MinecraftInstance() {

    val elements = mutableListOf<Element>()
    val notifications = mutableListOf<Notification>()

    companion object {
        val elements = ClassUtils.resolvePackage("${HUD::class.java.`package`.name}.element.elements", Element::class.java).toTypedArray()

        /**
         * Create default HUD
         */
        @JvmStatic
        fun createDefault() = HUD()
                .addElement(Text.defaultClient())
                .addElement(Arraylist())
                .addElement(Scoreboard())
                .addElement(Armor())
                .addElement(Notifications())
    }

    /**
     * Render all elements
     */
    fun render(designer: Boolean) {
        elements.forEach { element ->
            GL11.glPushMatrix()

            if (!element.info.disableScale && element.scale != 1F)
                GL11.glScalef(element.scale, element.scale, element.scale)

            GL11.glTranslated(element.renderX, element.renderY, 0.0)

            try {
                element.border = element.drawElement()

                if (designer)
                    element.border?.draw()
            } catch (ex: Exception) {
                ClientUtils.logger
                        .error("Something went wrong while drawing ${element.name} element in HUD.", ex)
            }

            GL11.glScalef(1f, 1f, 1f)
            GL11.glPopMatrix()
        }
    }

    /**
     * Update all elements
     */
    fun update() {
        for (element in elements)
            element.updateElement()
    }

    fun handleDamage(ent: EntityPlayer) {
        for (element in elements) {
            if (element.info.retrieveDamage)
                element.handleDamage(ent)
        }
    }

    /**
     * Handle mouse click
     */
    fun handleMouseClick(mouseX: Int, mouseY: Int, button: Int) {
        for (element in elements)
            element.handleMouseClick((mouseX / element.scale) - element.renderX, (mouseY / element.scale)
                    - element.renderY, button)

        if (button == 0) {
            for (element in elements.reversed()) {
                if (!element.isInBorder((mouseX / element.scale) - element.renderX,
                                (mouseY / element.scale) - element.renderY))
                    continue

                element.drag = true
                elements.remove(element)
                elements.add(element)
                elements.sortBy { -it.info.priority }
                break
            }
        }
    }

    /**
     * Handle released mouse key
     */
    fun handleMouseReleased() {
        for (element in elements)
            element.drag = false
    }

    /**
     * Handle mouse move
     */
    fun handleMouseMove(mouseX: Int, mouseY: Int) {
        if (mc.currentScreen !is GuiHudDesigner)
            return

        val scaledResolution = ScaledResolution(mc)

        for (element in elements) {
            val scaledX = mouseX / element.scale
            val scaledY = mouseY / element.scale
            val prevMouseX = element.prevMouseX
            val prevMouseY = element.prevMouseY

            element.prevMouseX = scaledX
            element.prevMouseY = scaledY

            if (element.drag) {
                val moveX = scaledX - prevMouseX
                val moveY = scaledY - prevMouseY

                if (moveX == 0F && moveY == 0F)
                    continue

                val border = element.border ?: continue

                val minX = min(border.x, border.x2) + 1
                val minY = min(border.y, border.y2) + 1

                val maxX = max(border.x, border.x2) - 1
                val maxY = max(border.y, border.y2) - 1

                val width = scaledResolution.scaledWidth / element.scale
                val height = scaledResolution.scaledHeight / element.scale

                if ((element.renderX + minX + moveX >= 0.0 || moveX > 0) && (element.renderX + maxX + moveX <= width || moveX < 0))
                    element.renderX = moveX.toDouble()
                if ((element.renderY + minY + moveY >= 0.0 || moveY > 0) && (element.renderY + maxY + moveY <= height || moveY < 0))
                    element.renderY = moveY.toDouble()
            }
        }
    }

    /**
     * Handle incoming key
     */
    fun handleKey(c: Char, keyCode: Int) {
        for (element in elements)
            element.handleKey(c, keyCode)
    }

    /**
     * Add [element] to HUD
     */
    fun addElement(element: Element): HUD {
        elements.add(element)
        elements.sortBy { -it.info.priority }
        element.updateElement()
        return this
    }

    /**
     * Remove [element] from HUD
     */
    fun removeElement(element: Element): HUD {
        element.destroyElement()
        elements.remove(element)
        elements.sortBy { -it.info.priority }
        return this
    }

    /**
     * Clear all elements
     */
    fun clearElements() {
        for (element in elements)
            element.destroyElement()

        elements.clear()
    }

    /**
     * Add [notification]
     */
    fun addNotification(notification: Notification) = elements.any { it is Notifications } && notifications.add(notification)

    /**
     * Remove [notification]
     */
    fun removeNotification(notification: Notification) = notifications.remove(notification)

}
