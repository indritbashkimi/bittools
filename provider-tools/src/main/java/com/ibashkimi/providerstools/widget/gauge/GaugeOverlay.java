package com.ibashkimi.providerstools.widget.gauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;

import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.StyleUtils;

import java.text.DecimalFormat;

class GaugeOverlay extends View {
    protected int mCenterX;
    protected int mCenterY;
    protected int mCircleRadius;
    protected int mArrowLength;
    protected int mBigSegmentLength;

    @ColorInt
    protected int mCircleColor;
    @ColorInt
    protected int mArrowColor;
    @ColorInt
    protected int mCircleTextColor;
    @ColorInt
    protected int mMinArrowColor;
    @ColorInt
    protected int mMaxArrowColor;
    @ColorInt
    protected int mAvgArrowColor;

    protected float mValue;
    protected float mMinValue;
    protected float mMaxValue;
    protected float mAvgValue;
    protected float mAnglePerValue;
    protected int mGaugeMinValue;
    protected int mGaugeMaxValue;
    protected String mValueText;

    protected Paint mPaint;

    protected boolean mShowValue;
    protected boolean mShowMinMaxAvgValues;

    protected boolean mValueChanged;

    protected int mHalfTextHeight;
    protected int mArrowY1;
    protected int mPointY;
    protected int mPointRadius;
    protected int mSmallPointRadius;
    protected int mStrokeWidth;
    protected int mSmallStrokeWidth;
    protected int mBoardTextHeight;
    protected int mTextSize;
    protected int mBoardTextSize;
    protected boolean showUnitInsteadOfValue;

    protected DecimalFormat decimalFormat = new DecimalFormat();

    public GaugeOverlay(Context context) {
        this(context, null);
    }

