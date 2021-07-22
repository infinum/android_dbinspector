package com.infinum.dbinspector.ui.content.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.shared.headers.Header

internal sealed class ContentState {

    data class Headers(
        val headers: List<Header>
    ) : ContentState()

    data class Content(
        val content: PagingData<Cell>
    ) : ContentState()
}
