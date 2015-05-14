package im.dino.dbinspector.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import im.dino.dbinspector.R;
import im.dino.dbinspector.helpers.models.TableRowModel;
import im.dino.dbinspector.interfaces.DbInspectorListCommunicator;

/**
 * Created by Zeljko Plesac on 07/04/15.
 */
public class SearchQueryAdapter extends ArrayAdapter<TableRowModel> {

    private ArrayAdapter<CharSequence> sqlActionsAdapter;
    private DbInspectorListCommunicator listener;


    public SearchQueryAdapter(Context mContext, ArrayList<TableRowModel> items) {
        super(mContext, 0, items);

        sqlActionsAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.dbinspector_sql_actions, android.R.layout.simple_spinner_item);

        sqlActionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_search_dialog, parent, false);
            holder = new ViewHolder();
            holder.spAction = (Spinner) convertView.findViewById(R.id.dbinspector_sql_action_spinner);
            holder.tvName = (TextView) convertView.findViewById(R.id.dbinspector_field_name);
            holder.spCondition = (Spinner) convertView.findViewById(R.id.dbinspector_field_condition);
            holder.etValue = (EditText) convertView.findViewById(R.id.dbinspector_field_value);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.dbinspector_field_delete);
            convertView.setTag(holder);
        }

        if(position == 0){
            holder.spAction.setVisibility(View.INVISIBLE);
        }
        else{
            holder.spAction.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(getItem(position).getName());

        ArrayAdapter<CharSequence> sqlConditions;

        switch (getItem(position).getType()){
            case INTEGER:
                sqlConditions = ArrayAdapter.createFromResource(getContext(),
                        R.array.dbinspector_conditions_number, android.R.layout.simple_spinner_item);
                break;
            case REAL:
                sqlConditions = ArrayAdapter.createFromResource(getContext(),
                        R.array.dbinspector_conditions_number, android.R.layout.simple_spinner_item);
                break;
            default:
                sqlConditions = ArrayAdapter.createFromResource(getContext(),
                        R.array.dbinspector_conditions_text, android.R.layout.simple_spinner_item);
                break;
        }
        sqlConditions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        holder.spAction.setAdapter(sqlActionsAdapter);
        holder.spCondition.setAdapter(sqlConditions);

        if(listener != null){
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDelete(position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        EditText etValue;
        Spinner spAction;
        Spinner spCondition;
        ImageView ivDelete;
    }

    public void setListener(DbInspectorListCommunicator listener){
        this.listener = listener;
    }
}
