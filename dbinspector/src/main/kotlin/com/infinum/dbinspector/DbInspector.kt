package com.infinum.dbinspector

import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.databases.DatabasesActivity
import timber.log.Timber
import timber.log.Timber.DebugTree

public object DbInspector {

    private var loggerPlanted = false

    @JvmStatic
    public fun show() {

        if (BuildConfig.DEBUG && !loggerPlanted) {
            loggerPlanted = true
            Timber.plant(DebugTree())
        }

        with(Presentation.applicationContext()) {
            this.startActivity(
                Intent(this, DatabasesActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }
}
