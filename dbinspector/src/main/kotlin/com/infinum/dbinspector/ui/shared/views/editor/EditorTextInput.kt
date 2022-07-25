package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import com.infinum.dbinspector.R
import com.infinum.dbinspector.ui.extensions.getColorFromAttribute
import kotlin.math.roundToInt

internal class EditorTextInput @JvmOverloads constructor(
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

    private val sqlKeywords = context.resources.getStringArray(R.array.dbinspector_sqlite_keywords).map {
        Keyword(
            value = it,
            type = KeywordType.SQLITE_KEYWORD
        )
    }
    private val sqlFunctions = context.resources.getStringArray(R.array.dbinspector_sqlite_functions).map {
        Keyword(
            value = it,
            type = KeywordType.SQLITE_FUNCTION
        )
    }
    private val sqlTypes = context.resources.getStringArray(R.array.dbinspector_sqlite_types).map {
        Keyword(
            value = it,
            type = KeywordType.SQLITE_TYPE
        )
    }

    private val spanFactory = KeywordSpanFactory(context)
    private val keywordAdapter = KeywordAdapter(
        context,
        sqlKeywords + sqlFunctions + sqlTypes,
        spanFactory
    )
    private val wordTokenizer = WordTokenizer(
        sqlKeywords + sqlFunctions + sqlTypes,
        spanFactory
    )

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        setDropDownBackgroundResource(R.drawable.dbinspector_keyword_popup_background)
        doOnLayout {
            doOnTextChanged { text, _, _, _ ->
                dropDownHorizontalOffset = layout.getPrimaryHorizontal(
                    text?.toString().orEmpty().length
                ).roundToInt() + lineCountPadding
                dropDownVerticalOffset = layout.getLineTop(lineCount) + paddingTop
            }
        }

        keywordAdapter.registerDataSetObserver(
            object : DataSetObserver() {
                override fun onChanged() {
                    dropDownWidth = (0 until keywordAdapter.count)
                        .mapNotNull { keywordAdapter.getItem(it) }
                        .maxOf {
                            val bounds = Rect()
                            paint.getTextBounds(
                                it.value,
                                0,
                                it.value.length,
                                bounds
                            )
                            bounds.width() + context.resources.getDimensionPixelSize(
                                R.dimen.dbinspector_keyword_padding
                            ) * 2
                        }
                }
            }
        )

        setAdapter(keywordAdapter)
        threshold = DEFAULT_TOKENIZER_THRESHOLD
        setTokenizer(wordTokenizer)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection =
        super.onCreateInputConnection(
            outAttrs.apply {
                imeOptions = imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            }
        )

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)

        when (id) {
            android.R.id.paste -> onTextPaste()
            else -> Unit
        }

        return consumed
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

    fun addKeywords(keywords: List<Keyword>) {
        keywordAdapter.addDatabaseKeywords(keywords)
        wordTokenizer.addDatabaseKeywords(keywords)
    }

    fun setContent(text: String) {
        this.text.clear()
        this.text.insert(0, text)
        onTextPaste()
    }

    private fun isNumberedLine(line: Int) =
        line == 0 || text?.get(layout.getLineStart(line) - 1)?.toString() == System.lineSeparator()

    private fun onTextPaste() {
        text?.let {
            it.split(" ").forEach { word ->
                val start = it.indexOf(word)
                val end = start + word.length
                when {
                    word.isBlank() -> it.replace(start, end, "")
                    else -> it.replace(start, end, wordTokenizer.tokenize(word, false))
                }
            }
            if (it.last().isWhitespace().not()) {
                it.append(" ")
            }
        }
    }
}
