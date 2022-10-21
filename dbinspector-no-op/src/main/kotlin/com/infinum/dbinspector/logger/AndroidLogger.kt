package com.infinum.dbinspector.logger

import android.util.Log

/**
 * Android specific logger implementation.
 *
 * @constructor Create an Android logger instance without any parameters.
 */
public class AndroidLogger : Logger {

    internal companion object {
        internal const val DEFAULT_LOG_TAG = "[DbInspector]"
    }

    private var logTag: String? = null

    private var logLevel: Level = Level.INFO

    override fun log(level: Level, message: String) {
        if (canLog(level)) {
            doLog(level, message)
        }
    }

    override fun debug(message: String): Unit = doLog(Level.DEBUG, message)

    override fun info(message: String): Unit = doLog(Level.INFO, message)

    override fun error(message: String): Unit = doLog(Level.ERROR, message)

    override fun setTag(tag: String) {
        this.logTag = tag
    }

    override fun setLevel(level: Level) {
        this.logLevel = level
    }

    private fun tag(): String = logTag ?: DEFAULT_LOG_TAG

    private fun canLog(level: Level): Boolean = this.logLevel <= level

    private fun doLog(level: Level, message: String) {
        when (level) {
            Level.DEBUG -> Log.d(tag(), message)
            Level.INFO -> Log.i(tag(), message)
            Level.ERROR -> Log.e(tag(), message)
            else -> Log.e(tag(), message)
        }
    }
}
