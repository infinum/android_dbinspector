package im.dino.dbinspector.ui.shared.base

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import im.dino.dbinspector.ui.shared.Searchable

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
