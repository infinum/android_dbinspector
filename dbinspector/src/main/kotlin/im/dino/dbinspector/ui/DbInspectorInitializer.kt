package im.dino.dbinspector.ui

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DbInspectorInitializer : Initializer<Class<DbInspectorInitializer>> {

    override fun create(context: Context): Class<DbInspectorInitializer> {

        startKoin {
            androidContext(context)
            modules(Presentation.modules())
            printLogger(level = Level.INFO)
        }

        return DbInspectorInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
