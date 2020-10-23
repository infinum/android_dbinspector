package com.infinum.dbinspector.ui

import android.content.Context
import androidx.startup.Initializer
import com.infinum.dbinspector.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class DbInspectorInitializer : Initializer<Class<DbInspectorInitializer>> {

    override fun create(context: Context): Class<DbInspectorInitializer> {
        Presentation.init(context)

        Presentation.koinApplication.apply {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(context)
            modules(Presentation.modules())
        }

        return DbInspectorInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
