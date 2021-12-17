package com.infinum.dbinspector.domain.history.models

@JvmInline
internal value class History(
    val executions: List<Execution> = listOf()
)
