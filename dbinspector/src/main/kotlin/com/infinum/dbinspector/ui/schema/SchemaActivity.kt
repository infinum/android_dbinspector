package com.infinum.dbinspector.ui.schema

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.tabs.TabLayoutMediator
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivitySchemaBinding
import com.infinum.dbinspector.domain.schema.shared.models.SchemaType
import com.infinum.dbinspector.extensions.searchView
import com.infinum.dbinspector.extensions.setup
import com.infinum.dbinspector.extensions.uppercase
import com.infinum.dbinspector.ui.schema.shared.SchemaFragment
import com.infinum.dbinspector.ui.schema.shared.SchemaTypeAdapter
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleConnection
import com.infinum.dbinspector.ui.shared.contracts.EditContract
import com.infinum.dbinspector.ui.shared.delegates.lifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.searchable.Searchable
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SchemaActivity : BaseActivity(), Searchable {

    override val binding by viewBinding(DbinspectorActivitySchemaBinding::inflate)

    override val viewModel: SchemaViewModel by viewModel()

    private val connection: LifecycleConnection by lifecycleConnection()

    private lateinit var contract: ActivityResultLauncher<EditContract.Input>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { finish() }

        if (connection.hasDatabaseData) {
            viewModel.databasePath = connection.databasePath!!
            viewModel.open()
            setupUi(connection.databaseName!!, connection.databasePath!!)
        } else {
            showDatabaseParametersError()
        }

        contract = registerForActivityResult(EditContract()) { shouldRefresh ->
            if (shouldRefresh) {
                supportFragmentManager.fragments
                    .filterIsInstance<SchemaFragment>()
                    .forEach { it.refresh() }
            }
        }
    }

    override fun onDestroy() {
        contract.unregister()
        viewModel.close()
        super.onDestroy()
    }

    override fun onSearchOpened() = Unit

    override fun search(query: String?) =
        supportFragmentManager
            .fragments
            .filterIsInstance<Searchable>()
            .forEach { it.search(query) }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

    override fun onSearchClosed() = Unit

    private fun setupUi(databaseName: String, databasePath: String) {
        with(binding) {
            toolbar.subtitle = databaseName
            toolbar.menu.searchView?.setup(
                hint = getString(R.string.dbinspector_search_by_name),
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(it) }
            )
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.edit -> {
                        showEdit(databasePath, databaseName)
                        true
                    }
                    else -> false
                }
            }

            viewPager.adapter = SchemaTypeAdapter(
                fragmentActivity = this@SchemaActivity,
                databasePath = databasePath,
                databaseName = databaseName
            )
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(SchemaType.values()[position].nameRes).uppercase()
            }.attach()
        }
    }

    private fun showEdit(databasePath: String, databaseName: String) =
        contract.launch(
            EditContract.Input(
                databasePath = databasePath,
                databaseName = databaseName
            )
        )
}
