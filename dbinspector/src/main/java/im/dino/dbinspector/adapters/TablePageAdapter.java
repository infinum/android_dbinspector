package im.dino.dbinspector.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.dino.dbinspector.helpers.CursorOperation;
import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbinspector.helpers.DisplayHelper;
import im.dino.dbinspector.R;

/**
 * Created by dino on 27/02/14.
 */
public class TablePageAdapter {

    private final Context context;

    private final File databaseFile;

    private final String tableName;

    private int rowsPerPage = 10;

    private int position = 0;

    private int count = 0;

    private int paddingPx;

    public TablePageAdapter(Context context, File databaseFile, String tableName, int startPage) {

        this.context = context;
        this.databaseFile = databaseFile;
        this.tableName = tableName;
        paddingPx = DisplayHelper.dpToPx(this.context, 5);

        String keyRowsPerPage = this.context.getString(R.string.dbinspector_pref_key_rows_per_page);
        String defaultRowsPerPage = this.context.getString(R.string.dbinspector_rows_per_page_default);
        String rowsPerPage = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(keyRowsPerPage, defaultRowsPerPage);
        this.rowsPerPage = Integer.parseInt(rowsPerPage);
        position = this.rowsPerPage * startPage;
    }

    public List<TableRow> getStructure() {

        CursorOperation<List<TableRow>> operation = new CursorOperation<List<TableRow>>(databaseFile) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(String.format(DatabaseHelper.PRAGMA_FORMAT, tableName), null);
            }

            @Override
            public List<TableRow> provideResult(SQLiteDatabase database, Cursor cursor) {
                cursor.moveToFirst();
                return getTableRows(cursor, true);
            }
        };

        return operation.execute();
    }

    public List<TableRow> getContentPage() {

        CursorOperation<List<TableRow>> operation = new CursorOperation<List<TableRow>>(databaseFile) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.query(tableName, null, null, null, null, null, null);
            }

            @Override
            public List<TableRow> provideResult(SQLiteDatabase database, Cursor cursor) {
                count = cursor.getCount();
                cursor.moveToPosition(position);
                return getTableRows(cursor, false);
            }
        };

        return operation.execute();
    }

    private List<TableRow> getTableRows(Cursor cursor, boolean allRows) {

        List<TableRow> rows = new ArrayList<>();
        TableRow header = new TableRow(context);

        for (int col = 0; col < cursor.getColumnCount(); col++) {
            TextView textView = new TextView(context);
            textView.setText(cursor.getColumnName(col));
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            header.addView(textView);
        }

        rows.add(header);

        boolean alternate = true;

        if (cursor.getCount() == 0) {
            return rows;
        }

        do {
            TableRow row = new TableRow(context);

            for (int col = 0; col < cursor.getColumnCount(); col++) {
                TextView textView = new TextView(context);
                if (DatabaseHelper.getColumnType(cursor, col) == DatabaseHelper.FIELD_TYPE_BLOB) {
                    textView.setText("(data)");
                } else {
                    textView.setText(cursor.getString(col));
                }
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);

                if (alternate) {
                    textView.setBackgroundColor(Color.rgb(242, 242, 242));
                }

                row.addView(textView);
            }

            alternate = !alternate;
            rows.add(row);

        } while (cursor.moveToNext() && (allRows || rows.size() <= rowsPerPage));

        return rows;
    }

    public void nextPage() {
        if (position + rowsPerPage < count) {
            position += rowsPerPage;
        }
    }

    public void previousPage() {
        if (position - rowsPerPage >= 0) {
            position -= rowsPerPage;
        }
    }

    public boolean hasNext() {
        return position + rowsPerPage < count;
    }

    public boolean hasPrevious() {
        return position - rowsPerPage >= 0;
    }

    public int getPageCount() {
        return (int) Math.ceil((float) count / rowsPerPage);
    }

    public int getCurrentPage() {
        return (position / rowsPerPage) + 1;
    }

}
