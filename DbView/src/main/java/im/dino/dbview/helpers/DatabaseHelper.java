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

    public static List<String> getAllTables(Context context, String databaseName) {

        File databasePath = context.getDatabasePath(databaseName);

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(databasePath, null);

        List<String> tableList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableList.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }

        return tableList;
    }

}
