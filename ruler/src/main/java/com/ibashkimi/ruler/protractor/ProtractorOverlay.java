package com.ibashkimi.ruler.protractor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ibashkimi.ruler.R;
import com.ibashkimi.theme.utils.MathUtils;
import com.ibashkimi.theme.utils.StyleUtils;

public class ProtractorOverlay extends View implements View.OnTouchListener {
    private static final float ANGLE_DISTANCE = 20;
    private Paint mFirstLinePaint;
    private Paint mPaint;
    private Paint mSecondLinePaint;
    private Rect mMargins;
    private int mHeight;
    private float mCenterX;
    private float mCenterY;
    private int mOriginX;
    private int mOriginY;
    private int mTextWidth;
    private int mTextHeight;
    private float mTextPadding;
    private boolean portraitOrientation;

    private float mText0X;
    private float mText0Y;
    private float mText1X;
    private float mText1Y;
    private float mText2X;
    private float mText2Y;

    private Line[] mLines;
    private Line mMovingLine;
    private Line tmpLine = new Line(0f, 1f);

    private SparseArray<Line> mLinePointer = new SparseArray<>(2);

    public ProtractorOverlay(Context context) {
        this(context, null);
    }

    public ProtractorOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProtractorOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProtractorOverlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mMargins = new Rect();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, metrics);
        float lineTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, metrics);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mPaint.setColor(StyleUtils.obtainColor(context, R.attr.colorAccent, Color.RED));
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mFirstLinePaint = new Paint();
        mFirstLinePaint.setAntiAlias(true);
        mFirstLinePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mFirstLinePaint.setColor(StyleUtils.obtainColor(context, R.attr.colorSecondary, Color.BLACK));
        mFirstLinePaint.setTextSize(lineTextSize);
        mFirstLinePaint.setTextAlign(Paint.Align.CENTER);

        mSecondLinePaint = new Paint();
        mSecondLinePaint.setAntiAlias(true);
        mSecondLinePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mSecondLinePaint.setColor(StyleUtils.obtainColor(context, R.attr.colorPrimary, Color.BLACK));
        mSecondLinePaint.setTextSize(lineTextSize);
        mSecondLinePaint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        mFirstLinePaint.getTextBounds("360°", 0, 3, bounds);
        mTextHeight = bounds.height();
        mTextWidth = bounds.width();

        mTextPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);

        mLines = new Line[2];
        mLines[0] = new Line(0.866025404f, 0.5f);
        mLines[1] = new Line(1f, 0f);

        setOnTouchListener(this);
    }

    public Line[] getLines() {
        return mLines;
    }

    public void setLines(Line[] lines) {
        mLines = lines;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw lines
        drawLine(canvas, mLines[0], mFirstLinePaint);
        drawLine(canvas, mLines[1], mSecondLinePaint);

        // Draw lines angle
        canvas.drawText(MathUtils.round(mLines[0].getAngle(), 1) + "°", mText1X, mText1Y, mFirstLinePaint);
        canvas.drawText(MathUtils.round(mLines[1].getAngle(), 1) + "°", mText2X, mText2Y, mSecondLinePaint);

        double angle = Math.abs(mLines[0].getAngle() - mLines[1].getAngle());
        canvas.drawText(MathUtils.round(angle, 1) + "°", mText0X, mText0Y, mPaint);
    }

    protected void drawLine(Canvas canvas, Line line, Paint paint) {
        float radius = 4 * mHeight; // I need a long mRadius
        if (portraitOrientation) {
            canvas.drawLine(
                    mOriginX,
                    mCenterY,
                    mOriginX + radius * line.getSin(),
                    mCenterY - radius * line.getCos(),
                    paint);
        } else {
            canvas.drawLine(
                    mOriginX,
                    mOriginY,
                    mOriginX - radius * line.getCos(),
                    mOriginY + radius * line.getSin(),
                    paint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled = false;
        Line touchedLine;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                if (portraitOrientation) {
                    if (xTouch < mOriginX)
                        xTouch = mOriginX;
                } else {
                    if (yTouch < mOriginY) {
                        yTouch = mOriginY;
                    }
                }

                // check if we've touched inside some circle
                touchedLine = getTouchedLine(xTouch, yTouch);
                if (touchedLine != null) {
                    mMovingLine = touchedLine;
                    touchedLine.setSin(getSin(xTouch, yTouch));
                    touchedLine.setCos(getCos(xTouch, yTouch));
                    invalidate();
                }
                handled = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // It secondary pointers, so obtain their ids and check mCircles
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                if (portraitOrientation) {
                    if (xTouch < mOriginX)
                        xTouch = mOriginX;
                } else {
                    if (yTouch < mOriginY) {
                        yTouch = mOriginY;
                    }
                }

                // check if we've touched inside some circle
                touchedLine = getTouchedLine(xTouch, yTouch);
                if (touchedLine != null) {
                    mLinePointer.put(pointerId, touchedLine);
                    touchedLine.setSin(getSin(xTouch, yTouch));
                    touchedLine.setCos(getCos(xTouch, yTouch));
                    invalidate();
                }
                handled = true;
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                if (portraitOrientation) {
                    if (xTouch < mOriginX)
                        xTouch = mOriginX;
                } else {
                    if (yTouch < mOriginY) {
                        yTouch = mOriginY;
                    }
                }
                // check if we've touched inside some circle
                touchedLine = getTouchedLine(xTouch, yTouch);
                if (touchedLine != null) {
                    touchedLine.setSin(getSin(xTouch, yTouch));
                    touchedLine.setCos(getCos(xTouch, yTouch));
                    invalidate();
                }
                handled = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mMovingLine = null;
                clearCirclePointer();
                invalidate();
                handled = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mMovingLine = null;
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                mLinePointer.remove(pointerId);
                invalidate();
                handled = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                mMovingLine = null;
                handled = true;
                break;
            default:
                mMovingLine = null;
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    protected void clearCirclePointer() {
        mLinePointer.clear();
    }

    private float getDistanceFromOrigin(float xTouch, float yTouch) {
        return (float) Math.sqrt(Math.pow(yTouch - mOriginY, 2) + Math.pow(xTouch - mOriginX, 2));
    }

    private float getSin(int xTouch, int yTouch) {
        return (portraitOrientation ? xTouch - mOriginX : yTouch - mOriginY) / getDistanceFromOrigin(xTouch, yTouch);
    }

    private float getCos(int xTouch, int yTouch) {
        return (portraitOrientation ? mCenterY - yTouch : mCenterX - xTouch) / getDistanceFromOrigin(xTouch, yTouch);
    }

    protected Line getTouchedLine(final int xTouch, final int yTouch) {
        tmpLine.setSin(getSin(xTouch, yTouch));
        tmpLine.setCos(getCos(xTouch, yTouch));
        if (mMovingLine != null && Math.abs(mMovingLine.getAngle() - tmpLine.getAngle()) < ANGLE_DISTANCE)
            return mMovingLine;
        for (Line line : mLines) {
            if (Math.abs(line.getAngle() - tmpLine.getAngle()) < ANGLE_DISTANCE)
                return line;
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mMargins.left = getPaddingLeft();
        mMargins.right = getPaddingRight();
        mMargins.top = getPaddingTop();
        mMargins.right = getPaddingRight();

        mCenterX = w / 2;
        mCenterY = h / 2;
        int width = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();

        portraitOrientation = w < h;
        float halfHeight;
        float radius;
        if (portraitOrientation) {
            halfHeight = mHeight / 2;
            radius = halfHeight;
            mOriginX = mMargins.left;
            mOriginY = h / 2;
            mText0X = mOriginX + width / 4;
            mText0Y = mCenterY + mTextHeight / 2;
            mText1X = width - mTextWidth - mTextPadding;
            mText1Y = mOriginY - radius + mTextPadding + 1.5f * mTextHeight;
        } else {
            halfHeight = mHeight / 2;
            radius = mHeight;
            mOriginX = w / 2;
            mOriginY = mMargins.top;
            mText0X = mOriginX;
            mText0Y = mOriginY + radius / 4;
            mText1X = mOriginX - radius + mTextWidth / 2 + mTextPadding;
            mText1Y = mOriginY + radius - mTextHeight / 2 - mTextPadding;
        }
        mText2X = width - mTextWidth - mTextPadding;
        mText2Y = mOriginY + radius - mTextPadding - 0.5f * mTextHeight;
    }
}