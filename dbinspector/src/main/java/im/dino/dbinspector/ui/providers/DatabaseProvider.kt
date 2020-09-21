package im.dino.dbinspector.ui.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.util.Arrays

/**
 * Class that is similar to FileProvider. The issue was
 * that FileProvider does not support sharing internal
 * storage database directory so we need this class to
 * share any directory.
 */
class DatabaseProvider : ContentProvider() {

    companion object {
        private val COLUMNS = arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        // Code from FileProvider
        var projection1 = projection
        val file = File(uri.path)
        if (projection1 == null) {
            projection1 = COLUMNS
        }
        var cols = arrayOfNulls<String>(projection1.size)
        var values = arrayOfNulls<Any>(projection1.size)
        var i = 0
        for (col in projection1) {
            if (OpenableColumns.DISPLAY_NAME == col) {
                cols[i] = OpenableColumns.DISPLAY_NAME
                values[i++] = file.name
            } else if (OpenableColumns.SIZE == col) {
                cols[i] = OpenableColumns.SIZE
                values[i++] = file.length()
            }
        }
        cols = Arrays.copyOf(cols, i)
        values = Arrays.copyOf(values, i)
        val cursor = MatrixCursor(cols, 1)
        cursor.addRow(values)
        return cursor
    }

    override fun getType(uri: Uri): String? = "application/octet-stream"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}