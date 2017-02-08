package im.dino.dbinspector.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import im.dino.dbinspector.BuildConfig;
import im.dino.dbinspector.R;
import im.dino.dbinspector.cache.InMemoryFileDescriptorCache;
import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbinspector.services.CopyDbIntentService;
import im.dino.dbinspector.services.ImportDbIntentService;

/**
 * Created by dino on 24/02/14.
 */
public class TableListFragment extends ListFragment {

    private static final String KEY_DATABASE = "database_name";

    private static final int REQUEST_FILE_CODE = 10;

    private static final int REQUEST_PERMISSION_CODE = 13;

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

    private BroadcastReceiver dbImportedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Database imported", Toast.LENGTH_SHORT).show();
        }
    };

    private LocalBroadcastManager localBroadcastManager;


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
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager
                .registerReceiver(dbCopiedReceiver, new IntentFilter(CopyDbIntentService.INTENT_DATABASE_COPIED));
        localBroadcastManager
                .registerReceiver(dbImportedReceiver, new IntentFilter(ImportDbIntentService.DATABASE_IMPORTED_ACTION));
    }

    @Override
    public void onPause() {
        localBroadcastManager.unregisterReceiver(dbCopiedReceiver);
        localBroadcastManager.unregisterReceiver(dbImportedReceiver);
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
        } else if (item.getItemId() == R.id.dbinspector_action_share) {
            shareDatabase();
        } else if (item.getItemId() == R.id.dbinspector_action_copy) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                CopyDbIntentService.startService(getActivity(), database);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
            return true;
        } else if (item.getItemId() == R.id.dbinspector_action_import) {
            Intent requestFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            requestFileIntent.setType("application/*");
            startActivityForResult(requestFileIntent, REQUEST_FILE_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE && Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                CopyDbIntentService.startService(getActivity(), database);
            } else {
                Toast.makeText(getContext(), getString(R.string.dbinspector_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareDatabase() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("application/octet-stream");
        Uri uri = Uri.parse(String.format("content://%s%s%s", BuildConfig.AUTHORITY_PREFIX,
                getActivity().getApplicationInfo().packageName, database.getAbsolutePath()));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.dbinspector_no_activity_to_handle_intent, Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            //Start a new intent service to replace the file
            Uri returnUri = data.getData();
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                Log.e(TableListFragment.class.getName(), "File not found.");
                return;
            }
            InMemoryFileDescriptorCache.getInstance().store(parcelFileDescriptor.getFileDescriptor());
            ImportDbIntentService.startService(getActivity(), database);
        }
    }
}
