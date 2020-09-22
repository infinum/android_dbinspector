package im.dino.dbinspector.ui

import android.content.Context
import androidx.startup.Initializer
import im.dino.dbinspector.data.source.local.DatabaseFinder

class DbInspectorInitializer : Initializer<Class<DbInspectorInitializer>> {

    override fun create(context: Context): Class<DbInspectorInitializer> {
        DatabaseFinder.initialise(context)
        DatabaseFinder.find()
        return DbInspectorInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
