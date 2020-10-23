package com.infinum.dbinspector.ui.schema.views

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class ViewsViewModel(
    private val getSchema: UseCases.GetViews
) : SchemaSourceViewModel() {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.views(query = query)

    override fun dataSource(databasePath: String, statement: String) =
        ViewsDataSource(
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
