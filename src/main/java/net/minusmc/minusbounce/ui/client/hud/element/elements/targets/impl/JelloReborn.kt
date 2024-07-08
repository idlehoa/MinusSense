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
import net.minusmc.minusbounce.utils.render.RenderUtils
import java.awt.Color

class JelloReborn(inst: Target): TargetStyle("JelloReborn", inst, false) {

    override fun drawTarget(entity: EntityPlayer) {
        updateAnim(entity.health)

        val healthString = "${decimalFormat2.format(entity.health)} Health"

        // background
        RenderUtils.newDrawRect(1F, 1F, 145F, 48F, getColor(Color(82, 82, 82)).rgb)

        // health bar
        RenderUtils.newDrawRect(4F, 40F, 3.5F + (easingHealth / entity.maxHealth).coerceIn(0F, 1F) * 138F, 43F, targetInstance.barColor.rgb)

        // name
        Fonts.fontSFUI40.drawString(entity.name, 41F, 12F, getColor(-1).rgb)

        // Info
        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) {
            // actual head
            drawHead(mc.netHandler.getPlayerInfo(entity.uniqueID).locationSkin, 5, 5, 32, 32, 1F - targetInstance.getFadeProgress())

            Fonts.fontSFUI40.drawString(healthString, 41F, 24F, getColor(-1).rgb)
        }
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        return Border(0F, 0F, 146F, 49F)
    }
}
