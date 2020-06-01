package com.pichu.nanamare.camera.filter.shader

import android.content.Context
import android.opengl.GLES20

class GuassianBlurFilter(context: Context, filterName: String) : CameraFilter(context, filterName) {
    private val program: Int = com.pichu.nanamare.camera.MyGLUtils.buildProgram(
        context,
        com.pichu.nanamare.R.raw.shader_vertext,
        com.pichu.nanamare.R.raw.random_blur
    )

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(
            program,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(cameraTexId),
            arrayOf()
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}