package com.infinum.dbinspector.ui.edit

import com.infinum.dbinspector.domain.history.models.History as HistoryModel
import com.infinum.dbinspector.ui.shared.views.editor.Keyword

internal sealed class EditEvent {

    data class Keywords(
        val keywords: List<Keyword>
    ) : EditEvent()

    data class History(
        val history: HistoryModel
    ) : EditEvent()

    data class SimilarExecution(
        val history: HistoryModel
    ) : EditEvent()
}
