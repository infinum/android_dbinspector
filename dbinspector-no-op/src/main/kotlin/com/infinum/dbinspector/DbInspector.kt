@file:Suppress("UNUSED_PARAMETER")

package com.infinum.dbinspector

import com.infinum.dbinspector.data.sources.memory.logger.Logger

@Suppress("UnusedPrivateMember")
public object DbInspector {

    @JvmStatic
    @JvmOverloads
    public fun show(logger: Logger? = null): Unit = Unit
}
