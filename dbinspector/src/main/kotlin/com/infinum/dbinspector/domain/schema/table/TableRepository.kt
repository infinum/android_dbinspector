package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TableRepository(
    getPage: Interactors.GetTables,
    getByName: Interactors.GetTableByName,
    dropByName: Interactors.DropTableContentByName,
    control: Control.Schema
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    control
)
