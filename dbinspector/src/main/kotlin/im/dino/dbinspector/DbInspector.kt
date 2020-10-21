package im.dino.dbinspector

import android.content.Context
import android.content.Intent
import im.dino.dbinspector.ui.databases.DatabasesActivity
import im.dino.dbinspector.ui.shared.logger.DeadTree
import timber.log.Timber
import timber.log.Timber.DebugTree

object DbInspector {

    fun show(context: Context) {

        when (BuildConfig.DEBUG) {
            true -> Timber.plant(DebugTree())
            false -> Timber.plant(DeadTree())
        }

        context.startActivity(
            Intent(context, DatabasesActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}
