package im.dino.dbinspector.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.io.File;

import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbview.R;

/**
 * Created by dino on 23/02/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatabaseListFragment
        extends ListFragment
        implements AdapterView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBarActivity activity = (ActionBarActivity) getActivity();

        activity.getSupportActionBar().setTitle(getString(R.string.dbinspector_databases));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                DatabaseHelper.getDatabaseList(getActivity()));

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dbinspector_container,
                TableListFragment.newInstance((File) getListAdapter().getItem(position)));
        ft.addToBackStack(null).commit();

        fm.executePendingTransactions();
    }
}
