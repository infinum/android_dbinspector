package im.dino.dbinspector.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.adapters.TablePageFormatters.ICellValueFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.ITablesFormatter;
import im.dino.dbinspector.helpers.CursorOperation;
import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbinspector.helpers.PragmaType;

/**
 * Created by dino on 27/02/14.
 */
public class TablePageAdapter {

    public static final int DEFAULT_ROWS_PER_PAGE = 10;

    private final Context context;

    private final File databaseFile;

    private final String tableName;

    private int rowsPerPage = DEFAULT_ROWS_PER_PAGE;

    private int position = 0;

    private int count = 0;

    private int paddingPx;

    private String pragma;

    private TablePageFormatters.IRowFormatter rowFormatter;
    private TablePageFormatters.ICellViewFormatter defaultCellViewFormatter;

    public TablePageAdapter(Context context, File databaseFile, String tableName, int startPage) {

        this.context = context;
        this.databaseFile = databaseFile;
        this.tableName = tableName;
        paddingPx = context.getResources().getDimensionPixelSize(R.dimen.dbinspector_row_padding);

        String keyRowsPerPage = this.context.getString(R.string.dbinspector_pref_key_rows_per_page);
        String defaultRowsPerPage = this.context.getString(R.string.dbinspector_rows_per_page_default);
        String rowsPerPage = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(keyRowsPerPage, defaultRowsPerPage);
        this.rowsPerPage = Integer.parseInt(rowsPerPage);

        int pageCount = getPageCount();
        if (startPage > pageCount) {
            startPage = pageCount;
        }
        position = this.rowsPerPage * startPage;

        ITablesFormatter tablesFormatter = TablePageFormatters.createTablesFormatter(context);
        rowFormatter = tablesFormatter.getRowFormatterOfTable(tableName);
        if (rowFormatter == null) {
            rowFormatter = new TablePageFormatters.RowFormatter(); // a dummy no-op instance if no special formatting is specified
        }
        defaultCellViewFormatter = tablesFormatter.getDefaultCellViewFormatter();
    }

    public List<TableRow> getByPragma(PragmaType pragmaType) {
        switch (pragmaType) {
            case FOREIGN_KEY:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_FOREIGN_KEYS, tableName);
                break;
            case INDEX_LIST:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_INDEX, tableName);
                break;
            case TABLE_INFO:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_TABLE_INFO, tableName);
                break;
            default:
                Log.w(DatabaseHelper.LOGTAG, "Pragma type unknown: " + pragmaType);
        }

        CursorOperation<List<TableRow>> operation = new CursorOperation<List<TableRow>>(databaseFile) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(pragma, null);
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
            final String columnName = cursor.getColumnName(col);
            TextView textView = new TextView(context);
            textView.setText(columnName);
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            TablePageFormatters.ICellViewFormatter headerViewFormatter = rowFormatter.getHeaderViewFormatter(columnName);
            if (headerViewFormatter != null) {
                headerViewFormatter.formatView(textView);
            }
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
                final String columnName = cursor.getColumnName(col);
                TextView textView = new TextView(context);

                ICellValueFormatter valueFormatter = rowFormatter.getCellValueFormatter(columnName);
                String formattedValue;
                if (valueFormatter != null) {
                    formattedValue = valueFormatter.formatValue(cursor, col);
                } else if (DatabaseHelper.getColumnType(cursor, col) == DatabaseHelper.FIELD_TYPE_BLOB) {
                    formattedValue = "(data)";
                }  else {
                    formattedValue  = cursor.getString(col);
                }
                textView.setText(formattedValue);

                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);

                if (alternate) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.dbinspector_alternate_row_background));
                }

                TablePageFormatters.ICellViewFormatter cellViewFormatter = rowFormatter.getCellViewFormatter(columnName);
                if (cellViewFormatter == null) {
                    cellViewFormatter = defaultCellViewFormatter;
                }
                if (cellViewFormatter != null) {
                    cellViewFormatter.formatView(textView);
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
        return position / rowsPerPage + 1;
    }

    /**
     * Go back to the first page.
     */
    public void resetPage() {
        position = 0;
    }
}
