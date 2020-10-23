package com.infinum.dbinspector.di

import android.content.Context
import com.infinum.dbinspector.BuildConfig
import com.infinum.dbinspector.ui.Presentation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level

object LibraryKoin {

    private val koinApplication = KoinApplication.init()

    fun koin() = koinApplication.koin

    fun init(context: Context) {
        koinApplication.apply {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(context)
            modules(Presentation.modules())
        }
    }
}
