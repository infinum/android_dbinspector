package com.infinum.dbinspector.domain.history.control.mappers

import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.history.models.History

internal class HistoryMapper(
    private val executionMapper: Mappers.Execution
) : Mappers.History {

    override suspend fun invoke(model: HistoryEntity): History =
        History(
            executions = model.executionsList.map { executionMapper(it) }
        )
}
