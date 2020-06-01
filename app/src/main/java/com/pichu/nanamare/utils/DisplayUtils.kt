package com.pichu.nanamare.utils

import android.content.Context

object DisplayUtils {
    fun getRefLength(context: Context, len: Float): Int {
        return (context.resources.displayMetrics.density * len + 0.5f).toInt()
    }
}