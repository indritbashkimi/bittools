package com.ibashkimi.providerstools.widget.gauge3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ibashkimi.providerstools.R
import com.ibashkimi.theme.utils.StyleUtils
import java.text.DecimalFormat


open class SimpleGauge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val unitTextPaint: Paint = Paint()
    private var rect: RectF = RectF()

    private var mValue: Int = 0
    private var mPointAngle: Double = 0.toDouble()
    private var mPoint: Int = 0

    private var centerX: Float = 0f

    private var centerY: Float = 0f

    private var radius: Float = 0f

    private lateinit var shader: Shader

    var strokeWidth: Float = 10f
    var strokeColor: Int = 0
    var startAngle: Int = 0
    var sweepAngle: Int = 360
    var startValue: Int = 0
    var endValue: Int = 1000
        set(endValue) {
            field = endValue
            mPointAngle = Math.abs(sweepAngle).toDouble() / (this.endValue - startValue)
            invalidate()
        }

    var pointSize: Int = 0
    var pointStartColor: Int = 0
    var pointEndColor: Int = 0

    var formatter: DecimalFormat = DecimalFormat("#")

    var decimalFormat: String = "#"
        set(value) {
            formatter = DecimalFormat(value)
        }

    var measurementUnit: String = ""

    var value: Int
        get() = mValue
        set(value) {
            mValue = value
            mPoint = (startAngle + (mValue - startValue) * mPointAngle).toInt()
            invalidate()
        }

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleGauge,
            defStyleAttr,
            defStyleRes
        )

        // stroke style
        strokeWidth = a.getDimension(R.styleable.SimpleGauge_gaugeStrokeWidth, strokeWidth)
        strokeColor = a.getColor(
            R.styleable.SimpleGauge_gaugeStrokeColor,
            ContextCompat.getColor(context, android.R.color.darker_gray)
        )

        // angle start and sweep (opposite direction 0, 270, 180, 90)
        startAngle = a.getInt(R.styleable.SimpleGauge_gaugeStartAngle, startAngle)
        sweepAngle = a.getInt(R.styleable.SimpleGauge_gaugeSweepAngle, sweepAngle)

        // scale (from mStartValue to mEndValue)
        startValue = a.getInt(R.styleable.SimpleGauge_gaugeStartValue, startValue)
        endValue = a.getInt(R.styleable.SimpleGauge_gaugeEndValue, endValue)

        // pointer size and color
        pointSize = a.getInt(R.styleable.SimpleGauge_gaugePointSize, pointSize)
        pointStartColor = a.getColor(
            R.styleable.SimpleGauge_gaugePointStartColor,
            ContextCompat.getColor(context, android.R.color.white)
        )
        pointEndColor = a.getColor(
            R.styleable.SimpleGauge_gaugePointEndColor,
            ContextCompat.getColor(context, android.R.color.black)
        )

        // calculating one point sweep
        mPointAngle = Math.abs(sweepAngle).toDouble() / (endValue - startValue)

        a.recycle()

        //main Paint
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE

        textPaint.isAntiAlias = true
        textPaint.textSize = StyleUtils.dpToPx(context, 56)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color =
            StyleUtils.obtainColor(context, android.R.attr.textColorPrimary, Color.BLACK)

        unitTextPaint.isAntiAlias = true
        unitTextPaint.textSize = StyleUtils.dpToPx(context, 24)
        unitTextPaint.textAlign = Paint.Align.CENTER
        unitTextPaint.color =
            StyleUtils.obtainColor(context, android.R.attr.textColorSecondary, Color.BLACK)

        mValue = startValue

        mPoint = startAngle
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = strokeColor
        paint.shader = null
        canvas.drawArc(rect, startAngle.toFloat(), sweepAngle.toFloat(), false, paint)
        paint.color = pointStartColor
        paint.shader = shader
        if (pointSize > 0) {//if size of pointer is defined
            if (mPoint > startAngle + pointSize / 2) {
                canvas.drawArc(
                    rect,
                    (mPoint - pointSize / 2).toFloat(),
                    pointSize.toFloat(),
                    false,
                    paint
                )
            } else { //to avoid exceeding start/zero point
                canvas.drawArc(rect, mPoint.toFloat(), pointSize.toFloat(), false, paint)
            }
        } else { //draw from start point to value point (long pointer)
            if (mValue == startValue)
            //use non-zero default value for start point (to avoid lack of pointer for start/zero value)
                canvas.drawArc(
                    rect,
                    startAngle.toFloat(),
                    DEFAULT_LONG_POINTER_SIZE.toFloat(),
                    false,
                    paint
                )
            else
                canvas.drawArc(
                    rect,
                    startAngle.toFloat(),
                    (mPoint - startAngle).toFloat(),
                    false,
                    paint
                )
        }

        val yPos = (centerY - (textPaint.descent() + textPaint.ascent()) / 2)
        val valueRep = formatter.format(mValue)
        canvas.drawText(valueRep, centerX, yPos, textPaint)

        canvas.drawText(measurementUnit, centerX, centerY + radius, unitTextPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = w.toFloat() / 2
        centerY = h.toFloat() / 2

        val widthWithPadding = w - paddingStart - paddingEnd
        val heightWithPadding = h - paddingTop - paddingBottom

        radius = if (widthWithPadding < heightWithPadding) widthWithPadding.toFloat() / 2
        else heightWithPadding.toFloat() / 2

        val size =
            if (widthWithPadding < heightWithPadding) widthWithPadding / 2 else heightWithPadding / 2

        rect.set(
            centerX - size,
            centerY - size,
            centerX + size,
            centerY + size
        )

        shader = LinearGradient(
            w.toFloat(),
            h.toFloat(),
            0f,
            0f,
            pointEndColor,
            pointStartColor,
            Shader.TileMode.CLAMP
        )
    }

    companion object {

        private const val DEFAULT_LONG_POINTER_SIZE = 1
    }
}
