package com.ibashkimi.theme.preference

data class ThemeAdapterItem(
    val themeId: String,
    val isPremium: Boolean,
    val attrs: ThemeAttrs
)

data class ThemeAttrs(
    val colorPrimary: Int,
    val colorPrimaryDark: Int,
    val colorSecondary: Int,
    val appBarBackground: Int,
    val windowBackground: Int
)