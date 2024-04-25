package com.skyinbrowser.for_news.Hindi.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skyinbrowser.CustomJavaFiles.Function;
import com.skyinbrowser.CustomJavaFiles.NonScrollListView;
import com.skyinbrowser.R;
import com.skyinbrowser.for_news.Hindi.RSSItem;
import com.skyinbrowser.for_news.Hindi.RSSParser;
import com.skyinbrowser.for_news.NewsDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScienceHin extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    private View view;
    private ShimmerFrameLayout shimmerlayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView scrollView;
    private NonScrollListView listView;
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSParser rssParser = new RSSParser();
    List<RSSItem> rssItems = new ArrayList<>();
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_MAINTITLE="Maintitle";

    public static ScienceHin getInstance(String title) {
        ScienceHin sf = new ScienceHin();
        return sf;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.hindi_news_fragment_recyclerview,container,false);
        context=view.getContext();

        swipeRefreshLayout = view.findViewById(R.id.hindi_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.gplus_color_1);

        shimmerlayout=view.findViewById(R.id.hindiMoreNewsShimmer);
        scrollView=view.findViewById(R.id.hindiScrollview);
        listView=view.findViewById(R.id.hindiRecyclerView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView titletxt=view.findViewById(R.id.title);
                TextView link=view.findViewById(R.id.link);
                TextView souceText=view.findViewById(R.id.source);

                Intent intent=new Intent(context, NewsDetailActivity.class);
                intent.putExtra("title",titletxt.getText().toString());
                intent.putExtra("link",link.getText().toString());
                intent.putExtra("sourcetxt",souceText.getText().toString());
                intent.putExtra("moreNews","moreNews");
                startActivity(intent);
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>oldScrollY){
                    //Toast.makeText(Mpage.this, "down", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent("toolbarGone");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }else {
                    //Toast.makeText(Mpage.this, "top", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent("toolbarVisible");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });

        if (Function.isNetworkAvailable(context)){
            new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/science.xml");
        }else {
            Toast.makeText(context, "Error no Internet Connection", Toast.LENGTH_SHORT).show();
        }

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);

        return view;
    }

    private class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            shimmerlayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
            for (RSSItem item : rssItems) {
                // creating new HashMap
                if (item.link.toString().equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                map.put(TAG_TITLE, item.title);
                map.put(TAG_LINK, item.link);
                map.put(TAG_PUB_DATE, item.pubdate); // If you want parse the date
                map.put(TAG_DESRIPTION,item.description);
                map.put(TAG_MAINTITLE,item.mainTitle);

                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            context,
                            rssItemList, R.layout.list_row,
                            new String[]{ TAG_TITLE, TAG_PUB_DATE,TAG_DESRIPTION,TAG_LINK,TAG_MAINTITLE},
                            new int[]{ R.id.title, R.id.timeordate,R.id.desc,R.id.link,R.id.source});

                    // updating listview
                    listView.setAdapter(adapter);

                    if (rssItemList == null){
                        LinearLayout newsretry_layout = view.findViewById(R.id.newsretry_layout);
                        shimmerlayout.setVisibility(View.GONE);
                        newsretry_layout.setVisibility(View.VISIBLE);
                    }
                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            shimmerlayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/science.xml");
            }
        });

    }

    @Override
    public void onRefresh() {
        rssItemList.clear();
        new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/science.xml");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rssItemList.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rssItemList.clear();
    }
}
