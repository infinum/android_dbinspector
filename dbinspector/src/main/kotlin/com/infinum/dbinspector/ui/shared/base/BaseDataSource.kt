package com.infinum.dbinspector.ui.shared.base

import androidx.paging.PagingSource
import com.infinum.dbinspector.domain.shared.models.Query

internal abstract class BaseDataSource : PagingSource<Int, String>() {

    abstract var query: Query
}
