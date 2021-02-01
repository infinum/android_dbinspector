package com.infinum.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.shared.datasources.ContentDataSource
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class SchemaSourceViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    private val useCase: BaseUseCase<ContentParameters, Page>
) : PagingViewModel(openConnection, closeConnection) {

    abstract fun schemaStatement(query: String?): String

    override fun dataSource(databasePath: String, statement: String) =
        ContentDataSource(
            databasePath = databasePath,
            statement = statement,
            useCase = useCase
        )

    fun query(
        databasePath: String,
        query: String?,
        onData: suspend (value: PagingData<Cell>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(query)) {
                onData(it)
            }
        }
    }
}
