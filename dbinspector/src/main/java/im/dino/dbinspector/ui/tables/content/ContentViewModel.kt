package im.dino.dbinspector.ui.tables.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.pragma.TableInfoOperation
import im.dino.dbinspector.domain.pragma.models.TableInfoColumns

class ContentViewModel(
    path: String,
    name: String
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
    }

    private val infoDataSource = TableInfoOperation(name, PAGE_SIZE)(path, null)

    private val dataSource = ContentDataSource(path, name, PAGE_SIZE)

    fun header() = infoDataSource.map { it.fields[TableInfoColumns.NAME.ordinal] }

    fun query() =
        Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE * 3,
                enablePlaceholders = true
            )
        ) {
            dataSource
        }
            .flow.cachedIn(viewModelScope)
}