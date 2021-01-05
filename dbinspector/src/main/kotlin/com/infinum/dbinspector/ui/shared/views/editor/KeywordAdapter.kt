package com.infinum.dbinspector.ui.shared.views.editor

import android.content.Context
import android.widget.ArrayAdapter
import com.infinum.dbinspector.R

internal class KeywordAdapter(
    context: Context
) : ArrayAdapter<String>(context, R.layout.dbinspector_item_keyword, R.id.keywordView) {

    init {
        addAll(context.resources.getStringArray(R.array.dbinspector_keywords_sqlite).toList())
    }
}