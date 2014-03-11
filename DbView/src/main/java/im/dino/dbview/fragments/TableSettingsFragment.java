package im.dino.dbview.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import im.dino.dbview.R;

/**
 * Created by dino on 01/03/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TableSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof ActionBarActivity) {

            ActionBarActivity activity = (ActionBarActivity) getActivity();

            activity.getSupportActionBar().setTitle(R.string.action_settings);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
}
