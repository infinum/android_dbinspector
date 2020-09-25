package im.dino.dbinspector.extensions

import android.view.Menu
import androidx.appcompat.widget.SearchView
import im.dino.dbinspector.R

internal val Menu.searchView
    get() = findItem(R.id.search)?.actionView as? SearchView