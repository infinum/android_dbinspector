package com.infinum.dbinspector.data.sources.memory.logger

import com.infinum.dbinspector.data.models.memory.logger.Level

internal class EmptyLogger : Logger(Level.NONE) {

    override fun log(level: Level, message: String) = Unit
}
