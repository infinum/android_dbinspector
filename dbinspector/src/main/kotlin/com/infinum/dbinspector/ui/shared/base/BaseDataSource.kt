package com.infinum.dbinspector.ui.shared.base

import androidx.paging.PagingSource
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Query

internal abstract class BaseDataSource : PagingSource<Int, Cell>() {

    abstract var query: Query
}
