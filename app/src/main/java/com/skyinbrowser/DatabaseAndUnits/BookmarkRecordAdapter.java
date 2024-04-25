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

public class BookmarkRecordAdapter extends ArrayAdapter<Record> {
    private Context context;
    private int layoutResId;
    private List<Record> list;

    public BookmarkRecordAdapter(Context context, int layoutResId, List<Record> list) {
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
        BookmarkRecordAdapter.Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new BookmarkRecordAdapter.Holder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.time = (RelativeTimeTextView) view.findViewById(R.id.BookmarkTime);
            holder.url = (TextView) view.findViewById(R.id.desc);
            view.setTag(holder);
        } else {
            holder = (BookmarkRecordAdapter.Holder) view.getTag();
        }

        Record record = list.get(position);
        holder.title.setText(record.getTitle());
        holder.time.setReferenceTime(record.getTime());
        holder.url.setText(record.getURL());

        return view;
    }
}