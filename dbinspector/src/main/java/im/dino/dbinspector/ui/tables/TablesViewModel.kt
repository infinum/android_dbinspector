package im.dino.dbinspector.ui.tables

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TablesViewModel : BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
    }

    private var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }

    fun query(scope: CoroutineScope, path: String, args: String? = null, action: suspend (value: PagingData<String>) -> Unit) {
        job = scope.launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                TablesDataSource(path, PAGE_SIZE, args)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}