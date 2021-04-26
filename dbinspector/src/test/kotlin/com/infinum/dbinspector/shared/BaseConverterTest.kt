package com.infinum.dbinspector.shared

import com.infinum.dbinspector.domain.shared.base.BaseConverter

internal abstract class BaseConverterTest : BaseTest() {

    abstract val converter: BaseConverter<*, *>
}
