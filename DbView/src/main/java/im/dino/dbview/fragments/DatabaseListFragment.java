package im.dino.dbview.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import im.dino.dbview.R;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_database_list, null);
        mListView = (ListView) getView().findViewById(R.id.list_database);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                getActivity().databaseList());

        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), mAdapter.getItem(position), Toast.LENGTH_SHORT).show();


    }
}
