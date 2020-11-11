package com.infinum.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class SchemaSourceViewModel : PagingViewModel() {

    abstract fun schemaStatement(query: String?): String

    abstract fun query(
        databasePath: String,
        query: String?,
        onData: suspend (value: PagingData<String>) -> Unit
    )
}
