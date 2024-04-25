package com.skyinbrowser.MpageFrag;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.Function;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.CustomJavaFiles.NonScrollListView;
import com.skyinbrowser.R;
import com.skyinbrowser.Settings.NewsLanngChangeDialog;
import com.skyinbrowser.for_news.English.Adapter;
import com.skyinbrowser.for_news.English.MoreNewsActivity;
import com.skyinbrowser.for_news.English.Utils;
import com.skyinbrowser.for_news.English.api.ApiClient;
import com.skyinbrowser.for_news.English.modles.Article;
import com.skyinbrowser.for_news.English.modles.News;
import com.skyinbrowser.for_news.Hindi.MoreHindiNews;
import com.skyinbrowser.for_news.Hindi.RSSItem;
import com.skyinbrowser.for_news.Hindi.RSSParser;
import com.skyinbrowser.for_news.NewsDetailActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class NewsMpageFrag extends Fragment {

    private SharedPreferences preferences;
    private Context mContext;
    private FragmentToActivity mCallback;
    private View view;
    private TextView changeLanguage,moreNewsBtn,refreshNewsBtn;

    //for english news
    private ImageView newsretry_button;
    public static final String API_KEY = "26dad803c7e24553947fb576b96ee79d";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = Mpage.class.getSimpleName();
    private ShimmerFrameLayout shimmerLibrary;

    //for hindi news
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSParser rssParser = new RSSParser();
    List<RSSItem> rssItems = new ArrayList<>();
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_MAINTITLE="Maintitle";
    private NonScrollListView hindiListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.news_layout_mpage, container, false);
        mContext = view.getContext();

        moreNewsBtn=view.findViewById(R.id.moreNews);
        refreshNewsBtn=view.findViewById(R.id.refreshnews);
        shimmerLibrary = view.findViewById(R.id.shimmer_library);
        recyclerView = view.findViewById(R.id.recyclerView);
        newsretry_button = view.findViewById(R.id.newsretry_button);
        hindiListView=view.findViewById(R.id.hindiNewsList);
        changeLanguage=view.findViewById(R.id.changeLanguageMpage);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        BounceView.addAnimTo(changeLanguage);
        BounceView.addAnimTo(moreNewsBtn);
        BounceView.addAnimTo(refreshNewsBtn);

        hindiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView titletxt=view.findViewById(R.id.title);
                TextView link=view.findViewById(R.id.link);
                TextView souceText=view.findViewById(R.id.source);

                Intent intent=new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("title",titletxt.getText().toString());
                intent.putExtra("link",link.getText().toString());
                intent.putExtra("sourcetxt",souceText.getText().toString());
                startActivity(intent);
            }
        });

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                Fragment prev=getFragmentManager().findFragmentByTag("dialog");
                if (prev != null){
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment deleteActivity=new NewsLanngChangeDialog();
                deleteActivity.setTargetFragment(NewsMpageFrag.this,250);
                deleteActivity.show(fragmentTransaction,"dialog");
            }
        });

        SharedPreferences preferences=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
        if (preferences.contains("englishNews")){
            if (Function.isNetworkAvailable(mContext)) {
                LoadJson();
            } else {
                loadEnglishNewsData();
            }
        }else if (preferences.contains("hindiNews")){
            if (Function.isNetworkAvailable(mContext)) {
                new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/india.xml");
            } else {
                loadHindiNewsData();
            }
        }

        newsretry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout newsretry_layout = view.findViewById(R.id.newsretry_layout);
                if (Function.isNetworkAvailable(mContext)) {
                    SharedPreferences preferences=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
                    if (preferences.contains("englishNews")){
                        LoadJson();
                        newsretry_layout.setVisibility(View.GONE);
                    }else if (preferences.contains("hindiNews")){
                        new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/india.xml");
                        newsretry_layout.setVisibility(View.GONE);
                        rssItemList.clear();
                    }
                } else {
                    shimmerLibrary.setVisibility(View.GONE);
                    newsretry_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        moreNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
                if (preferences.contains("englishNews")){
                    Intent intent = new Intent(mContext, MoreNewsActivity.class);
                    startActivityForResult(intent,155);
                }else if (preferences.contains("hindiNews")){
                    Intent intent = new Intent(mContext, MoreHindiNews.class);
                    startActivityForResult(intent,155);
                }
            }
        });

        refreshNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
                if (preferences.contains("englishNews")){
                    articles.clear();
                    LoadJson();
                }else if (preferences.contains("hindiNews")){
                    rssItemList.clear();
                    new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/india.xml");
                }
            }
        });

        LocalBroadcastManager.getInstance(mContext).registerReceiver(hindiNews, new IntentFilter("hindiNewsActivate"));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(englishNews, new IntentFilter("englishNewsActivate"));

        return view;
    }

    private BroadcastReceiver hindiNews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            articles.clear();
            recyclerView.setVisibility(View.GONE);
            shimmerLibrary.setVisibility(View.VISIBLE);
            new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/india.xml");
        }
    };

    private BroadcastReceiver englishNews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rssItemList.clear();
            hindiListView.setVisibility(View.GONE);
            shimmerLibrary.setVisibility(View.VISIBLE);
            LoadJson();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==155){
            if (resultCode==RESULT_OK){
                String url=data.getStringExtra("newssUrl");
                sendData(url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    public void LoadJson() {

        topHeadlinesApi apiInterface = ApiClient.getApiClient().create(topHeadlinesApi.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;
        call = apiInterface.getNews(API_KEY);
        shimmerLibrary.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, mContext);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();

                    shimmerLibrary.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    saveEnglishNewsData();
                } else {
                    toastMessage("news error");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                LinearLayout newsretry_layout = view.findViewById(R.id.newsretry_layout);
                shimmerLibrary.setVisibility(View.GONE);
                newsretry_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private interface topHeadlinesApi {
        @GET("top-headlines?country=in")
        Call<News> getNews(
                @Query("apiKey") String apiKey
        );

        @GET("everything")
        Call<News> getNewsSearch(
                @Query("q") String keyword,
                @Query("language") String language,
                @Query("sortBy") String sortBy,
                @Query("apiKey") String apiKey
        );

    }

    private void initListener() {

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = articles.get(position);
                Intent intent=new Intent(mContext, NewsDetailActivity.class);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                //intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("englishNews","englishNews");
                startActivity(intent);
            }
        });
    }

    private class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            shimmerLibrary.setVisibility(View.VISIBLE);
            hindiListView.setVisibility(View.GONE);
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
                            mContext,
                            rssItemList, R.layout.list_row,
                            new String[]{ TAG_TITLE, TAG_PUB_DATE,TAG_DESRIPTION,TAG_LINK,TAG_MAINTITLE},
                            new int[]{ R.id.title, R.id.timeordate,R.id.desc,R.id.link,R.id.source});

                    // updating listview
                    hindiListView.setAdapter(adapter);

                    if (rssItemList == null){
                        LinearLayout newsretry_layout = view.findViewById(R.id.newsretry_layout);
                        shimmerLibrary.setVisibility(View.GONE);
                        newsretry_layout.setVisibility(View.VISIBLE);
                    }else {
                        
                    }
                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            shimmerLibrary.setVisibility(View.GONE);
            hindiListView.setVisibility(View.VISIBLE);
        }
    }

    private void saveEnglishNewsData() {
        SharedPreferences preferences = mContext.getSharedPreferences("save english news mpage", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(articles);
        editor.putString("englishNewsList", json);
        editor.apply();
    }

    private void saveHindiNewsData() {
        SharedPreferences preferences = mContext.getSharedPreferences("save hindi news mpage", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rssItemList);
        editor.putString("hindiNewsList", json);
        editor.apply();
    }

    private void loadEnglishNewsData() {
        SharedPreferences preferences = mContext.getSharedPreferences("save english news mpage", MODE_PRIVATE);
        Gson gson = new Gson();

        if (preferences.contains("englishNewsList")) {
            String json = preferences.getString("englishNewsList", null);
            Type type = new TypeToken<ArrayList<Article>>() {
            }.getType();
            articles = gson.fromJson(json, type);

            adapter = new Adapter(articles, mContext);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            shimmerLibrary.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initListener();
        } else {
            LoadJson();
        }
    }

    private void loadHindiNewsData() {
        SharedPreferences preferences = mContext.getSharedPreferences("save hindi news mpage", MODE_PRIVATE);
        Gson gson = new Gson();

        if (preferences.contains("hindiNewsList")) {
            String json = preferences.getString("hindiNewsList", null);
            Type type = new TypeToken<ArrayList<Article>>() {
            }.getType();
            rssItemList = gson.fromJson(json, type);

            ListAdapter adapter = new SimpleAdapter(
                    mContext,
                    rssItemList, R.layout.list_row,
                    new String[]{ TAG_TITLE, TAG_PUB_DATE,TAG_DESRIPTION},
                    new int[]{ R.id.title, R.id.source,R.id.desc});

            hindiListView.setAdapter(adapter);

            shimmerLibrary.setVisibility(View.GONE);
            hindiListView.setVisibility(View.VISIBLE);
        } else {
            new LoadRSSFeedItems().execute("https://zeenews.india.com/hindi/india.xml");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}