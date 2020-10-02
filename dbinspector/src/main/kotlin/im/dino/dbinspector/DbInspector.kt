package im.dino.dbinspector

import android.content.Context
import android.content.Intent
import im.dino.dbinspector.ui.databases.DatabasesActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

object DbInspector {

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun show(context: Context) =
        context.startActivity(
            Intent(context, DatabasesActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
}
