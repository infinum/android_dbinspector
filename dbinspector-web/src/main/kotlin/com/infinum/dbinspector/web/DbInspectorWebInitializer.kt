package com.infinum.dbinspector.web

import android.content.Context
import androidx.startup.Initializer

internal class DbInspectorWebInitializer : Initializer<Class<DbInspectorWebInitializer>> {

    override fun create(context: Context): Class<DbInspectorWebInitializer> {

        DbInspectorWeb.serve(context)

        return DbInspectorWebInitializer::class.java
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        mutableListOf()
}
