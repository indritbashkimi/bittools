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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ibashkimi.provider.providerdata.OrientationData;
import com.ibashkimi.provider.providerdata.SensorData;
import com.ibashkimi.providerstools.model.DisplayParams;
import com.ibashkimi.providerstools.model.ProviderDisplay;
import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.StyleUtils;

import org.jetbrains.annotations.NotNull;


public class CompassBoardVariant extends View implements ProviderDisplay {
    protected float direction;
    protected Paint paint;
    protected String[] mDirections;
    @ColorInt
    protected int mValueCircleColor;
    @ColorInt
    protected int mDirectionTextColor;
    private float mCircleRadius;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private float mActualXdpi;

    private boolean mShowExternalCircle;
    private int mExternalCircleStroke;
    @ColorInt
    private int mExternalCircleColor;

    private boolean mShowInternalCircles;
    private int mInternalCirclesStroke;
    @ColorInt
    private int mInternalCirclesColor;

    private int mSmallTextHeight;
    private int mBigTextHeight;
    private int mDirectionTextPadding;

    private boolean mShowSegments;
    private int mSegmentsStroke;
    @ColorInt
    private int mSegmentsColor;

    private boolean mShowDirections;

    private boolean mShowValueCircle;

    private int colorPrimary;

    private int bigTextSize;
    private int smallTextSize;
    private int segmentLength;

    private int value;

    public CompassBoardVariant(@NonNull Context context) {
        this(context, null);
    }

    public CompassBoardVariant(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassBoardVariant(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompassBoardVariant(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mActualXdpi = getResources().getDisplayMetrics().xdpi;

        mDirections = new String[]{
                getResources().getString(R.string.compass_direction_north),
                getResources().getString(R.string.compass_direction_north_east),
                getResources().getString(R.string.compass_direction_est),
                getResources().getString(R.string.compass_direction_south_east),
                getResources().getString(R.string.compass_direction_south),
                getResources().getString(R.string.compass_direction_south_west),
                getResources().getString(R.string.compass_direction_west),
                getResources().getString(R.string.compass_direction_north_west)
        };

        colorPrimary = StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.RED);
        int textColorSecondary = fetchColor(android.R.attr.textColorSecondary);

        mShowExternalCircle = true;
        mExternalCircleStroke = getDimensionPixelSize(3.0f);
        mExternalCircleColor = textColorSecondary;

        mShowInternalCircles = false;
        mInternalCirclesStroke = getDimensionPixelSize(3.0f);
        mInternalCirclesColor = textColorSecondary;

        mShowSegments = true;
        mSegmentsColor = textColorSecondary;
        mSegmentsStroke = getDimensionPixelSize(2.0f);

        mShowDirections = true;
        int directionTextSize = getDimensionPixelSize(16);
        mDirectionTextPadding = getDimensionPixelSize(8);
        mDirectionTextColor = fetchColor(android.R.attr.textColorPrimary);

        mShowValueCircle = true;
        mValueCircleColor = colorPrimary;

        bigTextSize = getDimensionPixelSize(18);
        smallTextSize = getDimensionPixelSize(12);
        segmentLength = getDimensionPixelSize(6);

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
            else if (attr == R.styleable.Compass_compassDirectionTextSize)
                bigTextSize = a.getDimensionPixelSize(attr, bigTextSize);
            else if (attr == R.styleable.Compass_compassDirectionNumberTextSize)
                smallTextSize = a.getDimensionPixelSize(attr, smallTextSize);
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

        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        paint.setTextSize(smallTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds("350", 0, 1, bounds);
        mSmallTextHeight = bounds.height();

        paint.setTextSize(bigTextSize);
        bounds = new Rect();
        paint.getTextBounds(mDirections[0], 0, 1, bounds);
        mBigTextHeight = bounds.height();

        paint.setTextSize(smallTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(mSegmentsColor);
        paint.setAntiAlias(true);
    }

    private int getDimensionPixelSize(float dimension) {
        return (int) (dimension * mActualXdpi / 160.0f);
    }

    public void setCircleColor(@ColorInt int color) {
        this.mValueCircleColor = color;
    }

    public void setCircleRadius(float smallCircleRadius) {
        this.mCircleRadius = smallCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(value, mCenterX, mCenterY);

        paint.setColor(mSegmentsColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(getDimensionPixelSize(1));
        paint.setTextSize(smallTextSize);
        for (int i = 0; i < 360; i++) {
            if (i % 10 == 0)
                canvas.drawLine(mCenterX, mCenterY - mRadius + 2 * segmentLength, mCenterX, mCenterY - mRadius, paint);
            else if (i % 5 == 0)
                canvas.drawLine(mCenterX, mCenterY - mRadius + 1.5f * segmentLength, mCenterX, mCenterY - mRadius, paint);
            else
                canvas.drawLine(mCenterX, mCenterY - mRadius + segmentLength, mCenterX, mCenterY - mRadius, paint);
            canvas.rotate(1, mCenterX, mCenterY);
        }

        float padding = 2f * segmentLength;
        paint.setStrokeWidth(1);
        for (int i = 0; i < 36; i++) {
            canvas.drawText("" + (i * 10), mCenterX, mCenterY - mRadius + mSmallTextHeight + segmentLength + padding, paint);
            canvas.rotate(10, mCenterX, mCenterY);
        }

        float directionPadding = 2 * segmentLength + mDirectionTextPadding;
        paint.setTextSize(bigTextSize);
        paint.setStrokeWidth(1);
        paint.setColor(mDirectionTextColor);
        for (int i = 0; i < 8; i++) {
            canvas.drawText(mDirections[i], mCenterX, mCenterY - mRadius + mSmallTextHeight + mBigTextHeight + segmentLength + padding + directionPadding, paint);
            canvas.rotate(45f, mCenterX, mCenterY);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorPrimary);
        //BlurMaskFilter filter = new BlurMaskFilter(getDimensionPixelSize(4f), BlurMaskFilter.Blur.OUTER);
        //paint.setMaskFilter(filter);
        canvas.drawCircle(mCenterX, mCenterY, mRadius - mSmallTextHeight - mBigTextHeight - segmentLength - padding - 2 * directionPadding, paint);
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
    }

    public void update(double dir) {
        direction = (float) dir;
        invalidate();
    }

    @Override
    public void setDisplayParams(@NonNull DisplayParams params) {

    }

    @Override
    public void onDataChanged(@NotNull SensorData data) {
        update(((OrientationData) data).getAzimuth());
    }

    public void setValue(int value) {
        this.value = value;
        invalidate();
    }

    private int fetchColor(int attr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
