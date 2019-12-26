package com.ibashkimi.providerstools.widget.digital;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.data.DisplayParams;
import com.ibashkimi.providerstools.data.ProviderDisplay;
import com.ibashkimi.providerstools.R;

import java.text.DecimalFormat;

public class CompactDigitalView extends LinearLayout implements ProviderDisplay {
    private TextView valueView;
    private TextView unitView;

    private String unitText;

    private DecimalFormat decimalFormat = new DecimalFormat();

    public CompactDigitalView(Context context) {
        this(context, null);
    }

    public CompactDigitalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactDigitalView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompactDigitalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DigitalDisplay,
                0, 0);
        try {
            unitText = a.getString(R.styleable.DigitalDisplay_unitText);
        } finally {
            a.recycle();
        }
        LayoutInflater.from(context).inflate(R.layout.compact_digital_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        valueView = findViewById(R.id.value);
        //valueView.setTextColor(valueTextColor);
        valueView.setVisibility(VISIBLE);
        unitView = findViewById(R.id.unit);
        unitView.setVisibility(VISIBLE);
        //unitView.setTextColor(valueTextColor);
        unitView.setText(unitText);
        setValue(0);
    }

    public void setValue(double value) {
        valueView.setText(decimalFormat.format(value));
    }

    protected void setTextValue(String value) {
        valueView.setText(value);
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        setValue(data.getModule());
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {
        unitView.setText(params.getMeasurementUnit());
        decimalFormat = new DecimalFormat(params.getDecimalFormat());
    }

    protected void showUnit(boolean show) {
        unitView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
