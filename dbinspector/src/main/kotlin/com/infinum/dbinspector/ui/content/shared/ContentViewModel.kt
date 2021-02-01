package com.infinum.dbinspector.ui.content.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.schema.shared.models.exceptions.DropException
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.datasources.ContentDataSource
import com.infinum.dbinspector.ui.shared.headers.Header
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class ContentViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    private val schemaInfo: BaseUseCase<PragmaParameters.Pragma, Page>,
    private val getSchema: BaseUseCase<ContentParameters, Page>,
    private val dropSchema: BaseUseCase<ContentParameters, Page>
) : PagingViewModel(openConnection, closeConnection) {

    abstract fun headerStatement(name: String): String

    abstract fun schemaStatement(name: String, orderBy: String?, sort: Sort): String

    abstract fun dropStatement(name: String): String

    override fun dataSource(databasePath: String, statement: String) =
        ContentDataSource(databasePath, statement, getSchema)

    fun header(
        schemaName: String,
        onData: suspend (value: List<Header>) -> Unit
    ) =
        launch {
            val result = io {
                schemaInfo(
                    PragmaParameters.Pragma(
                        databasePath = databasePath,
                        statement = headerStatement(schemaName)
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

    fun query(
        schemaName: String,
        orderBy: String?,
        sort: Sort,
        onData: suspend (value: PagingData<Cell>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(schemaName, orderBy, sort)) {
                onData(it)
            }
        }
    }

    fun drop(
        schemaName: String,
        onDone: suspend () -> Unit
    ) {
        launch {
            val result = io {
                dropSchema(
                    ContentParameters(
                        databasePath = databasePath,
                        statement = dropStatement(schemaName)
                    )
                ).cells
            }
            if (result.isEmpty()) {
                onDone()
            } else {
                throw DropException()
            }
        }
    }
}
