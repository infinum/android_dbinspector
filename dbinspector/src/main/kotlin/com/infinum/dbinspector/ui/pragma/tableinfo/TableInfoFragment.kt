package com.infinum.dbinspector.ui.pragma.tableinfo

import com.infinum.dbinspector.domain.pragma.models.TableInfoColumns
import com.infinum.dbinspector.ui.extensions.lowercase
import com.infinum.dbinspector.ui.pragma.shared.PragmaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TableInfoFragment : PragmaFragment() {

    companion object {

        fun newInstance(databasePath: String, tableName: String): TableInfoFragment =
            TableInfoFragment().apply {
                arguments = bundle(databasePath, tableName)
            }
    }

    override val viewModel: TableInfoViewModel by viewModel()

    override fun headers() =
        TableInfoColumns.values().map { it.name.lowercase() }
}
