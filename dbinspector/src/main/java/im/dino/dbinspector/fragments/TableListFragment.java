package im.dino.dbinspector.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbview.R;

/**
 * Created by dino on 24/02/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

        Activity activity = getActivity();

        activity.getActionBar().setTitle(mDatabaseName);
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

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

            FragmentManager fm = getActivity().getFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, TableFragment.newInstance(mDatabaseName,
                    (String) getListAdapter().getItem(position)));
            ft.addToBackStack(null).commit();

            fm.executePendingTransactions();

        }
    };
}
