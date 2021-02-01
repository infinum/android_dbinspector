package com.infinum.dbinspector.ui.schema.triggers

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class TriggersViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getTriggers: UseCases.GetTriggers
) : SchemaSourceViewModel(openConnection, closeConnection, getTriggers) {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.triggers(query = query)
}
