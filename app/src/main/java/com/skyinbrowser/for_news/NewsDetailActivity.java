package com.skyinbrowser.for_news;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.airbnb.lottie.LottieAnimationView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.skyinbrowser.Addons.AddonsActivity;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.DatabaseAndUnits.NewsBookmarkRecord;
import com.skyinbrowser.DatabaseAndUnits.NewsBookmarkRecordAdapter;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.DownloadManager.DownloadStarter;
import com.skyinbrowser.CustomJavaFiles.ProgressBarAnimation;
import com.skyinbrowser.MoreSites.more_sites_popup;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.PermissionDenyDialog;
import com.skyinbrowser.R;
import com.skyinbrowser.CustomJavaFiles.ScondaryPrgressBar;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import im.delight.android.webview.AdvancedWebView;

import static android.view.View.GONE;

public class NewsDetailActivity extends AppCompatActivity implements FragmentToActivity {

    private ImageView contentImg,srcImg,close;
    private TextView titleTxt,sourceTxt,newsTxt;
    private String data="";
    private EditText editText;
    private NestedScrollView scrollView;
    private CardView toolbar,newsTitleBar,newsMenuPopupBg,newsMenuPopupCard;
    private View newsTitleShadow;
    private AdvancedWebView webView;
    private ProgressBar progressBar,bigImageProgress;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout swipeUpAnimGone;
    private MaterialRippleLayout openInBrowser,share,bookmark,myBookmarks,menuBtn,adBlockRipple;
    private SwitchButton adBlockSwitch;
    private LottieAnimationView lottieAnimationView;
    Random random = new Random();
    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STR_LENGTH = 12;
    private KProgressHUD kProgressHUD;
    private Intent intent;

    NewsBookmarkRecordAdapter recordAdapter;
    List<NewsBookmarkRecord> recordList;
    int location;
    List<NewsBookmarkRecord> list;

    private RecordAction action;

