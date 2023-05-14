package com.ibashkimi.ruler.protractorplumb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ibashkimi.ruler.R;
import com.ibashkimi.theme.utils.MathUtils;
import com.ibashkimi.theme.utils.StyleUtils;

public class ProtractorPlumbOverlay extends View {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mOriginX;
    private int mOriginY;
    private int mTextWidth;
    private int mTextHeight;
    private int mTextPadding;
    private float mAngle;
    private int mRadius;
    private Paint mPlumbPaint;

    public ProtractorPlumbOverlay(Context context) {
        this(context, null);
    }

    public ProtractorPlumbOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProtractorPlumbOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProtractorPlumbOverlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint = new Paint();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, metrics));
        mPaint.setAntiAlias(true);
        mPaint.setColor(StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.RED));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mPlumbPaint = new Paint();
        mPlumbPaint.setAntiAlias(true);
        mPlumbPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics));
        mPlumbPaint.setColor(StyleUtils.obtainColor(context, R.attr.colorAccent, Color.RED));

        Rect bounds = new Rect();
        mPaint.getTextBounds("360°", 0, 3, bounds);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics));
        mTextHeight = bounds.height();
        mTextWidth = bounds.width();

        mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);
        mAngle = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw vertical line
        canvas.drawLine(mOriginX, mOriginY, mOriginX, mOriginY + mHeight, mPaint);

        // Draw angle text
        canvas.drawText(MathUtils.round(mAngle, 1) + "", mOriginX, mOriginY + mRadius / 2, mPaint);

        // Draw gravity line
        canvas.rotate(-mAngle, mOriginX, mOriginY);
        canvas.drawLine(mOriginX, mOriginY, mOriginX, mOriginY + mRadius, mPlumbPaint);
        canvas.rotate(mAngle, mOriginX, mOriginY);

        //Draw pitch text
        //canvas.drawText(Utils.round(mRoll, 1)+"", mOriginX + mWidth/2 - mTextWidth/2 - mTextPadding, );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mOriginX = w / 2;
        mOriginY = getPaddingTop();
        mRadius = mHeight;
    }

    public void setRoll(float roll) {
        mAngle = roll;
        invalidate();
    }

    //public void setValue(Orientation Value);
}
