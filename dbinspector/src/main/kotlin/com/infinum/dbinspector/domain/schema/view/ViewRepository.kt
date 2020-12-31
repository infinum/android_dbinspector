package com.infinum.dbinspector.domain.schema.view

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class ViewRepository(
    getPage: Interactors.GetViews,
    getByName: Interactors.GetViewByName,
    dropByName: Interactors.DropViewByName,
    control: Control.Schema
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName,
    control
)
