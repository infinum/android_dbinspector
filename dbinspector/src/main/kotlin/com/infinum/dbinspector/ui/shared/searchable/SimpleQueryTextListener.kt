package com.infinum.dbinspector.ui.shared.searchable

import androidx.appcompat.widget.SearchView

class SimpleQueryTextListener(
    private val onQueryTextChanged: (String?) -> Unit
) : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChanged(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onQueryTextChanged(newText)
        return true
    }
}
