package com.ibashkimi.providerstools.widget.gauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;

import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.StyleUtils;

class GaugeBoard extends View {

    private boolean showExternalCircle = false;
    private float bigCircleStrokeWidth;
    @ColorInt
    private int segmentColor;
    @ColorInt
    private int measurementUnitColor;
    private float startAngleInDegrees = -135;
    private float stopAngleInDegrees = 135;
    private boolean showSegments = true;

    private boolean mSplitInCircles;

    private int minValue;
    private int maxValue;
    private int nBigSegments;
    private int nSmallSegments;
    private String mMeasurementUnit = "Unit";

    private Paint mPaint;

    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    private float mBigSegmentStart;
    private float mSegmentStop;
    private float mSmallSegmentStart;
    private float mAnglePerStep;
    private float mMeasurementUnitRadius;
    private int bigSegmentLength;
    private int smallSegmentLength;
    private int mStep;
    private int mTotalSegments;
    private int mBoardTextSize;
    private int mUnitTextSize;
    private int mBigSegmentWidth;
    private int mSmallSegmentWidth;
    private float mTextRadius;
    private int mTextHeight;
    private Path path;
    private Paint mBackgroundPaint;
    private Paint secondaryBackgroundPaint;

    public GaugeBoard(Context context) {
        this(context, null);
    }

