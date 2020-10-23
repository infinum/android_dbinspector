package com.infinum.dbinspector.ui.pragma.foreignkeys

import com.infinum.dbinspector.domain.pragma.models.ForeignKeyListColumns
import com.infinum.dbinspector.ui.pragma.shared.PragmaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

internal class ForeignKeysFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): ForeignKeysFragment =
            ForeignKeysFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel: ForeignKeysViewModel by viewModel()

    override fun headers() =
        ForeignKeyListColumns.values().map { it.name.toLowerCase(Locale.getDefault()) }
}
