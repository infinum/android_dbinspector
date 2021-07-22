package com.infinum.dbinspector.ui.schema

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleViewModel

internal class SchemaViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection
) : LifecycleViewModel<Any, Any>(openConnection, closeConnection)
