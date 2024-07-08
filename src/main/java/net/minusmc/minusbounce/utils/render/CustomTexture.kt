/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.render

import net.minecraft.client.renderer.texture.TextureUtil
import org.lwjgl.opengl.GL11
import java.awt.image.BufferedImage

class CustomTexture(private val image: BufferedImage) {
    private var unloaded = false
    private var textureId = -1

    /**
     * @return ID of this texture loaded into memory
     * @throws IllegalStateException If the texture was unloaded via [.unload]
     */
    fun getTextureId(): Int {
        check(!unloaded) { "Texture unloaded" }
        if (textureId == -1) textureId = TextureUtil.uploadTextureImageAllocate(
            TextureUtil.glGenTextures(),
            image, true, true
        )
        return textureId
    }

    fun unload() {
        if (!unloaded) {
            GL11.glDeleteTextures(textureId)
            unloaded = true
        }
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        unload()
    }
}
