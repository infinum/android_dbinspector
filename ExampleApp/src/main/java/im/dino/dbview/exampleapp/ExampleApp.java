package im.dino.dbview.exampleapp;

import com.activeandroid.ActiveAndroid;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dino on 02/06/15.
 */
public class ExampleApp extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        ActiveAndroid.initialize(this);
    }

}
