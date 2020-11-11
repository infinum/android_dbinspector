package com.infinum.dbinspector.extensions

import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.infinum.dbinspector.R

internal val Menu.searchView
    get() = findItem(R.id.search)?.actionView as? SearchView
