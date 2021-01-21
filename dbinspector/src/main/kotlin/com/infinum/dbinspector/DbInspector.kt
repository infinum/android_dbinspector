package com.infinum.dbinspector

import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.databases.DatabasesActivity
import timber.log.Timber
import timber.log.Timber.DebugTree

public object DbInspector {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    @JvmStatic
    public fun show(): Unit =
        with(Presentation.applicationContext()) {
            this.startActivity(
                Intent(
                    this,
                    DatabasesActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
}
