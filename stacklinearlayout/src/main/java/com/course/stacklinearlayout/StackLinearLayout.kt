package com.course.stacklinearlayout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children

class StackLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var spacing: Int = DEFAULT_SPACING_SIZE

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.StackLinearLayout,
            0
        ) {
            spacing = getDimensionPixelSize(
                R.styleable.StackLinearLayout_spacing,
                DEFAULT_SPACING_SIZE
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val measuredHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> heightMeasureSpec
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED ->
                children.sumOf { it.measuredHeight } + spacing * (childCount - 1)
            else -> error("Unreachable")
        }

        setMeasuredDimension(widthMeasureSpec, measuredHeight)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        var layoutHeight = 0

        for (index in (childCount - 1) downTo 0) {
            getChildAt(index).also { child ->
                layoutHeight += child.measuredHeight
                if (layoutHeight > bottom - top) return

                child.layout(
                    left,
                    top + layoutHeight - child.measuredHeight,
                    right,
                    top + layoutHeight
                )

                layoutHeight += spacing
            }
        }
    }

    fun setSpacing(sizePx: Int) {
        spacing = sizePx
        invalidate()
    }

    companion object {
        private const val DEFAULT_SPACING_SIZE = 0
    }
}
