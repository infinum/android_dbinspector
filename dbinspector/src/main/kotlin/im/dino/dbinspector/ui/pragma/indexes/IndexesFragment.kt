package im.dino.dbinspector.ui.pragma.indexes

import androidx.fragment.app.viewModels
import im.dino.dbinspector.domain.pragma.schema.models.IndexListColumns
import im.dino.dbinspector.ui.pragma.shared.PragmaFragment
import java.util.Locale

internal class IndexesFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): IndexesFragment =
            IndexesFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel by viewModels<IndexViewModel>()

    override fun headers() =
        IndexListColumns.values().map { it.name.toLowerCase(Locale.getDefault()) }
}