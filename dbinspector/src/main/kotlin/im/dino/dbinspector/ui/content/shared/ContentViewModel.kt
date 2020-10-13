package im.dino.dbinspector.ui.content.shared

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.base.BaseUseCase
import im.dino.dbinspector.domain.shared.models.DropException
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.ui.shared.paging.PagingViewModel
import kotlinx.coroutines.launch

internal abstract class ContentViewModel(
    private val openConnection: UseCases.OpenConnection,
    private val closeConnection: UseCases.CloseConnection,
    private val schemaInfo: BaseUseCase<Query, Page>,
    private val dropSchema: BaseUseCase<Query, Page>
) : PagingViewModel() {

    abstract fun headerStatement(name: String): String

    abstract fun schemaStatement(name: String): String

    abstract fun dropStatement(name: String): String

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

    fun header(
        schemaName: String,
        onData: suspend (value: List<String>) -> Unit
    ) =
        launch {
            val result = io {
                schemaInfo(
                    Query(
                        databasePath = databasePath,
                        statement = headerStatement(schemaName)
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

    fun drop(
        schemaName: String,
        onDone: suspend () -> Unit
    ) {
        launch {
            val result = io {
                dropSchema(
                    Query(
                        databasePath = databasePath,
                        statement = dropStatement(schemaName)
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
