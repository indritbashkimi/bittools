package com.ibashkimi.theme.utils;

public class MathUtils {

    public static float round(float value, int places) {
        if (places < 0)
            return value;
        long factor = (long) java.lang.Math.pow(10, places);
        return ((float) java.lang.Math.round(value * factor)) / factor;
    }

    public static double round(double value, int places) {
        if (places < 0)
            return value;
        long factor = (long) java.lang.Math.pow(10, places);
        return ((double) java.lang.Math.round(value * factor)) / factor;
    }
}
