package com.infinum.dbinspector.ui.edit.history

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogHistoryBinding
import com.infinum.dbinspector.domain.history.models.Execution
import com.infinum.dbinspector.ui.shared.base.BaseBottomSheetDialogFragment
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

internal class HistoryDialog : BaseBottomSheetDialogFragment(R.layout.dbinspector_dialog_history) {

    companion object {
        private var databaseName: String? = null
        private lateinit var databasePath: String

        fun show(
            databaseName: String?,
            databasePath: String,
            fragmentManager: FragmentManager
        ) {
            this.databaseName = databaseName
            this.databasePath = databasePath
            HistoryDialog().show(fragmentManager, null)
        }
    }

    private val adapter = HistoryAdapter(
        onClicked = {
            selectExecution(it)
        }
    )

    private var listener: Listener? = null

    override val binding: DbinspectorDialogHistoryBinding by viewBinding(
        DbinspectorDialogHistoryBinding::bind
    )

    override val viewModel: HistoryViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as? Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }
            toolbar.title = getString(R.string.dbinspector_action_history)
            databaseName?.let { toolbar.subtitle = it }

            recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                RecyclerView.VERTICAL,
                false
            )
            ContextCompat.getDrawable(
                recyclerView.context,
                R.drawable.dbinspector_divider_vertical
            )?.let { drawable ->
                val verticalDecorator = DividerItemDecoration(
                    recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
                verticalDecorator.setDrawable(drawable)
                recyclerView.addItemDecoration(verticalDecorator)
            }
            recyclerView.adapter = adapter
        }

        viewModel.history(databasePath) {
            it.executions.forEachIndexed { index, execution ->
                Timber.tag("_BOJAN_").i("$index -> $execution")
            }
            adapter.submitList(it.executions)
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun selectExecution(execution: Execution) {
        listener?.onHistorySelected(execution.statement)
        dismiss()
    }

    interface Listener {

        fun onHistorySelected(statement: String)
    }
}
