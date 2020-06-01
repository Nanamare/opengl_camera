package com.pichu.nanamare.camera.filter.mx

import android.content.Context
import android.graphics.Bitmap
import com.pichu.nanamare.camera.filter.shader.CameraFilter
import com.pichu.nanamare.utils.BitmapTexture

open class MultiTextureFilter(context: Context, filterName: String) :
    CameraFilter(context, filterName) {

    var texture2Id: Int? = null

    fun setTexture2Id(array: IntArray) {
        for (i in 0 until histogramSize) {
            mHistogram[i] = array[i] shl 24
        }
        val bitmapTexture = BitmapTexture()
        bitmapTexture.loadBitmap(Bitmap.createBitmap(mHistogram, 256, 1, Bitmap.Config.ARGB_8888))
        texture2Id = bitmapTexture.imageTextureId
    }

    override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {}

}