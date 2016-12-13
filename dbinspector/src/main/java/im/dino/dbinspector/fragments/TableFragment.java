package im.dino.dbinspector.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.adapters.TablePageAdapter;
import im.dino.dbinspector.helpers.DialogHelper;
import im.dino.dbinspector.helpers.PragmaType;
import im.dino.dbinspector.helpers.RecordScreenType;
import im.dino.dbinspector.helpers.models.TableRowModel;
import im.dino.dbinspector.services.ClearTableIntentService;

/**
 * Created by dino on 24/02/14.
 */
public class TableFragment extends Fragment implements ActionBar.OnNavigationListener, DialogHelper.SearchQueryListener {

    private static final String KEY_DATABASE = "database_name";

    private static final String KEY_TABLE = "table_name";

    private static final String KEY_SHOWING_CONTENT = "showing_content";

    private static final String KEY_PAGE = "current_page";

    private static final String KEY_LAST_PRAGMA = "last_pragma";

    public static final int DROPDOWN_CONTENT_POSITION = 0;

    public static final int DROPDOWN_INFO_POSITION = 1;

    public static final int DROPDOWN_FOREIGN_KEYS_POSITION = 2;

    public static final int DROPDOWN_INDEXES_POSITION = 3;

    private File databaseFile;

    private String tableName;

    private TableLayout tableLayout;

    private TablePageAdapter adapter;

    private View nextButton;

    private View previousButton;

    private TextView currentPageText;

    private View contentHeader;

    private ScrollView scrollView;

    private HorizontalScrollView horizontalScrollView;

    private boolean showingContent = true;

    private PragmaType lastPragmaType = PragmaType.TABLE_INFO;

    private int currentPage;

    private ArrayList<TableRowModel> conditionList = new ArrayList<>();

    private BroadcastReceiver mClearTableReceiver = new ClearTableReceiver();
    private ArrayList<String> columnNames;

    private View.OnClickListener nextListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            currentPage++;
            adapter.nextPage();
            showContent(conditionList.isEmpty() ? adapter.getContentPage() : adapter.getContentPage(conditionList));

