package com.ibashkimi.providerstools.widget.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.model.Chart;
import com.ibashkimi.providerstools.model.DisplayParams;
import com.ibashkimi.providerstools.model.ProviderDisplay;
import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.StyleUtils;

import java.util.Random;

public class ChartDisplay extends LineChart implements Chart, ProviderDisplay {

    @ColorInt
    private int mLineColor;

    private YAxis mYAxis;

    private float mMaximum = 100;
    private float mMinimum = 0;

    private int mXRangeMinimum = 100;
    private int mXRangeMaximum = 100;

    public ChartDisplay(Context context) {
        this(context, null);
    }

    public ChartDisplay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int colorSecondary = StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.RED);
        int textColor = StyleUtils.obtainColor(context, android.R.attr.textColorPrimary, Color.BLACK);
        int axisTextColor = colorSecondary;
        mLineColor = colorSecondary;
        @ColorInt int background = Color.TRANSPARENT;
        boolean fill = false;
        @ColorInt int fillColor = colorSecondary;
        boolean showGrid = true;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Chart,
                0, 0);
        int n = a.getIndexCount();
        boolean previewMode = false;
        boolean showLabels = true;
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Chart_chartMinimum)
                mMinimum = a.getFloat(attr, mMinimum);
            else if (attr == R.styleable.Chart_chartMaximum)
                mMaximum = a.getFloat(attr, mMaximum);
            else if (attr == R.styleable.Chart_chartXRangeMinimum)
                mXRangeMinimum = a.getInteger(attr, mXRangeMinimum);
            else if (attr == R.styleable.Chart_chartXRangeMaximum)
                mXRangeMaximum = a.getInteger(attr, mXRangeMaximum);
            else if (attr == R.styleable.Chart_chartAxisTextColor)
                axisTextColor = a.getColor(attr, axisTextColor);
            else if (attr == R.styleable.Chart_chartLineColor)
                mLineColor = a.getColor(attr, mLineColor);
            else if (attr == R.styleable.Chart_chartBackground)
                background = a.getColor(attr, background);
            else if (attr == R.styleable.Chart_chartShowGrid)
                showGrid = a.getBoolean(attr, showGrid);
            else if (attr == R.styleable.Chart_chartShowLabels)
                showLabels = a.getBoolean(attr, showLabels);
            else if (attr == R.styleable.Chart_chartFill)
                fill = a.getBoolean(attr, fill);
            else if (attr == R.styleable.Chart_chartFillColor)
                fillColor = a.getColor(attr, fillColor);
            else if (attr == R.styleable.Chart_chartPreviewMode)
                previewMode = a.getBoolean(attr, previewMode);
        }
        a.recycle();

        // enable description text
        getDescription().setEnabled(false);

        // enable touch gestures
        setTouchEnabled(false);

        // enable scaling and dragging
        setDragEnabled(false);
        setScaleEnabled(false);
        setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(false);

        // set an alternative background color
        setBackgroundColor(background);

        LineData data = new LineData();

        // add empty data
        setData(data);

        // get the legend (only possible after setting data)
        Legend l = getLegend();
        l.setEnabled(false);

        XAxis xl = getXAxis();
        xl.setEnabled(false);

        mYAxis = getAxisLeft();
        //mYAxis.setTypeface(mTfLight);
        //mYAxis.setTextColor(axisTextColor);
        mYAxis.setAxisMinimum(mMinimum);
        mYAxis.setAxisMaximum(mMaximum);
        mYAxis.setDrawGridLines(showGrid);
        mYAxis.setEnabled(showGrid);
        mYAxis.setTextColor(textColor);
        mYAxis.setDrawLabels(showLabels);

        YAxis rightAxis = getAxisRight();
        rightAxis.setEnabled(false);

        if (previewMode) {
            Random random = new Random();
            for (int i = 0; i < mXRangeMaximum; i++) {
                double val = random.nextInt((int) ((mMaximum - mMinimum) + 1)) + mMinimum;
                addValue(val);
            }
        }
    }

    public void addValue(double value) {
        LineData data = getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) value), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            notifyDataSetChanged();

            // limit the number of visible entries
            setVisibleXRangeMaximum(mXRangeMaximum);
            setVisibleXRangeMinimum(mXRangeMinimum);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, null);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(mLineColor);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(mLineColor);
        set.setHighLightColor(mLineColor);
        set.setDrawValues(false);
        //set.setCubicIntensity(2f);
        return set;
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        addValue(data.getModule());
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {
        setMaxValue(params.getMaxValue());
        setMinValue(params.getMinValue());
    }

    @Override
    public void setMinValue(float minValue) {
        mMinimum = minValue;
        mYAxis.setAxisMinimum(minValue);
    }

    @Override
    public void setMaxValue(float maxValue) {
        mMaximum = maxValue;
        mYAxis.setAxisMaximum(maxValue);
    }
}
