package im.dino.dbinspector.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseHelper {

    public static final int FIELD_TYPE_NULL = 0;

    public static final int FIELD_TYPE_INTEGER = 1;

    public static final int FIELD_TYPE_FLOAT = 2;

    public static final int FIELD_TYPE_STRING = 3;

    public static final int FIELD_TYPE_BLOB = 4;

    public static final String LOGTAG = "DBINSPECTOR";

    public static final String COLUMN_CID = "cid";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_NOT_NULL = "not null";

    public static final String COLUMN_DEFAULT = "default value";

    public static final String COLUMN_PRIMARY = "primary key";

    public static final String TABLE_LIST_QUERY = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String PRAGMA_FORMAT_TABLE_INFO = "PRAGMA table_info(%s)";

    public static final String PRAGMA_FORMAT_INDEX = "PRAGMA index_list(%s)";

    public static final String PRAGMA_FORMAT_FOREIGN_KEYS = "PRAGMA foreign_key_list(%s)";

    private DatabaseHelper() {
        // no instantiations
    }

    public static List<File> getDatabaseList(Context context) {
        List<File> databaseList = new ArrayList<>();

        // look for standard sqlite databases in the databases dir
        String[] contextDatabases = context.databaseList();
        for (String database : contextDatabases) {
            // don't show *-journal databases, they only hold temporary rollback data
            if (!database.endsWith("-journal")) {
                databaseList.add(context.getDatabasePath(database));
            }
        }

        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".sql")
                        || filename.endsWith(".sqlite")
                        || filename.endsWith(".db")
                        || filename.endsWith(".cblite");
            }
        };

        //Add External Databases if Present
        if (isExternalAvailable()) {
            final File absoluteFile = context.getExternalFilesDir(null).getAbsoluteFile();
            String[] externalDatabases = absoluteFile.list(filenameFilter);
            for (String db : externalDatabases) {
                databaseList.add(new File(absoluteFile, db));
            }
        }

        // CouchBase Lite stores the databases in the app files dir
        String[] cbliteFiles = context.fileList();
        for (String filename : cbliteFiles) {
            if (filenameFilter.accept(context.getFilesDir(), filename)) {
                databaseList.add(new File(context.getFilesDir(), filename));
            }
        }

        return databaseList;
    }

    /**
     * @return true if External Storage Present
     */
    private static boolean isExternalAvailable() {
        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    public static String getVersion(File database) {
        CursorOperation<String> operation = new CursorOperation<String>(database) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery("PRAGMA user_version", null);
            }

            @Override
            public String provideResult(SQLiteDatabase database, Cursor cursor) {
                String result = "";
                if (cursor.moveToFirst()) {
                    result = cursor.getString(0);
                }
                return result;
            }
        };
        return operation.execute();


    }

    public static List<String> getAllTables(File database) {

        CursorOperation<List<String>> operation = new CursorOperation<List<String>>(database) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(TABLE_LIST_QUERY, null);
            }

            @Override
            public List<String> provideResult(SQLiteDatabase database, Cursor cursor) {
                List<String> tableList = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        tableList.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                        cursor.moveToNext();
                    }
                }
                return tableList;
            }
        };

        return operation.execute();
    }

    /**
     * Compat method so we can get type of column on API < 11.
     * Source: http://stackoverflow.com/a/20685546/2643666
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getColumnType(Cursor cursor, int col) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            SQLiteCursor sqLiteCursor = (SQLiteCursor) cursor;
            CursorWindow cursorWindow = sqLiteCursor.getWindow();
            int pos = cursor.getPosition();
            int type = -1;
            if (cursorWindow.isNull(pos, col)) {
                type = FIELD_TYPE_NULL;
            } else if (cursorWindow.isLong(pos, col)) {
                type = FIELD_TYPE_INTEGER;
            } else if (cursorWindow.isFloat(pos, col)) {
                type = FIELD_TYPE_FLOAT;
            } else if (cursorWindow.isString(pos, col)) {
                type = FIELD_TYPE_STRING;
            } else if (cursorWindow.isBlob(pos, col)) {
                type = FIELD_TYPE_BLOB;
            }
            return type;
        } else {
            return cursor.getType(col);
        }
    }
}
