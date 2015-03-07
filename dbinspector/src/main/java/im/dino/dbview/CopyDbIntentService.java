package im.dino.dbview;

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


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class CopyDbIntentService
        extends IntentService {

    public static final String EXTRA_PATH = "PATH";

    public static final String EXTRA_FILE = "File";

    public static final String INTENT_DATABASE_COPIED = "DatabaseCopied";

    public CopyDbIntentService() {
        super("CopyDbIntentService");
    }

    /**
     * Start service to copy the database. It will be copied to the app's director
     *
     * @param context : Context to start service
     */
    public static void startService(Context context, File database) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_FILE, database);
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
                appDirectory.mkdir();
            }
            String path = appDirectory.getAbsolutePath();

            File sqliteDir = new File(DatabaseHelper.getSqliteDir(this));
            if (database != null) {
                InputStream in;
                OutputStream out;
                try {
                    String databaseFileName = database.getName();
                    File opFile = new File(appDirectory, databaseFileName);
                    in = new FileInputStream(database);
                    out = new FileOutputStream(opFile);

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();

                    // write the output file
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(INTENT_DATABASE_COPIED));
            }
        }
    }
}
