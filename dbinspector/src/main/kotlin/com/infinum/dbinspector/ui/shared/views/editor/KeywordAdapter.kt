package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
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

        getItem(position)?.let {
            with(binding) {
                keywordView.text = it.value
                keywordView.setBackgroundResource(it.type.backgroundResId)
                keywordView.setTextColor(
                    ContextCompat.getColor(keywordView.context, it.type.textColorResId)
                )
            }
        }
        return binding.root
    }

    fun addDatabaseKeywords(keywords: List<Keyword>) {
        addAll(keywords)
    }
}
