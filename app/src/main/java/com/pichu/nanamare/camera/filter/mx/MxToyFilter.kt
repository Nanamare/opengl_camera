package com.pichu.nanamare.camera.filter.mx

import android.content.Context
import android.opengl.GLES20
import com.pichu.nanamare.R
import com.pichu.nanamare.camera.MyGLUtils

class MxToyFilter(context: Context, filterName: String) : MultiTextureFilter(context, filterName) {
    private val program: Int = MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.mx_toy)

    init {
        setTexture2Id(intArrayOf(0, 0, 0, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 21, 21, 22, 22, 23, 23, 24, 25, 25, 26, 27, 27, 28, 29, 29, 30, 31, 31, 32, 33, 34, 34, 35, 36, 37, 38, 38, 39, 40, 41, 42, 43, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 62, 63, 64, 65, 66, 67, 69, 70, 71, 72, 74, 75, 76, 78, 79, 81, 82, 83, 85, 86, 88, 89, 91, 92, 94, 95, 97, 98, 100, 101, 103, 105, 106, 108, 110, 111, 113, 114, 116, 118, 119, 121, 123, 125, 126, 128, 130, 131, 133, 135, 136, 138, 140, 142, 143, 145, 147, 148, 150, 152, 154, 155, 157, 159, 160, 162, 164, 165, 167, 168, 170, 172, 173, 175, 177, 178, 180, 181, 183, 184, 186, 187, 189, 190, 192, 193, 195, 196, 197, 199, 200, 201, 203, 204, 205, 206, 208, 209, 210, 211, 212, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 230, 231, 232, 233, 234, 234, 235, 236, 236, 237, 238, 239, 239, 240, 240, 241, 242, 242, 243, 243, 244, 245, 245, 246, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253, 253, 254, 254, 255))
    }

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(
            program,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(cameraTexId, texture2Id!!),
            arrayOf()
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}