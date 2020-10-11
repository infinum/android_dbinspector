package im.dino.dbinspector.ui.pragma.foreignkeys

import androidx.fragment.app.viewModels
import im.dino.dbinspector.domain.pragma.models.ForeignKeyListColumns
import im.dino.dbinspector.ui.pragma.shared.PragmaFragment
import java.util.Locale

internal class ForeignKeysFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): ForeignKeysFragment =
            ForeignKeysFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel by viewModels<ForeignKeyViewModel>()

    override fun headers() =
        ForeignKeyListColumns.values().map { it.name.toLowerCase(Locale.getDefault()) }
}
