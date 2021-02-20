package com.infinum.dbinspector.domain.history.models

internal data class Execution(
    val statement: String,
    val timestamp: Long,
    val isSuccessful: Boolean
)
