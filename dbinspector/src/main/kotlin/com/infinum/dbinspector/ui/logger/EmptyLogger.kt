package com.infinum.dbinspector.ui.logger

internal class EmptyLogger : Logger(Level.NONE) {

    override fun log(level: Level, message: String) = Unit
}
