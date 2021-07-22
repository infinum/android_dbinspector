package com.infinum.dbinspector.ui.pragma

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.infinum.dbinspector.databinding.DbinspectorActivityPragmaBinding
import com.infinum.dbinspector.domain.pragma.models.PragmaType
import com.infinum.dbinspector.extensions.uppercase
import com.infinum.dbinspector.ui.pragma.shared.PragmaTypeAdapter
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.lifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class PragmaActivity : BaseActivity<Any, Any>() {

    override val binding by viewBinding(DbinspectorActivityPragmaBinding::inflate)

    override val viewModel: PragmaViewModel by viewModel()

    private val connection: LifecycleConnection by lifecycleConnection()

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

        if (connection.hasSchemaData) {
            viewModel.databasePath = connection.databasePath!!
            viewModel.open()
            setupUi(
                connection.databaseName!!,
                connection.databasePath!!,
                connection.schemaName!!
            )
        } else {
            showDatabaseParametersError()
        }
    }

    override fun onState(state: Any) = Unit

    override fun onEvent(event: Any) = Unit

    private fun setupUi(databaseName: String, databasePath: String, tableName: String) {
        with(binding) {
            toolbar.subtitle = listOf(databaseName, tableName).joinToString(" → ")

            viewPager.adapter = PragmaTypeAdapter(
                fragmentActivity = this@PragmaActivity,
                databasePath = databasePath,
                tableName = tableName
            )
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(PragmaType.values()[position].nameRes).uppercase()
            }.attach()
        }
    }
}
