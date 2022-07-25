package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters

public data class ConnectionParameters(
    val databasePath: String
) : BaseParameters
