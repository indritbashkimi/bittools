package com.ibashkimi.theme.preference

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import com.ibashkimi.theme.R
import com.ibashkimi.theme.theme.Theme

@SuppressLint("ResourceType")
fun decodeTheme(context: Context, theme: Theme): DecodedTheme {
    val a = context.obtainStyledAttributes(
        theme.style, intArrayOf(
            R.attr.colorPrimary,
            R.attr.colorPrimaryDark,
            R.attr.colorSecondary,
            android.R.attr.colorBackground,
            R.attr.appBarStyle
        )
    )
    @ColorInt val colorPrimary = a.getColor(0, Color.TRANSPARENT)
    @ColorInt val colorPrimaryDark = a.getColor(1, Color.TRANSPARENT)
    @ColorInt val colorSecondary = a.getColor(2, Color.TRANSPARENT)
    @ColorInt val colorBackground = a.getColor(3, Color.TRANSPARENT)

    val appBarStyle = a.getResourceId(4, -1)
    a.recycle()

    val toolbarBackground: Int
    if (appBarStyle != -1) {
        val b = context.obtainStyledAttributes(appBarStyle, intArrayOf(android.R.attr.background))
        toolbarBackground = b.getColor(0, colorPrimary)
        b.recycle()
    } else {
        toolbarBackground = colorPrimary
    }

    return DecodedTheme(
        theme,
        colorPrimary,
        colorPrimaryDark,
        colorSecondary,
        toolbarBackground,
        colorBackground
    )
}