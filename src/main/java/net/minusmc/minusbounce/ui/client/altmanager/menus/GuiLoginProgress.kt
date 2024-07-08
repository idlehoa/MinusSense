/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.altmanager.menus

import me.liuli.elixir.account.MinecraftAccount
import net.minecraft.client.gui.GuiScreen
import net.minusmc.minusbounce.ui.client.altmanager.GuiAltManager.Companion.login

class GuiLoginProgress(minecraftAccount: MinecraftAccount, success: () -> Unit, error: (Exception) -> Unit, done: () -> Unit) : GuiScreen() {

    init {
        login(minecraftAccount, success, error, done)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        //RenderUtils.drawLoadingCircle((scaledResolution.scaledWidth / 2).toFloat(), (scaledResolution.scaledHeight / 4 + 70).toFloat())
        drawCenteredString(fontRendererObj, "Logging into account...", width / 2, height / 2 - 60, 16777215)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

}
