package im.dino.dbinspector.ui.pragma.indexes

import im.dino.dbinspector.domain.pragma.models.IndexListColumns
import im.dino.dbinspector.ui.pragma.shared.PragmaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

internal class IndexesFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): IndexesFragment =
            IndexesFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel: IndexViewModel by viewModel()

    override fun headers() =
        IndexListColumns.values().map { it.name.toLowerCase(Locale.getDefault()) }
}
