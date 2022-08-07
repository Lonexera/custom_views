package com.course.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Parcelable
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import kotlinx.parcelize.Parcelize
import kotlin.properties.Delegates.notNull

class CustomTextView(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    @Parcelize
    private data class Parameters(
        val text: String?,
        val textSize: Int,
        @FontRes val font: Int
    ) : Parcelable

    @Parcelize
    private data class SaveState(
        val superSavedState: Parcelable?,
        val parameters: Parameters
    ) : View.BaseSavedState(superSavedState), Parcelable

    private var parameters: Parameters = Parameters(
        text = null,
        textSize = DEFAULT_TEXT_SIZE,
        font = RESOURCE_NOT_FOUND
    )

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            0
        ) {
            parameters = parameters.copy(
                text = getString(R.styleable.CustomTextView_ctv_text),
                textSize = getDimensionPixelSize(
                    R.styleable.CustomTextView_ctv_textSize,
                    DEFAULT_TEXT_SIZE
                ),
                font = getResourceId(
                    R.styleable.CustomTextView_ctv_font,
                    RESOURCE_NOT_FOUND
                )
            )
        }
    }

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        typeface = getFontSafely(fontRes = parameters.font)
        textSize = parameters.textSize.toFloat()
    }

    private var staticLayout: StaticLayout by notNull()

    override fun onSaveInstanceState(): Parcelable {
        return SaveState(
            superSavedState = super.onSaveInstanceState(),
            parameters = parameters
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? SaveState
        super.onRestoreInstanceState(savedState?.superSavedState ?: state)

        savedState ?: return
        parameters = savedState.parameters
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (parameters.text == null) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
            return
        } else {
            val text = requireNotNull(parameters.text)

            val measuredWidth = textPaint.measureText(text).toInt()

            val width = resolveSize(measuredWidth, widthMeasureSpec)
            staticLayout = StaticLayout.Builder
                .obtain(text, 0, text.length, textPaint, width)
                .build()

            val height = resolveSize(staticLayout.height, heightMeasureSpec)

            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null || parameters.text == null) return

        staticLayout.draw(canvas)
    }

    fun setText(text: String) {
        parameters = parameters.copy(
            text = text
        )
        requestLayout()
        invalidate()
    }

    private fun getFontSafely(@FontRes fontRes: Int): Typeface {
        return try {
            ResourcesCompat.getFont(context, fontRes) ?: Typeface.DEFAULT
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.stackTraceToString())
            Typeface.DEFAULT
        }
    }

    companion object {
        private const val DEFAULT_TEXT_SIZE = 20
        private const val RESOURCE_NOT_FOUND = -1
    }
}
