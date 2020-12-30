package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TableRepository(
    getPage: Interactors.GetTables,
    getByName: Interactors.GetTableByName,
    dropByName: Interactors.DropTableContentByName,
    mapper: Mappers.Page,
    converter: Converters.Schema
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    mapper,
    converter
)
