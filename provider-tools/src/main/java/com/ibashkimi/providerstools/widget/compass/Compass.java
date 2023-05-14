package com.ibashkimi.providerstools.widget.compass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.OrientationData;
import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.R;
import com.ibashkimi.providerstools.data.DisplayParams;
import com.ibashkimi.providerstools.data.ProviderDisplay;
import com.ibashkimi.theme.utils.MathUtils;
import com.ibashkimi.theme.utils.StyleUtils;

public class Compass extends FrameLayout implements ProviderDisplay {

    private CompassBoard mBoard;
    private TextView mTextView;
    private int mCurrentDegree;
    private Direction mDirection;

    public Compass(Context context) {
        this(context, null);
    }

    public Compass(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Compass(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Compass(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);

        int valueTextSize = 24;
        int valueTextColor = StyleUtils.obtainColor(context, R.attr.colorOnPrimary, Color.RED);
        int valueCircleColor = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Gauge,
                defStyleAttr, defStyleRes);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Gauge_valueTextSize)
                valueTextSize = a.getDimensionPixelSize(attr, valueTextSize);
            else if (attr == R.styleable.Gauge_valueTextColor)
                valueTextColor = a.getColor(attr, valueTextColor);
            else if (attr == R.styleable.Gauge_gaugeCircleColor)
                valueCircleColor = a.getColor(attr, valueTextColor);
        }

        a.recycle();

        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mTextView = new TextView(context);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(p);
        mTextView.setTextColor(valueTextColor);

        mTextView.setTextSize(valueTextSize);
        String maxSizedText = new Direction(context, 335).toString() + " ";
        mTextView.setText(maxSizedText);
        mTextView.measure(0, 0);
        int width = mTextView.getMeasuredWidth();
        mTextView.setText("");

        mBoard = new CompassBoard(context, attrs, defStyleAttr, defStyleRes);
        mBoard.setCircleRadius(width / 2.0f);
        mBoard.setCircleColor(valueCircleColor);
        addView(mBoard);
        addView(mTextView);

        mDirection = new Direction(context, 0);
        setAzimuth(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    public void setAzimuth(double azimuth) {
        int azimuthInDegrees = (int) MathUtils.round(azimuth, 0);
        mCurrentDegree = -azimuthInDegrees;
        if (azimuthInDegrees == mCurrentDegree)
            return;
        mBoard.setValue(mCurrentDegree);
        mCurrentDegree = -azimuthInDegrees;
        // Note azimuth [-180, +180]
        mDirection.update((azimuthInDegrees >= 0 ? azimuthInDegrees : 360 + azimuthInDegrees));
        mTextView.setText(mDirection.toString());
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        setAzimuth(((OrientationData) data).getAzimuth());
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {

    }
}
