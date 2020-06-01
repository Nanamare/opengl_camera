package com.pichu.nanamare.ext

import android.opengl.GLSurfaceView
import androidx.databinding.BindingAdapter

@BindingAdapter("renderer")
fun setRenderer(view: GLSurfaceView, renderer: GLSurfaceView.Renderer?) {
    renderer?.let {
        view.setEGLContextClientVersion(2)
        view.setRenderer(renderer)
    }
}