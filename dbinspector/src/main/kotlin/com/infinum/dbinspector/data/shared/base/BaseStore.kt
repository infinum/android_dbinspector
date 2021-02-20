package com.infinum.dbinspector.data.shared.base

import androidx.datastore.core.DataStore

internal interface BaseStore<OutputModel> {

    suspend fun store(): DataStore<OutputModel> = throw NotImplementedError()

    suspend fun current(): OutputModel = throw NotImplementedError()
}
