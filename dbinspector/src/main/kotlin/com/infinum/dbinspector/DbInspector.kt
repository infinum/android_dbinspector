package com.infinum.dbinspector

import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.databases.DatabasesActivity
import com.infinum.dbinspector.ui.shared.logger.Stump
import timber.log.Timber
import timber.log.Timber.DebugTree

public object DbInspector {

    @JvmStatic
    public fun show() {

        when (BuildConfig.DEBUG) {
            true -> Timber.plant(DebugTree())
            false -> Timber.plant(Stump())
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
