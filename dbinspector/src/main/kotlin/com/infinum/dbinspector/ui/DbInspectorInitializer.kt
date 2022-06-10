package com.infinum.dbinspector.ui

import android.content.Context
import androidx.startup.Initializer
import com.infinum.dbinspector.di.LibraryKoin

internal class DbInspectorInitializer : Initializer<Class<DbInspectorInitializer>> {

    override fun create(context: Context): Class<DbInspectorInitializer> {

        LibraryKoin.init(context)

        Presentation.init(context)

        DbInspectorShortcutManager.init(context)

        return DbInspectorInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
