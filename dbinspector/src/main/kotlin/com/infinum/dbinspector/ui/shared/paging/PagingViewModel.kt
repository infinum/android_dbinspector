package com.infinum.dbinspector.ui.shared.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleViewModel
import kotlinx.coroutines.flow.Flow

internal abstract class PagingViewModel<State, Event>(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
) : LifecycleViewModel<State, Event>(openConnection, closeConnection) {

    abstract fun dataSource(databasePath: String, statement: String): BaseDataSource<*>

    fun pageFlow(
        databasePath: String,
        statement: String
    ): Flow<PagingData<Cell>> =
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
            .cachedIn(runningScope)
}
