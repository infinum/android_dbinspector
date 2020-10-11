package im.dino.dbinspector.ui.schema.shared

import im.dino.dbinspector.ui.shared.base.BaseDataSource

internal abstract class SchemaDataSource : BaseDataSource() {

    abstract var argument: String?
}