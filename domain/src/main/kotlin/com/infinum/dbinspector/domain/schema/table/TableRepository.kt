package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.domain.schema.shared.SchemaRepository

internal class TableRepository(
    getPage: Interactors.GetTables,
    getByName: Interactors.GetTableByName,
    dropByName: Interactors.DropTableContentByName,
    control: Control.Content
) : SchemaRepository(
    getPage,
    getByName,
    dropByName,
    control
)
