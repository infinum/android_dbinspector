package im.dino.dbinspector.fragments;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
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
import java.util.List;

import im.dino.dbinspector.adapters.TablePageAdapter;
import im.dino.dbview.R;

/**
 * Created by dino on 24/02/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TableFragment extends Fragment implements ActionBar.OnNavigationListener {

    private static final String KEY_DATABASE = "database_name";

    private static final String KEY_TABLE = "table_name";

    private static final String KEY_SHOWING_CONTENT = "showing_content";

    private static final String KEY_PAGE = "current_page";

    private File mDatabaseFile;

    private String mTableName;

    private TableLayout mTableLayout;

    private TablePageAdapter mAdapter;

    private View mNextButton;

    private View mPreviousButton;

    private TextView mCurrentPageText;

    private View mContentHeader;

    private ScrollView mScrollView;

    private HorizontalScrollView mHorizontalScrollView;

    private boolean mShowingContent = true;

    private int mCurrentPage;

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
            mDatabaseFile = (File) getArguments().getSerializable(KEY_DATABASE);
            mTableName = getArguments().getString(KEY_TABLE);
        }

        if (savedInstanceState != null) {
            mShowingContent = savedInstanceState.getBoolean(KEY_SHOWING_CONTENT, true);
            mCurrentPage = savedInstanceState.getInt(KEY_PAGE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dbinspector_fragment_table, container, false);

        mTableLayout = (TableLayout) view.findViewById(R.id.dbinspector_table_layout);
        mPreviousButton = view.findViewById(R.id.dbinspector_button_previous);
        mNextButton = view.findViewById(R.id.dbinspector_button_next);
        mCurrentPageText = (TextView) view.findViewById(R.id.dbinspector_text_current_page);
        mContentHeader = view.findViewById(R.id.dbinspector_layout_content_header);
        mScrollView = (ScrollView) view.findViewById(R.id.dbinspector_scrollview_table);
        mHorizontalScrollView = (HorizontalScrollView) view
            .findViewById(R.id.dbinspector_horizontal_scrollview_table);

        mPreviousButton.setOnClickListener(previousListener);
        mNextButton.setOnClickListener(nextListener);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ActionBar actionBar = getActivity().getActionBar();

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
                    getString(R.string.dbinspector_content),
                    getString(R.string.dbinspector_structure),
                }
            ),
            this
        );

        mAdapter = new TablePageAdapter(getActivity(), mDatabaseFile, mTableName, mCurrentPage);

        if (mShowingContent) {
            showContent();
        } else {
            showStructure();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dbinspector_fragment_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.dbinspector_action_settings
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.dbinspector_container, new TableSettingsFragment()).addToBackStack(null)
                .commit();
            getActivity().getFragmentManager().executePendingTransactions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        switch (itemPosition) {
            case 0:
                showContent();
                break;
            case 1:
                showStructure();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_SHOWING_CONTENT, mShowingContent);
        outState.putInt(KEY_PAGE, mCurrentPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    private void showContent() {

        mShowingContent = true;
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

        mShowingContent = false;
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
            mCurrentPage++;
            mAdapter.nextPage();
            showContent();

            mScrollView.scrollTo(0, 0);
            mHorizontalScrollView.scrollTo(0, 0);
        }
    };

    private View.OnClickListener previousListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mCurrentPage--;
            mAdapter.previousPage();
            showContent();

            mScrollView.scrollTo(0, 0);
            mHorizontalScrollView.scrollTo(0, 0);
        }
    };
}
