package com.ibashkimi.providerstools.widget.compass;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.OrientationData;
import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.widget.digital.CompactDigitalView;


public class CompassDigital extends CompactDigitalView {

    private Direction direction;

    public CompassDigital(Context context) {
        this(context, null);
    }

    public CompassDigital(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassDigital(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompassDigital(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.direction = new Direction(context, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showUnit(false);
        setTextValue(this.direction.toString());
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        float azimuthInDegrees = (float) ((OrientationData) data).getAzimuth();
        direction.update((azimuthInDegrees >= 0 ? azimuthInDegrees : 360 + azimuthInDegrees));
        setTextValue(direction.toString());
    }
}
