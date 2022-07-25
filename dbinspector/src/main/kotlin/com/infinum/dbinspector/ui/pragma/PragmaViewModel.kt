package com.infinum.dbinspector.ui.pragma

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleViewModel

internal class PragmaViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection
) : LifecycleViewModel<Any, Any>(openConnection, closeConnection)
