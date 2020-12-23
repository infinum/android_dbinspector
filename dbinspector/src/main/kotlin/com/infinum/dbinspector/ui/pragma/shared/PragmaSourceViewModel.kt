package com.infinum.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class PragmaSourceViewModel : PagingViewModel() {

    abstract fun pragmaStatement(name: String): String

    lateinit var databasePath: String

    fun query(
        schemaName: String,
        onData: suspend (value: PagingData<Cell>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, pragmaStatement(schemaName)) {
                onData(it)
            }
        }
    }
}
