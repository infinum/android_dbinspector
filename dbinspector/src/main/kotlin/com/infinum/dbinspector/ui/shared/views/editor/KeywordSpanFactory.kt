package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.infinum.dbinspector.R

internal class KeywordSpanFactory(
    private val context: Context
) {
    fun findSpans(keywordType: KeywordType): List<ParcelableSpan> {
        return when (keywordType) {
            KeywordType.SQLITE_KEYWORD -> listOf<ParcelableSpan>(StyleSpan(Typeface.BOLD))
            KeywordType.SQLITE_FUNCTION -> listOf<ParcelableSpan>(
                StyleSpan(Typeface.ITALIC),
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.dbinspector_color_keyword_sql_function)
                )
            )
            KeywordType.SQLITE_TYPE -> listOf<ParcelableSpan>(
                StyleSpan(Typeface.BOLD_ITALIC),
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.dbinspector_color_keyword_sql_type)
                )
            )
            KeywordType.TABLE_NAME -> listOf<ParcelableSpan>(
                StyleSpan(Typeface.ITALIC),
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.dbinspector_color_keyword_table_name)
                )
            )
            KeywordType.VIEW_NAME -> listOf<ParcelableSpan>(
                StyleSpan(Typeface.ITALIC),
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.dbinspector_color_keyword_view_name)
                )
            )
            KeywordType.TRIGGER_NAME -> listOf<ParcelableSpan>(
                StyleSpan(Typeface.ITALIC),
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.dbinspector_color_keyword_trigger_name)
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
