package com.infinum.dbinspector.ui.schema.shared

import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal abstract class SchemaDataSource : BaseDataSource() {

    abstract var argument: String?
}
