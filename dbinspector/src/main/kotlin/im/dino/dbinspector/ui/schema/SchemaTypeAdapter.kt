package im.dino.dbinspector.ui.schema

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.schema.tables.TablesFragment
import im.dino.dbinspector.ui.schema.triggers.TriggersFragment
import im.dino.dbinspector.ui.schema.views.ViewsFragment
import java.util.Locale

internal class SchemaTypeAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    private val databasePath: String,
    private val databaseName: String,
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence =
        context.getString(SchemaType.values()[position].nameRes).toUpperCase(Locale.getDefault())

    override fun getItem(position: Int): Fragment =
        when (SchemaType.values()[position]) {
            SchemaType.TABLE -> TablesFragment.newInstance(databasePath, databaseName)
            SchemaType.VIEW -> ViewsFragment.newInstance(databasePath, databaseName)
            SchemaType.TRIGGER -> TriggersFragment.newInstance(databasePath, databaseName)
        }

    override fun getCount(): Int = SchemaType.values().size
}
