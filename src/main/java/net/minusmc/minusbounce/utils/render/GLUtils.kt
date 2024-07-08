package net.minusmc.minusbounce.utils.render

import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer

object GLUtils {
    private var colorBuffer: FloatBuffer? = null
    private var LIGHT0_POS: Vec3? = null
    private var LIGHT1_POS: Vec3? = null
    fun disableGUIStandardItemLighting() {
        GlStateManager.disableLighting()
        GlStateManager.popMatrix()
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        RenderHelper.enableStandardItemLighting()
    }

    fun disableStandardItemLighting() {
        GlStateManager.disableLighting()
        GlStateManager.disableLight(0)
        GlStateManager.disableLight(1)
        GlStateManager.disableColorMaterial()
    }

    fun startSmooth() {
        GL11.glEnable(2848)
        GL11.glEnable(2881)
        GL11.glEnable(2832)
        GL11.glEnable(3042)
        GL11.glBlendFunc(770, 771)
        GL11.glHint(3154, 4354)
        GL11.glHint(3155, 4354)
        GL11.glHint(3153, 4354)
    }

    fun endSmooth() {
        GL11.glDisable(2848)
        GL11.glDisable(2881)
        GL11.glEnable(2832)
    }

    fun enableStandardItemLighting() {
        GlStateManager.enableLighting()
        GlStateManager.enableLight(0)
        GlStateManager.enableLight(1)
        GlStateManager.enableColorMaterial()
        GlStateManager.colorMaterial(1032, 5634)
        val n = 0.4f
        val n2 = 0.6f
        val n3 = 0.0f
        GL11.glLight(16384, 4611, setColorBuffer(LIGHT0_POS!!.xCoord, LIGHT0_POS!!.yCoord, LIGHT0_POS!!.zCoord, 0.0))
        GL11.glLight(16384, 4609, setColorBuffer(n2, n2, n2, 1.0f))
        GL11.glLight(16384, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f))
        GL11.glLight(16384, 4610, setColorBuffer(n3, n3, n3, 1.0f))
        GL11.glLight(16385, 4611, setColorBuffer(LIGHT1_POS!!.xCoord, LIGHT1_POS!!.yCoord, LIGHT1_POS!!.zCoord, 0.0))
        GL11.glLight(16385, 4609, setColorBuffer(n2, n2, n2, 1.0f))
        GL11.glLight(16385, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f))
        GL11.glLight(16385, 4610, setColorBuffer(n3, n3, n3, 1.0f))
        GlStateManager.shadeModel(7424)
        GL11.glLightModel(2899, setColorBuffer(n, n, n, 1.0f))
    }

    private fun setColorBuffer(
        p_setColorBuffer_0_: Double,
        p_setColorBuffer_2_: Double,
        p_setColorBuffer_4_: Double,
        p_setColorBuffer_6_: Double
    ): FloatBuffer? {
        return setColorBuffer(
            p_setColorBuffer_0_.toFloat(),
            p_setColorBuffer_2_.toFloat(),
            p_setColorBuffer_4_.toFloat(),
            p_setColorBuffer_6_.toFloat()
        )
    }

    private fun setColorBuffer(
        p_setColorBuffer_0_: Float,
        p_setColorBuffer_1_: Float,
        p_setColorBuffer_2_: Float,
        p_setColorBuffer_3_: Float
    ): FloatBuffer? {
        colorBuffer!!.clear()
        colorBuffer!!.put(p_setColorBuffer_0_).put(p_setColorBuffer_1_).put(p_setColorBuffer_2_)
            .put(p_setColorBuffer_3_)
        colorBuffer!!.flip()
        return colorBuffer
    }

    fun enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix()
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f)
        enableStandardItemLighting()
        GlStateManager.popMatrix()
    }

    init {
        colorBuffer = GLAllocation.createDirectFloatBuffer(16)
        LIGHT0_POS = Vec3(0.20000000298023224, 1.0, -0.699999988079071).normalize()
        LIGHT1_POS = Vec3(-0.20000000298023224, 1.0, 0.699999988079071).normalize()
    }

    fun setGLCap(cap: Int, flag: Boolean) {
        glCapMap[cap] = GL11.glGetBoolean(cap)
        if (flag) {
            GL11.glEnable(cap)
        } else {
            GL11.glDisable(cap)
        }
    }

    fun revertGLCap(cap: Int) {
        val origCap = glCapMap[cap]
        if (origCap != null) {
            if (origCap) {
                GL11.glEnable(cap)
            } else {
                GL11.glDisable(cap)
            }
        }
    }

    fun glEnable(cap: Int) {
        setGLCap(cap, true)
    }

    fun glDisable(cap: Int) {
        setGLCap(cap, false)
    }

    fun revertAllCaps() {
        val localIterator: Iterator<*> = glCapMap.keys.iterator()
        while (localIterator.hasNext()) {
            val cap = localIterator.next() as Int
            revertGLCap(cap)
        }
    }

    private val glCapMap = HashMap<Int?, Boolean?>()
    fun interpolate(current: Double, old: Double, scale: Double): Double {
        return old + (current - old) * scale
    }
}
