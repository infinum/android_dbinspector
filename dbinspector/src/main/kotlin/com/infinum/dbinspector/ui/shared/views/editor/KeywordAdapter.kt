package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemKeywordBinding

internal class KeywordAdapter(
    context: Context,
    keywords: List<Keyword>,
    private val spanFactory: KeywordSpanFactory
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
            for (span in spanFactory.findSpans(keyword.type)) {
                setSpan(
                    span,
                    0,
                    keyword.value.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
}
