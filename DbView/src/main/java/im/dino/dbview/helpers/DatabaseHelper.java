package im.dino.dbview.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import im.dino.dbview.Constants;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseHelper {

    public static final String META_KEY_DB_NAME = "DBVIEW_DB_NAME";

    public static String getDatabaseName(Context context) {

        try {
            final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);

            if (ai.metaData != null) {
                return ai.metaData.getString(META_KEY_DB_NAME);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constants.LOGTAG, "Couldn't find metadata: " + META_KEY_DB_NAME);
            Log.d(Constants.LOGTAG, "Did you set the database name in you app manifest?");
        }

        return null;
    }

    public static void getAllTables(Context context) {

        String databasePath = context.getDatabasePath(getDatabaseName(context)).toString();

    }

}
