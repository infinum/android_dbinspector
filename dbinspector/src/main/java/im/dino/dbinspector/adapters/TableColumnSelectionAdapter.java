package im.dino.dbinspector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import im.dino.dbinspector.helpers.models.TableRowModel;

/**
 * Created by Zeljko Plesac on 07/04/15.
 */
public class TableColumnSelectionAdapter extends ArrayAdapter<TableRowModel>{

    public TableColumnSelectionAdapter(Context mContext, ArrayList<TableRowModel> items) {
        super(mContext, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }

        holder.text.setText(getItem(position).getName());

        return convertView;
    }

    static class ViewHolder {
         TextView text;
    }
}
