package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TableRepository(
    getPage: Interactors.GetTables,
    getByName: Interactors.GetTableByName,
    dropByName: Interactors.DropTableContentByName
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName
)
