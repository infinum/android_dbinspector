package com.infinum.dbinspector.ui.shared.base

import androidx.paging.PagingSource
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.Cell

internal abstract class BaseDataSource<Parameters : BaseParameters> : PagingSource<Int, Cell>() {

    abstract var parameters: Parameters
}
