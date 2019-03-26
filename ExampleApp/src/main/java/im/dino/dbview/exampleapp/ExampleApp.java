package im.dino.dbview.exampleapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils.TruncateAt;

import com.activeandroid.ActiveAndroid;
import com.squareup.leakcanary.LeakCanary;

import im.dino.dbinspector.adapters.TablePageFormatters;
import im.dino.dbinspector.adapters.TablePageFormatters.ICellViewFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.ITablesFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.ITablesFormatterFactory;
import im.dino.dbinspector.adapters.TablePageFormatters.LongTextViewFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.RowFormatter;
import im.dino.dbinspector.adapters.TablePageFormatters.TablesFormatter;

import static im.dino.dbinspector.adapters.TablePageFormatters.dpToPx;
import static im.dino.dbinspector.adapters.TablePageFormatters.shortTimeStamp;

/**
 * Created by dino on 02/06/15.
 */
public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        ActiveAndroid.initialize(this);

        // Add optional app-specific database table display customization
        TablePageFormatters.setTablesFormatterFactory(new ExampleTablePageFormatterFactory());
    }

}

class ExampleTablePageFormatterFactory implements ITablesFormatterFactory {
    @Override
    public ITablesFormatter create(Context context) {
        final int defaultMaxWidth = dpToPx(200, context);

        final ICellViewFormatter vDefault = new LongTextViewFormatter(defaultMaxWidth, TruncateAt.END); // truncate long text if needed

        return new TablesFormatter(
                "articles", new RowFormatter()
                .setValueFormatters("pubblished_at", shortTimeStamp) // convert long timestamp to human-readable form
        ).setDefaultCellViewFormatter(vDefault);
    }
}
