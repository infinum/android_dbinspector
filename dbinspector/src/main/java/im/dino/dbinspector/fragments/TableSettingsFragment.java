package im.dino.dbinspector.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import im.dino.dbview.R;

/**
 * Created by dino on 01/03/14.
 */

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

        ActionBarActivity activity = (ActionBarActivity) getActivity();

        activity.getSupportActionBar().setTitle(R.string.dbinspector_action_settings);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
