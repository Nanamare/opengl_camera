package com.pichu.nanamare.camera.filter.shader

import android.content.Context
import android.opengl.GLES20
import com.pichu.nanamare.R
import com.pichu.nanamare.camera.MyGLUtils

class RefractionFilter(context: Context, filterName: String) : CameraFilter(context, filterName) {
    private val program: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_refraction)
    private val texture2Id: Int =
        MyGLUtils.loadTexture(context, R.raw.shader_tex_noise, IntArray(2))

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(
            program,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(cameraTexId, texture2Id),
            arrayOf()
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}
