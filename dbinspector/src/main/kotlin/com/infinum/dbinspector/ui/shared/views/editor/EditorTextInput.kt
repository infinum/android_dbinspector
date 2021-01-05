package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.extensions.getColorFromAttribute
import kotlin.math.roundToInt

class EditorTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyle) {

    companion object {
        private const val DEFAULT_FIRST_LINE = 1
        private const val DEFAULT_TOKENIZER_THRESHOLD = 1
    }

    private val border = context.resources.getDimensionPixelSize(R.dimen.dbinspector_default_linecount_border)
    private val lineCountPadding = context.resources.getDimensionPixelSize(
        R.dimen.dbinspector_default_linecount_padding
    ) + paddingStart
    private val lineCountRect = Rect().apply {
        left = (lineCountPadding / 2.0f).roundToInt()
    }
    private val lineCountPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColorFromAttribute(android.R.attr.textColorSecondary)
        textSize = paint.textSize
        textAlign = Paint.Align.LEFT
        typeface = Typeface.MONOSPACE
    }
    private val lineNumberBounds = Rect()

    private var lineNumber = DEFAULT_FIRST_LINE
    private var lineBounds: Int = 0

    private val lineCountBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColorFromAttribute(android.R.attr.textColorSecondaryInverse)
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                context.resources.getStringArray(R.array.dbinspector_keywords_sqlite)
            )
        )
        threshold = DEFAULT_TOKENIZER_THRESHOLD
        setTokenizer(WordTokenizer())
    }

    override fun onDraw(canvas: Canvas) {
        lineNumber = DEFAULT_FIRST_LINE

        paint.getTextBounds(
            lineCount.toString(),
            0,
            lineCount.toString().length,
            lineNumberBounds
        )
        canvas.drawRect(
            border.toFloat(),
            border.toFloat(),
            lineNumberBounds.width() + lineCountPadding / 2.0f + lineCountRect.left,
            height.toFloat() - border.toFloat(),
            lineCountBackgroundPaint
        )

        for (line in 0 until lineCount) {
            lineBounds = getLineBounds(line, null)
            when (isNumberedLine(line)) {
                true -> {
                    canvas.drawText(
                        "$lineNumber",
                        lineCountRect.left.toFloat(),
//                        (lineCountRect.left - lineNumberBounds.left).toFloat(),
                        lineBounds.toFloat(),
                        lineCountPaint
                    )
                    ++lineNumber
                }
                false -> Unit
            }
        }

        setPadding(
            lineNumberBounds.width() + lineCountPadding + lineCountRect.left,
            paddingTop,
            paddingEnd,
            paddingBottom
        )

        super.onDraw(canvas)
    }

    /**
     * Show line number only if it's a first line or any line starting with line separator.
     * Wrapped lines are not numbered.
     */
    private fun isNumberedLine(line: Int) =
        line == 0 || text?.get(layout.getLineStart(line) - 1)?.toString() == System.lineSeparator()
}


