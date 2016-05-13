package im.dino.dbinspector.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.File;

/**
 * Deletes the content of a specific table in a database.
 */
public class ClearTableIntentService extends IntentService {

    private static final String DATABASE = "database";
    private static final String TABLE = "table";
    private static final String ACTION = "im.dino.dbinspector.services.CLEAR_TABLE_RESULT";
    private static final String KEY_SUCCESS = "success";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ClearTableIntentService() {
        super("ClearTableIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        File database = (File) intent.getSerializableExtra(DATABASE);
        String table = intent.getStringExtra(TABLE);
        boolean success = false;
        if (database != null && database.exists() && !TextUtils.isEmpty(table)) {
            SQLiteDatabase db = null;
            try {
                db = SQLiteDatabase.openOrCreateDatabase(database, null);
                success = db.delete(table, null, null) > 0;
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        }
        Intent result = new Intent(ACTION);
        result.putExtra(KEY_SUCCESS, success);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
    }

    public static void deleteTable(Context context, File databaseFile, String tableName) {
        Intent service = new Intent(context, ClearTableIntentService.class);
        service.putExtra(DATABASE, databaseFile);
        service.putExtra(TABLE, tableName);
        context.startService(service);
    }

    public static void registerListener(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(ACTION));
    }

    public static void unregisterListener(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static boolean isSuccess(Intent intent) {
        return intent.getBooleanExtra(KEY_SUCCESS, false);
    }
}
