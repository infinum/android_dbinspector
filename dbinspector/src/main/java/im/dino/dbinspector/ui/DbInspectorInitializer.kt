package im.dino.dbinspector.ui

import android.content.Context
import androidx.startup.Initializer
import im.dino.dbinspector.data.source.local.DatabaseManager

class DbInspectorInitializer : Initializer<Class<DbInspectorInitializer>> {

    override fun create(context: Context): Class<DbInspectorInitializer> {
        DatabaseManager.initialise(context)
        return DbInspectorInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
