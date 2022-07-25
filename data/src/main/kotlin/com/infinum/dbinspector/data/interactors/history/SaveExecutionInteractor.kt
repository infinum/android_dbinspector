package com.infinum.dbinspector.data.interactors.history

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.Interactors

internal class SaveExecutionInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.History
) : Interactors.SaveExecution {

    override suspend fun invoke(input: HistoryTask) {
        dataStore.store().updateData {
            it.toBuilder().addExecutions(0, input.execution).build()
        }
    }
}
