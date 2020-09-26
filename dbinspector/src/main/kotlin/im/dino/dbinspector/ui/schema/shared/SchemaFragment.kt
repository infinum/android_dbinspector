package im.dino.dbinspector.ui.schema.shared

import android.content.Context
import androidx.annotation.LayoutRes
import im.dino.dbinspector.ui.shared.base.BaseFragment
import im.dino.dbinspector.ui.shared.Searchable

internal abstract class SchemaFragment (
    @LayoutRes contentLayoutId: Int):
    BaseFragment(contentLayoutId),
    Searchable {

    internal var parentSearchable: Searchable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentSearchable = (context as? Searchable)
    }

    override fun onDetach() {
        parentSearchable = null
        super.onDetach()
    }

    override fun onSearchOpened() = Unit

    override fun searchQuery(): String? = null

    override fun onSearchClosed() = Unit
}