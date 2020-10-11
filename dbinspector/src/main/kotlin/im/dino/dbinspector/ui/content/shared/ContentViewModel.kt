package im.dino.dbinspector.ui.content.shared

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.base.BaseUseCase
import im.dino.dbinspector.domain.shared.models.DropException
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.ui.shared.base.BaseDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal abstract class ContentViewModel(
    private val openConnection: UseCases.OpenConnection,
    private val closeConnection: UseCases.CloseConnection,
    private val schemaInfo: BaseUseCase<Query, Page>,
    private val dropSchema: BaseUseCase<Query, Page>
) : BaseViewModel() {

    abstract fun headerStatement(name: String): String

    abstract fun schemaStatement(name: String): String

    abstract fun dropStatement(name: String): String

    abstract fun dataSource(databasePath: String, statement: String): BaseDataSource

    lateinit var databasePath: String

    fun open(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            openConnection(databasePath)
        }
    }

    fun close(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(errorHandler) {
            closeConnection(databasePath)
        }
    }

    open fun header(
        schemaName: String,
        onData: suspend (value: List<String>) -> Unit
    ) =
        launch {
            val result = io {
                schemaInfo(
                    Query(
                        databasePath = databasePath,
                        statement = headerStatement(schemaName),
                        pageSize = PAGE_SIZE
                    )
                ).fields
            }
            onData(result)
        }

    fun query(
        schemaName: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(schemaName)) {
                onData(it)
            }
        }
    }

    private suspend fun pageFlow(
        databasePath: String,
        statement: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) =
        Pager(pagingConfig) {
            dataSource(databasePath, statement)
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }

    fun drop(
        schemaName: String,
        onDone: suspend () -> Unit
    ) {
        launch {
            val result = io {
                dropSchema(
                    Query(
                        databasePath = databasePath,
                        statement = dropStatement(schemaName),
                        pageSize = PAGE_SIZE
                    )
                ).fields
            }
            if (result.isEmpty()) {
                onDone()
            } else {
                throw DropException()
            }
        }
    }
}
