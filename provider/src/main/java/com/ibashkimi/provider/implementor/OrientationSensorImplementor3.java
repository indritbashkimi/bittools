package com.ibashkimi.provider.implementor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.ibashkimi.provider.filter.Filter;
import com.ibashkimi.provider.filter.LowPassFilter;
import com.ibashkimi.provider.providerdata.OrientationData;

import org.jetbrains.annotations.NotNull;

public class OrientationSensorImplementor3 extends AndroidSensorImplementor {

    private Filter azimuthFilter = new LowPassFilter(0.2);
    private Filter pitchFilter = new LowPassFilter(0.2);
    private Filter rollFilter = new LowPassFilter(0.2);

    public OrientationSensorImplementor3(@NotNull Context context, int samplingPeriodUs) {
        super(context, new int[]{Sensor.TYPE_ORIENTATION}, samplingPeriodUs);
    }

    @Override
    public void onSensorChanged(@NotNull SensorEvent event) {
        double azimuth = azimuthFilter.doJob(event.values[0]);
        double pitch = pitchFilter.doJob(event.values[1]);
        double roll = rollFilter.doJob(event.values[2]);
        listener.onDataChanged(new OrientationData(azimuth, pitch, -roll));
    }
}
