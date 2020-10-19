package im.dino.dbinspector.extensions

import androidx.appcompat.widget.SearchView
import im.dino.dbinspector.ui.shared.searchable.SimpleQueryTextListener

internal fun SearchView.setup(
    hint: String?,
    onSearchClosed: () -> Unit,
    onQueryTextChanged: (String?) -> Unit
) {
    setIconifiedByDefault(true)
    isSubmitButtonEnabled = false
    isQueryRefinementEnabled = true
    maxWidth = Integer.MAX_VALUE
    queryHint = hint
    setOnCloseListener {
        onSearchClosed()
        false
    }
    setOnQueryTextListener(
        SimpleQueryTextListener(onQueryTextChanged)
    )
}
