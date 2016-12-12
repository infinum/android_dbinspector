package im.dino.dbview.exampleapp;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.squareup.leakcanary.LeakCanary;

import im.dino.dbview.exampleapp.models.Article;
import im.dino.dbview.exampleapp.models.User;

/**
 * Created by dino on 02/06/15.
 */
public class ExampleApp extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        ActiveAndroid.dispose();
        final Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Article.class, User.class);
        ActiveAndroid.initialize(config.create());
    }

}
