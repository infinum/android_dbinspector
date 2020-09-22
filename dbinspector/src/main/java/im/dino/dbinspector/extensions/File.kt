package im.dino.dbinspector.extensions

import android.content.Context
import java.io.File

fun Context.databaseDir(): String {
    val destPath = this.filesDir.path
    return destPath.substring(0, destPath.lastIndexOf("/")) + "/databases"
}