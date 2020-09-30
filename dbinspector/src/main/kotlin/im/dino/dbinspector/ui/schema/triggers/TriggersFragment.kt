package im.dino.dbinspector.ui.schema.triggers

import androidx.fragment.app.viewModels
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import im.dino.dbinspector.ui.trigger.TriggerActivity

internal class TriggersFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TriggersFragment =
            TriggersFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override val viewModel by viewModels<TriggersViewModel>()

    override fun itemClass() = TriggerActivity::class.java
}