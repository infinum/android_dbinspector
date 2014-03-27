package im.dino.dbinspector.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import im.dino.dbview.R;

/**
 * Created by dino on 01/03/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TableSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the dbinspector_preferences from an XML resource
        addPreferencesFromResource(R.xml.dbinspector_preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        activity.getActionBar().setTitle(R.string.dbinspector_action_settings);
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
