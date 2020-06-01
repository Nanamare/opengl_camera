package com.pichu.nanamare.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pichu.nanamare.ui.anim.RotateTransformation

@BindingAdapter(value = ["loadUrl"])
fun ImageView.loadUrl(url: String?) {
    url?.let {
        Glide.with(this)
            .load(it)
            .into(this)
    }
}

@BindingAdapter(value = ["drawableResId"])
fun ImageView.setDrawableResId(resId: Int) {
    setImageResource(resId)
}

fun ImageView.rotateImage(url: String?) {
    url?.let {
        Glide.with(this)
            .load(it)
            .apply(
                RequestOptions.bitmapTransform(
                    RotateTransformation(
                        90F
                    )
                )
            )
            .into(this)

    }
}

fun ImageView.loadUrlWithoutCache(url: String?) {
    url?.let {
        Glide.with(this)
            .load(it)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .into(this)
    }
}
