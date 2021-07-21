package com.infinum.dbinspector.ui.edit.history

import com.infinum.dbinspector.domain.history.models.History as HistoryModel

internal sealed class HistoryState {

    data class History(
        val history: HistoryModel
    ) : HistoryState()
}
