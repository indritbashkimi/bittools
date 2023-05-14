package com.ibashkimi.ruler.ruler2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.ibashkimi.ruler.R;
import com.ibashkimi.theme.utils.StyleUtils;

import java.text.DecimalFormat;

public class Ruler2Overlay extends View implements View.OnTouchListener {

    /**
     * Paint to draw mCircles
     */
    protected static final int CIRCLES_LIMIT = 2;

    /**
     * All available mCircles
     */
    protected CircleArea[] mCircles;
    protected SparseArray<CircleArea> mCirclePointer = new SparseArray<>(CIRCLES_LIMIT);
    protected float mMarginY;
    protected float mMarginX;
    protected float mDpi;
    protected float mPixelsXMm;
    protected float mTextSize;
    protected Path mPath;
    protected float y0;
    protected float y1;
    protected float h;
    protected boolean mFill = true;
    private float mWidth;
    private float mHalfWidth;
    private float mHeight;
    private float mCircularTextRadius;
    private float mCircleRadius;
    private Paint mInsideCirclePaint;
    private Paint mCircularTextPaint;
    private Paint mNormalTextPaint;
    private Paint mFillPaint;

    private boolean mMoving;

    private float mOffset10;
    private float mOffset20;
    private float mOffset30;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public Ruler2Overlay(Context ct) {
        this(ct, null);
    }

