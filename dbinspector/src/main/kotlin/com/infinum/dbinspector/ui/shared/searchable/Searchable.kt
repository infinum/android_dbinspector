package com.infinum.dbinspector.ui.shared.searchable

internal interface Searchable {

    fun onSearchOpened()

    fun search(query: String?)

    fun searchQuery(): String?

    fun onSearchClosed()
}
