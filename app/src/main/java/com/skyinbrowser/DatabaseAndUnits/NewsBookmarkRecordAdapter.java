package com.skyinbrowser.DatabaseAndUnits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyinbrowser.R;
import com.skyinbrowser.CustomJavaFiles.RelativeTimeTextView;

import java.util.List;

public class NewsBookmarkRecordAdapter extends ArrayAdapter<NewsBookmarkRecord> {
    private Context context;
    private int layoutResId;
    private List<NewsBookmarkRecord> list;

    public NewsBookmarkRecordAdapter(Context context, int layoutResId, List<NewsBookmarkRecord> list) {
        super(context, layoutResId, list);
        this.context = context;
        this.layoutResId = layoutResId;
        this.list = list;
    }

    private static class Holder {
        TextView title;
        RelativeTimeTextView time;
        TextView url;
        TextView source;
        TextView language;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new Holder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.time = (RelativeTimeTextView) view.findViewById(R.id.timeordate);
            holder.url = (TextView) view.findViewById(R.id.link);
            holder.source=(TextView) view.findViewById(R.id.source);
            holder.language=(TextView) view.findViewById(R.id.language);
            holder.source.setVisibility(View.VISIBLE);
            holder.imageView=(ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        NewsBookmarkRecord record = list.get(position);
        holder.title.setText(record.getTitle());
        holder.time.setReferenceTime(record.getTime());
        holder.url.setText(record.getURL());
        holder.source.setText(record.getSource());
        holder.language.setText(record.getLanguage());
        holder.imageView.setImageBitmap(decodeToBase64(record.getImage()));

        return view;
    }

    public static Bitmap decodeToBase64(String input){
        byte[] decodeByte= Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }
}