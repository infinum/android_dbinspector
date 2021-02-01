package com.infinum.dbinspector.ui.schema.views

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class ViewsViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getViews: UseCases.GetViews
) : SchemaSourceViewModel(openConnection, closeConnection, getViews) {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.views(query = query)
}
