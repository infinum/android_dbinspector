package com.infinum.dbinspector.domain.history.control.mappers

import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.history.models.Execution

internal class ExecutionMapper : Mappers.Execution {

    override suspend fun invoke(model: HistoryEntity.ExecutionEntity): Execution =
        Execution(
            statement = model.execution,
            timestamp = model.timestamp,
            isSuccessful = model.success
        )
}
