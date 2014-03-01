package im.dino.dbview.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import im.dino.dbview.R;
import im.dino.dbview.fragments.DatabaseListFragment;

public class DbViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new DatabaseListFragment()).commit();
        }
    }

    @Override
    public boolean onNavigateUp() {
        getFragmentManager().popBackStack();

        return true;
    }
}
