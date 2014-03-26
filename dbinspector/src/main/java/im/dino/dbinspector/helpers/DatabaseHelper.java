package im.dino.dbinspector.helpers;

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

}
