package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemKeywordBinding

internal class KeywordAdapter(
    context: Context,
    keywords: List<Keyword>
) : ArrayAdapter<Keyword>(context, R.layout.dbinspector_item_keyword, R.id.keywordView, keywords) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let {
            DbinspectorItemKeywordBinding.bind(it)
        } ?: run {
            DbinspectorItemKeywordBinding.inflate(inflater, parent, false)
        }

        getItem(position)?.let { keyword ->
            with(binding) {
                keywordView.setBackgroundResource(keyword.type.backgroundResId)
                keywordView.setTextColor(
                    ContextCompat.getColor(keywordView.context, keyword.type.textColorResId)
                )
                keywordView.text = createSpannable(keyword)
            }
        }
        return binding.root
    }

    fun addDatabaseKeywords(keywords: List<Keyword>) {
        addAll(keywords)
    }

    private fun createSpannable(keyword: Keyword) =
        SpannableString(keyword.value).apply {
            findSpans(keyword).forEach {
                setSpan(
                    it,
                    0,
                    keyword.value.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

    private fun findSpans(keyword: Keyword): List<ParcelableSpan> =
        when (keyword.type) {
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
