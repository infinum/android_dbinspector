package im.dino.dbinspector.extensions

import androidx.appcompat.widget.SearchView

internal fun SearchView.setup(
    onSearchClosed: () -> Unit,
    onQueryTextChanged: (String?) -> Unit
) {
    setIconifiedByDefault(true)
    isSubmitButtonEnabled = false
    isQueryRefinementEnabled = true
    maxWidth = Integer.MAX_VALUE
    setOnCloseListener {
        onSearchClosed()
        false
    }
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextChanged(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryTextChanged(newText)
            return true
        }
    })
}