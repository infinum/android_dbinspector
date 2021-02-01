package com.infinum.dbinspector.ui.pragma

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleViewModel

internal class PragmaViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection
) : LifecycleViewModel(openConnection, closeConnection)
