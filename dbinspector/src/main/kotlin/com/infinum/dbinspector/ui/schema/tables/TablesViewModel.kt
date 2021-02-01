package com.infinum.dbinspector.ui.schema.tables

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class TablesViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getTables: UseCases.GetTables
) : SchemaSourceViewModel(openConnection, closeConnection, getTables) {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.tables(query = query)
}
