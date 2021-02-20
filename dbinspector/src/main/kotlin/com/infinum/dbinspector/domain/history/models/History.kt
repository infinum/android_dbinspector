package com.infinum.dbinspector.domain.history.models

internal data class History(
    val executions: List<Execution> = listOf()
)
