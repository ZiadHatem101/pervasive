package com.example.pervasiveproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ActivityListAdapter extends BaseAdapter {

    private Context context;
    private List<LogActivityItem> itemList;
    private LayoutInflater inflater;

    // Constructor
    public ActivityListAdapter(Context context, List<LogActivityItem> itemList) {
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
            convertView = inflater.inflate(R.layout.activity_log_item, parent, false);
            holder = new ViewHolder();
            holder.timestampTextView = convertView.findViewById(R.id.timestampTextView);
            holder.messageTextView = convertView.findViewById(R.id.messageTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current LogActivityItem
        LogActivityItem item = itemList.get(position);

        // Set data for the current list item
        holder.timestampTextView.setText(formatTimestamp(item.getTimestamp()));
        holder.messageTextView.setText(item.getText());

        return convertView;
    }

    private String formatTimestamp(long timestamp) {
        // Use the DateTime format for both date and time
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);

        // Combine date and time using a custom format
        java.util.Date date = new java.util.Date(timestamp);
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        return formattedDate + " " + formattedTime;  // Concatenate date and time
    }

    // ViewHolder pattern for efficient recycling of views
    static class ViewHolder {
        TextView timestampTextView;
        TextView messageTextView;
    }
}
