package com.thorhelgen.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

import java.util.Calendar

class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Multiplier for the radius size
    private val FRAME_MARK_SIZE: Float = 0.85F

    private val secondColor: Int
    private val minuteColor: Int
    private val hourColor:   Int

    private val secondLength: Float
    private val minuteLength: Float
    private val hourLength:   Float

    private val secondWidth: Float
    private val minuteWidth: Float
    private val hourWidth:   Float

    private val framePaint: Paint
    private val secondPaint: Paint
    private val minutePaint: Paint
    private val hourPaint: Paint

    private var frameRadius: Float = 200f
    // Array struct: [<frame_center_x>, <frame_center_y>]
    private val frameCenter: FloatArray = FloatArray(2)
    // Array struct: [ FloatArray: [<line_start_x>, <line_start_y>, <line_stop_x>, <line_stop_y>], ...]
    private val frameMarks: Array<FloatArray> = Array(12) { FloatArray(4) }

    // Tilt angle of the second hand
    private val secondAngel: Double
    // Calculate the hand angle according to the seconds number
    get() = calculateHandAngel(seconds.toDouble())
    // Number of seconds
    private var seconds: Int = 0
    // Tilt angle of the minute hand
    private val minuteAngel: Double
    // Calculate the hand angle according to the minutes number
    get() = calculateHandAngel(minutes + seconds.toDouble() / 60)
    // Number of minutes
    private var minutes: Int = 0
    // Tilt angle of the hour hand
    private val hourAngel: Double
    // Calculate the hand angle according to the hours number
    get() = calculateHandAngel((hours + minutes.toDouble() / 60) * 5)
    // Number of hours
    private var hours: Int = 0
        set(value) {
            field = if (value > 12) {
                value - 12
            } else {
                value
            }
        }

    private fun calculateHandAngel(secondsNumber: Double): Double {
        return PI * secondsNumber / 30 - PI * 0.5
    }

    private var calendarInstance: Calendar = Calendar.getInstance()

    init {
        // Extract XML attribute values
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Clock,
           0, 0).apply {
            try {
                secondColor = getColor(R.styleable.Clock_secondColor,  Color.BLUE)
                minuteColor = getColor(R.styleable.Clock_minuteColor,  Color.GREEN)
                hourColor = getColor(R.styleable.Clock_hourColor,  Color.RED)

                secondLength = getDimension(R.styleable.Clock_secondLength, 50f)
                minuteLength = getDimension(R.styleable.Clock_minuteLength, 50f)
                hourLength = getDimension(R.styleable.Clock_hourLength, 50f)

                secondWidth = getDimension(R.styleable.Clock_secondWidth, 15f)
                minuteWidth = getDimension(R.styleable.Clock_minuteWidth, 20f)
                hourWidth = getDimension(R.styleable.Clock_hourWidth, 25f)
            } finally {
                recycle()
            }
        }

        framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            strokeWidth = 20f
            style = Paint.Style.STROKE
        }
        secondPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color =       secondColor
            strokeWidth = secondWidth
            style = Paint.Style.FILL
        }
        minutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color =       minuteColor
            strokeWidth = minuteWidth
            style = Paint.Style.FILL
        }
        hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color =       hourColor
            strokeWidth = hourWidth
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val xpad = (paddingLeft + paddingRight)
        val ypad = (paddingTop + paddingBottom)

        val w: Int =
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
                // If "wrap_content" then calculate the view width according to the radius
                // of the clock frame
                frameRadius.toInt() * 2 + xpad
            } else {
                MeasureSpec.getSize(widthMeasureSpec)
            }
        val h: Int =
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                // If "wrap_content" then calculate the view height according to the radius
                // of the clock frame
                frameRadius.toInt() * 2 + ypad
            } else {
                MeasureSpec.getSize(heightMeasureSpec)
            }
        setMeasuredDimension(w, h)

        frameRadius = if (w < h) {
            (w - xpad) * 0.5f
        } else {
            (h - ypad) * 0.5f
        }
        // Calculate the center of the clock frame
        frameCenter[0] = (w + paddingLeft - paddingRight) * 0.5f
        frameCenter[1] = (h + paddingTop - paddingBottom) * 0.5f
        // One of the twelve clock parts
        val partAngel: Double = PI / 6
        for (i in frameMarks.indices) {
            val currentAngel: Double = partAngel * i
            // Coordinates of line of a clock mark
            frameMarks[i][0] = (frameCenter[0] + cos(currentAngel) * frameRadius).toFloat()
            frameMarks[i][1] = (frameCenter[1] + sin(currentAngel) * frameRadius).toFloat()
            frameMarks[i][2] = (frameCenter[0] + cos(currentAngel) * (frameRadius * FRAME_MARK_SIZE)).toFloat()
            frameMarks[i][3] = (frameCenter[1] + sin(currentAngel) * (frameRadius * FRAME_MARK_SIZE)).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        calendarInstance = Calendar.getInstance()
        seconds = calendarInstance.get(Calendar.SECOND)
        minutes = calendarInstance.get(Calendar.MINUTE)
        hours = calendarInstance.get(Calendar.HOUR)


        canvas.apply {
            // Draw circle of the clock frame
            drawCircle(frameCenter[0], frameCenter[1], frameRadius, framePaint)
            // Draw marks of the clock frame
            for (line in frameMarks) {
                drawLine(line[0], line[1], line[2], line[3], framePaint)
            }
            // Draw the clock hands
            drawLine(frameCenter[0], frameCenter[1],
                (frameCenter[0] + cos(secondAngel) * secondLength).toFloat(),
                (frameCenter[1] + sin(secondAngel) * secondLength).toFloat(),
                secondPaint)
            drawLine(frameCenter[0], frameCenter[1],
                (frameCenter[0] + cos(minuteAngel) * minuteLength).toFloat(),
                (frameCenter[1] + sin(minuteAngel) * minuteLength).toFloat(),
                minutePaint)
            drawLine(frameCenter[0], frameCenter[1],
                (frameCenter[0] + cos(hourAngel) * hourLength).toFloat(),
                (frameCenter[1] + sin(hourAngel) * hourLength).toFloat(),
                hourPaint)
        }

        invalidate()
    }
}