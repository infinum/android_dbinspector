package im.dino.dbview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private int mCount = 0;

    private int paddingPx;

    public TablePageAdapter(Context context, String databaseName, String tableName) {

        mContext = context;
        mDatabaseName = databaseName;
        mTableName = tableName;

        mDatabase = DatabaseHelper.getDatabase(mContext, mDatabaseName);
        paddingPx = DisplayHelper.dpToPx(mContext, 5);
    }

    public List<TableRow> getStructure() {

        Cursor cursor = mDatabase
                .rawQuery(String.format(DatabaseHelper.PRAGMA_FORMAT, mTableName), null);

        cursor.moveToFirst();

        return getTableRows(cursor);
    }

    public List<TableRow> getContentPage() {

        Cursor cursor = mDatabase.query(mTableName, null, null, null, null, null, null);

        mCount = cursor.getCount();

        cursor.moveToPosition(mPosition);

        return getTableRows(cursor);
    }

    private List<TableRow> getTableRows(Cursor cursor) {

        List<TableRow> rows = new ArrayList<>();

        TableRow header = new TableRow(mContext);

        for (int col = 0; col < cursor.getColumnCount(); col++) {
            TextView textView = new TextView(mContext);
            textView.setText(cursor.getColumnName(col));
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            header.addView(textView);
        }

        rows.add(header);

        boolean alternate = true;

        do {
            TableRow row = new TableRow(mContext);

            for (int col = 0; col < cursor.getColumnCount(); col++) {
                TextView textView = new TextView(mContext);
                textView.setText(cursor.getString(col));
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);

                if (alternate) {
                    textView.setBackgroundColor(Color.rgb(250, 250, 250));
                }

                row.addView(textView);
            }

            alternate = !alternate;

            rows.add(row);

        } while (cursor.moveToNext() && rows.size() <= ROWS_PER_PAGE);

        return rows;
    }

    public void nextPage() {

        if (mPosition + ROWS_PER_PAGE < mCount) {
            mPosition += ROWS_PER_PAGE;
        }

    }

    public void previousPage() {

        if (mPosition - ROWS_PER_PAGE >= 0) {
            mPosition -= ROWS_PER_PAGE;
        }

    }

    public boolean hasNext() {

        return mPosition + ROWS_PER_PAGE < mCount;
    }

    public boolean hasPrevious() {

        return mPosition - ROWS_PER_PAGE >= 0;
    }

    public int getPageCount() {
        return (int) Math.ceil((float) mCount / ROWS_PER_PAGE);
    }

    public int getCurrentPage() {
        return (mPosition / ROWS_PER_PAGE) + 1;
    }

}
