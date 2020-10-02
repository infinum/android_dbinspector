package im.dino.dbinspector.ui.shared

internal interface Searchable {

    fun onSearchOpened()

    fun search(query: String?)

    fun searchQuery(): String?

    fun onSearchClosed()
}
