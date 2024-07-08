/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets.impl

import net.minecraft.entity.player.EntityPlayer
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.elements.Target
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.extensions.getDistanceToEntityBox
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.value.IntegerValue
import java.awt.Color

class Mossware(inst: Target): TargetStyle("Mossware", inst, false) {

    val alpha = IntegerValue("Background-Alpha", 100, 0, 255)
    private val borderalpha = IntegerValue("Border-Alpha", 100, 0, 255)

    override fun drawTarget(entity: EntityPlayer) {
        updateAnim(entity.health)

        val healthString = "Health: ${decimalFormat2.format(entity.health)}"
        val font = Fonts.fontSFUI40
        val width = (38 + (entity.name?.let(font::getStringWidth) ?: 0))
                .coerceAtLeast(118)
                .toFloat()

        // background
        RenderUtils.drawMosswareRect(0F, 0F, width, 50F, 10F, Color(0,0,0,borderalpha.get()).rgb, Color(0,0,0,alpha.get()).rgb)

        // health bar
        // RenderUtils.newDrawRect(4F, 40F, 3.5F + (easingHealth / entity.maxHealth).coerceIn(0F, 1F) * 138F, 43F, targetInstance.barColor.rgb)
        RenderUtils.drawRect(0F, 34F, (easingHealth / entity.maxHealth) * width, 36F, targetInstance.barColor.rgb)
        
        // Win lose system
            if ((easingHealth - entity.health) > 0)
                 font.drawStringWithShadow("Winning", 42F, 40F, Color(135, 255, 45).rgb)
            if ((easingHealth - entity.health) < 0)
                font.drawStringWithShadow("Losing", 42F, 40F, Color(200, 20, 20).rgb)
            if ((easingHealth - entity.health) == 0.toFloat()) {
                font.drawStringWithShadow("None", 42F, 40F, Color(255, 213, 20).rgb)
            }

        // name
        font.drawStringWithShadow(entity.name, 36F, 3F, getColor(-1).rgb)

        // Info
        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) {
            drawMosswareHead(mc.netHandler.getPlayerInfo(entity.uniqueID).locationSkin, 30, 30)

            font.drawString("Distance: ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(entity))}", 36F, 15F, getColor(-1).rgb)
            font.drawStringWithShadow(healthString, 36F, 24F, getColor(-1).rgb)
        }
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        return Border(0F, 0F, 120F, 36F)
    }
}
