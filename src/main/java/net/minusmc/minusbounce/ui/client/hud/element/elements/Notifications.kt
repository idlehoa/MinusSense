/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.ui.client.hud.element.elements

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minusmc.minusbounce.MinusBounce.hud
import net.minusmc.minusbounce.ui.client.hud.designer.GuiHudDesigner
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minusmc.minusbounce.ui.client.hud.element.ElementInfo
import net.minusmc.minusbounce.ui.client.hud.element.Side
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.render.AnimationUtils
import net.minusmc.minusbounce.utils.render.BlurUtils
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.render.Stencil
import net.minusmc.minusbounce.utils.timer.MSTimer
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.FloatValue
import net.minusmc.minusbounce.value.IntegerValue
import net.minusmc.minusbounce.value.ListValue
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import kotlin.math.pow

@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 0.0, y: Double = 30.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    val styleValue = ListValue("Style", arrayOf("Full", "Full2", "Novoline", "Compact", "Material", "Test"), "Material")
    val barValue = BoolValue("Bar", true) { styleValue.get().equals("material", true) }
    val bgAlphaValue = IntegerValue("Background-Alpha", 120, 0, 255) { !styleValue.get().equals("material", true) }

    val blurValue = BoolValue("Blur", false) { !styleValue.get().equals("material", true) }
    val blurStrength = FloatValue("Strength", 0F, 0F, 30F) {
        !styleValue.get().equals("material", true) && blurValue.get()
    }

    val hAnimModeValue = ListValue("H-Animation", arrayOf("LiquidBounce", "Smooth"), "LiquidBounce")
    val vAnimModeValue = ListValue("V-Animation", arrayOf("None", "Smooth"), "Smooth")
    val animationSpeed = FloatValue("Speed", 0.5F, 0.01F, 1F) {
        hAnimModeValue.get().equals("smooth", true) || vAnimModeValue.get().equals("smooth", true)
    }

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Tested", Notification.Type.INFO)

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        var animationY = 30F
        val notifications = mutableListOf<Notification>()

        for (i in hud.notifications)
            notifications.add(i)
        
        if (mc.currentScreen !is GuiHudDesigner || notifications.isNotEmpty()) {
            var indexz = 0
            for (i in notifications) {
                if (indexz == 0 && styleValue.get().equals("material", true) && side.vertical != Side.Vertical.DOWN) animationY -= i.notifHeight - (if (barValue.get()) 2F else 0F)
                i.drawNotification(animationY, this)
                if (indexz < notifications.size - 1) indexz++
                animationY += (when (styleValue.get().lowercase()) {
                    "compact" -> 20F
                    "full" -> 30F
                    "full2" -> 30F
                    "test" -> 30F
                    else -> (if (side.vertical == Side.Vertical.DOWN) i.notifHeight else notifications[indexz].notifHeight) + 5F + (if (barValue.get()) 2F else 0F)
                }) * (if (side.vertical == Side.Vertical.DOWN) 1F else -1F)
            }
        } else {
            exampleNotification.drawNotification(animationY - if (styleValue.get().equals("material", true) && side.vertical != Side.Vertical.DOWN) (exampleNotification.notifHeight - 5F - (if (barValue.get()) 2F else 0F)) else 0F, this)
        }

        if (mc.currentScreen is GuiHudDesigner) {

            exampleNotification.fadeState = Notification.FadeState.STAY
            exampleNotification.x = if (styleValue.get().equals("material", true)) 160F else exampleNotification.textLength + 8F

            if (exampleNotification.stayTimer.hasTimePassed(exampleNotification.displayTime)) 
                exampleNotification.stayTimer.reset()

            return getNotifBorder()
        }

        return null
    }

    private fun getNotifBorder() = when (styleValue.get().lowercase()) {
        "full" -> Border(-130F, -58F, 0F, -30F)
        "full2" -> Border(-130F, -58F, 0F, -30F)
        "test" -> Border(-130F, -58F, 0F, -30F)
        "compact" -> Border(-102F, -48F, 0F, -30F)
        else -> if (side.vertical == Side.Vertical.DOWN) Border(-160F, -50F, 0F, -30F) else Border(-160F, -20F, 0F, 0F)
    }
}
class Notification(val message: String, val type: Type, val displayTime: Long) {
    constructor(message: String) : this(message, Type.INFO, 500L)
    constructor(message: String, type: Type) : this(message, type, 2000L)
    constructor(message: String, displayTime: Long) : this(message, Type.INFO, displayTime)

