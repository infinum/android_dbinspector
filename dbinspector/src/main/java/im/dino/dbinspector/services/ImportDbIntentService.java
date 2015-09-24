package im.dino.dbinspector.services;

import org.apache.commons.io.FileUtils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import im.dino.dbinspector.cache.InMemoryFileDescriptorCache;

public class ImportDbIntentService extends IntentService {

    private static final String EXTRA_FILE = "File";

    private static final String TAG = "ImportDbIntentService";

    public static final String DATABASE_IMPORTED_ACTION = "databaseReplaced";

    public ImportDbIntentService() {
        super("ImportDbIntentService");
    }

    /**
     * Start service to replace the database.
     *
     * @param context : Context to start service
     */
    public static void startService(Context context, File database) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_FILE, database);
        Intent intent = new Intent(context, ImportDbIntentService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        FileDescriptor fileDescriptor = InMemoryFileDescriptorCache.getInstance().getFromCache();
        FileInputStream fis = new FileInputStream(fileDescriptor);
        try {
            FileUtils.copyInputStreamToFile(fis, (File) intent.getSerializableExtra(EXTRA_FILE));
        } catch (IOException e) {
            Log.e(TAG, "Could not replace file");
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DATABASE_IMPORTED_ACTION));
    }
}
