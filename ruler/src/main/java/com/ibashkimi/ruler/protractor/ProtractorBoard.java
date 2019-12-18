package com.ibashkimi.ruler.protractor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ibashkimi.theme.utils.StyleUtils;


public class ProtractorBoard extends View {
    protected Rect mMargins; // use RectF instead.
    protected Paint mPaint;
    protected int mWidth;
    protected int mHeight;
    protected float h1;
    protected float h2;
    protected float h3;
    private float mOriginX;
    private float mOriginY;
    private float mRadiusBig;
    private float mRadiusSmall;
    private float y0;
    private float mDpi;
    private float mTextRadius;
    private float mTextHeight;
    private float mTextPadding;
    private float mTextSize;
    private int mStartValue = 180;

    public ProtractorBoard(Context context) {
        this(context, null);
    }

    public ProtractorBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProtractorBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProtractorBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDpi = getResources().getDisplayMetrics().xdpi;
        mMargins = new Rect();
        this.h1 = mDpi / 6.35f;
        this.h2 = 0.7f * this.h1;
        this.h3 = 0.5f * this.h1;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setColor(StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.BLACK));
        mPaint.setTextSize(mTextSize);

        Rect bounds = new Rect();
        mPaint.getTextBounds("360", 0, 2, bounds);
        mTextHeight = bounds.height();

        mTextPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mPaint.setStyle(Paint.Style.STROKE);
        // TODO: Fix me: use drawArc
        // Circles
        canvas.drawCircle(mOriginX, mOriginY, mRadiusBig, mPaint);
        canvas.drawCircle(mOriginX, mOriginY, mRadiusSmall, mPaint);
        // Line
        if (mWidth < mHeight) {
            canvas.drawLine(mOriginX, mMargins.top, mOriginX, mHeight - mMargins.bottom, mPaint);
            mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics));
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            for (int i = 1; i < 180; i++) {
                canvas.rotate(1, mOriginX, mOriginY);
                if (i % 10 == 0) {
                    y0 = h1;
                } else if (i % 5 == 0) {
                    y0 = h2;
                } else {
                    y0 = h3;
                }
                canvas.drawLine(mOriginX, y0 + mMargins.top, mOriginX, mMargins.top, mPaint);
                if (i % 10 == 0) {
                    String text = "" + i;
                    canvas.drawText(text, mOriginX, mOriginY - mTextRadius, mPaint);
                }
            }
        } else {
            canvas.drawLine(mOriginX - mRadiusBig, mOriginY, mOriginX + mRadiusBig, mOriginY, mPaint);
            mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics));
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            for (int i = 1; i < 180; i++) {
                canvas.rotate(1, mOriginX, mOriginY);
                if (i % 10 == 0) {
                    y0 = h1;
                } else if (i % 5 == 0) {
                    y0 = h2;
                } else {
                    y0 = h3;
                }
                canvas.drawLine(mOriginX + mRadiusBig, mOriginY, mOriginX + mRadiusBig - y0, mOriginY, mPaint);
                if (i % 10 == 0) {
                    String text = "" + (mStartValue - i);
                    canvas.rotate(-90, mOriginX + mTextRadius, mOriginY);
                    canvas.drawText(text, mOriginX + mTextRadius, mOriginY, mPaint);
                    canvas.rotate(90, mOriginX + mTextRadius, mOriginY);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w - getPaddingRight() - getPaddingLeft();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mMargins.left = getPaddingLeft();
        mMargins.right = getPaddingRight();
        mMargins.top = getPaddingTop();
        mMargins.bottom = getPaddingBottom();
        if (w < h) {
            mOriginX = mMargins.left;
            mOriginY = h / 2;
            mRadiusBig = mHeight / 2;
            mTextRadius = mRadiusBig - h1 - mTextHeight - mTextPadding;
        } else {
            mOriginX = w / 2;//mMargins.left;
            mOriginY = mMargins.top;
            mRadiusBig = mHeight;
            mTextRadius = mRadiusBig - h1 + mTextHeight - mTextPadding;
        }
        mRadiusSmall = mRadiusBig - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
    }
}
