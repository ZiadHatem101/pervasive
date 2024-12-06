package com.example.pervasiveproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ActionsListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itemList;
    private LayoutInflater inflater;

    // Constructor
    public ActionsListAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder pattern for performance optimization
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.itemIcon = convertView.findViewById(R.id.itemIcon);
            holder.itemName = convertView.findViewById(R.id.itemName);
            holder.itemDescription = convertView.findViewById(R.id.itemDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set data for the current list item
        Item item = itemList.get(position);
        holder.itemIcon.setImageResource(item.getImageResourceId());
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());

        return convertView;
    }

    // ViewHolder pattern for efficient recycling of views
    static class ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemDescription;
    }
}
