package com.infinum.dbinspector.ui.shared.views.editor

import android.text.ParcelableSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView.Tokenizer

internal class WordTokenizer(
    private var keywords: List<Keyword>,
    private val spanFactory: KeywordSpanFactory
) : Tokenizer {

    companion object {
        private const val TOKEN_SPACE = ' '
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

    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length
        while (i > 0 && text[i - 1] == TOKEN_SPACE) {
            i--
        }
        return if (i > 0 && text[i - 1] == TOKEN_SPACE) {
            findSpans(text)?.let {
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
                findSpans(text)?.let {
                    createSpannable("$text$TOKEN_SPACE", it, true)
                } ?: "$text$TOKEN_SPACE"
            }
        }
    }

    fun addDatabaseKeywords(keywords: List<Keyword>) {
        this.keywords = this.keywords + keywords
    }

    fun tokenize(text: String, chopLastChar: Boolean = true) =
        findSpans(text)?.let {
            createSpannable(text, it, chopLastChar)
        } ?: text

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

    private fun findSpans(token: CharSequence): List<ParcelableSpan>? =
        keywords
            .find { it.value == token.toString() }
            ?.let { spanFactory.findSpans(it.type) }
}
