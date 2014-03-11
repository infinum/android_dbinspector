package im.dino.dbview.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import im.dino.dbview.R;
import im.dino.dbview.fragments.DatabaseListFragment;

public class DbViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new DatabaseListFragment()).commit();
        }
    }

    @Override
    public boolean onNavigateUp() {
        getSupportFragmentManager().popBackStack();

        return true;
    }
}
