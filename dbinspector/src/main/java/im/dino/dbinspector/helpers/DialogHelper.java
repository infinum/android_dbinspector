package im.dino.dbinspector.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.adapters.SearchQueryAdapter;
import im.dino.dbinspector.adapters.TableColumnsAdapter;
import im.dino.dbinspector.helpers.models.TableRowModel;
import im.dino.dbinspector.interfaces.DbInspectorDialogCommunicator;
import im.dino.dbinspector.interfaces.DbInspectorListCommunicator;

/**
 * Created by Zeljko Plesac on 07/04/15.
 */
public class DialogHelper {

    private static ArrayList<TableRowModel> conditionList;

    private DialogHelper() {
        // no instantiations
    }

    public static void showSearchDialog(final Activity activity, File databaseFile, String tableName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.dbinspector_dialog_theme));

        conditionList = new ArrayList<>();
        final List<TableRowModel> allowedColumns = DatabaseHelper.getAllowedColumnsFromTable(activity, databaseFile, tableName);
        final SearchQueryAdapter adapter = new SearchQueryAdapter(activity, conditionList);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dbinspector_search_layout, null);
        Button button = (Button) dialogView.findViewById(R.id.dbinspector_add_new_selection);
        ListView listView = (ListView) dialogView.findViewById(R.id.dbinspector_list_conditions);
        listView.setAdapter(adapter);

        adapter.setListener(new DbInspectorListCommunicator() {

            @Override
            public void onItemDelete(int position) {
                conditionList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setTitle(activity.getString(R.string.dbinspector_search_builder));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog(activity, allowedColumns, new DbInspectorDialogCommunicator() {
                    @Override
                    public void onItemClicked(int position) {
                        conditionList.add(allowedColumns.get(position));
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        });

        builder.setNegativeButton(activity.getString(R.string.dbinspector_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(activity.getString(R.string.dbinspector_action_search), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(dialogView);

        Dialog searchDialog = builder.create();

        if (!activity.isFinishing()) {
            searchDialog.show();
        }
    }

    private static void showSelectionDialog(Activity activity, List<TableRowModel> allowedColumns,
            final DbInspectorDialogCommunicator listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dbinspector_available_data_types);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dbinspector_table_columns, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.dbinspector_list);
        TableColumnsAdapter adapter = new TableColumnsAdapter(activity, new ArrayList<>(allowedColumns));
        listView.setAdapter(adapter);

        builder.setView(dialogView);
        final Dialog dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClicked(position);
                dialog.dismiss();
            }
        });

        if (!activity.isFinishing()) {
            dialog.show();
        }
    }
}
