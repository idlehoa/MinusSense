/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets.impl

import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.elements.Target
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.extensions.darker
import net.minusmc.minusbounce.utils.extensions.getDistanceToEntityBox
import net.minusmc.minusbounce.utils.render.BlendUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import java.awt.Color

class Exhibition(inst: Target): TargetStyle("Exhibition", inst, false) {

    override fun drawTarget(entity: EntityPlayer) {
        val font = Fonts.fontTahoma
        val minWidth = 126F.coerceAtLeast(47F + font.getStringWidth(entity.name))

        RenderUtils.drawExhiRect(0F, 0F, minWidth, 45F, 1F - targetInstance.getFadeProgress())

        RenderUtils.drawRect(2.5F, 2.5F, 42.5F, 42.5F, getColor(Color(59, 59, 59)).rgb)
        RenderUtils.drawRect(3F, 3F, 42F, 42F, getColor(Color(19, 19, 19)).rgb)

        GL11.glColor4f(1f, 1f, 1f, 1f - targetInstance.getFadeProgress())
        RenderUtils.drawEntityOnScreen(22, 40, 16, entity)

        font.drawString(entity.name, 46, 5, getColor(-1).rgb)

        val barLength = 70F * (entity.health / entity.maxHealth).coerceIn(0F, 1F)
        RenderUtils.drawRect(45F, 14F, 45F + 70F, 18F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth).darker(0.3F)).rgb)
        RenderUtils.drawRect(45F, 14F, 45F + barLength, 18F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth)).rgb)

        for (i in 0..9)
            RenderUtils.drawRectBasedBorder(45F + i * 7F, 14F, 45F + (i + 1) * 7F, 18F, 0.5F, getColor(Color.black).rgb)

        Fonts.fontTahomaSmall.drawString("HP:${entity.health.toInt()} | Dist:${mc.thePlayer.getDistanceToEntityBox(entity).toInt()}", 45F, 21F, getColor(-1).rgb)

        GlStateManager.resetColor()
        GL11.glPushMatrix()
        GL11.glColor4f(1f, 1f, 1f, 1f - targetInstance.getFadeProgress())
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderHelper.enableGUIStandardItemLighting()

        val renderItem = mc.renderItem

        var x = 45
        val y = 28

        for (index in 3 downTo 0) {
            val stack = entity.inventory.armorInventory[index] ?: continue

            if (stack.item == null)
                continue

            renderItem.renderItemIntoGUI(stack, x, y)
            renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
            RenderUtils.drawExhiEnchants(stack, x.toFloat(), y.toFloat())

            x += 16
        }

        val mainStack = entity.heldItem
        if (mainStack != null && mainStack.item != null) {
            renderItem.renderItemIntoGUI(mainStack, x, y)
            renderItem.renderItemOverlays(mc.fontRendererObj, mainStack, x, y)
            RenderUtils.drawExhiEnchants(mainStack, x.toFloat(), y.toFloat())
        }

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        entity ?: return Border(0F, 0F, 126F, 45F)

        val font = Fonts.fontTahoma
        val minWidth = 126F.coerceAtLeast(47F + font.getStringWidth(entity.name))

        return Border(0F, 0F, minWidth, 45F)
    }

}
