package com.infinum.dbinspector.domain.schema.trigger

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TriggerRepository(
    getPage: Interactors.GetTriggers,
    getByName: Interactors.GetTriggerByName,
    dropByName: Interactors.DropTriggerByName,
    control: Control.Content
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    control
)