    public Ruler2Overlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ruler2Overlay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Ruler2Overlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mMarginX = 0;
        mDpi = getResources().getDisplayMetrics().ydpi;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mPixelsXMm = mDpi / (float) 25.4;
        mMarginY = mPixelsXMm;
        mPath = new Path();
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, metrics);
        float circleDiameter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, metrics);
        mCircleRadius = circleDiameter / 2;
        mCircularTextRadius = mCircleRadius + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, metrics);

        mOffset10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, metrics);
        mOffset20 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        mOffset30 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, metrics);

        // Set up mInsideCirclePaint;
        mInsideCirclePaint = new Paint();
        mInsideCirclePaint.setColor(StyleUtils.obtainColor(context, R.attr.colorOnSecondary, Color.BLACK));
        mInsideCirclePaint.setTextSize(mTextSize);
        mInsideCirclePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mInsideCirclePaint.setAntiAlias(true);
        mInsideCirclePaint.setStyle(Paint.Style.FILL);
        mInsideCirclePaint.setTextAlign(Paint.Align.CENTER);

        // Set up mCircularTextPaint
        mCircularTextPaint = new Paint();
        mCircularTextPaint.setColor(StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.BLACK));
        mCircularTextPaint.setTextSize(mTextSize);
        mCircularTextPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mCircularTextPaint.setAntiAlias(true);
        mCircularTextPaint.setStyle(Paint.Style.FILL);

        // Set up mNormalTextPaint
        mNormalTextPaint = new Paint();
        mNormalTextPaint.setColor(StyleUtils.obtainColor(context, android.R.attr.textColorPrimary, Color.BLACK));
        mNormalTextPaint.setTextSize(mTextSize);
        mNormalTextPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mNormalTextPaint.setAntiAlias(true);
        mNormalTextPaint.setStyle(Paint.Style.FILL);
        mNormalTextPaint.setTextAlign(Paint.Align.CENTER);

        // Set up mFillPaint;
        mFillPaint = new Paint();
        int accentColor = StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.BLACK);
        mFillPaint.setColor(ColorUtils.setAlphaComponent(accentColor, 30));
        mFillPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
        setOnTouchListener(this);
    }

    private void createCircle() {
        mCircles = new CircleArea[CIRCLES_LIMIT];
        mCircles[0] = new CircleArea(mHalfWidth, mMarginY, mCircleRadius);
        mCircles[1] = new CircleArea(mHalfWidth, mHeight - mMarginY, mCircleRadius);
        y0 = mMarginY;
        y1 = mHeight - mMarginY;
        h = y1 - y0;
    }

    public CircleArea[] getCircles() {
        return mCircles;
    }

    public void setCircles(CircleArea[] circles) {
        if (circles[0].getCenterX() == 0 || circles[1].getCenterX() == 0)
            return;
        this.mCircles = circles;
        y0 = circles[0].getCenterY();
        y1 = circles[1].getCenterY();
        h = y1 - y0;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mWidth == 0 || mHeight == 0)
            return;
        if (mCircles == null) {
            createCircle();
        }

        for (CircleArea circle : mCircles) {
            mPath.reset();
            mPath.addCircle(circle.getCenterX(), circle.getCenterY(), mCircularTextRadius, Path.Direction.CW);
            canvas.drawLine(0, circle.getCenterY(), mWidth, circle.getCenterY(), mCircularTextPaint);

            if (mMoving) {
                if (mFill) {
                    canvas.drawRect(mMarginX, mCircles[0].getCenterY(), mWidth - mMarginX, mCircles[1].getCenterY(), mFillPaint);
                }
                float ang = (circle.getCenterY() - y0) * 90 / h;
                canvas.rotate(-ang + 25, circle.getCenterX(), circle.getCenterY());
                String text = decimalFormat.format((circle.getCenterY() - mMarginY) / this.mDpi);
                canvas.drawTextOnPath(text, mPath, 0, 0, mCircularTextPaint);
                canvas.rotate(ang - 25, circle.getCenterX(), circle.getCenterY());

                mPath.reset();
                mPath.addCircle(circle.getCenterX(), circle.getCenterY(), mCircularTextRadius, Path.Direction.CW);
                ang = (circle.getCenterY() - y0) * 90 / h;

                canvas.rotate(115 + ang, circle.getCenterX(), circle.getCenterY());
                text = decimalFormat.format((circle.getCenterY() - mMarginY) / (mPixelsXMm * 10));
                canvas.drawTextOnPath(text, mPath, 10, 10, mCircularTextPaint);
                canvas.rotate(-115 - ang, circle.getCenterX(), circle.getCenterY());
            }
        }

        canvas.drawLine(mCircles[0].getCenterX(), mCircles[0].getCenterY(), mCircles[1].getCenterX(), mCircles[1].getCenterY(), mCircularTextPaint);

        // Draw inside circle
        for (CircleArea circle : mCircles) {
            canvas.drawCircle(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), mCircularTextPaint);
            if (!mMoving) {
                canvas.drawText(decimalFormat.format((circle.getCenterY() - mMarginY) / (this.mPixelsXMm * 10)),
                        circle.getCenterX(),
                        circle.getCenterY() - mOffset10,
                        mInsideCirclePaint);
                canvas.drawLine(circle.getCenterX() - circle.getRadius() + mOffset20, circle.getCenterY(), circle.getCenterX() + circle.getRadius() - mOffset20, circle.getCenterY(), mInsideCirclePaint);
                canvas.drawText(decimalFormat.format((circle.getCenterY() - mMarginY) / this.mDpi),
                        circle.getCenterX(),
                        circle.getCenterY() + mOffset30,
                        mInsideCirclePaint);
            }
        }

        // Draw values
        float distance = Math.abs(mCircles[1].getCenterY() - mCircles[0].getCenterY());
        float y;
        if (mCircles[1].getCenterY() > mCircles[0].getCenterY()) {
            y = mCircles[0].getCenterY();
        } else {
            y = mCircles[1].getCenterY();
        }
        canvas.drawText(decimalFormat.format((0.1 * distance / mPixelsXMm)) + " cm", // TODO
                mHalfWidth / 2, y + distance / 2, mNormalTextPaint);
        canvas.drawText(decimalFormat.format((distance / mDpi)) + " in", // TODO
                3 * mHalfWidth / 2, y + distance / 2, mNormalTextPaint);
        /*canvas.drawText(mUnitCentimeter, mHalfWidth/2, y + distance/2 + mOffset50, mNormalTextPaint);
        canvas.drawText(mUnitInch, 3*mHalfWidth/2, y + distance / 2 + mOffset50, mNormalTextPaint);*/
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mCircles == null)
            return false;
        boolean handled = false;
        CircleArea touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d(TAG, "action down");
                mMoving = true;
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some circle
                touchedCircle = getTouchedCircle(xTouch, yTouch);
                if (touchedCircle != null) {
                    touchedCircle.setCenterX(mWidth / 2);
                    touchedCircle.setCenterY(getCorrected(yTouch));
                    mCirclePointer.put(event.getPointerId(0), touchedCircle);
                    invalidate();
                }
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //Log.w(TAG, "Pointer down");

                // It secondary pointers, so obtain their ids and check mCircles
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some circle
                touchedCircle = getTouchedCircle(xTouch, yTouch);
                if (touchedCircle != null) {
                    mCirclePointer.put(pointerId, touchedCircle);
                    touchedCircle.setCenterX(mWidth / 2);
                    touchedCircle.setCenterY(getCorrected(yTouch));
                    invalidate();
                }
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();
                //Log.w(TAG, "Move");
                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    //xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedCircle = mCirclePointer.get(pointerId);
                    if (null != touchedCircle) {
                        touchedCircle.setCenterX(mWidth / 2);
                        touchedCircle.setCenterY(getCorrected(yTouch));
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                //Log.d(TAG, "action up");
                mMoving = false;
                clearCirclePointer();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                //Log.d(TAG, "action pointer up");
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                mCirclePointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                //Log.d(TAG, "action cancel");
                handled = true;
                break;

            default:
                // do nothing
                break;
        }
        return super.onTouchEvent(event) || handled;
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    protected void clearCirclePointer() {
        mCirclePointer.clear();
    }

    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     * @return {@link CircleArea} touched circle or null if no circle has been touched
     */
    protected CircleArea getTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touched = null;

        for (CircleArea circle : mCircles) {
            if (Math.abs(circle.getCenterY() - yTouch) <= circle.getRadius()) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    protected float getCorrected(float y) {
        if (y < mMarginY)
            return mMarginY;
        if (y > getHeight() - mMarginY)
            return getHeight() - mMarginY;
        return y;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w + getPaddingLeft() + getPaddingRight();
        mHeight = h + getPaddingBottom() + getPaddingTop();
        mHalfWidth = mWidth / 2;
    }
}