    private static final int STORAGE_PERMISSION_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentNewsDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentNewsAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.newsBgColor));
                window.setStatusBarColor(getResources().getColor(R.color.newsBgColor));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.TranslucentNewsDarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.black));
                        window.setStatusBarColor(getResources().getColor(android.R.color.black));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentNewsAppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.newsBgColor));
                        window.setStatusBarColor(getResources().getColor(R.color.newsBgColor));
                    }
                    break;
            }
        }
        setContentView(R.layout.news_ddetail_activity);

        CardView animVisible=findViewById(R.id.enterviewHindiNews);
        Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        action = new RecordAction(NewsDetailActivity.this);
        action.open(true);

        contentImg=findViewById(R.id.contentImage);
        srcImg=findViewById(R.id.sourceImage);
        sourceTxt=findViewById(R.id.sourceTitle);
        titleTxt=findViewById(R.id.newsTitle);
        newsTxt=findViewById(R.id.newsDetail);
        editText=findViewById(R.id.newsUrl);
        scrollView=findViewById(R.id.newsDetailScroll);
        toolbar=findViewById(R.id.newsToolbar);
        close=findViewById(R.id.hindiNewsBack);
        newsTitleBar=findViewById(R.id.newsTitleBar);
        newsTitleShadow=findViewById(R.id.newsTitleShadow);
        webView=findViewById(R.id.newsWebView);
        progressBar=findViewById(R.id.newsProgressbar);
        swipeRefreshLayout=findViewById(R.id.newsWebViewSwipeRefresh);
        bigImageProgress=findViewById(R.id.newsBigImageProgress);
        menuBtn=findViewById(R.id.newsMenuButton);
        swipeUpAnimGone=findViewById(R.id.swipeUpAnimGoneNews);
        openInBrowser=findViewById(R.id.openInBrowserNews);
        share=findViewById(R.id.shareNews);
        bookmark=findViewById(R.id.bookmarkNews);
        lottieAnimationView=findViewById(R.id.lottieAnimationViewNews);
        newsMenuPopupBg=findViewById(R.id.newsMenuPopup);
        newsMenuPopupCard=findViewById(R.id.mainPopupActionViewNews);
        myBookmarks=findViewById(R.id.myNewsBookmarks);
        adBlockRipple=findViewById(R.id.newsDetailAdBlock);
        adBlockSwitch=findViewById(R.id.adBlockNewsDetailSwitch);

        adservers();

        if (appptheme.contains("DarkOn")){
            progressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
            bigImageProgress.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.MULTIPLY);
        }
        if (appptheme.contains("lightOn")){
            progressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
            bigImageProgress.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
                    bigImageProgress.getIndeterminateDrawable().setColorFilter(
                            getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.MULTIPLY);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                    bigImageProgress.getIndeterminateDrawable().setColorFilter(
                            getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
                    break;
            }
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        close.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(NewsDetailActivity.this, "Close🔙", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        newsMenuPopupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>oldScrollY){
                    //Toast.makeText(Mpage.this, "down", Toast.LENGTH_SHORT).show();
                    if (toolbar.getVisibility()==View.VISIBLE){
                        Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.slide_up_search);
                        toolbar.setAnimation(animation);
                        animation.start();
                        toolbar.setVisibility(View.GONE);
                    }
                }else {
                    //Toast.makeText(Mpage.this, "top", Toast.LENGTH_SHORT).show();
                    if (toolbar.getVisibility()==View.GONE){
                        Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.slide_down_search);
                        toolbar.setAnimation(animation);
                        animation.start();
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        intent=this.getIntent();

        if (intent.hasExtra("BookmarkedNews")){
            bookmark.setVisibility(GONE);
            menuBtn.setVisibility(GONE);
            if (intent.hasExtra("englishNews")){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                newsTitleShadow.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams layoutParams=(ViewGroup.MarginLayoutParams) newsTitleBar.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                newsTitleBar.requestLayout();

                String file=intent.getStringExtra("desc");
                String url=intent.getStringExtra("url");
                String filePath= String.valueOf(getFilePath(file));
                if (isNetworkAvailable()){
                    initWebView(url);
                }else {
                    initWebView("file:///"+filePath);
                }
            }else {
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                String source=intent.getStringExtra("sourcetxt");
                String title=intent.getStringExtra("title");
                int locati=intent.getIntExtra("location",0);
                Picasso.get().load("https://api.faviconkit.com/zeenews.india.com/144").into(srcImg);

                list = action.listNewsBookmarks();
                recordAdapter = new NewsBookmarkRecordAdapter(NewsDetailActivity.this, R.layout.new_hiatory_list_layout, list);
                recordList=list;
                location=locati;
                final NewsBookmarkRecord record = recordList.get(location);
                String image=record.getImage();
                String desc=record.getDescription();

                titleTxt.setText(title);
                sourceTxt.setText(source);
                newsTxt.setText(desc);
                contentImg.setImageBitmap(decodeToBase64(image));
            }
        }else {
            if (intent.hasExtra("englishNews")){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                newsTitleShadow.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams layoutParams=(ViewGroup.MarginLayoutParams) newsTitleBar.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                newsTitleBar.requestLayout();
                String link=intent.getStringExtra("url");
                String imgUrl=intent.getStringExtra("img");
                Picasso.get().load(imgUrl).into(contentImg);
                initWebView(link);
            }else {
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                String src=intent.getStringExtra("sourcetxt");
                sourceTxt.setText(src.substring(0,14));
                titleTxt.setText(intent.getStringExtra("title"));
                String link=intent.getStringExtra("link");
                new LoadNews().execute(link.trim());
                new LoadbigImage().execute(link.trim());
                Picasso.get().load("https://api.faviconkit.com/zeenews.india.com/144").into(srcImg);
            }
        }

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsMenuPopupBg.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(GONE);
                swipeUpAnimGone.setVisibility(View.VISIBLE);
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_enter);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                newsMenuPopupCard.setVisibility(View.VISIBLE);
            }
        });

        menuBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(NewsDetailActivity.this, "More", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        myBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kProgressHUD=KProgressHUD.create(NewsDetailActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                        Intent intent1=new Intent(NewsDetailActivity.this,NewsBookmarks.class);
                        startActivity(intent1);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        newsMenuPopupBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lottieAnimationView.isAnimating()){
                    Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
                    animation.setDuration(100);
                    newsMenuPopupCard.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            newsMenuPopupCard.setVisibility(GONE);
                            newsMenuPopupBg.setVisibility(GONE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        openInBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slide_up_fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_out);
                slide_up_fade_out.setDuration(400);
                swipeUpAnimGone.startAnimation(slide_up_fade_out);
                slide_up_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        swipeUpAnimGone.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                lottieAnimationView.setAnimation(R.raw.upload);

                Animation slide_up_fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_in);
                slide_up_fade_in.setDuration(600);
                lottieAnimationView.startAnimation(slide_up_fade_in);
                slide_up_fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setMinAndMaxFrame(200,360);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) { }
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                lottieAnimationView.setVisibility(View.INVISIBLE);
                                lottieAnimationView.clearAnimation();
                                newsMenuPopupCard.setVisibility(GONE);
                                newsMenuPopupBg.setVisibility(GONE);

                                String link="";
                                if (intent.hasExtra("url")){
                                    link=intent.getStringExtra("url");
                                }else {
                                    link=intent.getStringExtra("link");
                                }
                                Intent intent1=new Intent("scanResult").putExtra("scanResult",link);
                                LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent1);
                                if (intent.hasExtra("moreNews")){
                                    Intent intent2=new Intent("finishMoreNews");
                                    LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent2);
                                }
                                finish();
                            }
                            @Override
                            public void onAnimationCancel(Animator animator) { }
                            @Override
                            public void onAnimationRepeat(Animator animator) { }
                        });
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slide_up_fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_out);
                slide_up_fade_out.setDuration(400);
                swipeUpAnimGone.startAnimation(slide_up_fade_out);
                slide_up_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        swipeUpAnimGone.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                lottieAnimationView.setAnimation(R.raw.share);

                Animation slide_up_fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_in);
                slide_up_fade_in.setDuration(600);
                lottieAnimationView.startAnimation(slide_up_fade_in);
                slide_up_fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) { }
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                lottieAnimationView.setVisibility(View.INVISIBLE);
                                lottieAnimationView.clearAnimation();
                                newsMenuPopupCard.setVisibility(GONE);
                                newsMenuPopupBg.setVisibility(GONE);

                                if (intent.hasExtra("englishNews")){
                                    String title=intent.getStringExtra("title");
                                    String mUrl=intent.getStringExtra("url");
                                    try{
                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setType("text/plan");
                                        String body = title + "\n" + mUrl + "\n" + "\n" + "Shared from the SK!YN Browser" + "\n";
                                        i.putExtra(Intent.EXTRA_TEXT, body);
                                        startActivity(Intent.createChooser(i, "On the way you are"));
                                    }catch (Exception e){
                                        Toast.makeText(NewsDetailActivity.this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    String title=intent.getStringExtra("title");
                                    String mUrl=intent.getStringExtra("link");
                                    try{
                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setType("text/plan");
                                        String body = title + "\n" +"\n" + mUrl + "\n" + "\n" + "Shared from the SK!YN Browser" + "\n";
                                        i.putExtra(Intent.EXTRA_TEXT, body);
                                        startActivity(Intent.createChooser(i, "On the way you are"));
                                    }catch (Exception e){
                                        Toast.makeText(NewsDetailActivity.this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onAnimationCancel(Animator animator) { }
                            @Override
                            public void onAnimationRepeat(Animator animator) { }
                        });
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsMenuPopupBg.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(GONE);
                swipeUpAnimGone.setVisibility(GONE);
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_enter);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                newsMenuPopupCard.setVisibility(View.VISIBLE);

                lottieAnimationView.setAnimation(R.raw.bookmark);

                lottieAnimationView.playAnimation();
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) { }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        int permissionWrite= ContextCompat.checkSelfPermission(NewsDetailActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        int permissionRead=ContextCompat.checkSelfPermission(NewsDetailActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (permissionWrite != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                            }else {
                                kProgressHUD=KProgressHUD.create(NewsDetailActivity.this)
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                        .setLabel("Please wait")
                                        .setCancellable(false)
                                        .setAnimationSpeed(2)
                                        .setDimAmount(0.5f)
                                        .show();
                                contentImg.setDrawingCacheEnabled(true);
                                Bitmap bitmap=contentImg.getDrawingCache();
                                String imageArray=encodeTobase64(bitmap);
                                if (intent.hasExtra("englishNews")){
                                    String title=intent.getStringExtra("title");
                                    String mUrl=intent.getStringExtra("url");
                                    String language=intent.getStringExtra("englishNews");
                                    String source=intent.getStringExtra("source");
                                    Ion.with(getApplicationContext()).
                                            load(mUrl)
                                            .asString()
                                            .setCallback(new FutureCallback<String>() {
                                                @Override
                                                public void onCompleted(Exception e, String result) {
                                                    String fileName=getRandomString()+".html";
                                                    generateNoteOnSD(NewsDetailActivity.this,fileName,result);
                                                    action.addNewsBookmark(new NewsBookmarkRecord(title, mUrl,source,language,imageArray,fileName, System.currentTimeMillis()));
                                                }
                                            });
                                }else {
                                    String title=intent.getStringExtra("title");
                                    String mUrl=intent.getStringExtra("link");
                                    String source=intent.getStringExtra("sourcetxt");
                                    String desc=newsTxt.getText().toString();
                                    action.addNewsBookmark(new NewsBookmarkRecord(title, mUrl,source,"Hindi",imageArray,desc, System.currentTimeMillis()));
                                    lottieAnimationView.setVisibility(View.INVISIBLE);
                                    lottieAnimationView.clearAnimation();
                                    newsMenuPopupCard.setVisibility(GONE);
                                    newsMenuPopupBg.setVisibility(GONE);
                                    kProgressHUD.dismiss();
                                }
                            }
                        }
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) { }
                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
        if (sharedPreferences.contains("adBlockOn")){
            adBlockSwitch.setChecked(true);
        }else {
            adBlockSwitch.setChecked(false);
        }
        adBlockSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                        if (isChecked){
                            SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("adBlockOn","adBlockOn");
                            editor.apply();
                            Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOn");
                            LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent);
                            webView.setWebViewClient(new AdBlockWebViewClient());
                            webView.reload();
                        }else {
                            SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.remove("adBlockOn");
                            editor.apply();
                            Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOff");
                            LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent);
                            webView.setWebViewClient(new SampWebViewClient());
                            webView.reload();
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        adBlockRipple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                        if (adBlockSwitch.isChecked()) {
                            adBlockSwitch.setChecked(false);
                            SharedPreferences sharedPreferences = getSharedPreferences("webViewSettings", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("adBlockOn");
                            editor.apply();
                            Intent intent = new Intent("adBlock").putExtra("adBlockStatus", "adBlockOff");
                            LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent);
                            webView.setWebViewClient(new SampWebViewClient());
                            webView.reload();
                        } else {
                            adBlockSwitch.setChecked(true);
                            SharedPreferences sharedPreferences = getSharedPreferences("webViewSettings", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("adBlockOn", "adBlockOn");
                            editor.apply();
                            Intent intent = new Intent("adBlock").putExtra("adBlockStatus", "adBlockOn");
                            LocalBroadcastManager.getInstance(NewsDetailActivity.this).sendBroadcast(intent);
                            webView.setWebViewClient(new AdBlockWebViewClient());
                            webView.reload();
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

            }
        });

        bookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(NewsDetailActivity.this, "Bookmark this", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(dismissProgress,new IntentFilter("dismiss Progress"));
    }

    private BroadcastReceiver dismissProgress=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (kProgressHUD==null){ }else {
                kProgressHUD.dismiss();
            }
        }
    };

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input){
        byte[] decodeByte= Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            FileWriter writer = new FileWriter(getFilePath(sFileName));
            writer.append(sBody);
            writer.flush();
            writer.close();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.clearAnimation();
            newsMenuPopupCard.setVisibility(GONE);
            newsMenuPopupBg.setVisibility(GONE);
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            if (kProgressHUD==null){ }else {
                kProgressHUD.dismiss();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getFilePath(String filename1) {
        File dirPath=new File(Environment.getExternalStorageDirectory()+"/SKY!N Browser");
        if (!dirPath.exists()){
            dirPath.mkdir();
        }
        File Saved_Pages=new File(dirPath,"/.BookMarked News");
        if (!Saved_Pages.exists()){
            Saved_Pages.mkdir();
        }
        return (new File(Saved_Pages,filename1));
    }

    public String getRandomString(){
        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STR_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = _CHAR.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    private int getRandomNumber() {
        int randomInt = 0;
        randomInt = random.nextInt(_CHAR.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    kProgressHUD=KProgressHUD.create(NewsDetailActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    contentImg.setDrawingCacheEnabled(true);
                    Bitmap bitmap=contentImg.getDrawingCache();
                    String imageArray=encodeTobase64(bitmap);
                    if (intent.hasExtra("englishNews")){
                        String title=intent.getStringExtra("title");
                        String mUrl=intent.getStringExtra("url");
                        String language=intent.getStringExtra("englishNews");
                        String source=intent.getStringExtra("source");
                        Ion.with(getApplicationContext()).
                                load(mUrl)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        String fileName=getRandomString()+".html";
                                        generateNoteOnSD(NewsDetailActivity.this,fileName,result);
                                        action.addNewsBookmark(new NewsBookmarkRecord(title, mUrl,source,language,imageArray,fileName, System.currentTimeMillis()));
                                    }
                                });
                    }else {
                        String title=intent.getStringExtra("title");
                        String mUrl=intent.getStringExtra("link");
                        String source=intent.getStringExtra("sourcetxt");
                        String desc=newsTxt.getText().toString();
                        action.addNewsBookmark(new NewsBookmarkRecord(title, mUrl,source,"Hindi",imageArray,desc, System.currentTimeMillis()));
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        lottieAnimationView.clearAnimation();
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                        kProgressHUD.dismiss();
                    }
                }else {
                    Bundle modify_intent=new Bundle();
                    modify_intent.putString("storageMsg", "storageMsg");

                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    Fragment prev=getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null){
                        fragmentTransaction.remove(prev);
                    }
                    fragmentTransaction.addToBackStack(null);

                    DialogFragment deleteActivity=new PermissionDenyDialog();
                    deleteActivity.setArguments(modify_intent);
                    deleteActivity.show(fragmentTransaction,"dialog");
                }
        }
    }

    @Override
    public void communicate(String comm) {
        if (comm.equals("storagePermission")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }else if (comm.equals("dismiss")){
            if (!lottieAnimationView.isAnimating()){
                Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private void initWebView(String url){
        SharedPreferences webSettings=getSharedPreferences("webViewSettings",MODE_PRIVATE);
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);
        registerForContextMenu(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setEnableSmoothTransition(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        SharedPreferences.Editor editor=webSettings.edit();
        int fixedFont=webView.getSettings().getDefaultFixedFontSize();
        int fontSize=webView.getSettings().getDefaultFontSize();
        editor.putInt("defaultfontSize11",fixedFont);
        editor.putInt("defaultfontSize22",fontSize);
        editor.apply();
        if (webSettings.contains("textSize")){
            int points=webSettings.getInt("textSize",0);
            webView.getSettings().setDefaultFontSize(points);
            webView.getSettings().setDefaultFixedFontSize(points);
        }else {
            int fixedFon=webSettings.getInt("defaultfontSize11",0);
            int fontSiz=webSettings.getInt("defaultfontSize22",0);
            webView.getSettings().setDefaultFontSize(fontSiz);
            webView.getSettings().setDefaultFixedFontSize(fixedFon);
        }
        if (webSettings.contains("swipeRefOff")){
            swipeRefreshLayout.setEnabled(false);
        }else {
            swipeRefreshLayout.setEnabled(true);
        }
        if (webSettings.contains("mainZoomOff")){
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().supportZoom();
            webView.getSettings().getBuiltInZoomControls();
            webView.getSettings().setBuiltInZoomControls(false);
        }else {
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().supportZoom();
            webView.getSettings().getBuiltInZoomControls();
            webView.getSettings().setBuiltInZoomControls(true);
        }
        if (webSettings.contains("zoomControl")){
            webView.getSettings().setDisplayZoomControls(true);
        }else {
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (webSettings.contains("ultraFastModeON")){
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }else {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        swipeRefreshLayout.setRefreshing(false);
        if (webSettings.contains("adBlockOn")){
            webView.setWebViewClient(new AdBlockWebViewClient());
        }else {
            webView.setWebViewClient(new SampWebViewClient());
        }
        webView.setWebChromeClient(new MyChrome());

        if (appptheme.contains("DarkOn")){
            if (webSettings.contains("applydarkweb")){
                Paint paint = new Paint();
                final float[] NEGETIVE_COLOR ={
                        -1.0f,0,0,0,255, //Red
                        0,-1.0f,0,0,255, //Green
                        0,0,-1.0f,0,255, //Blue
                        0,0,0,1.0f,0     //Alpha
                };
                ColorMatrix matrix=new ColorMatrix();
                matrix.set(NEGETIVE_COLOR);
                ColorMatrix gcm=new ColorMatrix();
                gcm.setSaturation(0);
                ColorMatrix concat=new ColorMatrix();
                concat.setConcat(matrix,gcm);
                ColorMatrixColorFilter filter=new ColorMatrixColorFilter(concat);
                paint.setColorFilter(filter);
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON);
                }else {
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                }
            }else {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                }else {
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }
            }
        }
        if (appptheme.contains("lightOn")){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(webView.getSettings(),
                        WebSettingsCompat.FORCE_DARK_OFF);
            }else {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
            }
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    if (webSettings.contains("applydarkweb")){
                        Paint paint = new Paint();
                        final float[] NEGETIVE_COLOR ={
                                -1.0f,0,0,0,255, //Red
                                0,-1.0f,0,0,255, //Green
                                0,0,-1.0f,0,255, //Blue
                                0,0,0,1.0f,0     //Alpha
                        };
                        ColorMatrix matrix=new ColorMatrix();
                        matrix.set(NEGETIVE_COLOR);
                        ColorMatrix gcm=new ColorMatrix();
                        gcm.setSaturation(0);
                        ColorMatrix concat=new ColorMatrix();
                        concat.setConcat(matrix,gcm);
                        ColorMatrixColorFilter filter=new ColorMatrixColorFilter(concat);
                        paint.setColorFilter(filter);

                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(webView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_ON);
                        }else {
                            webView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                        }
                    }else {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(webView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_OFF);
                        }else {
                            webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                        }
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                        WebSettingsCompat.setForceDark(webView.getSettings(),
                                WebSettingsCompat.FORCE_DARK_OFF);
                    }else {
                        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                    break;
            }
        }

        SharedPreferences preferences1=getSharedPreferences("desktopMode",MODE_PRIVATE);
        if (preferences1.contains("modeOn")){
            String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
            webView.getSettings().setUserAgentString(newUA);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url2, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String filename2 = URLUtil.guessFileName(url2, contentDisposition, mimetype);

                double length=1024*1024;
                double l1=contentLength/length;
                String totallength=new DecimalFormat("##.##").format(l1)+" MB";
                if (l1>1024){
                    l1=l1/1024;
                    totallength= new DecimalFormat("##.##").format(totallength)+" GB";
                }

                Intent intent = new Intent(NewsDetailActivity.this, DownloadStarter.class);
                intent.putExtra("fileurl", url2);
                intent.putExtra("filename", filename2);
                intent.putExtra("fileSize",totallength);

                startActivity(intent);
            }
        });

        webView.loadUrl(url);
    }

    private class LoadNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            final StringBuilder builder=new StringBuilder();
            String rss_url = args[0];
            try {
                Document document= Jsoup.connect(rss_url).get();
                Elements links=document.select("div");

                for (Element row: links.select("p")){
                    builder.append(row.text()).append("\n \n");
                }
            }catch (IOException e){
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    data=builder.toString();
                    if (data.contains("ये भी देखें") || data.contains("ये भी पढ़ें") || data.contains("ये भी देखें...")){
                        if (data.contains("ये भी देखें")){
                            int lastIndex=data.indexOf("ये भी देखें");
                            newsTxt.setText(data.substring(45,lastIndex));
                        }

                        if (data.contains("ये भी पढ़ें")){
                            int lastIndex=data.indexOf("ये भी पढ़ें");
                            newsTxt.setText(data.substring(45,lastIndex));
                        }

                        if (data.contains("ये भी देखें...")){
                            int lastIndex=data.indexOf("ये भी देखें...");
                            newsTxt.setText(data.substring(45,lastIndex));
                        }
                    }else {
                        newsTxt.setText(data.substring(45,data.length() - 109));
                    }
                }
            });
            return null;
        }

        protected void onPostExecute(String args) { }
    }

    private class LoadbigImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            final StringBuilder builder=new StringBuilder();
            String rss_url = args[0];
            try {
                Document document= Jsoup.connect(rss_url).get();
                Elements links=document.select("meta");

                for (Element row: document.select("meta")){
                    //builder.append(row.text()).append("\n \n");
                    builder.append(row.getElementsByAttributeValue("property","og:image").append("\n"));
                }
            }catch (IOException e){
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    data=builder.toString();
                    editText.setText(data.substring(35,data.length() - 11));
                    Picasso.get().load(editText.getText().toString()).into(contentImg);
                }
            });
            return null;
        }

        protected void onPostExecute(String args) { }
    }

    private void back(){
        CardView animVisible=findViewById(R.id.enterviewHindiNews);
        Animation animation= AnimationUtils.loadAnimation(NewsDetailActivity.this,R.anim.frag_exit);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.GONE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState ) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    class AdBlockWebViewClient extends WebViewClient {
        AdBlockWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(0);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.startsWith("http")) {
                Uri parsedUri = Uri.parse(url);
                PackageManager packageManager = getPackageManager();
                Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
                if (browseIntent.resolveActivity(packageManager) != null) {
                    startActivity(browseIntent);
                }
                // if no activity found, try to parse intent://
                else {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        //try to find fallback url
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            view.loadUrl(fallbackUrl);
                        }
                        //invite to install
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                                Uri.parse("market://details?id=" + intent.getPackage()));
                        if (marketIntent.resolveActivity(packageManager) != null) {
                            startActivity(marketIntent);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetailActivity.this);
            String message = "SSL certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate is untrusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate does not match the host.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Continue anyway?";

            builder.setTitle("SSL certificate error");
            builder.setMessage(message);
            builder.setPositiveButton("Proceed anyway", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
            String kk5 = String.valueOf(adservers);
            if (kk5.contains(":::::"+request.getUrl().getHost())) {
                return new WebResourceResponse("text/plain", "utf-8", EMPTY);
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

    class SampWebViewClient extends WebViewClient {
        SampWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(0);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.startsWith("http")) {
                Uri parsedUri = Uri.parse(url);
                PackageManager packageManager = getPackageManager();
                Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
                if (browseIntent.resolveActivity(packageManager) != null) {
                    startActivity(browseIntent);
                }
                // if no activity found, try to parse intent://
                else {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        //try to find fallback url
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            view.loadUrl(fallbackUrl);
                        }
                        //invite to install
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                                Uri.parse("market://details?id=" + intent.getPackage()));
                        if (marketIntent.resolveActivity(packageManager) != null) {
                            startActivity(marketIntent);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetailActivity.this);
            String message = "SSL certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate is untrusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate does not match the host.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Continue anyway?";

            builder.setTitle("SSL certificate error");
            builder.setMessage(message);
            builder.setPositiveButton("Proceed anyway", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    StringBuilder adservers;
    private void adservers(){
        String strLine2="";
        adservers = new StringBuilder();

        InputStream fis2 = getResources().openRawResource(R.raw.adblockserverlist);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
        if(fis2 != null) {
            try {
                while ((strLine2 = br2.readLine()) != null) {
                    adservers.append(strLine2);
                    adservers.append("\n");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyChrome extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(progressBar, 1000);
            mProgressAnimation.setProgress(65);

            ScondaryPrgressBar scondaryPrgressBar = new ScondaryPrgressBar(progressBar, 3000);
            scondaryPrgressBar.setProgress(100);

            if (progress > 65){
                mProgressAnimation.setProgress(progress);
            }

            if (progress < 100 && progressBar.getVisibility() == View.INVISIBLE) {
                //refresh_button.setVisibility(View.GONE);
                //cancel_button_toolbar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (progressBar.getSecondaryProgress()==100){
                    scondaryPrgressBar.setProgress(0);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() { runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scondaryPrgressBar.setProgress(100);
                            }
                        }); }}, 2000
                    );
                }
            }
            if (progress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setProgress(0);
                swipeRefreshLayout.setRefreshing(false);
                if (progressBar.getSecondaryProgress()==100){
                    scondaryPrgressBar.setProgress(0);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() { runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scondaryPrgressBar.setProgress(100);
                            }
                        }); }}, 2000
                    );
                }
            }
            super.onProgressChanged(view, progress);
        }
    }

    private void menuGone(){

    }
}
