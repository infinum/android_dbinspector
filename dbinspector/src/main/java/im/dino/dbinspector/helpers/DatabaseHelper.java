package im.dino.dbinspector.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.helpers.models.TableRowModel;

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

    public static final String TABLE_LIST_QUERY
            = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String PRAGMA_FORMAT_TABLE_INFO = "PRAGMA table_info(%s)";

    public static final String PRAGMA_FORMAT_INDEX = "PRAGMA index_list(%s)";

    public static final String PRAGMA_FORMAT_FOREIGN_KEYS = "PRAGMA foreign_key_list(%s)";

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

        // CouchBase Lite stores the databases in the app files dir
        String[] cbliteFiles = context.fileList();
        for (String filename : cbliteFiles) {
            if (filenameFilter.accept(context.getFilesDir(), filename)) {
                databaseList.add(new File(context.getFilesDir(), filename));
            }
        }

        return databaseList;
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

    public static List<TableRowModel> getAllowedColumnsFromTable(final Context context, File database, String tableName) {
        final String query = String.format(DatabaseHelper.PRAGMA_FORMAT_TABLE_INFO, tableName);

        CursorOperation<List<TableRowModel>> operation = new CursorOperation<List<TableRowModel>>(database) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(query, null);
            }

            @Override
            public List<TableRowModel> provideResult(SQLiteDatabase database, Cursor cursor) {
                List<TableRowModel> columnList = new ArrayList<>();
                String[] allowedDataTypes = context.getResources().getStringArray(R.array.dbinspector_crud_allowed_data_types);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String dataType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

                        if (isAllowedDataType(allowedDataTypes, dataType)) {
                            columnList.add(new TableRowModel(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)), cursor.getString(cursor.getColumnIndex(COLUMN_NAME))));
                        }

                        cursor.moveToNext();
                    }
                }
                return columnList;
            }
        };

        return operation.execute();
    }

    public static boolean isAllowedDataType(String[] dataTypes, String targetValue) {
        return Arrays.asList(dataTypes).contains(targetValue);
    }

    public static String getPrimaryKeyName(File database, String tableName) {
        final String query = String.format(DatabaseHelper.PRAGMA_FORMAT_TABLE_INFO, tableName);

        CursorOperation<String> operation = new CursorOperation<String>(database) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(query, null);
            }

            @Override
            public String provideResult(SQLiteDatabase database, Cursor cursor) {
                cursor.moveToFirst();
                return getPrimaryKey(cursor);
            }
        };

        return operation.execute();
    }

    private static String getPrimaryKey(Cursor cursor) {
        do {
            if (cursor.getString(5).equals("1")) {
                return cursor.getString(1);
            }

        } while (cursor.moveToNext());

        return null;
    }

    /**
     * Compat method so we can get type of column on API < 11
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

    public static boolean deleteRow(File databaseFile, String tableName, String primaryKey, String primaryKeyValue) {
        try {
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
            String whereCause = primaryKey + "=" + primaryKeyValue;

            int affectedRows = database.delete(tableName, whereCause, null);
            database.close();

            return affectedRows != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
