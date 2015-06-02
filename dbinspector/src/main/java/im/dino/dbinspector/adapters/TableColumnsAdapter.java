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
 * Created by Zeljko Plesac on 10/05/15.
 */
public class TableColumnsAdapter extends ArrayAdapter<TableRowModel> {

    public TableColumnsAdapter(Context mContext, ArrayList<TableRowModel> items) {
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
            holder.name = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }

        holder.name.setText(getItem(position).getName());

        return convertView;
    }

    static class ViewHolder {

        TextView name;
    }
}

