/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.gui;

import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiLanguage.class)
public class MixinGuiLanguage extends GuiScreen {
    @Override
    public void onGuiClosed() {
        mc.ingameGUI.getChatGUI().refreshChat();
    }
}
