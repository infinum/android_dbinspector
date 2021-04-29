package com.infinum.dbinspector.shared

import com.infinum.dbinspector.domain.shared.base.BaseMapper

internal abstract class BaseMapperTest : BaseTest() {

    abstract val mapper: BaseMapper<*, *>
}
