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
            R.attr.colorSurface
        )
    )
    @ColorInt val colorPrimary = a.getColor(0, Color.TRANSPARENT)
    @ColorInt val colorPrimaryDark = a.getColor(1, Color.TRANSPARENT)
    @ColorInt val colorSecondary = a.getColor(2, Color.TRANSPARENT)
    @ColorInt val colorBackground = a.getColor(3, Color.TRANSPARENT)
    @ColorInt val colorSurface = a.getColor(4, colorBackground)

    val appBarBackground = if (theme == Theme.BLACK_WHITE_TEAL) colorSurface else colorPrimary

    a.recycle()

    return DecodedTheme(
        theme,
        colorPrimary,
        colorPrimaryDark,
        colorSecondary,
        appBarBackground,
        colorBackground
    )
}