    private val notifyDir = "minusbounce/notification/"

    private val imgSuccess = ResourceLocation("${notifyDir}checkmark.png")
    private val imgError = ResourceLocation("${notifyDir}error.png")
    private val imgWarning = ResourceLocation("${notifyDir}warning.png")
    private val imgInfo = ResourceLocation("${notifyDir}info.png")

    private val newSuccess = ResourceLocation("${notifyDir}new/checkmark.png")
    private val newError = ResourceLocation("${notifyDir}new/error.png")
    private val newWarning = ResourceLocation("${notifyDir}new/warning.png")
    private val newInfo = ResourceLocation("${notifyDir}new/info.png")

    var x = 0F
    val height = 30
    var nowY = -height
    var textLength = 0
    var fadeState = FadeState.IN
    var stayTimer = MSTimer()
    var notifHeight = 0F
    var animeXTime = System.currentTimeMillis()
    var animeYTime = System.currentTimeMillis()
    var width = 0f
    private var messageList: List<String>
    private var stay = 0F
    private var fadeStep = 0F
    private var firstY = 0f

    init {
        this.messageList = Fonts.font40.listFormattedStringToWidth(message, 105)
        this.notifHeight = messageList.size.toFloat() * (Fonts.font40.FONT_HEIGHT.toFloat() + 2F) + 8F
        this.firstY = 19190F
        this.stayTimer.reset()
        this.textLength = Fonts.font40.getStringWidth(message)
    }

    enum class Type {
        SUCCESS, INFO, WARNING, ERROR
    }

    enum class FadeState {
        IN, STAY, OUT, END
    }

