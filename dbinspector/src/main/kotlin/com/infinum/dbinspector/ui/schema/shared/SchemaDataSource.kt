package com.infinum.dbinspector.ui.schema.shared

import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal abstract class SchemaDataSource<Parameters : BaseParameters> : BaseDataSource<Parameters>() {

    abstract var argument: String?
}
