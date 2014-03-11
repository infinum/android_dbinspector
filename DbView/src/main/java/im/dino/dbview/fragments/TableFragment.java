package im.dino.dbview.fragments;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import im.dino.dbview.R;
import im.dino.dbview.adapters.TablePageAdapter;

/**
 * Created by dino on 24/02/14.
 */
public class TableFragment extends Fragment implements ActionBar.OnNavigationListener {

    private static final String KEY_DATABASE = "database_name";

    private static final String KEY_TABLE = "table_name";

    private String mDatabaseName;

    private String mTableName;

    private TableLayout mTableLayout;

    private TablePageAdapter mAdapter;

    private View mNextButton;

    private View mPreviousButton;

    private TextView mCurrentPageText;

    private View mContentHeader;

    public static TableFragment newInstance(String databaseName, String tableName) {

        Bundle args = new Bundle();
        args.putString(KEY_DATABASE, databaseName);
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
            mDatabaseName = getArguments().getString(KEY_DATABASE);
            mTableName = getArguments().getString(KEY_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, null);
        mTableLayout = (TableLayout) view.findViewById(R.id.table_layout);
        mPreviousButton = view.findViewById(R.id.button_previous);
        mNextButton = view.findViewById(R.id.button_next);
        mCurrentPageText = (TextView) view.findViewById(R.id.text_current_page);
        mContentHeader = view.findViewById(R.id.layout_content_header);

        mPreviousButton.setOnClickListener(previousListener);
        mNextButton.setOnClickListener(nextListener);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof ActionBarActivity) {

            final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

            actionBar.setTitle(mTableName);
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Set up the action bar to show a dropdown list.
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            // Set up the dropdown list navigation in the action bar.
            actionBar.setListNavigationCallbacks(
                    // Specify a SpinnerAdapter to populate the dropdown list.
                    new ArrayAdapter<>(
                            actionBar.getThemedContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            new String[]{
                                    getString(R.string.structure),
                                    getString(R.string.content),
                            }
                    ),
                    this
            );

        }

        mAdapter = new TablePageAdapter(getActivity(), mDatabaseName, mTableName);

        showStructure();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new TableSettingsFragment()).addToBackStack(null).commit();
            getActivity().getFragmentManager().executePendingTransactions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {

        if (getActivity() != null && getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar()
                    .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }

        super.onDestroyView();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        switch (itemPosition) {
            case 0:
                showStructure();
                break;
            case 1:
                showContent();
                break;
            default:
                break;
        }

        return true;
    }

    private void showContent() {

        mTableLayout.removeAllViews();

        List<TableRow> rows = mAdapter.getContentPage();

        for (TableRow row : rows) {
            mTableLayout.addView(row);
        }

        mCurrentPageText.setText(mAdapter.getCurrentPage() + "/" + mAdapter.getPageCount());

        mContentHeader.setVisibility(View.VISIBLE);

        mNextButton.setEnabled(mAdapter.hasNext());
        mPreviousButton.setEnabled(mAdapter.hasPrevious());
    }

    private void showStructure() {

        mTableLayout.removeAllViews();

        List<TableRow> rows = mAdapter.getStructure();

        for (TableRow row : rows) {
            mTableLayout.addView(row);
        }

        mContentHeader.setVisibility(View.GONE);
    }

    private View.OnClickListener nextListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mAdapter.nextPage();
            showContent();
            // TODO scroll to top
        }
    };

    private View.OnClickListener previousListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mAdapter.previousPage();
            showContent();
            // TODO scroll to top
        }
    };
}
