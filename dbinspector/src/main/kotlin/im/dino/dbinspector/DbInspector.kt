package im.dino.dbinspector

import android.content.Context
import android.content.Intent
import im.dino.dbinspector.ui.databases.DatabasesActivity

object DbInspector {

    fun show(context: Context) =
        context.startActivity(
            Intent(context, DatabasesActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
}