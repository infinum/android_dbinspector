package im.dino.dbinspector.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import im.dino.dbinspector.fragments.DatabaseListFragment;
import im.dino.dbinspector.R;
import im.dino.dbinspector.interfaces.DbInspectorSqlCommunicator;

public class DbInspectorSqlActivity extends ActionBarActivity implements DbInspectorSqlCommunicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbinspector_activity);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dbinspector_container, new DatabaseListFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void recordDeleted() {
        popBackStack();
    }

    @Override
    public void recordUpdated() {
        popBackStack();

    }

    @Override
    public void recordInserted() {
        popBackStack();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void popBackStack(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
