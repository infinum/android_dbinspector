package im.dino.dbview.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import im.dino.dbview.R;

/**
 * Created by dino on 01/03/14.
 */
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

        getActivity().getActionBar().setTitle(R.string.action_settings);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
