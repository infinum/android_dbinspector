package com.infinum.dbinspector.ui.pragma

import androidx.lifecycle.LifecycleCoroutineScope
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.launch

internal class PragmaViewModel(
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
