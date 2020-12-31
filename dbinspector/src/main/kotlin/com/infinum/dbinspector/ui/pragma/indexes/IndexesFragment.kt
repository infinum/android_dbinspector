package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.pragma.models.IndexListColumns
import com.infinum.dbinspector.extensions.lowercase
import com.infinum.dbinspector.ui.pragma.shared.PragmaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class IndexesFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): IndexesFragment =
            IndexesFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel: IndexViewModel by viewModel()

    override fun headers() =
        IndexListColumns.values().map { it.name.lowercase() }
}
