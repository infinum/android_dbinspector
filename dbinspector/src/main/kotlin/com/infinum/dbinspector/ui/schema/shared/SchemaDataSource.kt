package com.infinum.dbinspector.ui.schema.shared

import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal abstract class SchemaDataSource<T : BaseParameters> : BaseDataSource<T>() {

    abstract var argument: String?
}
