package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.domain.Interactors

internal class SaveExecutionInteractor(
    private val dataStore: Sources.Local.History
) : Interactors.SaveExecution {

    override suspend fun invoke(input: HistoryTask) {
        dataStore.store().updateData {
            it.toBuilder().addExecutions(0, input.execution).build()
        }
//            .also {
//                Timber.tag("_BOJAN_").i("SaveExecutionInteractor: -> $it")
//            }
    }
}
