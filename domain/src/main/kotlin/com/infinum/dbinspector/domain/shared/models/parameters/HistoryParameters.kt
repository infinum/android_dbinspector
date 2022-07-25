package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters

public sealed class HistoryParameters : BaseParameters {

    public data class All(
        val databasePath: String
    ) : HistoryParameters()

    public data class Execution(
        val databasePath: String,
        val statement: String,
        val timestamp: Long,
        val isSuccess: Boolean
    ) : HistoryParameters()
}
