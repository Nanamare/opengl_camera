package com.pichu.nanamare.camera.filter.shader

import android.content.Context
import android.opengl.GLES20

import com.pichu.nanamare.R
import com.pichu.nanamare.camera.MyGLUtils

class LegofiedFilter(context: Context, filterName: String) : CameraFilter(context, filterName) {
    private val program: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_legofied)

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
