/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements.targets.impl

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.elements.Target
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.utils.Particle
import net.minusmc.minusbounce.ui.client.hud.element.elements.targets.utils.ShapeType
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.extensions.getDistanceToEntityBox
import net.minusmc.minusbounce.utils.misc.RandomUtils
import net.minusmc.minusbounce.utils.render.BlendUtils
import net.minusmc.minusbounce.utils.render.ColorUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.render.Stencil
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.FloatValue
import net.minusmc.minusbounce.value.IntegerValue
import net.minusmc.minusbounce.value.ListValue
import org.lwjgl.opengl.GL11
import java.awt.Color

class Rice(inst: Target): TargetStyle("Rice", inst, true) {

    // Bar gradient
    private val gradientLoopValue = IntegerValue("GradientLoop", 4, 1, 40)
    private val gradientDistanceValue = IntegerValue("GradientDistance", 50, 1, 200)
    private val gradientRoundedBarValue = BoolValue("GradientRoundedBar", true)

    val riceParticle = BoolValue("Particle", true)
    private val riceParticleSpin = BoolValue("ParticleSpin", true) { riceParticle.get() }
    private val generateAmountValue = IntegerValue("GenerateAmount", 10, 1, 40) { riceParticle.get() }
    private val riceParticleCircle = ListValue("Circle-Particles", arrayOf("Outline", "Solid", "None"), "Solid") { riceParticle.get() }
    private val riceParticleRect = ListValue("Rect-Particles", arrayOf("Outline", "Solid", "None"), "Outline") { riceParticle.get() }
    private val riceParticleTriangle = ListValue("Triangle-Particles", arrayOf("Outline", "Solid", "None"), "Outline") { riceParticle.get() }

    private val riceParticleSpeed = FloatValue("ParticleSpeed", 0.05F, 0.01F, 0.2F) { riceParticle.get() }
    private val riceParticleFade = BoolValue("ParticleFade", true) { riceParticle.get() }
    private val riceParticleFadingSpeed = FloatValue("ParticleFadingSpeed", 0.05F, 0.01F, 0.2F) { riceParticle.get() }

