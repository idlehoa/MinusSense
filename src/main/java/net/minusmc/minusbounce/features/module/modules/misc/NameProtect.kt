/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.misc

import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.TextEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.utils.ClientUtils
import net.minusmc.minusbounce.utils.misc.StringUtils
import net.minusmc.minusbounce.utils.render.ColorUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.TextValue
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

@ModuleInfo(name = "NameProtect", spacedName = "Name Protect", description = "Changes player names client-side.", category = ModuleCategory.MISC)
class NameProtect: Module() {

    private val fakeNameValue = TextValue("FakeName", "&cMe")
    private val allFakeNameValue = TextValue("AllPlayersFakeName", "Censored")
    private val selfValue = BoolValue("Yourself", true)
    private val tagValue = BoolValue("Tag", false)
    val allPlayersValue = BoolValue("AllPlayers", false)
    val skinProtectValue = BoolValue("SkinProtect", false)
    val customSkinValue = BoolValue("CustomSkin", false) { skinProtectValue.get() }

    lateinit var skinImage: ResourceLocation

    init {
        val skinFile = File(MinusBounce.fileManager.dir, "cskin.png")
        if (skinFile.isFile()) {
            try {
                val bufferedImage = ImageIO.read(FileInputStream(skinFile))
                if (bufferedImage != null) {
                    skinImage = ResourceLocation(MinusBounce.CLIENT_NAME.lowercase() + "/cskin.png")
                    mc.textureManager.loadTexture(skinImage, DynamicTexture(bufferedImage))
                    ClientUtils.logger.info("Loaded custom skin for NameProtect.")
                }
            } catch (e: Exception) {
                ClientUtils.logger.error("Failed to load custom skin.", e)
            }
        }
    }

    @EventTarget
    fun onText(event: TextEvent) {
        mc.thePlayer ?: return
        val namePlayer = mc.thePlayer.name
        val text = event.text ?: return
        if (text.contains("§8[§9§l" + MinusBounce.CLIENT_NAME + "§8] §3") || text.startsWith("/") || text.startsWith(MinusBounce.commandManager.prefix + ""))
            return
        
        val tag = if (selfValue.get()) {
            if (tagValue.get()) StringUtils.injectAirString(namePlayer) + " §7(§r" + ColorUtils.translateAlternateColorCodes(fakeNameValue.get() + "§r§7)")
            else ColorUtils.translateAlternateColorCodes(fakeNameValue.get()) + "§r"
        } else namePlayer

        event.text = StringUtils.replace(text, namePlayer, tag)

        if(allPlayersValue.get())
            for(playerInfo in mc.netHandler.playerInfoMap)
                event.text = StringUtils.replace(
                    text,
                    playerInfo.gameProfile.name,
                    ColorUtils.translateAlternateColorCodes(allFakeNameValue.get()) + "§f"
                )
    }

}