    public GaugeBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GaugeBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        minValue = 0;
        maxValue = 100;
        mMeasurementUnit = "";
        startAngleInDegrees = -135;
        stopAngleInDegrees = 135;
        nBigSegments = 10;
        nSmallSegments = 5;
        bigSegmentLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        smallSegmentLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics);
        segmentColor = StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.RED);
        mBoardTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        mUnitTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, metrics);
        showExternalCircle = false;
        mBigSegmentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, metrics);
        mSmallSegmentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
        bigCircleStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);

        mSplitInCircles = true;
        int backgroundColor = 0x00000000;
        int secondaryBackColor = 0x00000000;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Gauge,
                defStyleAttr, defStyleRes);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Gauge_minValue)
                minValue = a.getInteger(R.styleable.Gauge_minValue, minValue);
            else if (attr == R.styleable.Gauge_maxValue)
                maxValue = a.getInteger(R.styleable.Gauge_maxValue, maxValue);
            else if (attr == R.styleable.Gauge_measurementUnit)
                mMeasurementUnit = a.getString(attr);
            else if (attr == R.styleable.Gauge_startAngle)
                startAngleInDegrees = a.getInteger(R.styleable.Gauge_startAngle, -135);
            else if (attr == R.styleable.Gauge_endAngle)
                stopAngleInDegrees = a.getInteger(R.styleable.Gauge_endAngle, 135);
            else if (attr == R.styleable.Gauge_bigSegments)
                nBigSegments = a.getInteger(attr, nBigSegments);
            else if (attr == R.styleable.Gauge_smallSegments)
                nSmallSegments = a.getInteger(attr, nSmallSegments);
            else if (attr == R.styleable.Gauge_bigSegmentLength)
                bigSegmentLength = a.getDimensionPixelSize(attr, bigSegmentLength);
            else if (attr == R.styleable.Gauge_smallSegmentLength)
                smallSegmentLength = a.getDimensionPixelSize(attr, smallSegmentLength);
            else if (attr == R.styleable.Gauge_boardTextColor)
                segmentColor = a.getColor(attr, segmentColor);
            else if (attr == R.styleable.Gauge_boardTextSize)
                mBoardTextSize = a.getDimensionPixelSize(attr, mBoardTextSize);
            else if (attr == R.styleable.Gauge_unitTextSize)
                mUnitTextSize = a.getDimensionPixelSize(attr, mUnitTextSize);
            else if (attr == R.styleable.Gauge_splitInCircles)
                mSplitInCircles = a.getBoolean(attr, mSplitInCircles);
            else if (attr == R.styleable.Gauge_gaugeBackgroundColor)
                backgroundColor = a.getColor(attr, backgroundColor);
            else if (attr == R.styleable.Gauge_gaugeSecondaryBackgroundColor)
                secondaryBackColor = a.getColor(attr, secondaryBackColor);
            /*else if (attr == R.styleable.Gauge_previewMode)
                previewMode = a.getBoolean(R.styleable.Gauge_previewMode, false);*/
        }
        a.recycle();

        if (mSplitInCircles) {
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(backgroundColor);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mBackgroundPaint.setAntiAlias(true);

            secondaryBackgroundPaint = new Paint();
            secondaryBackgroundPaint.setColor(secondaryBackColor);
            secondaryBackgroundPaint.setStyle(Paint.Style.FILL);
            secondaryBackgroundPaint.setAntiAlias(true);
        }
        measurementUnitColor = segmentColor;

        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);

        mStep = (maxValue - minValue) / nBigSegments;
        mAnglePerStep = (stopAngleInDegrees - startAngleInDegrees) / (nBigSegments * nSmallSegments);
        mTotalSegments = nBigSegments * nSmallSegments;

        Rect bounds = new Rect();
        mPaint.setTextSize(mBoardTextSize);
        mPaint.getTextBounds("9", 0, 1, bounds);
        mTextHeight = bounds.height();
        path = new Path();

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSplitInCircles) {
            canvas.drawCircle(mCenterX, mCenterY, mRadius, mBackgroundPaint);
            canvas.drawCircle(mCenterX, mCenterY, 2 * mRadius / 3, secondaryBackgroundPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        // Draw measurement unit
        if (mMeasurementUnit != null) {
            mPaint.setColor(measurementUnitColor);
            mPaint.setTextSize(mUnitTextSize);
            canvas.drawText(mMeasurementUnit, mCenterX, mMeasurementUnitRadius, mPaint);
        }
        // Draw board
        mPaint.setColor(segmentColor);
        mPaint.setTextSize(mBoardTextSize);
        canvas.rotate(startAngleInDegrees, mCenterX, mCenterY);
        path.reset();
        path.addCircle(mCenterX, mCenterY, mTextRadius, Path.Direction.CW);
        for (int i = 0; i < mTotalSegments + 1; i++) {
            if (i % nSmallSegments == 0) {
                mPaint.setStrokeWidth(mBigSegmentWidth);
                canvas.drawLine(mCenterX, mBigSegmentStart, mCenterX, mSegmentStop, mPaint);
                mPaint.setStrokeWidth(mSmallSegmentWidth);
                float xOffset = (float) (mTextRadius * Math.PI / 2.0f);
                canvas.drawTextOnPath("" + (minValue + i * mStep / nSmallSegments), path, xOffset, 0, mPaint);
            } else if (showSegments) {
                canvas.drawLine(mCenterX, mCenterY - mSmallSegmentStart, mCenterX, mSegmentStop, mPaint);
            }
            canvas.rotate(mAnglePerStep, mCenterX, mCenterY);
        }
        // Draw big circle
        if (showExternalCircle) {
            renderExternalCircle(canvas);
        }
    }

    private void renderExternalCircle(Canvas canvas) {
        mPaint.setStrokeWidth(bigCircleStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterY, mRadius - bigCircleStrokeWidth / 2, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        int widthWithPadding = w - getPaddingLeft() - getPaddingRight();
        int heightWithPadding = h - getPaddingTop() - getPaddingBottom();
        mRadius = widthWithPadding < heightWithPadding ? widthWithPadding / 2 : heightWithPadding / 2;
        mBigSegmentStart = mCenterY - mRadius + bigSegmentLength;
        mSegmentStop = mCenterY - mRadius;
        mSmallSegmentStart = mRadius - smallSegmentLength;
        mMeasurementUnitRadius = mCenterY + mRadius - 2 * bigSegmentLength;
        mTextRadius = mRadius - bigSegmentLength - 1.5f * mTextHeight;
    }

    public void setMeasurementUnit(String unit) {
        mMeasurementUnit = unit;
    }

    public void setMinValue(float value) {
        minValue = (int) value;
        mStep = (maxValue - minValue) / nBigSegments;
        mAnglePerStep = (stopAngleInDegrees - startAngleInDegrees) / (nBigSegments * nSmallSegments);
        mTotalSegments = nBigSegments * nSmallSegments;
        invalidate();
    }

    public void setMaxValue(float value) {
        maxValue = (int) value;
        mStep = (maxValue - minValue) / nBigSegments;
        mAnglePerStep = (stopAngleInDegrees - startAngleInDegrees) / (nBigSegments * nSmallSegments);
        mTotalSegments = nBigSegments * nSmallSegments;
        invalidate();
    }

    public void showSegments(boolean show) {
        showSegments = show;
        invalidate();
    }
}
