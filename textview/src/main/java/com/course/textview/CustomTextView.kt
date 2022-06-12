package com.course.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates.notNull

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
    private val attrsTextSize =
        attributes.getInt(R.styleable.CustomTextView_ctv_textSize, DEFAULT_TEXT_SIZE)

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        textSize = attrsTextSize * resources.displayMetrics.scaledDensity
    }

    private var staticLayout: StaticLayout by notNull()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (text == null) return

        val desiredWidth = textPaint.measureText(text).toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        staticLayout = StaticLayout.Builder
            .obtain(text, 0, text.length, textPaint, width)
            .build()

        val height = resolveSize(staticLayout.height, heightMeasureSpec)

        // To avoid issues in layout preview
        if (width < 0 || height < 0) return

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null || text == null) return

        staticLayout.draw(canvas)
    }

    companion object {
        private const val DEFAULT_TEXT_SIZE = 20
    }
}
