package im.dino.dbinspector.ui.tables.pragma

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.dino.dbinspector.domain.pragma.models.PragmaType
import im.dino.dbinspector.ui.tables.pragma.foreignkeys.ForeignKeysFragment
import im.dino.dbinspector.ui.tables.pragma.indexes.IndexesFragment
import im.dino.dbinspector.ui.tables.pragma.tableinfo.TableInfoFragment
import java.util.Locale

class PragmaTypeAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    private val databasePath: String,
    private val tableName: String
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence =
        context.getString(PragmaType.values()[position].nameRes).toUpperCase(Locale.getDefault())

    override fun getItem(position: Int): Fragment =
        when (PragmaType.values()[position]) {
            PragmaType.TABLE_INFO -> TableInfoFragment.newInstance(databasePath, tableName)
            PragmaType.FOREIGN_KEY -> ForeignKeysFragment.newInstance(databasePath, tableName)
            PragmaType.INDEX -> IndexesFragment.newInstance(databasePath, tableName)
        }

    override fun getCount(): Int = PragmaType.values().size
}
