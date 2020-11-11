package com.infinum.dbinspector.ui.pragma

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityPragmaBinding
import com.infinum.dbinspector.domain.pragma.models.PragmaType
import com.infinum.dbinspector.ui.pragma.shared.PragmaTypeAdapter
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

internal class PragmaActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityPragmaBinding::inflate)

    private val viewModel: PragmaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    else -> false
                }
            }
        }

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val tableName = it.getString(Constants.Keys.SCHEMA_NAME)
            if (
                databaseName.isNullOrBlank().not() &&
                databasePath.isNullOrBlank().not() &&
                tableName.isNullOrBlank().not()
            ) {
                setupUi(databaseName!!, databasePath!!, tableName!!)
            } else {
                showError()
            }
        } ?: showError()
    }

    override fun onStop() {
        super.onStop()
        viewModel.close(lifecycleScope)
    }

    private fun setupUi(databaseName: String, databasePath: String, tableName: String) {
        viewModel.databasePath = databasePath
        viewModel.open(lifecycleScope)

        with(binding) {
            toolbar.subtitle = listOf(databaseName, tableName).joinToString(" → ")

            viewPager.adapter = PragmaTypeAdapter(
                fragmentActivity = this@PragmaActivity,
                databasePath = databasePath,
                tableName = tableName
            )
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(PragmaType.values()[position].nameRes).toUpperCase(Locale.getDefault())
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
