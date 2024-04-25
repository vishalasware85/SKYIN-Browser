package com.skyinbrowser.DatabaseAndUnits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skyinbrowser.R;
import com.skyinbrowser.CustomJavaFiles.RelativeTimeTextView;

import java.util.List;

public class HistoryRecordAdapter extends ArrayAdapter<Record> {
    private Context context;
    private int layoutResId;
    private List<Record> list;

    public HistoryRecordAdapter(Context context, int layoutResId, List<Record> list) {
        super(context, layoutResId, list);
        this.context = context;
        this.layoutResId = layoutResId;
        this.list = list;
    }

    private static class Holder {
        TextView title;
        RelativeTimeTextView time;
        TextView url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new Holder();
            holder.title = (TextView) view.findViewById(R.id.textHistoryTitle);
            holder.time = (RelativeTimeTextView) view.findViewById(R.id.historyTime);
            holder.url = (TextView) view.findViewById(R.id.textHistoryurl);
            holder.title.setMaxLines(1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Record record = list.get(position);
        holder.title.setText(record.getTitle());
        holder.time.setReferenceTime(record.getTime());
        holder.url.setText(record.getURL());

        return view;
    }
}