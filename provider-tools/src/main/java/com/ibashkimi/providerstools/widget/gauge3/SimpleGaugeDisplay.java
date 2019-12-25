package com.ibashkimi.providerstools.widget.gauge3;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.model.DisplayParams;
import com.ibashkimi.providerstools.model.ProviderDisplay;

import org.jetbrains.annotations.NotNull;

public class SimpleGaugeDisplay extends SimpleGauge implements ProviderDisplay {

    public SimpleGaugeDisplay(Context context) {
        super(context);
    }

    public SimpleGaugeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {
        setStartValue(params.getMinValue());
        setEndValue(params.getMaxValue());
        setDecimalFormat(params.getDecimalFormat());
        setMeasurementUnit(params.getMeasurementUnit());
    }

    @Override
    public void onDataChanged(@NotNull SensorData data) {
        setValue((int) data.getModule());
    }
}
