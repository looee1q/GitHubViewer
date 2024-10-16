package com.example.githubviewer.repository.util

import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment

@ColorInt
fun Fragment.getColorFromFragment(@ColorRes colorRes: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(colorRes, requireContext().theme)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(colorRes)
    }
}