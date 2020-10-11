package im.dino.dbinspector.domain.schema.trigger

import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TriggerRepository(
    getPage: Interactors.GetTriggers,
    getByName: Interactors.GetTriggerByName,
    dropByName: Interactors.DropTriggerByName
) : AbstractSchemaRepository(getPage, getByName, dropByName)