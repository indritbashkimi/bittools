package com.ibashkimi.provider.provider;

import android.content.Context;
import android.hardware.Sensor;

import org.jetbrains.annotations.NotNull;

public class OrientationProvider5 extends OrientationProvider2 {

    public OrientationProvider5(@NotNull Context context, int samplingPeriodUs) {
        this(context, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, samplingPeriodUs);
    }

    private OrientationProvider5(@NotNull Context context, int sensor, int samplingPeriodUs) {
        super(context, sensor, samplingPeriodUs);
    }
}