            scrollView.scrollTo(0, 0);
            horizontalScrollView.scrollTo(0, 0);
        }
    };
    private View.OnClickListener previousListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            currentPage--;
            adapter.previousPage();
            showContent(conditionList.isEmpty() ? adapter.getContentPage() : adapter.getContentPage(conditionList));

            scrollView.scrollTo(0, 0);
            horizontalScrollView.scrollTo(0, 0);
        }
    };

    public static TableFragment newInstance(File databaseFile, String tableName) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE, databaseFile);
        args.putString(KEY_TABLE, tableName);

        TableFragment tf = new TableFragment();
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
        }

        if (savedInstanceState != null) {
            showingContent = savedInstanceState.getBoolean(KEY_SHOWING_CONTENT, true);
            lastPragmaType = PragmaType.values()[savedInstanceState.getInt(KEY_LAST_PRAGMA, 1)];
            currentPage = savedInstanceState.getInt(KEY_PAGE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dbinspector_fragment_table, container, false);

        tableLayout = (TableLayout) view.findViewById(R.id.dbinspector_table_layout);
        previousButton = view.findViewById(R.id.dbinspector_button_previous);
        nextButton = view.findViewById(R.id.dbinspector_button_next);
        currentPageText = (TextView) view.findViewById(R.id.dbinspector_text_current_page);
        contentHeader = view.findViewById(R.id.dbinspector_layout_content_header);
        scrollView = (ScrollView) view.findViewById(R.id.dbinspector_scrollview_table);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.dbinspector_horizontal_scrollview_table);

        previousButton.setOnClickListener(previousListener);
        nextButton.setOnClickListener(nextListener);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpActionBar();
        adapter = new TablePageAdapter(getActivity(), databaseFile, tableName, currentPage);
        if (showingContent) {
            showContent(adapter.getContentPage());
        } else {
            showByPragma(lastPragmaType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActionBar();
        ClearTableIntentService.registerListener(getActivity(), mClearTableReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        ClearTableIntentService.unregisterListener(getActivity(), mClearTableReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_SHOWING_CONTENT, showingContent);
        outState.putInt(KEY_PAGE, currentPage);
        outState.putInt(KEY_LAST_PRAGMA, lastPragmaType.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dbinspector_fragment_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dbinspector_action_settings) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.dbinspector_container, PreferenceListFragment.newInstance(R.xml.dbinspector_preferences))
                    .addToBackStack("Settings")
                    .commit();
            getFragmentManager().executePendingTransactions();
        } else if (item.getItemId() == R.id.dbinspector_action_search) {
            DialogHelper.showSearchDialog(getActivity(), databaseFile, tableName, this);
        } else if (item.getItemId() == R.id.dbinspector_action_add) {
            showRecord(RecordScreenType.CREATE, null);
        } else if (item.getItemId() == R.id.dbinspector_action_clear_table) {
            ClearTableAlertDialogFragment fragment = ClearTableAlertDialogFragment.newInstance(databaseFile, tableName);
            fragment.show(getFragmentManager(), "CONFIRM_DIALOG");
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setTitle(tableName);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the action bar to show a dropdown list.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[]{getString(R.string.dbinspector_content),
                        getString(R.string.dbinspector_structure), getString(R.string.dbinspector_foreign_keys),
                        getString(R.string.dbinspector_indexes)}),
                this);
    }

    private void showContent(List<TableRow> rows) {

        showingContent = true;
        tableLayout.removeAllViews();

        for (final TableRow row : rows) {
            tableLayout.addView(row);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> columnValues = getTableRowValues(tableLayout.indexOfChild(v));
                    showRecord(RecordScreenType.UPDATE, columnValues);
                }
            });
        }

        currentPageText.setText(adapter.getCurrentPage() + "/" + adapter.getPageCount());

        contentHeader.setVisibility(View.VISIBLE);

        nextButton.setEnabled(adapter.hasNext());
        previousButton.setEnabled(adapter.hasPrevious());

        columnNames = getTableRowValues(0);
    }

    private void showByPragma(PragmaType pragmaType) {
        lastPragmaType = pragmaType;
        showingContent = false;
        tableLayout.removeAllViews();

        List<TableRow> rows = adapter.getByPragma(pragmaType);

        for (TableRow row : rows) {
            tableLayout.addView(row);
        }

        contentHeader.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        switch (itemPosition) {
            case DROPDOWN_CONTENT_POSITION:
                showContent(adapter.getContentPage());
                break;
            case DROPDOWN_INFO_POSITION:
                showByPragma(PragmaType.TABLE_INFO);
                break;
            case DROPDOWN_FOREIGN_KEYS_POSITION:
                showByPragma(PragmaType.FOREIGN_KEY);
                break;
            case DROPDOWN_INDEXES_POSITION:
                showByPragma(PragmaType.INDEX_LIST);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onQuerySubmited(ArrayList<TableRowModel> conditionList) {
        this.conditionList = conditionList;
        List<TableRow> rows = adapter.getContentPage(conditionList);
        showContent(rows);
    }

    /**
     * Listen for the result of deleting the content of a table.
     */
    private class ClearTableReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ClearTableIntentService.isSuccess(intent)) {
                adapter.resetPage();
                showContent(adapter.getContentPage());
            }

        }

    }

    private ArrayList<String> getTableRowValues(int rowIndex) {
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);
        return getTableRowValues(tableRow);
    }

    private ArrayList<String> getTableRowValues(TableRow tableRow) {
        ArrayList<String> values = new ArrayList<>();

        if (tableRow == null) {
            return values;
        }

        for (int i = 0; i < tableRow.getChildCount(); i++) {
            TextView textView = (TextView) tableRow.getChildAt(i);
            values.add(textView.getText().toString());
        }

        return values;
    }

    private void showRecord(RecordScreenType screenType, ArrayList<String> columnValues) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dbinspector_container, RecordFragment.newInstance(screenType, databaseFile, tableName, columnNames, columnValues))
                .addToBackStack("Record")
                .commit();
        getFragmentManager().executePendingTransactions();
    }

}


