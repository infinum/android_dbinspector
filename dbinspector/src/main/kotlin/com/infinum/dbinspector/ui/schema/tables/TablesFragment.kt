package com.infinum.dbinspector.ui.schema.tables

import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.table.TableActivity
import com.infinum.dbinspector.ui.schema.shared.SchemaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TablesFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TablesFragment =
            TablesFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override var statement: String = Statements.Schema.tables()

    override val viewModel: TablesViewModel by viewModel()

    override fun childView() = TableActivity::class.java
}
