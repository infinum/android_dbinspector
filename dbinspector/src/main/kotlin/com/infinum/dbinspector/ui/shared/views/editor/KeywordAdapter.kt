package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemKeywordBinding


internal class KeywordAdapter(
    context: Context,
    keywords: List<Token>
) : ArrayAdapter<Token>(context, R.layout.dbinspector_item_keyword, R.id.keywordView, keywords) {

    private val inflater = LayoutInflater.from(context)

//    init {
//        addDatabaseKeywords(
//            listOf(
//                Token(
//                    value = "users",
//                    type = TokenType.NAME
//                ),
//                Token(
//                    value = "articles",
//                    type = TokenType.NAME
//                )
//            )
//        )
//    }

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
            }
        }
        return binding.root
    }

    fun addDatabaseKeywords(keywords: List<Token>) {
        addAll(keywords)
    }
}