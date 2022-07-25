package com.infinum.dbinspector.ui.schema.views

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class ViewsViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
    getViews: com.infinum.dbinspector.domain.UseCases.GetViews
) : SchemaSourceViewModel(openConnection, closeConnection, getViews) {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.views(query = query)
}
