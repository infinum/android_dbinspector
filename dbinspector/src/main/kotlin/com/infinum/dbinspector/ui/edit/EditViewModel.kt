package com.infinum.dbinspector.ui.edit

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.datasources.ContentDataSource
import com.infinum.dbinspector.ui.shared.headers.Header
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel
import com.infinum.dbinspector.ui.shared.views.editor.Keyword
import com.infinum.dbinspector.ui.shared.views.editor.KeywordType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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
    private val getSimilarExecution: UseCases.GetSimilarExecution,
    private val saveHistoryExecution: UseCases.SaveExecution
) : PagingViewModel<EditState, EditEvent>(openConnection, closeConnection) {

    private var debounceSimilarExecutionJob: Job? = null

    override fun dataSource(databasePath: String, statement: String) =
        ContentDataSource(
            databasePath = databasePath,
            statement = statement,
            useCase = getRawQuery
        )

    fun header(query: String) {
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
            setState(EditState.Headers(headers = result))
        }
    }

    fun query(query: String) {
        launch {
            pageFlow(databasePath, query)
                .flowOn(runningDispatchers)
                .catch { error -> setError(error) }
                .collectLatest {
                    setState(EditState.Content(content = it))
                }
        }
    }

    fun affectedRows() {
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
                setState(EditState.AffectedRows(affectedRows = it))
            } ?: setError(IllegalStateException("Unknown error."))
        }
    }

    @Suppress("LongMethod")
    fun keywords() {
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
            emitEvent(EditEvent.Keywords(keywords = result))
        }
    }

    fun history() =
        launch {
            getHistory(HistoryParameters.All(databasePath))
                .flowOn(runningDispatchers)
                .catch { errorHandler.handleException(coroutineContext, it) }
                .collectLatest {
                    emitEvent(EditEvent.History(history = it))
                }
        }

    fun findSimilarExecution(scope: CoroutineScope, statement: String) {
        debounceSimilarExecutionJob?.cancel()
        debounceSimilarExecutionJob = scope.launch {
            delay(Presentation.Constants.Limits.DEBOUNCE_MILIS)
            val result = io {
                getSimilarExecution(
                    HistoryParameters.Execution(
                        databasePath = databasePath,
                        statement = statement,
                        timestamp = System.currentTimeMillis(),
                        isSuccess = false
                    )
                )
            }
            emitEvent(EditEvent.SimilarExecution(history = result))
        }
    }

    @Suppress("RedundantUnitExpression")
    fun saveSuccessfulExecution(statement: String) {
        if (statement.isNotBlank()) {
            launch {
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
}
