package com.infinum.dbinspector.domain.schema.view

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.schema.shared.SchemaRepository

internal class ViewRepository(
    getPage: Interactors.GetViews,
    getByName: Interactors.GetViewByName,
    dropByName: Interactors.DropViewByName,
    control: Control.Content
) : SchemaRepository(
    getPage,
    getByName,
    dropByName,
    control
)
