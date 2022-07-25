package com.infinum.dbinspector.domain.history.control.converters

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters

internal class HistoryConverter : Converters.History {

    override fun get(parameters: HistoryParameters.All): HistoryTask =
        HistoryTask(
            databasePath = parameters.databasePath
        )

    override suspend fun execution(parameters: HistoryParameters.Execution): HistoryTask =
        HistoryTask(
            execution = HistoryEntity.ExecutionEntity.getDefaultInstance().toBuilder()
                .setDatabasePath(parameters.databasePath)
                .setExecution(parameters.statement)
                .setTimestamp(parameters.timestamp)
                .setSuccess(parameters.isSuccess)
                .build()
        )

    override suspend fun clear(parameters: HistoryParameters.All): HistoryTask =
        HistoryTask(
            databasePath = parameters.databasePath
        )
}
