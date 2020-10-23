package com.infinum.dbinspector.ui.schema.tables

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class TablesViewModel(
    private val getSchema: UseCases.GetTables
) : SchemaSourceViewModel() {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.tables(query = query)

    override fun dataSource(databasePath: String, statement: String) =
        TablesDataSource(
            databasePath = databasePath,
            statement = statement,
            getSchema = getSchema
        )

    override fun query(
        databasePath: String,
        query: String?,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(query)) {
                onData(it)
            }
        }
    }
}
