package im.dino.dbinspector.ui.schema.shared

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import im.dino.dbinspector.domain.schema.shared.models.SchemaType
import im.dino.dbinspector.ui.schema.tables.TablesFragment
import im.dino.dbinspector.ui.schema.triggers.TriggersFragment
import im.dino.dbinspector.ui.schema.views.ViewsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal class SchemaTypeAdapter(
    fragmentActivity: FragmentActivity,
    private val databasePath: String,
    private val databaseName: String,
) : FragmentStateAdapter(fragmentActivity) {

    @ExperimentalCoroutinesApi
    override fun createFragment(position: Int): Fragment =
        when (SchemaType(position)) {
            SchemaType.TABLE -> TablesFragment.newInstance(databasePath, databaseName)
            SchemaType.VIEW -> ViewsFragment.newInstance(databasePath, databaseName)
            SchemaType.TRIGGER -> TriggersFragment.newInstance(databasePath, databaseName)
        }

    override fun getItemCount(): Int = SchemaType.values().size
}
