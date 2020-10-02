package im.dino.dbinspector.ui.shared.base.searchable

internal interface Searchable {

    fun onSearchOpened()

    fun search(query: String?)

    fun searchQuery(): String?

    fun onSearchClosed()
}
