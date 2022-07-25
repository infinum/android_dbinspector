package com.infinum.dbinspector

import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.databases.DatabasesActivity
import com.infinum.dbinspector.ui.logger.Logger

public object DbInspector {

    @JvmStatic
    @JvmOverloads
    public fun show(logger: Logger? = null) {
        Presentation.setLogger(logger)
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
}
