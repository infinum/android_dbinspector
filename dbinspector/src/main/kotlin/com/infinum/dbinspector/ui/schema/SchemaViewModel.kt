package com.infinum.dbinspector.ui.schema

import androidx.lifecycle.LifecycleCoroutineScope
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.launch

internal class SchemaViewModel(
    private val openConnection: UseCases.OpenConnection,
    private val closeConnection: UseCases.CloseConnection
) : BaseViewModel() {

    lateinit var databasePath: String

    fun open(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            openConnection(databasePath)
        }
    }

    fun close(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            closeConnection(databasePath)
        }
    }
}
