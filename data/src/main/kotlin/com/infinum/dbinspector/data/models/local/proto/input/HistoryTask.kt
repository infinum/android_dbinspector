package com.infinum.dbinspector.data.models.local.proto.input

import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity

public data class HistoryTask(
    val databasePath: String? = null,
    val execution: HistoryEntity.ExecutionEntity? = null,
)
