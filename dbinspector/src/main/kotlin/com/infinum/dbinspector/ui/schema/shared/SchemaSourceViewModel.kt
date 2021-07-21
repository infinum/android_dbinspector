package com.infinum.dbinspector.ui.schema.shared

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.shared.datasources.ContentDataSource
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

internal abstract class SchemaSourceViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    private val useCase: BaseUseCase<ContentParameters, Page>
) : PagingViewModel<SchemaState, Any>(openConnection, closeConnection) {

    abstract fun schemaStatement(query: String?): String

    override fun dataSource(databasePath: String, statement: String) =
        ContentDataSource(
            databasePath = databasePath,
            statement = statement,
            useCase = useCase
        )

    fun query(
        databasePath: String,
        query: String?
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(query))
                .flowOn(runningDispatchers)
                .collectLatest {
                    setState(SchemaState.Schema(schema = it))
                }
        }
    }
}
