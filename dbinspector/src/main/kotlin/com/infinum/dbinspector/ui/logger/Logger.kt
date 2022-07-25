package com.infinum.dbinspector.ui.logger

public abstract class Logger(
    public var level: Level = Level.INFO
) {

    public companion object {
        internal const val DEFAULT_LOG_TAG = "[DbInspector]"
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public var tag: String? = null

    public abstract fun log(level: Level, message: String)

    public fun debug(message: String) {
        doLog(Level.DEBUG, message)
    }

    public fun info(message: String) {
        doLog(Level.INFO, message)
    }

    public fun error(message: String) {
        doLog(Level.ERROR, message)
    }

    protected fun tag(): String = tag ?: DEFAULT_LOG_TAG

    private fun canLog(level: Level): Boolean = this.level <= level

    private fun doLog(level: Level, message: String) {
        if (canLog(level)) {
            log(level, message)
        }
    }
}
