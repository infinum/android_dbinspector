package com.infinum.dbinspector.ui.shared.searchable

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import com.infinum.dbinspector.ui.shared.base.BaseFragment

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseSearchableFragment(
    @LayoutRes contentLayoutId: Int
) :
    BaseFragment(contentLayoutId),
    Searchable {

    private var parentSearchable: Searchable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentSearchable = (context as? Searchable)
    }

    override fun onDetach() {
        parentSearchable = null
        super.onDetach()
    }

    override fun onSearchOpened() = Unit

    override fun searchQuery(): String? = parentSearchable?.searchQuery()

    override fun onSearchClosed() = Unit
}
