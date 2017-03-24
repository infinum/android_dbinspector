package im.dino.dbinspector;

import android.content.Context;
import android.content.Intent;

import im.dino.dbinspector.activities.DbInspectorActivity;

public final class DbInspector {

    public static Intent getLaunchIntent(Context context) {
        return DbInspectorActivity.launch(context)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
