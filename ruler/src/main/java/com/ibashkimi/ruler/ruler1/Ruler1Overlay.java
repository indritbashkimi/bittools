package com.ibashkimi.ruler.ruler1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.ibashkimi.ruler.R;
import com.ibashkimi.theme.utils.StyleUtils;

import java.text.DecimalFormat;

public class Ruler1Overlay extends View implements View.OnTouchListener {
    private static final boolean FILL = false;
    private float xTouch;
    private float yTouch;
    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;
    private float mPXMm;
    private float mPXCm;
    private Rect mPadding;
    private float mActualDpi;
    private boolean mMoving;
    private int mLineColor;
    private int mFillColor;
    private int mTextColor;

    private int mMinX;
    private int mMaxX;
    private int mMinY;
    private int mMaxY;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public Ruler1Overlay(Context context) {
        this(context, null);
    }

    public Ruler1Overlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ruler1Overlay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Ruler1Overlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mActualDpi = getResources().getDisplayMetrics().xdpi;
        mPadding = new Rect();
        mPXCm = getResources().getDisplayMetrics().xdpi / 2.54f;
        mPaint = new Paint();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics));
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics));

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mLineColor = StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.BLACK);
        mFillColor = ColorUtils.setAlphaComponent(mLineColor, 50);
        mTextColor = StyleUtils.obtainColor(context, android.R.attr.textColorPrimary, Color.BLACK);
        setOnTouchListener(this);
    }

    public float getX() {
        return xTouch;
    }

    public float getY() {
        return yTouch;
    }

    public void setPoint(float x, float y) {
        this.xTouch = x;
        this.yTouch = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mLineColor);
        canvas.drawLine(0, yTouch, mWidth, yTouch, mPaint);
        canvas.drawLine(xTouch, 0, xTouch, mHeight, mPaint);

        mPaint.setColor(mTextColor);
        String left = decimalFormat.format((yTouch - mPadding.left) / mPXCm) + " cm"; // TODO
        String top = decimalFormat.format((xTouch - mPadding.top) / mPXCm) + " cm"; // TODO
        String right = decimalFormat.format((yTouch - mPadding.right) / mActualDpi) + " in"; // TODO
        String bottom = decimalFormat.format((xTouch - mPadding.bottom) / mActualDpi) + " in"; // TODO

        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(left, mPXCm, mCenterY, mPaint);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(top, mCenterX, mPXCm * 1.2f, mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(right, mWidth - mPXCm, mCenterY, mPaint);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(bottom, mCenterX, mHeight - mPXCm, mPaint);

        if (FILL && mMoving) {
            mPaint.setColor(mFillColor);
            canvas.drawRect(0, 0, xTouch, yTouch, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mCenterX = w / 2;
        mCenterY = h / 2;

        mPadding.left = getPaddingLeft();
        mPadding.right = getPaddingRight();
        mPadding.top = getPaddingTop();
        mPadding.bottom = getPaddingBottom();

        mMinX = mPadding.left;
        mMaxX = w - mPadding.right;
        mMinY = mPadding.top;
        mMaxY = h - mPadding.bottom;
    }

    protected void normalize() {
        if (xTouch < mMinX)
            xTouch = mMinX;
        else if (xTouch > mMaxX)
            xTouch = mMaxX;
        if (yTouch < mMinY)
            yTouch = mMinY;
        else if (yTouch > mMaxY)
            yTouch = mMaxY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled = false;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mMoving = true;
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                normalize();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mMoving = true;
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                normalize();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                mMoving = true;
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                normalize();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                mMoving = false;
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mMoving = false;
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                mMoving = false;
                handled = true;
                break;

            default:
                mMoving = false;
                // do nothing
                break;
        }
        return super.onTouchEvent(event) || handled;
    }
}
