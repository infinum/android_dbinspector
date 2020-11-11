package com.infinum.dbinspector.ui.shared.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class PagingViewModel : BaseViewModel() {

    abstract fun dataSource(databasePath: String, statement: String): BaseDataSource

    suspend fun pageFlow(
        databasePath: String,
        statement: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) =
        Pager(
            config = PagingConfig(
                pageSize = Constants.Limits.PAGE_SIZE,
                enablePlaceholders = true
            ),
            initialKey = Constants.Limits.INITIAL_PAGE
        ) {
            dataSource(databasePath, statement)
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }
}
