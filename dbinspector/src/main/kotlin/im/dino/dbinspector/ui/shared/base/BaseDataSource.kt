package im.dino.dbinspector.ui.shared.base

import androidx.paging.PagingSource
import im.dino.dbinspector.domain.shared.models.Query

internal abstract class BaseDataSource : PagingSource<Int, String>() {

    abstract var query: Query
}
