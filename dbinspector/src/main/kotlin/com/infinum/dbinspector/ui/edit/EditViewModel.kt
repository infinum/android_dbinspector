package com.infinum.dbinspector.ui.edit

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.schema.shared.models.exceptions.DropException
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.schema.tables.TablesDataSource
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

internal class EditViewModel(
    private val openConnection: UseCases.OpenConnection,
    private val closeConnection: UseCases.CloseConnection,
    private val getRawQuery: UseCases.GetRawQuery
) : PagingViewModel() {

    lateinit var databasePath: String

    private lateinit var onError: suspend (value: Throwable) -> Unit

    override val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        showError(throwable)
    }

    override fun dataSource(databasePath: String, statement: String) =
        EditDataSource(
            databasePath = databasePath,
            statement = statement,
            getRawQuery = getRawQuery
        )

    fun open(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            openConnection(ConnectionParameters(databasePath))
        }
    }

    fun close(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            closeConnection(ConnectionParameters(databasePath))
        }
    }

    fun query(
        query: String,
        onData: suspend (value: PagingData<Cell>) -> Unit,
        onError: suspend (value: Throwable) -> Unit
    ) {
        this.onError = onError
        launch {
            pageFlow(databasePath, query) {
                onData(it)
            }
        }
    }

    private fun showError(throwable: Throwable) =
        launch {
            onError(throwable)
        }
}
