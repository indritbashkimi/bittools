package com.ibashkimi.providerstools.widget.compass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;

import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.StyleUtils;

public class CompassBoard extends View {
    protected String[] mDirections;
    @ColorInt
    protected int mValueCircleColor;
    @ColorInt
    protected int mDirectionTextColor;

    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private float mCircleRadius;
    private float mTextRadius;

    private float mActualXdpi;

    private boolean mShowExternalCircle;
    private int mExternalCircleStroke;
    @ColorInt
    private int mExternalCircleColor;

    private boolean mShowInternalCircles;
    private int mInternalCirclesStroke;
    @ColorInt
    private int mInternalCirclesColor;

    private int mDirectionTextHeight;
    private int mDirectionTextPadding;

    private boolean mShowSegments;
    private int mSegmentsStroke;
    @ColorInt
    private int mSegmentsColor;

    private boolean mShowDirections;

    private boolean mShowValueCircle;

    private int value;

    public CompassBoard(Context context) {
        this(context, null);
    }

    public CompassBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompassBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mActualXdpi = getResources().getDisplayMetrics().xdpi;

        mDirections = new String[4];
        mDirections[0] = getResources().getString(R.string.compass_direction_north);
        mDirections[1] = getResources().getString(R.string.compass_direction_est);
        mDirections[2] = getResources().getString(R.string.compass_direction_south);
        mDirections[3] = getResources().getString(R.string.compass_direction_west);

        int colorPrimary = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        int textColorSecondary = fetchColor(android.R.attr.textColorSecondary);

        mShowExternalCircle = true;
        mExternalCircleStroke = getDimensionPixelSize(3.0f);
        mExternalCircleColor = colorPrimary;

        mShowInternalCircles = false;
        mInternalCirclesStroke = getDimensionPixelSize(3.0f);
        mInternalCirclesColor = colorPrimary;

        mShowSegments = true;
        mSegmentsColor = colorPrimary;
        mSegmentsStroke = getDimensionPixelSize(2.0f);

        mShowDirections = true;
        int directionTextSize = getDimensionPixelSize(28);
        mDirectionTextPadding = getDimensionPixelSize(12);
        mDirectionTextColor = textColorSecondary;

        mShowValueCircle = true;
        mValueCircleColor = colorPrimary;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Compass,
                defStyleAttr, defStyleRes);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Compass_compassShowExternalCircle)
                mShowExternalCircle = a.getBoolean(attr, mShowExternalCircle);
            else if (attr == R.styleable.Compass_compassExternalCircleColor)
                mExternalCircleColor = a.getColor(attr, mExternalCircleColor);
            else if (attr == R.styleable.Compass_compassExternalCircleStrokeWidth)
                mExternalCircleStroke = a.getDimensionPixelSize(attr, mExternalCircleStroke);
            else if (attr == R.styleable.Compass_compassShowInternalCircles)
                mShowInternalCircles = a.getBoolean(attr, mShowInternalCircles);
            else if (attr == R.styleable.Compass_compassInternalCirclesColor)
                mInternalCirclesColor = a.getColor(attr, mInternalCirclesColor);
            else if (attr == R.styleable.Compass_compassInternalCirclesStrokeWidth)
                mInternalCirclesStroke = a.getDimensionPixelSize(attr, mInternalCirclesStroke);
            else if (attr == R.styleable.Compass_compassShowSegments)
                mShowSegments = a.getBoolean(attr, mShowSegments);
            else if (attr == R.styleable.Compass_compassSegmentStroke)
                mSegmentsStroke = a.getDimensionPixelSize(attr, mSegmentsStroke);
            else if (attr == R.styleable.Compass_compassSegmentColor)
                mSegmentsColor = a.getColor(attr, mSegmentsColor);
            else if (attr == R.styleable.Compass_compassDirectionColor)
                mDirectionTextColor = a.getColor(attr, mDirectionTextColor);
            else if (attr == R.styleable.Compass_compassShowDirections)
                mShowDirections = a.getBoolean(attr, mShowDirections);
            else if (attr == R.styleable.Compass_compassDirectionTextSize)
                directionTextSize = a.getDimensionPixelSize(attr, directionTextSize);
            else if (attr == R.styleable.Compass_compassDirectionTextPadding)
                mDirectionTextPadding = a.getDimensionPixelSize(attr, mDirectionTextPadding);
            else if (attr == R.styleable.Compass_compassShowValueCircle)
                mShowValueCircle = a.getBoolean(attr, mShowValueCircle);
            else if (attr == R.styleable.Compass_compassValueCircleColor) {
                mValueCircleColor = a.getColor(attr, mValueCircleColor);
            }
        }
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(directionTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        mPaint.getTextBounds(mDirections[0], 0, 1, bounds);
        mDirectionTextHeight = bounds.height();

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(value, mCenterX, mCenterY);

        // Draw directions text
        if (mShowDirections) {
            mPaint.setColor(mDirectionTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < 4; i++) {
                canvas.drawText(mDirections[i], mCenterX, mCenterY - mTextRadius, mPaint);
                canvas.rotate(90, mCenterX, mCenterY);
            }
        }

        // Draw segments
        if (mShowSegments) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mSegmentsColor);
            mPaint.setStrokeWidth(mSegmentsStroke);
            canvas.drawLine(mCenterX, mCenterY - mCircleRadius - mDirectionTextPadding, mCenterX, mCenterY - mTextRadius + mDirectionTextPadding, mPaint);
            canvas.drawLine(mCenterX - mTextRadius + mDirectionTextPadding, mCenterY, mCenterX - mCircleRadius - mDirectionTextPadding, mCenterY, mPaint);
            canvas.drawLine(mCenterX, mCenterY + mCircleRadius + mDirectionTextPadding, mCenterX, mCenterY + mTextRadius - mDirectionTextPadding, mPaint);
            canvas.drawLine(mCenterX + mCircleRadius + mDirectionTextPadding, mCenterY, mCenterX + mTextRadius - mDirectionTextPadding, mCenterY, mPaint);
        }

        // Draw circles
        if (mShowInternalCircles) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mInternalCirclesColor);
            mPaint.setStrokeWidth(mInternalCirclesStroke);
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius + mDirectionTextPadding, mPaint);
            canvas.drawCircle(mCenterX, mCenterY, mTextRadius - mDirectionTextPadding, mPaint);
        }

        if (mShowExternalCircle) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mExternalCircleColor);
            mPaint.setStrokeWidth(mExternalCircleStroke);
            canvas.drawCircle(mCenterX, mCenterY, mRadius - mExternalCircleStroke / 2, mPaint);
        }

        // Draw value circle
        if (mShowValueCircle) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mValueCircleColor);
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingRight() - getPaddingLeft();
        int height = h - getPaddingTop() - getPaddingBottom();

        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = Math.min(width, height) / 2;
        mTextRadius = mRadius - mExternalCircleStroke / 2 - mDirectionTextHeight - mDirectionTextPadding;
    }

    public void setCircleColor(@ColorInt int color) {
        this.mValueCircleColor = color;
    }

    public void setCircleRadius(float smallCircleRadius) {
        this.mCircleRadius = smallCircleRadius;
    }

    public void setValue(int value) {
        this.value = value;
        invalidate();
    }

    private int getDimensionPixelSize(float dimension) {
        return (int) (dimension * mActualXdpi / 160.0f);
    }

    private int fetchColor(int attr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
