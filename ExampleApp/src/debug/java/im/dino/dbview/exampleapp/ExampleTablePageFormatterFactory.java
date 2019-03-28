package im.dino.dbview.exampleapp;

import android.content.Context;
import android.text.TextUtils;

import im.dino.dbinspector.adapters.TablePageFormatters;
import im.dino.dbinspector.adapters.TablePageFormatters.ICellViewFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.ITablesFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.LongTextViewFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.TablesFormatter;

import static im.dino.dbinspector.adapters.TablePageFormatters.dpToPx;
import static im.dino.dbinspector.adapters.TablePageFormatters.shortTimeStamp;

// Optional app-specific database table display customization in DbInspector
class ExampleTablePageFormatterFactory implements TablePageFormatters.ITablesFormatterFactory {

    public static void init() {
        TablePageFormatters.setTablesFormatterFactory(new ExampleTablePageFormatterFactory());
    }

    @Override
    public ITablesFormatter create(Context context) {
        final int defaultMaxWidth = dpToPx(200, context);

        // truncate long text if needed
        final ICellViewFormatter vDefault = new LongTextViewFormatter(defaultMaxWidth, TextUtils.TruncateAt.END);

        return new TablesFormatter(
                "articles", new TablePageFormatters.RowFormatter()
                .setValueFormatters("pubblished_at", shortTimeStamp) // convert long timestamp to human-readable form
        ).setDefaultCellViewFormatter(vDefault);
    }
}
