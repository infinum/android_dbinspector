package com.infinum.dbinspector.ui.schema

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivitySchemaBinding
import com.infinum.dbinspector.domain.schema.shared.models.SchemaType
import com.infinum.dbinspector.extensions.searchView
import com.infinum.dbinspector.extensions.setup
import com.infinum.dbinspector.extensions.uppercase
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.edit.EditActivity
import com.infinum.dbinspector.ui.schema.shared.SchemaTypeAdapter
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.searchable.Searchable
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SchemaActivity : BaseActivity(), Searchable {

    override val binding by viewBinding(DbinspectorActivitySchemaBinding::inflate)

    private val viewModel: SchemaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.edit -> {
                        startActivity(
                            Intent(this@SchemaActivity, EditActivity::class.java)
                        )
                        true
                    }
                    else -> false
                }
            }

            toolbar.menu.searchView?.setup(
                hint = getString(R.string.dbinspector_search_by_name),
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(it) }
            )
        }

        intent.extras?.let {
            val databaseName = it.getString(Presentation.Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Presentation.Constants.Keys.DATABASE_PATH)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not()) {
                setupUi(databaseName!!, databasePath!!)
            } else {
                showError()
            }
        } ?: showError()
    }

    override fun onStop() {
        super.onStop()
        viewModel.close(lifecycleScope)
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
        viewModel.databasePath = databasePath
        viewModel.open(lifecycleScope)

        with(binding) {
            toolbar.subtitle = databaseName

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

    private fun showError() =
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.dbinspector_title_error)
            .setMessage(R.string.dbinspector_error_parameters)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()
}
