package com.infinum.dbinspector.ui.schema.views

import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.view.ViewActivity
import com.infinum.dbinspector.ui.schema.shared.SchemaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class ViewsFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): ViewsFragment =
            ViewsFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override var statement: String = Statements.Schema.views()

    override val viewModel: ViewsViewModel by viewModel()

    override fun childView() = ViewActivity::class.java
}
