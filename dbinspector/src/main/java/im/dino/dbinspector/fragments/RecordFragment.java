package im.dino.dbinspector.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import im.dino.dbinspector.R;
import im.dino.dbinspector.helpers.DatabaseHelper;
import im.dino.dbinspector.helpers.RecordScreenType;
import im.dino.dbinspector.interfaces.DbInspectorSqlCommunicator;

/**
 * Created by Zeljko Plesac on 18/04/15.
 */
public class RecordFragment extends Fragment {

    private static final String KEY_DATABASE = "database_name";

    private static final String KEY_TABLE = "table_name";

    private static final String KEY_NAMES = "key_names";

    private static final String KEY_VALUES = "key_values";

    private static final String KEY_SCREEN_TYPE = "scren_type";

    private File databaseFile;

    private String tableName;

    private String primaryKeyName;

    private String primaryKeyValue;

    private ArrayList<String> columnNames;

    private ArrayList<String> columnValues;

    private DbInspectorSqlCommunicator mCallback;

    private LinearLayout containerLayout;

    private RecordScreenType screenType;

    private MenuItem menuItemDelete;

    public RecordFragment() {

    }

    public static RecordFragment newInstance(RecordScreenType recordScreenType, File databaseFile, String tableName, ArrayList<String> columnNames, ArrayList<String> columnValues) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE, databaseFile);
        args.putString(KEY_TABLE, tableName);
        args.putStringArrayList(KEY_NAMES, columnNames);
        args.putStringArrayList(KEY_VALUES, columnValues);
        args.putSerializable(KEY_SCREEN_TYPE, recordScreenType);
        RecordFragment tf = new RecordFragment();
        tf.setArguments(args);

        return tf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            databaseFile = (File) getArguments().getSerializable(KEY_DATABASE);
            tableName = getArguments().getString(KEY_TABLE);
            columnNames = getArguments().getStringArrayList(KEY_NAMES);
            columnValues = getArguments().getStringArrayList(KEY_VALUES);
            screenType = (RecordScreenType) getArguments().getSerializable(KEY_SCREEN_TYPE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (DbInspectorSqlCommunicator) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement DbInspectorCommunicator");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dbinspector_record, container, false);

        init(view, inflater);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dbinspector_fragment_record, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
       // menu.findItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dbinspector_action_delete) {
            deleteRecord();
            return true;
        } else if (item.getItemId() == R.id.dbinspector_action_save) {
            if (screenType== RecordScreenType.CREATE){
                insertRecord();
            }
            else{
                updateRecord();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init(View view, LayoutInflater inflater) {
        primaryKeyName = DatabaseHelper.getPrimaryKeyName(databaseFile, tableName);

        containerLayout = (LinearLayout) view.findViewById(R.id.dbinspector_data_container);

        for (int i = 0; i < columnNames.size(); i++) {
            LinearLayout recordView = (LinearLayout) inflater.inflate(R.layout.dbinspector_record_row, null);
            TextView tvName = (TextView) recordView.findViewById(R.id.dbinspector_column_name);
            EditText etValue = (EditText) recordView.findViewById(R.id.dbinspector_column_value);

            tvName.setText(columnNames.get(i));

            if(screenType == RecordScreenType.UPDATE){
                etValue.setText(columnValues.get(i));
            }

            containerLayout.addView(recordView);

        }

        if(screenType == RecordScreenType.UPDATE){
            setupPrimaryKey();

        }
    }

    private void setupPrimaryKey() {
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equalsIgnoreCase(primaryKeyName)) {
                primaryKeyValue = columnValues.get(i);
            }
        }
    }

    private void deleteRecord() {
        if (DatabaseHelper.deleteRow(databaseFile, tableName, primaryKeyName, primaryKeyValue)) {
            mCallback.recordDeleted();
        } else {
            Toast.makeText(getActivity(), getString(R.string.dbinspector_query_fail), Toast.LENGTH_LONG).show();
        }
    }

    private void updateRecord() {
        int k = 0;

        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            LinearLayout recordRow = (LinearLayout) containerLayout.getChildAt(i);
            EditText et = (EditText) recordRow.getChildAt(1);
            columnValues.set(k, et.getText().toString());
            k++;
        }

        if (DatabaseHelper.updateRow(databaseFile, tableName, primaryKeyName, primaryKeyValue, columnNames, columnValues)) {
            mCallback.recordUpdated();
        } else {
            Toast.makeText(getActivity(), getString(R.string.dbinspector_query_fail), Toast.LENGTH_LONG).show();
        }
    }

    private void insertRecord() {
        int k = 0;
        columnValues = new ArrayList<>();
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            LinearLayout recordRow = (LinearLayout) containerLayout.getChildAt(i);
            EditText et = (EditText) recordRow.getChildAt(1);
            columnValues.add(et.getText().toString());
            k++;
        }

        if (DatabaseHelper.insertRow(databaseFile, tableName, primaryKeyName, primaryKeyValue, columnNames, columnValues)) {
            mCallback.recordInserted();
        } else {
            Toast.makeText(getActivity(), getString(R.string.dbinspector_query_fail), Toast.LENGTH_LONG).show();
        }
    }
}

