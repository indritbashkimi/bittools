package com.ibashkimi.provider.utils;

public class PatternCreator {

    public static float[] createFloat(float begin, float end, int step) {
        float[] values = new float[(int) ((end - begin + step) / step)];
        for (int i = 0; i < values.length; i++) {
            values[i] = begin + i * step;
        }
        return values;
    }

    public static float[] createFloat(float begin, float end, float step) {
        float[] values = new float[(int) ((end - begin + step) / step)];
        for (int i = 0; i < values.length; i++) {
            values[i] = begin + i * step;
        }
        return values;
    }
}
