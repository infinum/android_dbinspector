package im.dino.dbinspector.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import im.dino.dbinspector.helpers.DatabaseHelper;

public class CopyDbIntentService extends IntentService {

    public static final String EXTRA_FILE = "File";

    public static final String INTENT_DATABASE_COPIED = "DatabaseCopied";

    public static final String EXTRA_SHAREABLE_FILE = "ShareableFile";

    private static final String EXTRA_SHARING = "Sharing";

    public static final int BYTES_IN_KIBIBYTE = 1024;

    public CopyDbIntentService() {
        super("CopyDbIntentService");
    }

    /**
     * Start service to copy the database. It will be copied to the app's directory
     *
     * @param context  : Context to start service
     * @param database : File with the database that we want to copy
     * @param sharing  : true if we want to share the db after copying it.
     */
    public static void startService(Context context, File database, boolean sharing) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_FILE, database);
        bundle.putBoolean(EXTRA_SHARING, sharing);
        Intent intent = new Intent(context, CopyDbIntentService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            File database = (File) intent.getSerializableExtra(EXTRA_FILE);
            String directoryName = getString(getApplicationInfo().labelRes);
            File appDirectory = new File(Environment.getExternalStorageDirectory().toString(), directoryName);

            if (!appDirectory.exists() || !appDirectory.isDirectory()) {
                boolean dirCreated = appDirectory.mkdir();

                if (!dirCreated) {
                    Log.d(DatabaseHelper.LOGTAG, "Failed creating directory for storing db!");
                    return;
                }
            }

            if (database != null) {
                InputStream in;
                OutputStream out;
                try {
                    String databaseFileName = database.getName();
                    File outFile = new File(appDirectory, databaseFileName);
                    in = new FileInputStream(database);
                    out = new FileOutputStream(outFile);

                    byte[] buffer = new byte[BYTES_IN_KIBIBYTE];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();

                    // write the output file
                    out.flush();
                    out.close();
                    Intent successIntent = new Intent(INTENT_DATABASE_COPIED);
                    if (intent.getBooleanExtra(EXTRA_SHARING, false)) {
                        successIntent.putExtra(EXTRA_SHAREABLE_FILE, outFile);
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(successIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
