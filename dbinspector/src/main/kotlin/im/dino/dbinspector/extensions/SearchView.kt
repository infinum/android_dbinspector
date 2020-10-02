package im.dino.dbinspector.extensions

import androidx.appcompat.widget.SearchView
import im.dino.dbinspector.ui.shared.SimpleQueryTextListener

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
    setOnQueryTextListener(
        SimpleQueryTextListener(onQueryTextChanged)
    )
}