    private val particleRange = FloatValue("ParticleRange", 50f, 0f, 50f) { riceParticle.get() }
    val minParticleSize: FloatValue = object : FloatValue("MinParticleSize", 0.5f, 0f, 5f, { riceParticle.get() }) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxParticleSize.get()
            if (v < newValue) set(v)
        }
    }
    val maxParticleSize: FloatValue = object : FloatValue("MaxParticleSize", 2.5f, 0f, 5f, { riceParticle.get() }) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minParticleSize.get()
            if (v > newValue) set(v)
        }
    }

    val particleList = mutableListOf<Particle>()
    private var gotDamaged = false

    override fun drawTarget(entity: EntityPlayer) {
        updateAnim(entity.health)

        val font = Fonts.fontSFUI40
        val name = "Name: ${entity.name}"
        val info = "Distance: ${decimalFormat2.format(mc.thePlayer.getDistanceToEntityBox(entity))}"
        val healthName = decimalFormat2.format(easingHealth)
                    
        val length = (font.getStringWidth(name).coerceAtLeast(font.getStringWidth(info)).toFloat() + 40F).coerceAtLeast(125F)
        val maxHealthLength = font.getStringWidth(decimalFormat2.format(entity.maxHealth)).toFloat()

        // background
        RenderUtils.drawRoundedRect(0F, 0F, 10F + length, 55F, 8F, targetInstance.bgColor.rgb)

        // particle engine
        if (riceParticle.get()) {
            // adding system
            if (gotDamaged) {
                for (j in 0..(generateAmountValue.get())) {
                    val parSize = RandomUtils.nextFloat(minParticleSize.get(), maxParticleSize.get())
                    val parDistX = RandomUtils.nextFloat(-particleRange.get(), particleRange.get())
                    val parDistY = RandomUtils.nextFloat(-particleRange.get(), particleRange.get())
                    val firstChar = RandomUtils.random(1, "${if (riceParticleCircle.get().equals("none", true)) "" else "c"}${if (riceParticleRect.get().equals("none", true)) "" else "r"}${if (riceParticleTriangle.get().equals("none", true)) "" else "t"}")
                    val drawType = ShapeType.getTypeFromName(when (firstChar) {
                        "c" -> "c_${riceParticleCircle.get().lowercase()}"
                        "r" -> "r_${riceParticleRect.get().lowercase()}"
                        else -> "t_${riceParticleTriangle.get().lowercase()}"
                    }) ?: break

                    BlendUtils.blendColors(
                        floatArrayOf(0F, 1F),
                        arrayOf(Color.white, targetInstance.barColor),
                        if (RandomUtils.nextBoolean()) RandomUtils.nextFloat(0.5F, 1.0F) else 0F)?.let {
                        Particle(
                            it,
                            parDistX, parDistY, parSize, drawType)
                    }?.let { particleList.add(it) }
                }
                gotDamaged = false
            }

            // render and removing system
            val deleteQueue = mutableListOf<Particle>()

            particleList.forEach { particle ->
                if (particle.alpha > 0F)
                    particle.render(20F, 20F, riceParticleFade.get(), riceParticleSpeed.get(), riceParticleFadingSpeed.get(), riceParticleSpin.get())
                else
                    deleteQueue.add(particle)
            }

            particleList.removeAll(deleteQueue)
        }

        // custom head 
        val scaleHT = (entity.hurtTime.toFloat() / entity.maxHurtTime.coerceAtLeast(1).toFloat()).coerceIn(0F, 1F)
        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) 
            drawHead(mc.netHandler.getPlayerInfo(entity.uniqueID).locationSkin, 
                    5F + 15F * (scaleHT * 0.2F), 
                    5F + 15F * (scaleHT * 0.2F), 
                    1F - scaleHT * 0.2F, 
                    30, 30, 
                    1F, 0.4F + (1F - scaleHT) * 0.6F, 0.4F + (1F - scaleHT) * 0.6F,
                    1F - targetInstance.getFadeProgress())

        // player's info
        GlStateManager.resetColor()
        font.drawString(name, 39F, 11F, getColor(-1).rgb)
        font.drawString(info, 39F, 23F, getColor(-1).rgb)

        // gradient health bar
        val barWidth = (length - 5F - maxHealthLength) * (easingHealth / entity.maxHealth).coerceIn(0F, 1F)
        Stencil.write(false)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        if (gradientRoundedBarValue.get()) {
            if (barWidth > 0F)
                RenderUtils.fastRoundedRect(5F, 42F, 5F + barWidth, 48F, 3F)
        } else
            RenderUtils.quickDrawRect(5F, 42F, 5F + barWidth, 48F)

        GL11.glDisable(GL11.GL_BLEND)
        Stencil.erase(true)
        when (targetInstance.colorModeValue.get().lowercase()) {
            "custom", "health" -> RenderUtils.drawRect(5F, 42F, length - maxHealthLength, 48F, targetInstance.barColor.rgb)
            else -> for (i in 0 until gradientLoopValue.get()) {
                val barStart = i.toDouble() / gradientLoopValue.get().toDouble() * (length - 5F - maxHealthLength).toDouble()
                val barEnd = (i + 1).toDouble() / gradientLoopValue.get().toDouble() * (length - 5F - maxHealthLength).toDouble()
                RenderUtils.drawGradientSideways(5.0 + barStart, 42.0, 5.0 + barEnd, 48.0, getColorAtIndex(i), getColorAtIndex(i + 1))
            }
        }
        Stencil.dispose()

        GlStateManager.resetColor()
        font.drawString(healthName, 10F + barWidth, 41F, getColor(-1).rgb)
    }

    private fun getColorAtIndex(i: Int): Int {
        return getColor(when (targetInstance.colorModeValue.get()) {
            "Rainbow" -> RenderUtils.getRainbowOpaque(targetInstance.waveSecondValue.get(), targetInstance.saturationValue.get(), targetInstance.brightnessValue.get(), i * gradientDistanceValue.get())
            "Sky" -> RenderUtils.SkyRainbow(i * gradientDistanceValue.get(), targetInstance.saturationValue.get(), targetInstance.brightnessValue.get())
            "Slowly" -> ColorUtils.LiquidSlowly(System.nanoTime(), i * gradientDistanceValue.get(), targetInstance.saturationValue.get(), targetInstance.brightnessValue.get())!!.rgb
            "Fade" -> ColorUtils.fade(Color(targetInstance.redValue.get(), targetInstance.greenValue.get(), targetInstance.blueValue.get()), i * gradientDistanceValue.get(), 100).rgb
            else -> -1
        }).rgb
    }

    override fun handleDamage(entity: EntityPlayer) {
        gotDamaged = true
    }

    override fun handleBlur(entity: EntityPlayer) {
        val font = Fonts.fontSFUI40
        val name = "Name: ${entity.name}"
        val info = "Distance: ${decimalFormat2.format(mc.thePlayer.getDistanceToEntityBox(entity))}"            
        val length = (font.getStringWidth(name).coerceAtLeast(font.getStringWidth(info)).toFloat() + 40F).coerceAtLeast(125F)

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.fastRoundedRect(0F, 0F, 10F + length, 55F, 8F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    override fun handleShadowCut(entity: EntityPlayer) = handleBlur(entity)

    override fun handleShadow(entity: EntityPlayer) {
        val font = Fonts.fontSFUI40
        val name = "Name: ${entity.name}"
        val info = "Distance: ${decimalFormat2.format(mc.thePlayer.getDistanceToEntityBox(entity))}"            
        val length = (font.getStringWidth(name).coerceAtLeast(font.getStringWidth(info)).toFloat() + 40F).coerceAtLeast(125F)

        RenderUtils.originalRoundedRect(0F, 0F, 10F + length, 55F, 8F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: EntityPlayer?): Border {
        entity ?: return Border(0F, 0F, 135F, 55F)

        val font = Fonts.fontSFUI40
        val name = "Name: ${entity.name}"
        val info = "Distance: ${decimalFormat2.format(mc.thePlayer.getDistanceToEntityBox(entity))}"            
        val length = (font.getStringWidth(name).coerceAtLeast(font.getStringWidth(info)).toFloat() + 40F).coerceAtLeast(125F)

        return Border(0F, 0F, 10F + length, 55F)
    }

}
