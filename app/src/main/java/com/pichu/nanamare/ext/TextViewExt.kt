package com.pichu.nanamare.ext

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pichu.nanamare.R
import com.pichu.nanamare.ui.anim.TextViewIntAnimation
import org.jetbrains.anko.textColorResource
import java.util.*

@BindingAdapter(value = ["animInt"])
fun TextView.setAnimInt(value: Int) {
    startAnimation(TextViewIntAnimation(this, to = value))
}

@BindingAdapter(value = ["htmlText"])
fun TextView.setHtmlText(htmlText: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }
}

@BindingAdapter(value = ["textColorResId"])
fun TextView.setTextColorResId(resId: Int) {
    textColorResource = resId
}