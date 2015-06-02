package im.dino.dbinspector.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbinspector.services.CopyDbIntentService;

/**
 * Created by dino on 24/02/14.
 */
public class TableListFragment extends ListFragment {

    private static final String KEY_DATABASE = "database_name";

    private File database;

    private AdapterView.OnItemClickListener tableClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            String item = (String) getListAdapter().getItem(position);
            ft.replace(R.id.dbinspector_container, TableFragment.newInstance(database, item));
            ft.addToBackStack(item).commit();
            getFragmentManager().executePendingTransactions();
        }
    };

    private ArrayAdapter<String> adapter;

    private BroadcastReceiver dbCopiedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Database copied", Toast.LENGTH_SHORT).show();
        }
    };

    public static TableListFragment newInstance(File database) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE, database);

        TableListFragment tlf = new TableListFragment();
        tlf.setArguments(args);

        return tlf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            database = (File) getArguments().getSerializable(KEY_DATABASE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(dbCopiedReceiver, new IntentFilter(CopyDbIntentService.INTENT_DATABASE_COPIED));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(dbCopiedReceiver);
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        List<String> tableList = DatabaseHelper.getAllTables(database);
        String version = DatabaseHelper.getVersion(database);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {

            actionBar.setTitle(database.getName());
            if (!TextUtils.isEmpty(version)) {
                actionBar.setSubtitle("v" + version);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tableList);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(tableClickListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dbinspector_fragment_table_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dbinspector_action_search) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(new OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    search(s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        } else if (item.getItemId() == R.id.dbinspector_action_copy) {
            CopyDbIntentService.startService(getActivity(), database);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search(String queryString) {
        if (adapter != null) {
            adapter.getFilter().filter(queryString);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setSubtitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
