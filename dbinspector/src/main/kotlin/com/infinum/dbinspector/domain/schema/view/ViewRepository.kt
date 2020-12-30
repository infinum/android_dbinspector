package com.infinum.dbinspector.domain.schema.view

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class ViewRepository(
    getPage: Interactors.GetViews,
    getByName: Interactors.GetViewByName,
    dropByName: Interactors.DropViewByName,
    mapper: Mappers.Page,
    converter: Converters.Schema
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    mapper,
    converter
)
