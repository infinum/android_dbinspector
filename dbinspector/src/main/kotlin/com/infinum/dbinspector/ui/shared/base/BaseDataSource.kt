package com.infinum.dbinspector.ui.shared.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinum.dbinspector.domain.shared.base.PageParameters
import com.infinum.dbinspector.domain.shared.models.Cell

internal abstract class BaseDataSource<T : PageParameters> : PagingSource<Int, Cell>() {

    abstract var parameters: T

    override fun getRefreshKey(state: PagingState<Int, Cell>): Int? {
        return parameters.page
    }
}