    public GaugeOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_AppTheme_Gauge);
    }

    public GaugeOverlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mGaugeMinValue = 0;
        mGaugeMaxValue = 135;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, metrics);
        mBoardTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, metrics);
        mBigSegmentLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        mCircleTextColor = StyleUtils.obtainColor(context, R.attr.colorOnPrimary, Color.RED);
        mCircleColor = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        mArrowColor = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        float initialValue = 0;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Gauge,
                defStyleAttr, defStyleAttr);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Gauge_minValue)
                mGaugeMinValue = a.getInteger(R.styleable.Gauge_minValue, mGaugeMinValue);
            else if (attr == R.styleable.Gauge_maxValue)
                mGaugeMaxValue = a.getInteger(R.styleable.Gauge_maxValue, mGaugeMaxValue);
            else if (attr == R.styleable.Gauge_valueTextSize)
                mTextSize = a.getDimensionPixelSize(R.styleable.Gauge_valueTextSize, mTextSize);
            else if (attr == R.styleable.Gauge_boardTextSize)
                mBoardTextSize = a.getDimensionPixelSize(attr, mBoardTextSize);
            else if (attr == R.styleable.Gauge_bigSegmentLength)
                mBigSegmentLength = a.getDimensionPixelSize(attr, mBigSegmentLength);
            else if (attr == R.styleable.Gauge_valueTextColor)
                mCircleTextColor = a.getColor(R.styleable.Gauge_valueTextColor, mCircleTextColor);
            else if (attr == R.styleable.Gauge_arrowColor) {
                mArrowColor = a.getColor(R.styleable.Gauge_arrowColor, mArrowColor);
            } else if (attr == R.styleable.Gauge_gaugeCircleColor) {
                mCircleColor = a.getColor(R.styleable.Gauge_gaugeCircleColor, mCircleColor);
            } else if (attr == R.styleable.Gauge_value)
                initialValue = a.getFloat(attr, 0);
        }
        a.recycle();

        mAnglePerValue = (270.0f) / (mGaugeMaxValue - mGaugeMinValue);

        mStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);
        mSmallStrokeWidth = mStrokeWidth / 3;
        // Maybe I can convert values
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);//context.getResources().getInteger(R.integer.gauge_unit_text_size));
        mPaint.setColor(mCircleColor);

        calculateRadius();

        // Calculate boardTextSize
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(mBoardTextSize);
        String text = "" + mGaugeMaxValue;
        paint.getTextBounds(text, 0, text.length(), rect);
        mBoardTextHeight = rect.height();

        //Rect rect = new Rect();
        mPaint.getTextBounds("9", 0, 1, rect);
        mHalfTextHeight = rect.height() / 2;
        mValueText = "";

        setWillNotDraw(false);

        mValueText = "";

        int textColorSecondary = StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.RED);
        mMinArrowColor = textColorSecondary;
        mMaxArrowColor = textColorSecondary;
        mAvgArrowColor = textColorSecondary;

        mMinValue = mGaugeMinValue;
        mMaxValue = mGaugeMaxValue;
        mAvgValue = (mGaugeMaxValue + mMinArrowColor) / 2;

        mShowValue = true;
        mShowMinMaxAvgValues = false;
        showUnitInsteadOfValue = false;

        if (initialValue != 0)
            onValueChanged(5);
    }

    private void calculateRadius() {
        Rect rect = new Rect();
        // Don't use 0.999!
        String text = decimalFormat.format(0.347 + mGaugeMaxValue);
        String text2 = decimalFormat.format(-0.347 - mGaugeMinValue);
        if (text2.length() > text.length())
            text = text2;
        mPaint.getTextBounds(text, 0, text.length(), rect);
        mCircleRadius = rect.width() / 2 + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowMinMaxAvgValues) {
            mPaint.setStrokeWidth(mSmallStrokeWidth);
            // Draw min arrow
            mPaint.setColor(mMinArrowColor);
            canvas.save();
            canvas.rotate(-135 + (mMinValue - mGaugeMinValue) * mAnglePerValue, mCenterX, mCenterY);
            canvas.drawLine(mCenterX, mCenterY, mCenterX, mArrowY1, mPaint);
            canvas.drawCircle(mCenterX, mPointY, mSmallPointRadius, mPaint);
            canvas.restore();

            // Draw max arrow
            mPaint.setColor(mMaxArrowColor);
            canvas.save();
            canvas.rotate(-135 + (mMaxValue - mGaugeMinValue) * mAnglePerValue, mCenterX, mCenterY);
            canvas.drawLine(mCenterX, mCenterY, mCenterX, mArrowY1, mPaint);
            canvas.drawCircle(mCenterX, mPointY, mSmallPointRadius, mPaint);
            canvas.restore();

            // Draw avg arrow
            /*mPaint.setColor(mAvgArrowColor);
            canvas.save();
            canvas.rotate(-135 + (mAvgValue - mGaugeMinValue) * mAnglePerValue, mCenterX, mCenterY);
            canvas.drawLine(mCenterX, mCenterY, mCenterX, mArrowY1, mPaint);
            canvas.drawCircle(mCenterX, mPointY, mSmallPointRadius, mPaint);
            canvas.restore();*/

            mPaint.setStrokeWidth(mStrokeWidth);
        }
        if (mShowValue) {
            // Draw arrow
            if (mValue >= mGaugeMinValue && mValue <= mGaugeMaxValue) {
                renderArrow(canvas);
            }
            // Draw circle value
            renderCircle(canvas);
        }
    }

    protected void renderArrow(Canvas canvas) {
        mPaint.setColor(mArrowColor);
        canvas.save();
        canvas.rotate(-135 + (mValue - mGaugeMinValue) * mAnglePerValue, mCenterX, mCenterY);
        canvas.drawLine(mCenterX, mCenterY, mCenterX, mArrowY1, mPaint);
        canvas.drawCircle(mCenterX, mPointY, mPointRadius, mPaint);
        canvas.restore();
    }

    protected void renderCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaint);

        mPaint.setColor(mCircleTextColor);
        if (showUnitInsteadOfValue) {
            canvas.drawText("hPa", mCenterX, mCenterY + mHalfTextHeight, mPaint);
        } else {
            canvas.drawText(mValueText, mCenterX, mCenterY + mHalfTextHeight, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        int widthWithPadding = w - getPaddingLeft() - getPaddingRight();
        int heightWithPadding = h - getPaddingTop() - getPaddingBottom();
        int radius = widthWithPadding < heightWithPadding ? widthWithPadding / 2 : heightWithPadding / 2;
        mArrowLength = radius - mBigSegmentLength - 2 * mBoardTextHeight;
        mPointRadius = mStrokeWidth / 2;//adjustSize(6);
        mSmallPointRadius = mSmallStrokeWidth / 2;
        mArrowY1 = mCenterY - mArrowLength + mPointRadius;
        mPointY = mCenterY - mArrowLength + mPointRadius;
    }

    public void onValueChanged(float value) {
        mValue = value;
        mValueText = decimalFormat.format(value);
        mValueChanged = true;
        invalidate();
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        calculateRadius();
        invalidate();
    }

    public void setGaugeMinValue(int value) {
        mGaugeMinValue = value;
        mAnglePerValue = (270.0f) / (mGaugeMaxValue - mGaugeMinValue);
    }

    public void setGaugeMaxValue(int value) {
        mGaugeMaxValue = value;
        mAnglePerValue = (270.0f) / (mGaugeMaxValue - mGaugeMinValue);
    }

    public void setMinValue(float min, boolean invalidate) {
        mMinValue = min;
        if (invalidate) {
            invalidate();
        }
    }

    public void setMaxValue(float max, boolean invalidate) {
        mMaxValue = max;
        if (invalidate) {
            invalidate();
        }
    }

    public void setMinMaxValues(float min, float max, boolean invalidate) {
        mMinValue = min;
        mMaxValue = max;
        if (invalidate) {
            invalidate();
        }
    }
}
