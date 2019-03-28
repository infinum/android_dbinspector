package im.dino.dbview.exampleapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dino on 02/06/15.
 */
public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        ActiveAndroid.initialize(this);

        // Add optional app-specific database table display customization with DbInspector.
        // The actual definition is in debug source tree, given DbInspector is included in debug builds only.
        // Release source tree has a no-op implementation.
        ExampleTablePageFormatterFactory.init();
    }

}