    fun drawNotification(animationY: Float, parent: Notifications) {
        val delta = RenderUtils.deltaTime

        val style = parent.styleValue.get()
        val barMaterial = parent.barValue.get()

        val blur = parent.blurValue.get()
        val strength = parent.blurStrength.get()

        val hAnimMode = parent.hAnimModeValue.get()
        val vAnimMode = parent.vAnimModeValue.get()
        val animSpeed = parent.animationSpeed.get()

        val originalX = parent.renderX.toFloat()
        val originalY = parent.renderY.toFloat()
        width = when (style.lowercase()) {
            "material" -> 160F
            else -> textLength.toFloat() + 8.0f
        }

        val backgroundColor = Color(0, 0, 0, parent.bgAlphaValue.get())
        val enumColor = when (type) {
            Type.SUCCESS -> Color(80, 255, 80).rgb
            Type.ERROR -> Color(255, 80, 80).rgb
            Type.INFO -> Color(255, 255, 255).rgb
            Type.WARNING -> Color(255, 255, 0).rgb
        }

        firstY = if (vAnimMode.equals("smooth", true)) {
            if (firstY == 19190.0F)
                animationY
            else
                AnimationUtils.animate(animationY, firstY, 0.02F * delta)
        } else {
            animationY
        }

        val y = firstY


        when (style.lowercase()) {
            "compact" -> {
                GlStateManager.resetColor()

                if (blur) {
                    GL11.glTranslatef(-originalX, -originalY, 0F)
                    GL11.glPushMatrix()
                    BlurUtils.blurAreaRounded(originalX + -x - 5F, originalY + -18F - y, originalX + -x + 8F + textLength, originalY + -y, 3F, strength)
                    GL11.glPopMatrix()
                    GL11.glTranslatef(originalX, originalY, 0F)
                }

                RenderUtils.customRounded(-x + 8F + textLength, -y, -x - 2F, -18F - y, 0F, 3F, 3F, 0F, backgroundColor.rgb)
                RenderUtils.customRounded(-x - 2F, -y, -x - 5F, -18F - y, 3F, 0F, 0F, 3F, when(type) {
                    Type.SUCCESS -> Color(80, 255, 80).rgb
                    Type.ERROR -> Color(255, 80, 80).rgb
                    Type.INFO -> Color(255, 255, 255).rgb
                    Type.WARNING -> Color(255, 255, 0).rgb
                })

                GlStateManager.resetColor()
                Fonts.font40.drawString(message, -x + 3, -13F - y, -1)
            }
            "full" -> {
                val dist = (x + 1 + 26F) - (x - 8 - textLength)
                val kek = -x - 1 - 26F

                GlStateManager.resetColor()

                if (blur) {
                    GL11.glTranslatef(-originalX, -originalY, 0F)
                    GL11.glPushMatrix()
                    BlurUtils.blurArea(originalX + kek, originalY + -28F - y, originalX + -x + 8 + textLength, originalY + -y, strength)
                    GL11.glPopMatrix()
                    GL11.glTranslatef(originalX, originalY, 0F)
                }

                RenderUtils.drawRect(-x + 8 + textLength, -y, kek, -28F - y, backgroundColor.rgb)

                GL11.glPushMatrix()
                GlStateManager.disableAlpha()
                RenderUtils.drawImage2(when (type) {
                    Type.SUCCESS -> imgSuccess
                    Type.ERROR -> imgError
                    Type.WARNING -> imgWarning
                    Type.INFO -> imgInfo
                }, kek, -27F - y, 26, 26)
                GlStateManager.enableAlpha()
                GL11.glPopMatrix()

                GlStateManager.resetColor()
                if (fadeState == FadeState.STAY && !stayTimer.hasTimePassed(displayTime))
                    RenderUtils.drawRect(kek, -y, kek + (dist * if (stayTimer.hasTimePassed(displayTime)) 0F else ((displayTime - (System.currentTimeMillis() - stayTimer.time)).toFloat() / displayTime.toFloat())), -1F - y, enumColor)
                else if (fadeState == FadeState.IN)
                    RenderUtils.drawRect(kek, -y, kek + dist, -1F - y, enumColor)

                GlStateManager.resetColor()
                Fonts.font40.drawString(message, -x + 2, -18F - y, -1)
            }
            "full2" -> {
                val dist = (x + 1 + 26F) - (x - 8 - textLength)
                val kek = -x - 1 - 26F

                GlStateManager.resetColor()

                if (blur) {
                    GL11.glTranslatef(-originalX, -originalY, 0F)
                    GL11.glPushMatrix()
                    BlurUtils.blurAreaRounded(originalX + kek, originalY + -28F - y, originalX + -x + 8 + textLength, originalY + -y, 1.8f, strength)
                    GL11.glPopMatrix()
                    GL11.glTranslatef(originalX, originalY, 0F)
                }

                RenderUtils.drawRoundedRect(-x + 8 + textLength, -y, kek, -28F - y, 1.8f, backgroundColor.rgb)

                GL11.glPushMatrix()
                GlStateManager.disableAlpha()
                RenderUtils.drawImage2(when (type) {
                    Type.SUCCESS -> newSuccess
                    Type.ERROR -> newError
                    Type.WARNING -> newWarning
                    Type.INFO -> newInfo
                }, kek, -27F - y, 26, 26)
                GlStateManager.enableAlpha()
                GL11.glPopMatrix()

                GlStateManager.resetColor()
                if (fadeState == FadeState.STAY && !stayTimer.hasTimePassed(displayTime))
                    RenderUtils.drawRoundedRect(kek, -y, kek + (dist * if (stayTimer.hasTimePassed(displayTime)) 0F else ((displayTime - (System.currentTimeMillis() - stayTimer.time)).toFloat() / displayTime.toFloat())), -1F - y, 1.8f, enumColor)
                else if (fadeState == FadeState.IN)
                    RenderUtils.drawRoundedRect(kek, -y, kek + dist, -1F - y, 1.8f, enumColor)

                GlStateManager.resetColor()
                Fonts.fontSFUI40.drawStringWithShadow(message, -x + 2, -18F - y, enumColor)
            }
            "test" -> {
                val kek = -x - 1 - 20F

                GlStateManager.resetColor()
                if (blur) {
                    GL11.glTranslatef(-originalX, -originalY, 0F)
                    GL11.glPushMatrix()
                    BlurUtils.blurAreaRounded(originalX + kek, originalY + -28F - y, originalX + -x + 8 + textLength, originalY + -y, 3F, strength)
                    GL11.glPopMatrix()
                    GL11.glTranslatef(originalX, originalY, 0F)
                }

                Stencil.write(true)

                if (type == Type.ERROR) {
                    RenderUtils.drawRoundedRect(-x + 9 + textLength, -y + 1, kek - 1, -28F - y - 1, 0F, Color(115,69,75).rgb)
                    RenderUtils.drawRoundedRect(-x + 8 + textLength, -y, kek, -28F - y, 0F, Color(89,61,65).rgb)
                    Fonts.minecraftFont.drawStringWithShadow("Error:", -x - 4, -25F - y, Color(249,130,108).rgb)
                }
                if (type == Type.INFO) {
                    RenderUtils.drawRoundedRect(-x + 9 + textLength, -y + 1, kek - 1, -28F - y - 1, 0F, Color(70,94,115).rgb)
                    RenderUtils.drawRoundedRect(-x + 8 + textLength, -y, kek, -28F - y, 0F, Color(61,72,87).rgb)
                    Fonts.minecraftFont.drawStringWithShadow("Information:", -x - 4, -25F - y, Color(119,145,147).rgb)
                }
                if (type == Type.SUCCESS) {
                    RenderUtils.drawRoundedRect(-x + 9 + textLength, -y + 1, kek - 1, -28F - y - 1, 0F, Color(67,104,67).rgb)
                    RenderUtils.drawRoundedRect(-x + 8 + textLength, -y, kek, -28F - y, 0F, Color(55,78,55).rgb)
                    Fonts.minecraftFont.drawStringWithShadow("Success:", -x - 4, -25F - y, Color(10,142,2).rgb)
                }
                if (type == Type.WARNING) {
                    RenderUtils.drawRoundedRect(-x + 9 + textLength, -y + 1, kek - 1, -28F - y - 1, 0F, Color(103,103,63).rgb)
                    RenderUtils.drawRoundedRect(-x + 8 + textLength, -y, kek, -28F - y, 0F, Color(80,80,57).rgb)
                    Fonts.minecraftFont.drawStringWithShadow("Warning:", -x - 4, -25F - y, Color(175,163,0).rgb)
                }

                Stencil.erase(true)

                GlStateManager.resetColor()

                Stencil.dispose()

                GL11.glPushMatrix()
                GlStateManager.disableAlpha()
                GlStateManager.resetColor()
                GL11.glColor4f(1F, 1F, 1F, 1F)
                RenderUtils.drawImage2(when (type) {
                    Type.SUCCESS -> imgSuccess
                    Type.ERROR -> imgError
                    Type.WARNING -> imgWarning
                    Type.INFO -> imgInfo
                }, kek + 5, -25F - y, 7, 7)
                GlStateManager.enableAlpha()
                GL11.glPopMatrix()

                Fonts.minecraftFont.drawStringWithShadow(message, -x - 4, -13F - y, -1)
            }
            "material" -> {
                GlStateManager.resetColor()

                GL11.glPushMatrix()
                GL11.glTranslatef(-x, -y - notifHeight - (if (barMaterial) 2F else 0F), 0F)

                RenderUtils.originalRoundedRect(1F, -1F, 159F, notifHeight + (if (barMaterial) 2F else 0F) + 1F, 1F, when (type) {
                        Type.SUCCESS -> Color(72, 210, 48, 70).rgb
                        Type.ERROR -> Color(227, 28, 28, 70).rgb
                        Type.WARNING -> Color(245, 212, 25, 70).rgb
                        Type.INFO -> Color(255, 255, 255, 70).rgb
                    })
                RenderUtils.originalRoundedRect(-1F, 1F, 161F, notifHeight + (if (barMaterial) 2F else 0F) - 1F, 1F, when (type) {
                        Type.SUCCESS -> Color(72, 210, 48, 70).rgb
                        Type.ERROR -> Color(227, 28, 28, 70).rgb
                        Type.WARNING -> Color(245, 212, 25, 70).rgb
                        Type.INFO -> Color(255, 255, 255, 70).rgb
                    })
                RenderUtils.originalRoundedRect(-0.5F, -0.5F, 160.5F, notifHeight + (if (barMaterial) 2F else 0F) + 0.5F, 1F, when (type) {
                        Type.SUCCESS -> Color(72, 210, 48, 80).rgb
                        Type.ERROR -> Color(227, 28, 28, 80).rgb
                        Type.WARNING -> Color(245, 212, 25, 80).rgb
                        Type.INFO -> Color(255, 255, 255, 80).rgb
                    })

                if (barMaterial) {
                    Stencil.write(true)
                    RenderUtils.originalRoundedRect(0F, 0F, 160F, notifHeight + 2F, 1F, when (type) {
                        Type.SUCCESS -> Color(72, 210, 48, 255).rgb
                        Type.ERROR -> Color(227, 28, 28, 255).rgb
                        Type.WARNING -> Color(245, 212, 25, 255).rgb
                        Type.INFO -> Color(255, 255, 255, 255).rgb
                    })
                    Stencil.erase(true)
                    if (fadeState == FadeState.STAY) RenderUtils.newDrawRect(0F, notifHeight, 160F * if (stayTimer.hasTimePassed(displayTime)) 1F else ((System.currentTimeMillis() - stayTimer.time).toFloat() / displayTime.toFloat()), notifHeight + 2F, when (type) {
                        Type.SUCCESS -> Color(72 + 90, 210 + 30, 48 + 90, 255).rgb
                        Type.ERROR -> Color(227 + 20, 28 + 90, 28 + 90, 255).rgb
                        Type.WARNING -> Color(245 - 70, 212 - 70, 25, 255).rgb
                        Type.INFO -> Color(155, 155, 155, 255).rgb
                    })
                    Stencil.dispose()
                } else RenderUtils.originalRoundedRect(0F, 0F, 160F, notifHeight, 1F, when (type) {
                    Type.SUCCESS -> Color(72, 210, 48, 255).rgb
                    Type.ERROR -> Color(227, 28, 28, 255).rgb
                    Type.WARNING -> Color(245, 212, 25, 255).rgb
                    Type.INFO -> Color(255, 255, 255, 255).rgb
                })

                var yHeight = 7F
                for (s in messageList) {
                    Fonts.font40.drawString(s, 30F, yHeight, if (type == Type.ERROR) -1 else 0)
                    yHeight += Fonts.font40.FONT_HEIGHT.toFloat() + 2F
                }

                GL11.glPushMatrix()
                GlStateManager.disableAlpha()
                RenderUtils.drawImage3(when (type) {
                    Type.SUCCESS -> newSuccess
                    Type.ERROR -> newError
                    Type.WARNING -> newWarning
                    Type.INFO -> newInfo
                }, 9F, notifHeight / 2F - 6F, 12, 12,
                if (type == Type.ERROR) 1F else 0F,
                if (type == Type.ERROR) 1F else 0F,
                if (type == Type.ERROR) 1F else 0F, 1F)
                GlStateManager.enableAlpha()
                GL11.glPopMatrix()

                GL11.glPopMatrix()

                GlStateManager.resetColor()
            }
        }

        when (fadeState) {
            FadeState.IN -> {
                if (x < width) {
                    x = if (hAnimMode.equals("smooth", true))
                        AnimationUtils.animate(width, x, animSpeed * 0.025F * delta)
                    else
                        AnimationUtils.easeOut(fadeStep, width) * width
                    fadeStep += delta / 4F
                }
                if (x >= width) {
                    fadeState = FadeState.STAY
                    x = width
                    fadeStep = width
                }

                stay = 60F
                stayTimer.reset()
            }

            FadeState.STAY -> {
                if (stay > 0) {
                    stay = 0F
                    stayTimer.reset()
                }
                if (stayTimer.hasTimePassed(displayTime))
                    fadeState = FadeState.OUT
            }

            FadeState.OUT -> if (x > 0) {
                x = if (hAnimMode.equals("smooth", true))
                    AnimationUtils.animate(-width / 2F, x, animSpeed * 0.025F * delta)
                else
                    AnimationUtils.easeOut(fadeStep, width) * width

                fadeStep -= delta / 4F
            } else
                fadeState = FadeState.END

            FadeState.END -> hud.removeNotification(this)
        }
    }
}