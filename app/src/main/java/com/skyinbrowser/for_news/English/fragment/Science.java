package com.skyinbrowser.for_news.English.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyinbrowser.CustomJavaFiles.Function;
import com.skyinbrowser.R;
import com.skyinbrowser.for_news.English.Adapter;
import com.skyinbrowser.for_news.English.Utils;
import com.skyinbrowser.for_news.English.api.ApiClient;
import com.skyinbrowser.for_news.English.modles.Article;
import com.skyinbrowser.for_news.English.modles.News;
import com.skyinbrowser.for_news.NewsDetailActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.content.Context.MODE_PRIVATE;

public class Science extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    private Context context;
    public static final String API_KEY = "26dad803c7e24553947fb576b96ee79d";
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    private View view;
    private ShimmerFrameLayout shimmerlayout;
    boolean refresh =false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView scrollView;

    public static Science getInstance(String title){
        Science sf=new Science();
        return sf;
    }

    @Nullable
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_fragment_recyclerview,container,false);
        context=view.getContext();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.gplus_color_1);

        recyclerView = view.findViewById(R.id.recyclerView);
        shimmerlayout=view.findViewById(R.id.moreNewsShimmer);
        scrollView=view.findViewById(R.id.scrollview);

        if (GRID_LAYOUT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (Function.isNetworkAvailable(context)){
            onLoadingSwipeRefresh("");
        }else {
            loadData();
        }

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

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);

        return view;
    }


    private void loadData(){
        SharedPreferences preferences = getActivity().getSharedPreferences("save news", MODE_PRIVATE);
        Gson gson = new Gson();
        if (preferences.contains("scienceNews")){
            String json = preferences.getString("scienceNews", null);
            Type type = new TypeToken<ArrayList<Article>>() {}.getType();
            articles = gson.fromJson(json, type);

            adapter = new Adapter(articles, context);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            shimmerlayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }else {
            onLoadingSwipeRefresh("");
        }
    }

    public void LoadJson(final String keyword){

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);

        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        shimmerlayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        topHeadlinesApi apiInterface = ApiClient.getApiClient().create(topHeadlinesApi.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;

        if (keyword.length() > 0 ){
            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
        } else {
            call = apiInterface.getNews(API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, context);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    shimmerlayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    //saveData();
                    swipeRefreshLayout.setRefreshing(false);
                    initListener();

                } else {

                    shimmerlayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.no_result,
                            "No Result",
                            "Please Try Again!\n"+
                                    errorCode);

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerlayout.setVisibility(View.GONE);
                showErrorMessage(
                        R.drawable.oops,
                        "Oops..",
                        "Network failure, Please Try Again");
            }
        });

    }

    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Article article = articles.get(position);
                Intent intent=new Intent(context, NewsDetailActivity.class);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                //intent.putExtra("img", article.getUrlToImage());
                //intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("englishNews","englishNews");
                intent.putExtra("moreNews","moreNews");
                startActivity(intent);
            }
        });
    }

    private void onLoadingSwipeRefresh(final String keyword){

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );

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
                onLoadingSwipeRefresh("");
            }
        });

    }

    private void saveData(){
        SharedPreferences preferences=getActivity().getSharedPreferences("save news",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(articles);
        editor.putString("scienceNews",json);
        editor.apply();
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }


    private interface topHeadlinesApi {

        @GET("top-headlines?country=in&category=science")
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
}
