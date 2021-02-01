package com.infinum.dbinspector.ui.shared.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class PagingViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
) : LifecycleViewModel(openConnection, closeConnection) {

    abstract fun dataSource(databasePath: String, statement: String): BaseDataSource<*>

    suspend fun pageFlow(
        databasePath: String,
        statement: String,
        onData: suspend (value: PagingData<Cell>) -> Unit
    ) =
        Pager(
            config = PagingConfig(
                pageSize = Presentation.Constants.Limits.PAGE_SIZE,
                enablePlaceholders = true
            ),
            initialKey = Presentation.Constants.Limits.INITIAL_PAGE
        ) {
            dataSource(databasePath, statement)
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }
}
