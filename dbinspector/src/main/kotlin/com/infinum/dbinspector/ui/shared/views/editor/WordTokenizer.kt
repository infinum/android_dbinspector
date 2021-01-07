package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.MultiAutoCompleteTextView.Tokenizer
import androidx.core.content.ContextCompat
import com.infinum.dbinspector.R

internal class WordTokenizer(
    private val context: Context,
    private val keywords: List<Keyword>
) : Tokenizer {

    companion object {
        private const val TOKEN_SPACE = ' '
        private const val TOKEN_SEMICOLON = ';'
        private val TOKEN_NEW_LINE = System.lineSeparator().first()
    }

    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor
        while (i > 0 && (text[i - 1] != TOKEN_SPACE)) {
            i--
        }
        while (i < cursor && text[i] == TOKEN_NEW_LINE) {
            i++
        }
        return i
    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        val length = text.length
        while (i < length) {
            if (text[i] == TOKEN_NEW_LINE) { // || text[i] == TOKEN_SEMICOLON
                return i
            } else {
                i++
            }
        }
        return length
    }

    @Suppress("NestedBlockDepth")
    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length
        while (i > 0 && text[i - 1] == TOKEN_SPACE) {
            i--
        }
        return if (i > 0 && text[i - 1] == TOKEN_SPACE) {
            applySpan(text)?.let {
                createSpannable(text, it)
            } ?: text
        } else {
            if (text is Spanned) {
                val spannableString = SpannableString("$text$TOKEN_NEW_LINE")
                TextUtils.copySpansFrom(
                    text,
                    0,
                    text.length,
                    Any::class.java,
                    spannableString,
                    0
                )
                spannableString
            } else {
                applySpan(text)?.let {
                    createSpannable("$text$TOKEN_SPACE", it, true)
                } ?: "$text$TOKEN_SPACE"
            }
        }
    }

    private fun createSpannable(
        text: CharSequence,
        spans: List<ParcelableSpan>,
        chopLastChar: Boolean = false
    ) = SpannableString(text).apply {
        spans.forEach {
            setSpan(
                it,
                0,
                text.length - (if (chopLastChar) 1 else 0),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun applySpan(token: CharSequence): List<ParcelableSpan>? =
        keywords
            .find { it.value == token.toString() }
            ?.let {
                when (it.type) {
                    KeywordType.SQL -> listOf<ParcelableSpan>(StyleSpan(Typeface.BOLD))
                    KeywordType.TABLE_NAME -> listOf<ParcelableSpan>(
                        StyleSpan(Typeface.ITALIC),
                        ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.dbinspector_color_keyword_table_name)
                        )
                    )
                    KeywordType.COLUMN_NAME -> listOf<ParcelableSpan>(
                        StyleSpan(Typeface.ITALIC),
                        ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.dbinspector_color_keyword_column_name)
                        )
                    )
                }
            }
}
