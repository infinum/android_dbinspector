@file:Suppress("UNUSED_PARAMETER")

package com.infinum.dbinspector

import com.infinum.dbinspector.data.sources.memory.logger.EmptyLogger
import com.infinum.dbinspector.data.sources.memory.logger.Logger

@Suppress("UnusedPrivateMember")
public object DbInspector {

    @JvmStatic
    public fun show(logger: Logger = EmptyLogger()): Unit = Unit
}
