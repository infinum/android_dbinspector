package com.infinum.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class PragmaSourceViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    private val useCase: BaseUseCase<PragmaParameters.Pragma, Page>
) : PagingViewModel(openConnection, closeConnection) {

    abstract fun pragmaStatement(name: String): String

    override fun dataSource(databasePath: String, statement: String) =
        PragmaDataSource(databasePath, statement, useCase)

    fun query(
        schemaName: String,
        onData: suspend (value: PagingData<Cell>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, pragmaStatement(schemaName)) {
                onData(it)
            }
        }
    }
}
