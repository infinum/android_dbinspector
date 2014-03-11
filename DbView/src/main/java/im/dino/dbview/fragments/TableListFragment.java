package im.dino.dbview.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import im.dino.dbview.R;
import im.dino.dbview.helpers.DatabaseHelper;

/**
 * Created by dino on 24/02/14.
 */
public class TableListFragment extends ListFragment {

    private static final String KEY_DATABASE = "database_name";

    private String mDatabaseName;

    public static TableListFragment newInstance(String databaseName) {

        Bundle args = new Bundle();
        args.putString(KEY_DATABASE, databaseName);

        TableListFragment tlf = new TableListFragment();
        tlf.setArguments(args);

        return tlf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDatabaseName = getArguments().getString(KEY_DATABASE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof ActionBarActivity) {

            ActionBarActivity activity = (ActionBarActivity) getActivity();

            activity.getSupportActionBar().setTitle(mDatabaseName);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List<String> tableList = DatabaseHelper.getAllTables(getActivity(), mDatabaseName);

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                tableList);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(tableClickListener);
    }

    private AdapterView.OnItemClickListener tableClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            FragmentManager fm = getActivity().getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, TableFragment.newInstance(mDatabaseName,
                    (String) getListAdapter().getItem(position)));
            ft.addToBackStack(null).commit();

            fm.executePendingTransactions();

        }
    };
}
