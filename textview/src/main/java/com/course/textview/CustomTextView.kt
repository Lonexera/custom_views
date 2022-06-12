package com.course.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CustomTextView(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    private val attributes = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.CustomTextView,
        0,
        0
    )
    private val text = attributes.getString(R.styleable.CustomTextView_ctv_text)
    private val attrsTextSize = attributes.getInt(R.styleable.CustomTextView_ctv_textSize, DEFAULT_TEXT_SIZE)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
            textSize = attrsTextSize * resources.displayMetrics.scaledDensity
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (text == null) return

        val desiredWidth = paint.measureText(text).toInt()
        val desiredHeight = paint.getTextHeight(text)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(widthSize, desiredWidth)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(heightSize, desiredHeight)
            else -> desiredHeight
        }

        // To avoid issues in layout preview
        if (width < 0 || height < 0) return

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null || text == null) return

        canvas.drawText(
            text,
            0F,
            paint.getTextBaseline(text),
            paint
        )
    }

    private fun Paint.getTextHeight(text: String): Int {
        return (getTextBaseline(text) + fontMetrics.bottom).toInt()
    }

    private fun Paint.getTextBaseline(text: String): Float {
        val textBounds = Rect()
        getTextBounds(text, 0, text.length, textBounds)

        return textBounds.height().toFloat()
    }

    companion object {
        private const val DEFAULT_TEXT_SIZE = 20
    }
}
