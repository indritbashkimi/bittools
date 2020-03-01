package com.ibashkimi.providerstools.widget.gauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ibashkimi.providerstools.R;


public class Gauge extends FrameLayout {

    protected GaugeBoard mBoard;
    protected GaugeOverlay mOverlay;

    public Gauge(Context context) {
        this(context, null);
    }

    public Gauge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.style.Widget_AppTheme_Gauge);
    }

    public Gauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Gauge,
                defStyleAttr, defStyleRes);

        mBoard = new GaugeBoard(context, attrs, defStyleAttr);
        mOverlay = new GaugeOverlay(context, attrs, defStyleAttr);
        int style = a.getInteger(R.styleable.Gauge_gaugeStyle, 0);
        switch (style) {
            case 0:
                mBoard = new GaugeBoard(context, attrs, defStyleAttr);
                mOverlay = new GaugeOverlay(context, attrs, defStyleAttr);
                break;
            case 1:
                mBoard = new GaugeBoard(context, attrs, defStyleAttr);
                mOverlay = new GaugeOverlayTriangle(context, attrs, defStyleAttr);
                break;
            default:
                mBoard = new GaugeBoard(context, attrs, defStyleAttr);
                mOverlay = new GaugeOverlay(context, attrs, defStyleAttr);
        }

        addView(mBoard);
        addView(mOverlay);
        setWillNotDraw(false);
    }

    public void setValue(double value) {
        mOverlay.onValueChanged((float) value);
    }

    public void setGaugeMinValue(int value) {
        mOverlay.setGaugeMinValue(value);
        mBoard.setMinValue(value);
    }

    public void setGaugeMaxValue(int value) {
        mOverlay.setGaugeMaxValue(value);
        mBoard.setMaxValue(value);
    }

    public void setMinValue(float min) {
        mOverlay.setMinValue(min, false);
        mBoard.setMinValue(min);
    }

    public void setMaxValue(float max) {
        mOverlay.setMaxValue(max, false);
        mBoard.setMaxValue(max);
    }
}
