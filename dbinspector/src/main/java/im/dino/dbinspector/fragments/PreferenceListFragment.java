package im.dino.dbinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.XmlRes;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import im.dino.dbinspector.R;

/**
 * Original Thread http://forum.xda-developers.com/showthread.php?t=1363906 Created by Prasham on 12-02-2015.
 */
public class PreferenceListFragment extends ListFragment {

    /**
     * The starting request code given out to preference framework.
     */
    private static final int FIRST_REQUEST_CODE = 100;

    private static final int MSG_BIND_PREFERENCES = 0;

    public static final String XML_ID = "xml_id";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_BIND_PREFERENCES:
                    bindPreferences();
                    break;

                default:
                    // nothing to do
            }
        }
    };

    private PreferenceManager mPreferenceManager;

    private ListView lv;

    private int xmlId;

    public static PreferenceListFragment newInstance(@XmlRes int xmlId) {
        Bundle args = new Bundle();
        args.putInt(XML_ID, xmlId);
        PreferenceListFragment prefListFragment = new PreferenceListFragment();
        prefListFragment.setArguments(args);
        return prefListFragment;
    }

    //must be provided
    public PreferenceListFragment() {

    }

    private void bindPreferences() {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(lv);
        }
    }

    /**
     * Finds a {@link Preference} based on its key.
     *
     * @param key The key of the preference to retrieve.
     * @return The {@link Preference} with the key, or null.
     * @see android.preference.PreferenceGroup#findPreference(CharSequence)
     */
    public Preference findPreference(CharSequence key) {
        if (mPreferenceManager == null) {
            return null;
        }
        return mPreferenceManager.findPreference(key);
    }

    /**
     * Returns the {@link PreferenceManager} used by this activity.
     *
     * @return The {@link PreferenceManager}.
     */
    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityResult", int.class, int.class, Intent.class);
            m.setAccessible(true);
            m.invoke(mPreferenceManager, requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        if (getArguments() != null) {
            xmlId = getArguments().getInt(XML_ID);
        }
        mPreferenceManager = onCreatePreferenceManager();
        lv = (ListView) LayoutInflater.from(getActivity()).inflate(R.layout.dbinspector_preference_list_content, null);
        lv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        addPreferencesFromResource(xmlId);
        postBindPreferences();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityStop");
            m.setAccessible(true);
            m.invoke(mPreferenceManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lv = null;
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityDestroy");
            m.setAccessible(true);
            m.invoke(mPreferenceManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the {@link PreferenceManager}.
     *
     * @return The {@link PreferenceManager} used by this activity.
     */
    private PreferenceManager onCreatePreferenceManager() {
        try {
            Constructor<PreferenceManager> c = PreferenceManager.class.getDeclaredConstructor(Activity.class, int.class);
            c.setAccessible(true);
            PreferenceManager preferenceManager = c.newInstance(this.getActivity(), FIRST_REQUEST_CODE);
            return preferenceManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inflates the given XML resource and adds the preference hierarchy to the current preference hierarchy.
     *
     * @param preferencesResId The XML resource ID to inflate.
     */
    public void addPreferencesFromResource(int preferencesResId) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("inflateFromResource", Context.class, int.class, PreferenceScreen.class);
            m.setAccessible(true);
            PreferenceScreen prefScreen = (PreferenceScreen) m
                    .invoke(mPreferenceManager, getActivity(), preferencesResId, getPreferenceScreen());
            setPreferenceScreen(prefScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the root of the preference hierarchy that this activity is showing.
     *
     * @return The {@link PreferenceScreen} that is the root of the preference hierarchy.
     */
    public PreferenceScreen getPreferenceScreen() {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("getPreferenceScreen");
            m.setAccessible(true);
            return (PreferenceScreen) m.invoke(mPreferenceManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the root of the preference hierarchy that this activity is showing.
     *
     * @param preferenceScreen The root {@link PreferenceScreen} of the preference hierarchy.
     */
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("setPreferences", PreferenceScreen.class);
            m.setAccessible(true);
            boolean result = (Boolean) m.invoke(mPreferenceManager, preferenceScreen);
            if (result && preferenceScreen != null) {
                postBindPreferences();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        postBindPreferences();
        return lv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewParent p = lv.getParent();
        if (p != null) {
            ((ViewGroup) p).removeView(lv);
        }
    }

    /**
     * Posts a message to bind the preferences to the list view.
     * <p/>
     * Binding late is preferred as any custom preference types created in {@link #onCreate(Bundle)} are able to have their views recycled.
     */
    private void postBindPreferences() {
        if (mHandler.hasMessages(MSG_BIND_PREFERENCES)) {
            return;
        }
        mHandler.obtainMessage(MSG_BIND_PREFERENCES).sendToTarget();
    }
}