package im.dino.dbinspector

import android.content.Context
import android.content.Intent
import im.dino.dbinspector.ui.databases.DatabasesActivity

object DbInspector {

    fun launch(context: Context) {
        context.startActivity(
            Intent(context, DatabasesActivity::class.java)
        )
    }
}