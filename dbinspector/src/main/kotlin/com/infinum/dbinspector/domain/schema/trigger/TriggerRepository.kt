package com.infinum.dbinspector.domain.schema.trigger

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TriggerRepository(
    getPage: Interactors.GetTriggers,
    getByName: Interactors.GetTriggerByName,
    dropByName: Interactors.DropTriggerByName,
    mapper: Mappers.Page,
    converter: Converters.Schema
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    mapper,
    converter
)
