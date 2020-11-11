package com.infinum.dbinspector.ui.schema.triggers

import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.trigger.TriggerActivity
import com.infinum.dbinspector.ui.schema.shared.SchemaFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TriggersFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TriggersFragment =
            TriggersFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override var statement: String = Statements.Schema.triggers()

    override val viewModel: TriggersViewModel by viewModel()

    override fun childView() = TriggerActivity::class.java
}
