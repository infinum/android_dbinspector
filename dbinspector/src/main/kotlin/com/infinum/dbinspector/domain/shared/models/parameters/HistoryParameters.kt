package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters

internal sealed class HistoryParameters : BaseParameters {

    data class All(
        val databasePath: String
    ) : HistoryParameters()

    data class Execution(
        val databasePath: String,
        val statement: String,
        val timestamp: Long,
        val isSuccess: Boolean
    ) : HistoryParameters()
}
