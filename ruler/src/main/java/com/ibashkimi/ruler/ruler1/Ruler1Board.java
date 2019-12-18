package com.ibashkimi.ruler.ruler1;

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

public class Ruler1Board extends View {
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

    private RectF mMargins;

    private Paint mPaint = new Paint();

    private float H1_STOKE_WIDTH;
    private float H2_STOKE_WIDTH;
    private float H3_STOKE_WIDTH;

    public Ruler1Board(Context context) {
        this(context, null);
    }

    public Ruler1Board(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ruler1Board(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Ruler1Board(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        float pxi = getResources().getDisplayMetrics().xdpi;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics));
        mPaint.setColor(StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.BLACK));
        mPXIn = pxi;
        mPXCm = pxi / 2.54f;
        mPXMm = pxi / 25.4f;
        mPXIm = pxi / 16;
        H1 = mPXCm * 0.6f;
        H2 = H1 * 0.7f;
        H3 = H1 * 0.5f;
        H4 = H1 * 0.3f;

        H3_STOKE_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
        H2_STOKE_WIDTH = H3_STOKE_WIDTH;
        H1_STOKE_WIDTH = H3_STOKE_WIDTH * 1.5f;

        mMargins = new RectF(0, 0, 0, 0);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float length;
        for (int i = 0; i < mHeight / mPXMm; i++) {
            if (i % 10 == 0) {
                length = H1;
                mPaint.setStrokeWidth(H1_STOKE_WIDTH);
            } else if (i % 5 == 0) {
                length = H2;
                mPaint.setStrokeWidth(H2_STOKE_WIDTH);
            } else {
                length = H3;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            }
            canvas.drawLine(mMargins.left, mMargins.top + i * mPXMm, length, mMargins.top + i * mPXMm, mPaint);
            if ((i + 1) % 10 == 0) {
                canvas.drawText("" + ((i + 1) / 10), H1 - mPXMm, mMargins.top + i * mPXMm, mPaint);
            }
        }
        for (int i = 0; i < mWidth / mPXMm; i++) {
            if (i % 10 == 0) {
                length = H1;
                mPaint.setStrokeWidth(H1_STOKE_WIDTH);
            } else if (i % 5 == 0) {
                length = H2;
                mPaint.setStrokeWidth(H2_STOKE_WIDTH);
            } else {
                length = H3;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            }
            canvas.drawLine(mMargins.left + i * mPXMm, mMargins.top, mMargins.left + i * mPXMm, mMargins.top + length, mPaint);
            if ((i + 1) % 10 == 0)
                canvas.drawText("" + ((i + 1) / 10), mMargins.left + i * mPXMm - mPXMm, H1, mPaint);
        }

        for (int i = 0; i < mHeight / mPXIm; i++) {
            if (i % 16 == 0) {
                length = H1;
                mPaint.setStrokeWidth(H1_STOKE_WIDTH);
            } else if (i % 8 == 0) {
                length = H2;
                mPaint.setStrokeWidth(H2_STOKE_WIDTH);
            } else if (i % 4 == 0) {
                length = H3;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            } else {
                length = H4;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            }
            canvas.drawLine(mWidth - length, mMargins.top + i * mPXIm, mWidth, mMargins.top + i * mPXIm, mPaint);
            if ((i + 1) % 16 == 0)
                canvas.drawText("" + ((i + 1) / 16), mWidth - H1 + mPXMm, mMargins.top + i * mPXIm, mPaint);
        }

        for (int i = 0; i < mWidth / mPXIm; i++) {
            if (i % 16 == 0) {
                length = H1;
                mPaint.setStrokeWidth(H1_STOKE_WIDTH);
            } else if (i % 8 == 0) {
                length = H2;
                mPaint.setStrokeWidth(H2_STOKE_WIDTH);
            } else if (i % 4 == 0) {
                length = H3;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            } else {
                length = H4;
                mPaint.setStrokeWidth(H3_STOKE_WIDTH);
            }
            canvas.drawLine(mMargins.left + i * mPXIm, mHeight - mMargins.bottom, mMargins.left + i * mPXIm, mHeight - mMargins.bottom - length, mPaint);
            if ((i + 1) % 16 == 0)
                canvas.drawText("" + ((i + 1) / 16), mMargins.left + i * mPXIm - mPXMm, mHeight - H1 + 2 * mPXMm, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = getWidth();
        mHeight = getHeight();
        mMargins.top = getPaddingTop();
        mMargins.left = getPaddingLeft();
        mMargins.bottom = getPaddingBottom();
        mMargins.right = getPaddingRight();
    }
}