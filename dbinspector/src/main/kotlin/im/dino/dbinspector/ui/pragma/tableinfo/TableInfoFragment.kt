package im.dino.dbinspector.ui.pragma.tableinfo

import androidx.fragment.app.viewModels
import im.dino.dbinspector.domain.pragma.schema.models.TableInfoColumns
import im.dino.dbinspector.ui.pragma.shared.PragmaFragment
import java.util.Locale

internal class TableInfoFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): TableInfoFragment =
            TableInfoFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel by viewModels<TableInfoViewModel>()

    override fun headers() =
        TableInfoColumns.values().map { it.name.toLowerCase(Locale.getDefault()) }
}