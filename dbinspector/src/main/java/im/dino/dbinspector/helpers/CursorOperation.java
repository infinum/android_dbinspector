package im.dino.dbinspector.helpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by dino on 10/01/15.
 */
public abstract class CursorOperation<T> {

    private File databaseFile;

    public CursorOperation(File databaseFile) {
        this.databaseFile = databaseFile;
    }

    public T execute() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
        Cursor cursor = provideCursor(database);
        T result = provideResult(database, cursor);
        cursor.close();
        database.close();
        return result;
    }

    public abstract Cursor provideCursor(SQLiteDatabase database);

    public abstract T provideResult(SQLiteDatabase database, Cursor cursor);
}
