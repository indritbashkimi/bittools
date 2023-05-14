/*
 * Copyright (c) 2016 Indrit Bashkimi <indrit.bashkimi@gmail.com>.
 * All rights reserved.
 */

package com.ibashkimi.theme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
public class StyleUtils {

    @ColorInt
    public static int obtainColor(Context context, int attr, @ColorInt int defaultColor) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, defaultColor);
        a.recycle();
        return color;
    }

    @ColorInt
    public static int obtainColor(Context context, @StyleRes int style, int attr, @ColorInt int defaultColor) {
        TypedArray a = context.obtainStyledAttributes(style, new int[]{attr});
        @ColorInt int color = a.getColor(0, defaultColor);
        a.recycle();
        return color;
    }

    public static int obtainAttribute(final Resources.Theme theme, int attr) {
        final TypedValue value = new TypedValue();
        theme.resolveAttribute(attr, value, true);
        return value.data;
    }

    public static int[] getColorsFromStyle(Context context, @StyleRes int style, int[] attrs, @ColorInt int defaultColor) {
        int[] colors = new int[attrs.length];
        TypedArray a = context.obtainStyledAttributes(style, attrs);
        for (int i = 0; i < colors.length; i++) {
            colors[i] = a.getColor(i, defaultColor);
        }
        a.recycle();
        return colors;
    }

    public static int[] getColorsFromContextTheme(Context context, int[] attrs, @ColorInt int defaultColor) {
        int[] colors = new int[attrs.length];
        TypedArray a = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < colors.length; i++) {
            colors[i] = a.getColor(i, defaultColor);
        }
        a.recycle();
        return colors;
    }

    @Deprecated
    public static float getDimensionPixelSize(float dots, float actualDpi) {
        return dots * actualDpi / 160;
    }

    public static float dpToPx(DisplayMetrics metrics, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static float dpToPx(Context context, float dp) {
        return dpToPx(context.getResources().getDisplayMetrics(), dp);
    }

    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean hasSoftwareKeys(Activity activity) {
        Display d = activity.getWindowManager().getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 ||
                (realHeight - displayHeight) > 0;
    }
}
