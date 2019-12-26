package com.ibashkimi.providerstools.widget.digital;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.data.DisplayParams;
import com.ibashkimi.providerstools.data.ProviderDisplay;
import com.ibashkimi.providerstools.R;

import java.text.DecimalFormat;


public class LevelDigitalView extends LinearLayout implements ProviderDisplay {
    private TextView rollText;
    private TextView pitchText;

    private DecimalFormat decimalFormat = new DecimalFormat();

    public LevelDigitalView(Context context) {
        this(context, null);
    }

    public LevelDigitalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelDigitalView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LevelDigitalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.level_digital_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rollText = findViewById(R.id.roll_text);
        pitchText = findViewById(R.id.pitch_text);
        updateValues(0, 0);
    }

    public void updateValues(double roll, double pitch) {
        rollText.setText(decimalFormat.format(-roll));
        pitchText.setText(decimalFormat.format(-pitch));
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        updateValues(data.getValues()[2], data.getValues()[1]);
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {
        this.decimalFormat = new DecimalFormat(params.getDecimalFormat());
    }
}
