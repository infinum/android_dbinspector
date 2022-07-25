package com.infinum.dbinspector.data.shared.base

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

public interface BaseStore<OutputModel> {

    public suspend fun store(): DataStore<OutputModel> = throw NotImplementedError()

    public suspend fun current(): OutputModel = throw NotImplementedError()

    public fun flow(): Flow<OutputModel> = throw NotImplementedError()
}
