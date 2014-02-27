package im.dino.dbview.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseHelper {

    public static final String TABLE_LIST_QUERY
            = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String COLUMN_NAME = "name";

    public static final String PRAGMA_FORMAT = "PRAGMA table_info(%s)";

    public static SQLiteDatabase getDatabase(Context context, String databaseName) {
        File databasePath = context.getDatabasePath(databaseName);

        return SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    public static List<String> getAllTables(Context context, String databaseName) {

        SQLiteDatabase db = getDatabase(context, databaseName);

        List<String> tableList = new ArrayList<>();
        Cursor c = db.rawQuery(TABLE_LIST_QUERY, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableList.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
                c.moveToNext();
            }
        }

        return tableList;
    }

    public static Cursor getTableContent(Context context, String databaseName, String tableName) {

        SQLiteDatabase db = getDatabase(context, databaseName);

        List<String> columns = new ArrayList<>();

        // PRAGMA
//        cid|name                 |type    |notnull |dflt_value |pk
//        0  |id_fields_starring   |INTEGER |0       |           |1
//        1  |fields_descriptor_id |INTEGER |1       |           |0
//        2  |starring_id          |INTEGER |1       |           |0
//        3  |form_mandatory       |INTEGER |1       |1          |0
//        4  |form_visible         |INTEGER |1       |1          |0

        Cursor ti = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (ti.moveToFirst()) {
            do {
                columns.add(ti.getString(1));
            } while (ti.moveToNext());
        }

        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        return cursor;
    }

}
