package com.ibashkimi.provider.implementor;

import android.content.Context;
import android.hardware.Sensor;

import org.jetbrains.annotations.NotNull;

public class OrientationSensorImplementor5 extends OrientationSensorImplementor2 {

    public OrientationSensorImplementor5(@NotNull Context context, int samplingPeriodUs) {
        this(context, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, samplingPeriodUs);
    }

    private OrientationSensorImplementor5(@NotNull Context context, int sensor, int samplingPeriodUs) {
        super(context, sensor, samplingPeriodUs);
    }
}
