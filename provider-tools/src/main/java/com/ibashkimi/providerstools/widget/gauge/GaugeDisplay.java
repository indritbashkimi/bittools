package com.ibashkimi.providerstools.widget.gauge;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.data.DisplayParams;
import com.ibashkimi.providerstools.data.ProviderDisplay;

import java.text.DecimalFormat;

public class GaugeDisplay extends Gauge implements ProviderDisplay {

    public GaugeDisplay(Context context) {
        this(context, null);
    }

    public GaugeDisplay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GaugeDisplay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        setValue(data.getModule());
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {
        setGaugeMinValue(params.getMinValue());
        setGaugeMaxValue(params.getMaxValue());
        mBoard.setMeasurementUnit(params.getMeasurementUnit());
        mOverlay.setDecimalFormat(new DecimalFormat(params.getDecimalFormat()));
    }
}
