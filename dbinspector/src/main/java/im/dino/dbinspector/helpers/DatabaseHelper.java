package im.dino.dbinspector.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseHelper {

    public static final String LOGTAG = "DBINSPECTOR";

    public static final String COLUMN_CID = "cid";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_NOT_NULL = "not null";

    public static final String COLUMN_DEFAULT = "default value";

    public static final String COLUMN_PRIMARY = "primary key";

    public static final String TABLE_LIST_QUERY
            = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String PRAGMA_FORMAT = "PRAGMA table_info(%s)";

    public static String getSqliteDir(Context context) {
        return context.getFilesDir().getParent() + File.separator +  "databases" + File.separator;
    }

    public static String getCbliteDir(Context context) {
        return context.getFilesDir().getPath();
    }

    public static List<File> getDatabaseList(Context context) {
        List<File> databaseList = new ArrayList<>();

        File sqliteDir = new File(DatabaseHelper.getSqliteDir(context));
        File cbliteDir = new File(DatabaseHelper.getCbliteDir(context));

        FilenameFilter mFilenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".sql")
                    || filename.endsWith(".sqlite")
                    || filename.endsWith(".db")
                    || filename.endsWith(".cblite");
            }
        };

        databaseList.addAll(Arrays.asList(sqliteDir.listFiles(mFilenameFilter)));
        databaseList.addAll(Arrays.asList(cbliteDir.listFiles(mFilenameFilter)));

        return databaseList;
    }


    public static SQLiteDatabase getDatabase(File database) {
        return SQLiteDatabase.openOrCreateDatabase(database, null);
    }

    public static List<String> getAllTables(File database) {

        SQLiteDatabase db = getDatabase(database);

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
