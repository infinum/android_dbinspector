package com.infinum.dbinspector.ui.edit

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.datasources.ContentDataSource
import com.infinum.dbinspector.ui.shared.headers.Header
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel
import com.infinum.dbinspector.ui.shared.views.editor.Keyword
import com.infinum.dbinspector.ui.shared.views.editor.KeywordType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

@Suppress("LongParameterList")
internal class EditViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    private val getRawQueryHeaders: UseCases.GetRawQueryHeaders,
    private val getRawQuery: UseCases.GetRawQuery,
    private val getAffectedRows: UseCases.GetAffectedRows,
    private val getTables: UseCases.GetTables,
    private val getTableInfo: UseCases.GetTableInfo,
    private val getHistory: UseCases.GetHistory,
    private val saveHistoryExecution: UseCases.SaveExecution
) : PagingViewModel(openConnection, closeConnection) {

    private var onError: suspend (value: Throwable) -> Unit = { throwable ->
        Timber.e(throwable)
    }

    override val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        showError(throwable)
    }

    override fun dataSource(databasePath: String, statement: String) =
        ContentDataSource(
            databasePath = databasePath,
            statement = statement,
            useCase = getRawQuery
        )

    fun header(
        query: String,
        onData: suspend (value: List<Header>) -> Unit,
        onError: suspend (value: Throwable) -> Unit
    ) {
        this.onError = onError
        launch {
            val result = io {
                getRawQueryHeaders(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = query
                    )
                ).cells
                    .map {
                        Header(
                            name = it.text.orEmpty(),
                            sort = Sort.ASCENDING
                        )
                    }
            }
            onData(result)
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

    fun affectedRows(
        onData: suspend (value: String) -> Unit,
        onError: suspend (value: Throwable) -> Unit
    ) {
        this.onError = onError
        launch {
            val result = io {
                getAffectedRows(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = Statements.RawQuery.affectedRows()
                    )
                )
            }
            result.cells.firstOrNull()?.text?.let {
                onData(it)
            } ?: this@EditViewModel.onError(IllegalStateException("Unknown error."))
        }
    }

    @Suppress("LongMethod")
    fun keywords(
        onData: suspend (value: List<Keyword>) -> Unit
    ) {
        launch {
            val result = io {
                val tableNames: List<Keyword> = getTables(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = Statements.Schema.tables(),
                        pageSize = Int.MAX_VALUE
                    )
                )
                    .cells
                    .filterNot { it.text.isNullOrBlank() }
                    .map {
                        Keyword(
                            value = it.text.orEmpty(),
                            type = KeywordType.TABLE_NAME
                        )
                    }

                val viewNames = getTables(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = Statements.Schema.views(),
                        pageSize = Int.MAX_VALUE
                    )
                )
                    .cells
                    .filterNot { it.text.isNullOrBlank() }
                    .map {
                        Keyword(
                            value = it.text.orEmpty(),
                            type = KeywordType.VIEW_NAME
                        )
                    }

                val triggerNames = getTables(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = Statements.Schema.triggers(),
                        pageSize = Int.MAX_VALUE
                    )
                )
                    .cells
                    .filterNot { it.text.isNullOrBlank() }
                    .map {
                        Keyword(
                            value = it.text.orEmpty(),
                            type = KeywordType.TRIGGER_NAME
                        )
                    }

                val columnNames: List<Keyword> = tableNames.map {
                    getTableInfo(
                        PragmaParameters.Pragma(
                            databasePath = databasePath,
                            statement = Statements.Pragma.tableInfo(it.value)
                        )
                    ).cells
                        .filterNot { cell -> cell.text.isNullOrBlank() }
                        .map { cell ->
                            Keyword(
                                value = cell.text.orEmpty(),
                                type = KeywordType.COLUMN_NAME
                            )
                        }
                }.flatten()
                    .distinctBy { it.value }

                listOf<Keyword>().plus(tableNames).plus(viewNames).plus(triggerNames).plus(columnNames)
            }
            onData(result)
        }
    }

    fun history(onData: suspend (value: History) -> Unit) =
        launch {
            getHistory(HistoryParameters.All(databasePath))
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    onData(it)
                }
        }

    @Suppress("RedundantUnitExpression")
    suspend fun saveSuccessfulExecution(statement: String) {
        if (statement.isNotBlank()) {
            io {
                saveHistoryExecution(
                    HistoryParameters.Execution(
                        databasePath = databasePath,
                        statement = statement,
                        timestamp = System.currentTimeMillis(),
                        isSuccess = true
                    )
                )
            }
        } else {
            Unit
        }
    }

    @Suppress("RedundantUnitExpression")
    fun saveFailedExecution(statement: String) {
        if (statement.isNotBlank()) {
            launch {
                io {
                    saveHistoryExecution(
                        HistoryParameters.Execution(
                            databasePath = databasePath,
                            statement = statement,
                            timestamp = System.currentTimeMillis(),
                            isSuccess = false
                        )
                    )
                }
            }
        } else {
            Unit
        }
    }

    private fun showError(throwable: Throwable) =
        launch {
            onError(throwable)
        }
}
