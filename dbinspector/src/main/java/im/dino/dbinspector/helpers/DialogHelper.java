package im.dino.dbinspector.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.dino.dbinspector.R;
import im.dino.dbinspector.adapters.TableColumnSelectionAdapter;
import im.dino.dbinspector.helpers.models.TableRowModel;

/**
 * Created by Zeljko Plesac on 07/04/15.
 */
public class DialogHelper {

    private static List<TableRowModel> allowedColumns;

    public static void showSearchDialog(final Activity activity , File databaseFile, String tableName) {

        final Dialog searchDialog = new Dialog(activity, R.style.dbinspector_dialog_theme);
        allowedColumns = DatabaseHelper.getAllowedColumnsFromTable(activity, databaseFile, tableName);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dbinspector_search_layout, null);
        Button button = (Button) dialogView.findViewById(R.id.dbinspector_add_new_selection);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog(activity, allowedColumns, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        allowedColumns.remove(which);
                    }
                });
            }
        });

        searchDialog.setContentView(dialogView);
        if(!activity.isFinishing()){
            searchDialog.show();
        }
    }

    private static void showSelectionDialog(Activity activity, List<TableRowModel> allowedColumns, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dbinspector_available_data_types);

        TableColumnSelectionAdapter adapter = new TableColumnSelectionAdapter(activity, new ArrayList<>(allowedColumns));

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dbinspector_table_columns, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.dbinspector_list);


        listView.setAdapter(adapter);

        builder.setView(dialogView);

        Dialog dialog = builder.create();

        if(!activity.isFinishing()){
            dialog.show();
        }
    }
}
