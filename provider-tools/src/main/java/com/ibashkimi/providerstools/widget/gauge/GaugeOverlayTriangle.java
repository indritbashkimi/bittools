package com.ibashkimi.providerstools.widget.gauge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import com.ibashkimi.providerstools.R;

public class GaugeOverlayTriangle extends GaugeOverlay {
    private Point[] mPoints;
    private float[] mFloatPoints;
    private Path mPath;

    public GaugeOverlayTriangle(Context context) {
        super(context);
    }

    public GaugeOverlayTriangle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GaugeOverlayTriangle(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_AppTheme_Gauge);
    }

    public GaugeOverlayTriangle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPoints = new Point[3];
        mFloatPoints = new float[8];
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPoints[0] = new Point(mCenterX - 40, mArrowY1 + 150);
        mPoints[1] = new Point(mCenterX + 40, mArrowY1 + 150);
        mPoints[2] = new Point(mCenterX, mArrowY1 + 70);
    }

    @Override
    protected void renderArrow(Canvas canvas) {
        mPaint.setColor(mArrowColor);
        canvas.save();
        canvas.rotate(-135 + (mValue - mGaugeMinValue) * mAnglePerValue, mCenterX, mCenterY);
        drawArrows(mPoints, canvas, mPaint);
        canvas.restore();
    }

    private void drawArrows(Point[] point, Canvas canvas, Paint paint) {
        mFloatPoints[0] = point[0].x;
        mFloatPoints[1] = point[0].y;
        mFloatPoints[2] = point[1].x;
        mFloatPoints[3] = point[1].y;
        mFloatPoints[4] = point[2].x;
        mFloatPoints[5] = point[2].y;
        mFloatPoints[6] = point[0].x;
        mFloatPoints[7] = point[0].y;

        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 8, mFloatPoints, 0, null, 0, null, 0, null, 0, 0, paint);
        mPath.moveTo(point[0].x, point[0].y);
        mPath.lineTo(point[1].x, point[1].y);
        mPath.lineTo(point[2].x, point[2].y);
        canvas.drawPath(mPath, paint);
    }
}
