package com.infinum.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.shared.models.Cell

internal sealed class PragmaState {

    data class Pragma(
        val pragma: PagingData<Cell>
    ) : PragmaState()
}
