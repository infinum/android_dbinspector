package com.infinum.dbinspector

import android.content.Intent
import com.infinum.dbinspector.data.sources.memory.logger.EmptyLogger
import com.infinum.dbinspector.data.sources.memory.logger.Logger
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.databases.DatabasesActivity

public object DbInspector {

    @JvmStatic
    public fun show(logger: Logger = EmptyLogger()) {
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
