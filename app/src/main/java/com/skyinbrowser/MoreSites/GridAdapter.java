package com.skyinbrowser.MoreSites;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.skyinbrowser.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hari.bounceview.BounceView;

public class GridAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public GridAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListNewsViewHolder holder = null;
        if (convertView == null) {
            holder = new ListNewsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.my_sites_grid_item, parent, false);
            holder.siteIcon = (CircleImageView) convertView.findViewById(R.id.siteIcon);
            holder.siteName = (TextView) convertView.findViewById(R.id.siteName);
            holder.siteUrl = (TextView) convertView.findViewById(R.id.siteUrl);
            convertView.setTag(holder);
        } else {
            holder = (ListNewsViewHolder) convertView.getTag();
        }

        holder.siteIcon.setId(position);
        holder.siteName.setId(position);
        holder.siteUrl.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.siteName.setText(song.get("siteName"));
            holder.siteUrl.setText(song.get("siteUrl"));

            Picasso.get()
                    .load(song.get("iconUrl"))
                    .error(activity.getResources().getDrawable(R.drawable.error))
                    .into(holder.siteIcon);
        }catch(Exception e) {}

        return convertView;
    }
}

class ListNewsViewHolder {
    CircleImageView siteIcon;
    TextView siteName, siteUrl;
}