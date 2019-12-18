package com.ibashkimi.theme.preference

import com.ibashkimi.theme.theme.Theme

data class DecodedTheme(
    val theme: Theme,
    val colorPrimary: Int,
    val colorPrimaryDark: Int,
    val colorSecondary: Int,
    val appBarBackground: Int,
    val windowBackground: Int
)