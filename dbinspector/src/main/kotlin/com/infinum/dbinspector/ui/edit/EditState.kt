package com.infinum.dbinspector.ui.edit

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.shared.headers.Header

internal sealed class EditState {

    data class Headers(
        val headers: List<Header>
    ) : EditState()

    data class Content(
        val content: PagingData<Cell>
    ) : EditState()

    data class AffectedRows(
        val affectedRows: String
    ) : EditState()
}
