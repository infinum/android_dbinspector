package im.dino.dbinspector

import android.content.Context
import android.content.Intent
import im.dino.dbinspector.ui.databases.DatabasesActivity
import im.dino.dbinspector.ui.shared.logger.DeadTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

import timber.log.Timber.DebugTree

object DbInspector {

    @FlowPreview
    @ExperimentalCoroutinesApi
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
