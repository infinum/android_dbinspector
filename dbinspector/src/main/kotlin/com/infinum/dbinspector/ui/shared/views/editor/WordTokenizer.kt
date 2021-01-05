package com.infinum.dbinspector.ui.shared.views.editor

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView.Tokenizer

internal class WordTokenizer : Tokenizer {

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

    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length
        while (i > 0 && text[i - 1] == TOKEN_SPACE) {
            i--
        }
        return if (i > 0 && text[i - 1] == TOKEN_SPACE) {
            text
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
                "$text$TOKEN_SPACE"
            }
        }
    }
}
