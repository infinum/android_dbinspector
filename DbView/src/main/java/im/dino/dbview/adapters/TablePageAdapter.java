package im.dino.dbview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.dino.dbview.helpers.DatabaseHelper;
import im.dino.dbview.helpers.DisplayHelper;

/**
 * Created by dino on 27/02/14.
 */
public class TablePageAdapter {

    private final Context mContext;

    private final String mDatabaseName;

    private final String mTableName;

    private final SQLiteDatabase mDatabase;

    private static final int ROWS_PER_PAGE = 25;

    private int mPosition = 0;

    private int paddingPx;

    public TablePageAdapter(Context context, String databaseName, String tableName) {

        mContext = context;
        mDatabaseName = databaseName;
        mTableName = tableName;

        mDatabase = DatabaseHelper.getDatabase(mContext, mDatabaseName);
        paddingPx = DisplayHelper.dpToPx(mContext, 5);
    }

    public List<TableRow> getStructure() {

        List<TableRow> rows = new ArrayList<>();

        Cursor cursor = mDatabase
                .rawQuery(String.format(DatabaseHelper.PRAGMA_FORMAT, mTableName), null);

        cursor.moveToFirst();

        TableRow header = new TableRow(mContext);

        for (int col = 0; col < cursor.getColumnCount(); col++) {
            TextView textView = new TextView(mContext);
            textView.setText(cursor.getColumnName(col));
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
            header.addView(textView);
        }

        rows.add(header);

        do {
            TableRow row = new TableRow(mContext);

            for (int col = 0; col < cursor.getColumnCount(); col++) {
                TextView textView = new TextView(mContext);
                textView.setText(cursor.getString(col));
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
                row.addView(textView);
            }

            rows.add(row);

        } while (cursor.moveToNext());

        return rows;
    }
}
