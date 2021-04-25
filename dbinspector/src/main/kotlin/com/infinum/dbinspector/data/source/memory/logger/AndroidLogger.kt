package com.infinum.dbinspector.data.source.memory.logger

import android.util.Log
import com.infinum.dbinspector.data.models.memory.logger.Level

public class AndroidLogger(
    level: Level = Level.INFO
) : Logger(level) {

    override fun log(level: Level, message: String) {
        if (this.level <= level) {
            logOnLevel(message, level)
        }
    }

    private fun logOnLevel(message: String, level: Level) {
        when (level) {
            Level.DEBUG -> Log.d(tag(), message)
            Level.INFO -> Log.i(tag(), message)
            Level.ERROR -> Log.e(tag(), message)
            else -> Log.e(tag(), message)
        }
    }
}
