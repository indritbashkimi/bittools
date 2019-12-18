package com.ibashkimi.ruler.ruler2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ibashkimi.theme.utils.StyleUtils;


public class Ruler2Board extends View {
    private float H1;
    private float H2;
    private float H3;
    private float H4;

    /**
     * Pixels per inch
     */
    private float mPXIn;
    /**
     * Pixels per centimeter
     */
    private float mPXCm;
    /**
     * Pixels per millimeter
     */
    private float mPXMm;
    /**
     * Pixels per 1/16 of inch
     */
    private float mPXIm;

    private int mWidth;
    private int mHeight;
    private int mHalfWidth;
    private RectF mMargins;
    private Paint mPaint = new Paint();
    private float stroke1Dp;
    private float stroke2Dp;

    public Ruler2Board(Context context) {
        this(context, null);
    }

    public Ruler2Board(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ruler2Board(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Ruler2Board(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        float dpi = getResources().getDisplayMetrics().xdpi;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        stroke1Dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
        stroke2Dp = 1.5f * stroke1Dp;

        mPaint.setColor(StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.BLACK));
        mPaint.setStrokeWidth(stroke1Dp);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics));

        mPXIn = dpi;
        mPXCm = dpi / 2.54f;
        mPXMm = dpi / 25.4f;
        mPXIm = dpi / 16;
        H1 = mPXCm * 0.6f;
        H2 = H1 * 0.7f;
        H3 = H1 * 0.5f;
        H4 = H1 * 0.3f;

        mMargins = new RectF(0, mPXMm, 0, 0);

        setWillNotDraw(false);
        setDrawingCacheEnabled(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float length;
        for (int i = 0; i < mHeight / mPXMm; i++) {
            if (i % 10 == 0) {
                length = H1;
                mPaint.setStrokeWidth(stroke2Dp);
            } else if (i % 5 == 0) {
                length = H2;
                mPaint.setStrokeWidth(stroke1Dp);
            } else {
                length = H3;
                mPaint.setStrokeWidth(stroke1Dp);
            }
            canvas.drawLine(mMargins.left, mMargins.top + i * mPXMm, length, mMargins.top + i * mPXMm, mPaint);
            if ((i + 1) % 10 == 0)
                canvas.drawText("" + ((i + 1) / 10), H1 - mPXMm, mMargins.top + i * mPXMm, mPaint);
        }

        for (int i = 0; i < mHeight / mPXIm; i++) {
            if (i % 16 == 0) {
                length = H1;
                mPaint.setStrokeWidth(stroke2Dp);
            } else if (i % 8 == 0) {
                length = H2;
                mPaint.setStrokeWidth(stroke2Dp);
            } else if (i % 4 == 0) {
                length = H3;
                mPaint.setStrokeWidth(stroke1Dp);
            } else {
                length = H4;
                mPaint.setStrokeWidth(stroke1Dp);
            }
            canvas.drawLine(mWidth - length, mMargins.top + i * mPXIm, mWidth, mMargins.top + i * mPXIm, mPaint);
            if ((i + 1) % 16 == 0)
                canvas.drawText("" + ((i + 1) / 16), mWidth - H1 + mPXMm, mMargins.top + i * mPXIm, mPaint);
        }

        canvas.drawLine(mHalfWidth, 0, mHalfWidth, mHeight, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = getWidth();
        mHeight = getHeight();
        mHalfWidth = mWidth / 2;
    }
}
