package com.infinum.dbinspector.domain.history.models

public data class Execution(
    val statement: String,
    val timestamp: Long,
    val isSuccessful: Boolean
)
