package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters

@JvmInline
internal value class ConnectionParameters(
    val databasePath: String
) : BaseParameters
