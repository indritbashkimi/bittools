package com.ibashkimi.providerstools.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ibashkimi.shared.Tool;
import com.ibashkimi.theme.activity.ThemePreferences;
import com.ibashkimi.theme.activity.ThemeSupportPreferences;

public class PreferencesResolver {
    public static SharedPreferences resolvePreferences(Context context, Tool tool) {
        return context.getSharedPreferences(tool.name().toLowerCase(), Context.MODE_PRIVATE);
    }

    public static SharedPreferences resolveGlobalPreferences(Context context) {
        return context.getSharedPreferences("bittools", Context.MODE_PRIVATE);
    }

    public static ThemeSupportPreferences resolveGlobalThemePreferences(Context context) {
        return new ThemePreferences(resolveGlobalPreferences(context));
    }

    public static ThemeSupportPreferences resolveToolThemePreferences(Context context, Tool tool) {
        return resolveGlobalThemePreferences(context);
    }
}
