package com.ibashkimi.providerstools.widget.level;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.R;
import com.ibashkimi.providerstools.data.DisplayParams;
import com.ibashkimi.providerstools.data.ProviderDisplay;
import com.ibashkimi.theme.utils.StyleUtils;

public class CircularLevel extends View implements ProviderDisplay {

    private Paint paint;
    private float centerX;
    private float centerY;
    private float radius;
    private float x; // roll
    private float y; // pitch
    private float unit;
    private float radius1; // Medium circle
    private float radius2; // Smallest circle
    private float bubbleRadius;
    @ColorInt
    private int bubbleColor;
    @ColorInt
    private int outsideBubbleColor;
    @ColorInt
    private int centeredBubbleColor;
    @ColorInt
    private int linesColor;

    private float strokeWidth;

    public CircularLevel(Context context) {
        this(context, null);
    }

    public CircularLevel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLevel(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircularLevel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        linesColor = StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.RED);
        outsideBubbleColor = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        centeredBubbleColor = StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.RED);
        bubbleColor = outsideBubbleColor;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Gauge,
                defStyleAttr, defStyleRes);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Gauge_gaugeCircleColor) {
                outsideBubbleColor = a.getColor(attr, outsideBubbleColor);
            }
        }
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background
        paint.setColor(linesColor);
        paint.setStyle(Paint.Style.STROKE);
        // Draw external circle
        canvas.drawCircle(centerX, centerY, radius, paint);
        // Other other lines
        canvas.drawCircle(centerX, centerY, radius1, paint);
        canvas.drawCircle(centerX, centerY, radius2, paint);
        canvas.drawLine(centerX - radius, centerY, centerX - unit, centerY, paint);
        canvas.drawLine(centerX + unit, centerY, centerX + radius, centerY, paint);
        canvas.drawLine(centerX, centerY - radius, centerX, centerY - unit, paint);
        canvas.drawLine(centerX, centerY + unit, centerX, centerY + radius, paint);
        // Draw bubble
        paint.setColor(bubbleColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX + x, centerY + y, bubbleRadius, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(width, height) / 2 - strokeWidth / 2;
        unit = radius / 5;
        radius1 = radius - 2 * unit;
        radius2 = radius - 4 * unit;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        bubbleRadius = radius2 - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);
    }

    public void update(float roll, float pitch) {
        bubbleColor = (Math.abs(roll) < 1.8 && Math.abs(pitch) < 1.8) ? centeredBubbleColor : outsideBubbleColor;
        this.x = -radius * roll / 90;
        this.y = radius * pitch / 90;
        invalidate();
    }

    @Override
    public void onDataChanged(@NonNull SensorData data) {
        update((float) data.getValues()[2], (float) data.getValues()[1]);
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {

    }
}
