package com.pichu.nanamare.camera.filter.shader

import android.content.Context
import android.opengl.GLES20

import com.pichu.nanamare.R
import com.pichu.nanamare.camera.MyGLUtils
import com.pichu.nanamare.camera.RenderBuffer

class JFAVoronoiFilter(context: Context, filterName: String) : CameraFilter(context, filterName) {
    private val programImg: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_voronoi)
    private val programA: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_voronoi_buf_a)
    private val programB: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_voronoi_buf_b)
    private val programC: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_voronoi_buf_c)

    private var bufA: RenderBuffer? = null
    private var bufB: RenderBuffer? = null
    private var bufC: RenderBuffer? = null

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        // TODO move?
        if (bufA == null || bufA!!.width != canvasWidth || bufB!!.height != canvasHeight) {
            // Create new textures for buffering
            bufA = RenderBuffer(canvasWidth, canvasHeight, GLES20.GL_TEXTURE4)
            bufB = RenderBuffer(canvasWidth, canvasHeight, GLES20.GL_TEXTURE5)
            bufC = RenderBuffer(canvasWidth, canvasHeight, GLES20.GL_TEXTURE6)
        }

        // Render to buf a
        setupShaderInputs(
            programA,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(cameraTexId, bufA!!.texId),
            arrayOf(intArrayOf(canvasWidth, canvasHeight), intArrayOf(canvasWidth, canvasHeight))
        )
        bufA!!.bind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        bufA!!.unbind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)


        // Render to buf b
        setupShaderInputs(
            programB,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(bufB!!.texId, bufA!!.texId),
            arrayOf(intArrayOf(canvasWidth, canvasHeight), intArrayOf(canvasWidth, canvasHeight))
        )
        bufB!!.bind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        bufB!!.unbind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)


        // Render to buf c
        setupShaderInputs(
            programC,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(bufC!!.texId, bufB!!.texId),
            arrayOf(intArrayOf(canvasWidth, canvasHeight), intArrayOf(canvasWidth, canvasHeight))
        )
        bufC!!.bind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        bufC!!.unbind()
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)


        // Render to screen
        setupShaderInputs(
            programImg,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(bufC!!.texId, bufA!!.texId),
            arrayOf(intArrayOf(canvasWidth, canvasHeight), intArrayOf(canvasWidth, canvasHeight))
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}
