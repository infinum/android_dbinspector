package com.infinum.dbinspector.ui.pragma.shared

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

internal abstract class PragmaSourceViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
    private val useCase: BaseUseCase<PragmaParameters.Pragma, Page>
) : PagingViewModel<PragmaState, Any>(openConnection, closeConnection) {

    abstract fun pragmaStatement(name: String): String

    override fun dataSource(databasePath: String, statement: String) =
        PragmaDataSource(databasePath, statement, useCase)

    fun query(schemaName: String) {
        launch {
            pageFlow(databasePath, pragmaStatement(schemaName))
                .flowOn(runningDispatchers)
                .collectLatest {
                    setState(PragmaState.Pragma(pragma = it))
                }
        }
    }
}
