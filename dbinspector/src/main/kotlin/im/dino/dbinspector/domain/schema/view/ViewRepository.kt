package im.dino.dbinspector.domain.schema.view

import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class ViewRepository(
    getPage: Interactors.GetViews,
    getByName: Interactors.GetViewByName,
    dropByName: Interactors.DropViewByName
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName
)
