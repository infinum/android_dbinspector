package com.infinum.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.shared.models.Cell

internal sealed class SchemaState {

    data class Schema(
        val schema: PagingData<Cell>
    ) : SchemaState()
}
