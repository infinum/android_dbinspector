package com.infinum.dbinspector.sample

import android.app.Application
import com.infinum.dbinspector.sample.data.AssetsDatabaseProvider
import com.infinum.dbinspector.sample.data.DatabaseProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class DbSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@DbSampleApp)
            modules(
                module {
                    single<DatabaseProvider> { AssetsDatabaseProvider(get()) }
                    viewModel { MainViewModel(get()) }
                }
            )
        }
    }
}