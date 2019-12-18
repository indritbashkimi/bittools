package com.ibashkimi.theme.theme

import androidx.annotation.StyleRes
import com.ibashkimi.theme.R

@Suppress("unused")
enum class Theme(@StyleRes val style: Int) {
    RED_BLUE(R.style.AppTheme_DayNight_Red_Blue),
    RED_TEAL(R.style.AppTheme_DayNight_Red_Teal),
    PINK_BLUE(R.style.AppTheme_DayNight_Pink_Blue),
    PINK_LIME(R.style.AppTheme_DayNight_Pink_Lime),
    PURPLE_RED(R.style.AppTheme_DayNight_Purple_Red),
    PURPLE_GREEN(R.style.AppTheme_DayNight_Purple_Green),
    DEEP_PURPLE_RED(R.style.AppTheme_DayNight_DeepPurple),
    INDIGO_PINK(R.style.AppTheme_DayNight_Indigo),
    BLUE_LIME(R.style.AppTheme_DayNight_Blue),
    LIGHT_BLUE(R.style.AppTheme_DayNight_LightBlue),
    CYAN_AMBER(R.style.AppTheme_DayNight_Cyan),
    TEAL(R.style.AppTheme_DayNight_Teal),
    GREEN_RED(R.style.AppTheme_DayNight_Green),
    LIGHT_GREEN_CYAN(R.style.AppTheme_DayNight_LightGreen),
    LIME_BLUE_PINK(R.style.AppTheme_DayNight_Lime),
    AMBER_LIGHT_BLUE(R.style.AppTheme_DayNight_Amber),
    YELLOW_RED(R.style.AppTheme_DayNight_Yellow),
    ORANGE_TEAL(R.style.AppTheme_DayNight_Orange),
    DEEP_ORANGE_INDIGO(R.style.AppTheme_DayNight_DeepOrange),
    BROWN_RED(R.style.AppTheme_DayNight_Brown),
    GRAY_PINK(R.style.AppTheme_DayNight_Gray),
    BLUE_GREY_RED(R.style.AppTheme_DayNight_BlueGrey),

    BLACK_WHITE_BLUE(R.style.AppTheme_DayNight_BlackWhite_Blue),
    BLACK_WHITE_TEAL(R.style.AppTheme_DayNight_BlackWhite_Teal),

    MATERIAL(R.style.AppTheme_DayNight_Material),

    FULL_CRANE_PURPLE(R.style.AppTheme_Extra_CranePurple),
    FULL_REPLY(R.style.AppTheme_Extra_Reply),

    FULL_INDIGO(R.style.AppTheme_Extra_Indigo),
    FULL_CYAN(R.style.AppTheme_Extra_Cyan),
    FULL_BROWN(R.style.AppTheme_Extra_Brown),
    FULL_GRAY(R.style.AppTheme_Extra_Gray);
}
