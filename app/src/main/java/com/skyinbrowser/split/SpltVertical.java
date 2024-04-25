package com.skyinbrowser.split;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.DownloadManager.DownloadStarter;
import com.skyinbrowser.CustomJavaFiles.ProgressBarAnimation;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;
import com.skyinbrowser.CustomJavaFiles.ScondaryPrgressBar;
import com.skyinbrowser.for_search.search_bar_place;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import im.delight.android.webview.AdvancedWebView;

import static android.view.View.GONE;

public class SpltVertical extends AppCompatActivity {

    ImageButton splitmodeMoewActioons,splithidemoreactions;
    LinearLayout splitmodeactionview,orientyationchanginglayout;
    Button btnchangeorientation;
    public static Activity splitActivity;

    //for upper
    private AdvancedWebView upperWebView;
    RelativeLayout visibilityUpperWeb;
    LinearLayout upperWebToolbar;
    SwipeRefreshLayout upperSwipeRefresh;
    ProgressBar upperProgressBar;
    TextView upperSearchBarOpener,upperOpenedWebText;
    ImageButton upperWebNext,upperHome,upperWebBack,upperCancelLoading,upperRefresh;
    CardView upperBottomSheetLayout;
    String upperUrl="";
    private boolean upperWebActivated=false;

    //for lower
    private AdvancedWebView lowerWebView;
    RelativeLayout visibilityLowerWeb;
    LinearLayout lowerWebToolbar;
    SwipeRefreshLayout lowerSwipeRefresh;
    ProgressBar lowerProgressBar;
    TextView lowerSearchBarOpener,lowerOpenedWebText;
    ImageButton lowerWebNext,lowerHome,lowerWebBack,lowerCancelLoading,lowerRefresh;
    CardView lowerBottomSheetLayout;
    String lowerUrl="";
    private boolean lowerWebActivated=false;

    RecordAction action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentNewsDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentNewsAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                window.setStatusBarColor(getResources().getColor(R.color.normalStatusBar));
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
                        window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentNewsAppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                        window.setStatusBarColor(getResources().getColor(R.color.normalStatusBar));
                    }
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.split_mode_vertical);

        splitActivity=this;

        CardView animVisible=findViewById(R.id.splitAnimVisible);
        Animation animation= AnimationUtils.loadAnimation(SpltVertical.this,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        splitmodeMoewActioons=findViewById(R.id.splitmodeMoreActioons);
        splitmodeactionview=findViewById(R.id.splitmodeActionView);
        splithidemoreactions=findViewById(R.id.splithidemoreactions);
        orientyationchanginglayout=findViewById(R.id.orientationchanginglayout);
        btnchangeorientation=findViewById(R.id.btnchangeorientation);

        upperBottomSheetLayout=findViewById(R.id.upperBottomSheetLayout);
        lowerBottomSheetLayout=findViewById(R.id.lowerBottomSheetLayout);
        upperWebView=findViewById(R.id.upperWebView);
        lowerWebView=findViewById(R.id.lowerWebView);

        splitmodeMoewActioons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splitmodeactionview.setVisibility(View.VISIBLE);
            }
        });
        splithidemoreactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splitmodeactionview.setVisibility(View.GONE);
            }
        });

        splitmodeactionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splitmodeactionview.setVisibility(View.GONE);
            }
        });

        btnchangeorientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orientyationchanginglayout.getOrientation()==LinearLayout.VERTICAL){
                    orientyationchanginglayout.setOrientation(LinearLayout.HORIZONTAL);
                }else {
                    orientyationchanginglayout.setOrientation(LinearLayout.VERTICAL);
                }
            }
        });

        int orientation=getResources().getConfiguration().orientation;
        if (orientation==Configuration.ORIENTATION_LANDSCAPE){
            orientyationchanginglayout.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            orientyationchanginglayout.setOrientation(LinearLayout.VERTICAL);
        }

        SharedPreferences references=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
        if (references.contains("splitIncoOn")){
            lowerBottomSheetLayout.setBackgroundColor(getResources().getColor(R.color.cardviewinco));
        }else {
            if (appptheme.contains("DarkOn")){
                lowerBottomSheetLayout.setBackgroundColor(getResources().getColor(R.color.darkcardbackground));
            }else if (appptheme.contains("lightOn")){
                lowerBottomSheetLayout.setBackgroundColor(getResources().getColor(R.color.purewhite));
            }else if (appptheme.contains("sysDef")){
                int nightModeFlags=getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags){
                    case Configuration.UI_MODE_NIGHT_YES:
                        lowerBottomSheetLayout.setBackgroundColor(getResources().getColor(R.color.darkcardbackground));
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        lowerBottomSheetLayout.setBackgroundColor(getResources().getColor(R.color.purewhite));
                        break;
                }
            }
        }
        action = new RecordAction(SpltVertical.this);
        action.open(true);

        adservers();

        populateUpperWebView();

        populateLowerWebView();

    }

    private void populateUpperWebView(){
        upperSearchBarOpener=findViewById(R.id.upperSearchBarOpener);
        visibilityUpperWeb=findViewById(R.id.visibilityUpperWeb);
        upperSwipeRefresh=findViewById(R.id.upperSwipeRefresh);
        upperWebToolbar=findViewById(R.id.upperWebToolbar);
        upperProgressBar=findViewById(R.id.upperProgressBar);
        upperHome=findViewById(R.id.upperHome);
        upperWebNext=findViewById(R.id.upperWebNext);
        upperWebBack=findViewById(R.id.upperWebBack);
        upperOpenedWebText=findViewById(R.id.upperOpenedWebText);
        upperRefresh=findViewById(R.id.upperRefresh);
        upperCancelLoading=findViewById(R.id.upperSplitCancelLoading);

        SharedPreferences preferences=getSharedPreferences("Split Web Url",MODE_PRIVATE);
        if (preferences.contains("upperSplitUrl")){
            String url=preferences.getString("upperSplitUrl",null);
            upperWebToolbar.setVisibility(View.VISIBLE);
            visibilityUpperWeb.setVisibility(View.VISIBLE);
            upperWebView.loadUrl(url);
        }

        upperSwipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        upperSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.swipe_reefresh_color11),
                getResources().getColor(R.color.swipe_reefresh_color22),
                getResources().getColor(R.color.swipe_reefresh_color33),
                getResources().getColor(R.color.swipe_reefresh_color44));

        upperSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upperWebView.reload();
            }
        });

        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            upperProgressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("lightOn")){
            upperProgressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    upperProgressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    upperProgressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                    break;
            }
        }

        SharedPreferences webSettings=getSharedPreferences("webViewSettings",MODE_PRIVATE);

        upperProgressBar.setVisibility(View.INVISIBLE);
        upperProgressBar.setMax(100);
        registerForContextMenu(upperWebView);
        upperWebView.getSettings().setJavaScriptEnabled(true);
        upperWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        upperWebView.getSettings().setDomStorageEnabled(true);
        upperWebView.getSettings().setAllowFileAccess(true);
        upperWebView.getSettings().setUseWideViewPort(true);
        upperWebView.getSettings().setSavePassword(true);
        upperWebView.getSettings().setSaveFormData(true);
        upperWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        upperWebView.getSettings().setEnableSmoothTransition(true);
        upperWebView.getSettings().setLoadWithOverviewMode(true);
        upperWebView.getSettings().setUseWideViewPort(true);
        SharedPreferences.Editor editor=webSettings.edit();
        int fixedFont=upperWebView.getSettings().getDefaultFixedFontSize();
        int fontSize=upperWebView.getSettings().getDefaultFontSize();
        editor.putInt("defaultfontSize11",fixedFont);
        editor.putInt("defaultfontSize22",fontSize);
        editor.apply();
        if (webSettings.contains("textSize")){
            int points=webSettings.getInt("textSize",0);
            upperWebView.getSettings().setDefaultFontSize(points);
            upperWebView.getSettings().setDefaultFixedFontSize(points);
        }else {
            int fixedFon=webSettings.getInt("defaultfontSize11",0);
            int fontSiz=webSettings.getInt("defaultfontSize22",0);
            upperWebView.getSettings().setDefaultFontSize(fontSiz);
            upperWebView.getSettings().setDefaultFixedFontSize(fixedFon);
        }
        if (webSettings.contains("swipeRefOff")){
            upperSwipeRefresh.setEnabled(false);
        }else {
            upperSwipeRefresh.setEnabled(true);
        }
        if (webSettings.contains("mainZoomOff")){
            upperWebView.getSettings().setSupportZoom(false);
            upperWebView.getSettings().supportZoom();
            upperWebView.getSettings().getBuiltInZoomControls();
            upperWebView.getSettings().setBuiltInZoomControls(false);
        }else {
            upperWebView.getSettings().setSupportZoom(true);
            upperWebView.getSettings().supportZoom();
            upperWebView.getSettings().getBuiltInZoomControls();
            upperWebView.getSettings().setBuiltInZoomControls(true);
        }
        if (webSettings.contains("zoomControl")){
            upperWebView.getSettings().setDisplayZoomControls(true);
        }else {
            upperWebView.getSettings().setDisplayZoomControls(false);
        }
        if (webSettings.contains("ultraFastModeON")){
            upperWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            upperWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                upperWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            upperWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }else {
            upperWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
            upperWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                upperWebView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                upperWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            upperWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        upperSwipeRefresh.setRefreshing(false);
        if (webSettings.contains("adBlockOn")){
            upperWebView.setWebViewClient(new UpperAdBlockWebViewClient());
        }else {
            upperWebView.setWebViewClient(new UpperSampWebViewClient());
        }
        upperWebView.setWebChromeClient(new UpperMyChrome());

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
                    WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON);
                }else {
                    upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                }
            }else {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                }else {
                    upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }
            }
        }
        if (appptheme.contains("lightOn")){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                        WebSettingsCompat.FORCE_DARK_OFF);
            }else {
                upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
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
                            WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_ON);
                        }else {
                            upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                        }
                    }else {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_OFF);
                        }else {
                            upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                        }
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                        WebSettingsCompat.setForceDark(upperWebView.getSettings(),
                                WebSettingsCompat.FORCE_DARK_OFF);
                    }else {
                        upperWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                    break;
            }
        }

        upperWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url2, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                //String filename2=url2.substring(url2.lastIndexOf('/')+1,
                //url2.length());

                String filename2=URLUtil.guessFileName(url2, null, null);

                Intent intent=new Intent(SpltVertical.this, DownloadStarter.class);
                intent.putExtra("fileurl", url2);
                intent.putExtra("filename",filename2);
                startActivity(intent);

                //request.allowScanningByMediaScanner();
                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
                //DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                //dm.enqueue(request);
                //Toast.makeText(getApplicationContext(), "Downloading File",
                //        Toast.LENGTH_LONG).show();
            }
        });

        upperOpenedWebText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openedwebUrl = "" + upperWebView.getOriginalUrl();
                Intent intent = new Intent(SpltVertical.this, search_bar_place.class);
                intent.putExtra("openedwebUrl", openedwebUrl);
                startActivityForResult(intent, 1);
            }
        });
        upperWebNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upperWebActivated){
                    if (upperWebView.canGoForward()) {
                        upperWebView.stopLoading();
                        upperWebView.goForward();
                    } else {
                        Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_down_search);
                        upperWebToolbar.startAnimation(slide_down_search);
                        upperWebToolbar.setVisibility(View.VISIBLE);

                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        visibilityUpperWeb.startAnimation(slide_up);
                        visibilityUpperWeb.setVisibility(View.VISIBLE);

                        upperWebView.setVisibility(View.VISIBLE);
                        upperWebBack.setVisibility(View.VISIBLE);
                        upperWebNext.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        upperHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upperWebView.stopLoading();
                upperWebView.clearView();

                Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down_search);
                visibilityUpperWeb.startAnimation(slide_down_search);
                visibilityUpperWeb.setVisibility(View.GONE);

                Animation slide_up_search = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_search);
                upperWebToolbar.startAnimation(slide_up_search);
                upperWebToolbar.setVisibility(View.GONE);
            }
        });

        upperWebBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upperWebView.canGoBack()) {
                    upperWebView.stopLoading();
                    upperWebView.goBack();
                } else {
                    Animation slide_up_search = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up_search);
                    upperWebToolbar.startAnimation(slide_up_search);
                    upperWebToolbar.setVisibility(View.GONE);

                    Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_down_search);
                    visibilityUpperWeb.startAnimation(slide_down_search);
                    visibilityUpperWeb.setVisibility(View.GONE);
                }
            }
        });


        upperSearchBarOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mainweb_view_url = "";
                Intent intent = new Intent(SpltVertical.this, search_bar_place.class);
                intent.putExtra("mainweb_view_url", mainweb_view_url);
                startActivityForResult(intent, 1);
            }
        });
    }

    class UpperAdBlockWebViewClient extends WebViewClient {
        UpperAdBlockWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            upperUrl = url;
            upperWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                upperWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            upperCancelLoading.setVisibility(View.VISIBLE);
            upperRefresh.setVisibility(GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addUpperSplitHistory(new Record(fileTitle, upperUrl, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            upperCancelLoading.setVisibility(GONE);
            upperRefresh.setVisibility(View.VISIBLE);
            upperWebView.setVisibility(View.VISIBLE);
            upperProgressBar.setProgress(0);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(SpltVertical.this);
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            upperOpenedWebText.setText(R.string.error);
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

    class UpperSampWebViewClient extends WebViewClient {
        UpperSampWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            upperUrl = url;
            upperWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                upperWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            upperCancelLoading.setVisibility(View.VISIBLE);
            upperRefresh.setVisibility(GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addUpperSplitHistory(new Record(fileTitle, upperUrl, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            upperWebView.setVisibility(View.VISIBLE);
            upperCancelLoading.setVisibility(GONE);
            upperRefresh.setVisibility(View.VISIBLE);
            upperProgressBar.setProgress(0);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(SpltVertical.this);
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            upperOpenedWebText.setText(R.string.error);
        }
    }

    StringBuilder adservers;
    private void adservers(){
        String strLine2="";
        adservers = new StringBuilder();

        InputStream fis2 = this.getResources().openRawResource(R.raw.adblockserverlist);
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

    private class UpperMyChrome extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        UpperMyChrome() {}

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
            ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(upperProgressBar, 1000);
            mProgressAnimation.setProgress(65);

            ScondaryPrgressBar scondaryPrgressBar = new ScondaryPrgressBar(upperProgressBar, 3000);
            scondaryPrgressBar.setProgress(100);

            if (progress > 65){
                mProgressAnimation.setProgress(progress);
            }

            if (progress < 100 && upperProgressBar.getVisibility() == View.INVISIBLE) {
                upperOpenedWebText.setText(R.string.loading);
                upperWebToolbar.setVisibility(View.VISIBLE);
                //disabledback.setVisibility(View.GONE);
                //webback.setVisibility(View.GONE);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                upperProgressBar.setVisibility(View.VISIBLE);
                upperWebView.setVisibility(View.VISIBLE);
                //cancelLoading.setVisibility(View.VISIBLE);
                upperWebActivated=true;
                if (lowerProgressBar.getSecondaryProgress()==100){
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
                upperOpenedWebText.setText(upperWebView.getTitle());
                //cancel_button_toolbar.setVisibility(View.GONE);
                //refresh_button.setVisibility(View.VISIBLE);
                upperSwipeRefresh.setRefreshing(false);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                upperProgressBar.setVisibility(View.INVISIBLE);
                upperWebView.setVisibility(View.VISIBLE);
                //cancelLoading.setVisibility(View.GONE);
                //webback.setVisibility(View.VISIBLE);
                //disabledback.setVisibility(View.GONE);
                upperWebActivated=true;
                lowerProgressBar.setProgress(0);
                if (lowerProgressBar.getSecondaryProgress()==100){
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

    private void populateLowerWebView(){
        lowerSearchBarOpener=findViewById(R.id.lowerSearchBarOpener);
        visibilityLowerWeb=findViewById(R.id.visibilityLowerWeb);
        lowerSwipeRefresh=findViewById(R.id.lowerSwipeRefresh);
        lowerWebToolbar=findViewById(R.id.lowerWebToolbar);
        lowerProgressBar=findViewById(R.id.lowerProgressBar);
        lowerHome=findViewById(R.id.lowerHome);
        lowerWebNext=findViewById(R.id.lowerWebNext);
        lowerWebBack=findViewById(R.id.lowerWebBack);
        lowerOpenedWebText=findViewById(R.id.lowerOpenedWebText);
        lowerCancelLoading=findViewById(R.id.lowerSplitCancelLoading);
        lowerRefresh=findViewById(R.id.lowerSplitRefresh);

        SharedPreferences preferences=getSharedPreferences("Split Web Url",MODE_PRIVATE);
        if (preferences.contains("lowerSplitUrl")){
            String url=preferences.getString("lowerSplitUrl",null);
            lowerWebToolbar.setVisibility(View.VISIBLE);
            visibilityLowerWeb.setVisibility(View.VISIBLE);
            lowerWebView.loadUrl(url);
        }

        lowerSwipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        lowerSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.swipe_reefresh_color11),
                getResources().getColor(R.color.swipe_reefresh_color22),
                getResources().getColor(R.color.swipe_reefresh_color33),
                getResources().getColor(R.color.swipe_reefresh_color44));

        lowerSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lowerWebView.reload();
            }
        });

        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            lowerProgressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("lightOn")){
            lowerProgressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    lowerProgressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    lowerProgressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                    break;
            }
        }

        SharedPreferences webSettings=getSharedPreferences("webViewSettings",MODE_PRIVATE);

        lowerProgressBar.setVisibility(View.INVISIBLE);
        lowerProgressBar.setMax(100);
        registerForContextMenu(lowerWebView);
        lowerWebView.getSettings().setJavaScriptEnabled(true);
        lowerWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        lowerWebView.getSettings().setDomStorageEnabled(true);
        lowerWebView.getSettings().setAllowFileAccess(true);
        lowerWebView.getSettings().setUseWideViewPort(true);
        lowerWebView.getSettings().setSavePassword(true);
        lowerWebView.getSettings().setSaveFormData(true);
        lowerWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        lowerWebView.getSettings().setEnableSmoothTransition(true);
        lowerWebView.getSettings().setLoadWithOverviewMode(true);
        lowerWebView.getSettings().setUseWideViewPort(true);
        SharedPreferences.Editor editor=webSettings.edit();
        int fixedFont=lowerWebView.getSettings().getDefaultFixedFontSize();
        int fontSize=lowerWebView.getSettings().getDefaultFontSize();
        editor.putInt("defaultfontSize11",fixedFont);
        editor.putInt("defaultfontSize22",fontSize);
        editor.apply();
        if (webSettings.contains("textSize")){
            int points=webSettings.getInt("textSize",0);
            lowerWebView.getSettings().setDefaultFontSize(points);
            lowerWebView.getSettings().setDefaultFixedFontSize(points);
        }else {
            int fixedFon=webSettings.getInt("defaultfontSize11",0);
            int fontSiz=webSettings.getInt("defaultfontSize22",0);
            lowerWebView.getSettings().setDefaultFontSize(fontSiz);
            lowerWebView.getSettings().setDefaultFixedFontSize(fixedFon);
        }
        if (webSettings.contains("swipeRefOff")){
            lowerSwipeRefresh.setEnabled(false);
        }else {
            lowerSwipeRefresh.setEnabled(true);
        }
        if (webSettings.contains("mainZoomOff")){
            lowerWebView.getSettings().setSupportZoom(false);
            lowerWebView.getSettings().supportZoom();
            lowerWebView.getSettings().getBuiltInZoomControls();
            lowerWebView.getSettings().setBuiltInZoomControls(false);
        }else {
            lowerWebView.getSettings().setSupportZoom(true);
            lowerWebView.getSettings().supportZoom();
            lowerWebView.getSettings().getBuiltInZoomControls();
            lowerWebView.getSettings().setBuiltInZoomControls(true);
        }
        if (webSettings.contains("zoomControl")){
            lowerWebView.getSettings().setDisplayZoomControls(true);
        }else {
            lowerWebView.getSettings().setDisplayZoomControls(false);
        }
        if (webSettings.contains("ultraFastModeON")){
            lowerWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            lowerWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                lowerWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            lowerWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }else {
            lowerWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
            lowerWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                lowerWebView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                lowerWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            lowerWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        upperSwipeRefresh.setRefreshing(false);
        if (webSettings.contains("adBlockOn")){
            lowerWebView.setWebViewClient(new LowerAdBlockWebViewClient());
        }else {
            lowerWebView.setWebViewClient(new LowerSampWebViewClient());
        }
        lowerWebView.setWebChromeClient(new LowerMyChrome());

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
                    WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON);
                }else {
                    lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                }
            }else {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                }else {
                    lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }
            }
        }
        if (appptheme.contains("lightOn")){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                        WebSettingsCompat.FORCE_DARK_OFF);
            }else {
                lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
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
                            WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_ON);
                        }else {
                            lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                        }
                    }else {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_OFF);
                        }else {
                            lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                        }
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                        WebSettingsCompat.setForceDark(lowerWebView.getSettings(),
                                WebSettingsCompat.FORCE_DARK_OFF);
                    }else {
                        lowerWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                    break;
            }
        }

        lowerWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url2, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                //String filename2=url2.substring(url2.lastIndexOf('/')+1,
                //url2.length());

                String filename2=URLUtil.guessFileName(url2, null, null);

                Intent intent=new Intent(SpltVertical.this, DownloadStarter.class);
                intent.putExtra("fileurl", url2);
                intent.putExtra("filename",filename2);
                startActivity(intent);

                //request.allowScanningByMediaScanner();
                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
                //DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                //dm.enqueue(request);
                //Toast.makeText(getApplicationContext(), "Downloading File",
                //        Toast.LENGTH_LONG).show();
            }
        });

        lowerOpenedWebText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openedwebUrl = "" + lowerWebView.getOriginalUrl();
                Intent intent = new Intent(SpltVertical.this, search_bar_place.class);
                intent.putExtra("openedwebUrl", openedwebUrl);
                startActivityForResult(intent, 10);
            }
        });

        lowerWebNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lowerWebActivated){
                    if (lowerWebView.canGoForward()) {
                        lowerWebView.stopLoading();
                        lowerWebView.goForward();
                    } else {
                        Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_down_search);
                        lowerWebToolbar.startAnimation(slide_down_search);
                        lowerWebToolbar.setVisibility(View.VISIBLE);

                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        visibilityLowerWeb.startAnimation(slide_up);
                        visibilityLowerWeb.setVisibility(View.VISIBLE);

                        lowerWebView.setVisibility(View.VISIBLE);
                        lowerWebBack.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        lowerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lowerWebView.stopLoading();
                lowerWebView.clearView();

                Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down_search);
                visibilityLowerWeb.startAnimation(slide_down_search);
                visibilityLowerWeb.setVisibility(View.GONE);

                Animation slide_up_search = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_search);
                lowerWebToolbar.startAnimation(slide_up_search);
                lowerWebToolbar.setVisibility(View.GONE);
            }
        });

        lowerWebBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lowerWebView.canGoBack()) {
                    lowerWebView.stopLoading();
                    lowerWebView.goBack();
                } else {
                    Animation slide_up_search = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up_search);
                    lowerWebToolbar.startAnimation(slide_up_search);
                    lowerWebToolbar.setVisibility(View.GONE);

                    Animation slide_down_search = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_down_search);
                    visibilityLowerWeb.startAnimation(slide_down_search);
                    visibilityLowerWeb.setVisibility(View.GONE);
                }
            }
        });


        lowerSearchBarOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        String mainweb_view_url = "";
                        Intent intent = new Intent(SpltVertical.this, search_bar_place.class);
                        intent.putExtra("mainweb_view_url", mainweb_view_url);
                        startActivityForResult(intent, 10);

            }
        });
    }

    class LowerAdBlockWebViewClient extends WebViewClient {
        LowerAdBlockWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            lowerUrl = url;
            lowerWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                lowerWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            lowerCancelLoading.setVisibility(View.VISIBLE);
            lowerRefresh.setVisibility(GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addLowerSplitHistory(new Record(fileTitle, upperUrl, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            lowerCancelLoading.setVisibility(GONE);
            lowerRefresh.setVisibility(View.VISIBLE);
            lowerWebView.setVisibility(View.VISIBLE);
            lowerProgressBar.setProgress(0);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(SpltVertical.this);
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            lowerOpenedWebText.setText(R.string.error);
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

    class LowerSampWebViewClient extends WebViewClient {
        LowerSampWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            lowerUrl = url;
            lowerWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                lowerWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            lowerCancelLoading.setVisibility(View.VISIBLE);
            lowerRefresh.setVisibility(GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addLowerSplitHistory(new Record(fileTitle, lowerUrl, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            lowerWebView.setVisibility(View.VISIBLE);
            lowerCancelLoading.setVisibility(GONE);
            lowerRefresh.setVisibility(View.VISIBLE);
            lowerProgressBar.setProgress(0);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(SpltVertical.this);
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            lowerOpenedWebText.setText(R.string.error);
        }
    }

    private class LowerMyChrome extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        LowerMyChrome() {}

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
            ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(lowerProgressBar, 1000);
            mProgressAnimation.setProgress(65);

            ScondaryPrgressBar scondaryPrgressBar = new ScondaryPrgressBar(lowerProgressBar, 3000);
            scondaryPrgressBar.setProgress(100);

            if (progress > 65){
                mProgressAnimation.setProgress(progress);
            }

            if (progress < 100 && lowerProgressBar.getVisibility() == View.INVISIBLE) {
                lowerOpenedWebText.setText(R.string.loading);
                lowerWebToolbar.setVisibility(View.VISIBLE);
                //disabledback.setVisibility(View.GONE);
                //webback.setVisibility(View.GONE);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                lowerProgressBar.setVisibility(View.VISIBLE);
                lowerWebView.setVisibility(View.VISIBLE);
                //cancelLoading.setVisibility(View.VISIBLE);
                lowerWebActivated=true;
                if (lowerProgressBar.getSecondaryProgress()==100){
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
                lowerOpenedWebText.setText(lowerWebView.getTitle());
                //cancel_button_toolbar.setVisibility(View.GONE);
                //refresh_button.setVisibility(View.VISIBLE);
                lowerSwipeRefresh.setRefreshing(false);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                lowerProgressBar.setVisibility(View.INVISIBLE);
                lowerWebView.setVisibility(View.VISIBLE);
                //cancelLoading.setVisibility(View.GONE);
                //webback.setVisibility(View.VISIBLE);
                //disabledback.setVisibility(View.GONE);
                lowerWebActivated=true;
                lowerProgressBar.setProgress(0);
                if (lowerProgressBar.getSecondaryProgress()==100){
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            if (resultCode==RESULT_OK){
                String url=data.getStringExtra("result");
                upperWebToolbar.setVisibility(View.VISIBLE);
                visibilityUpperWeb.setVisibility(View.VISIBLE);
                upperWebView.loadUrl(url);
            }
        }

        if (requestCode==10){
            if (resultCode==RESULT_OK){
                String url=data.getStringExtra("result");
                lowerWebToolbar.setVisibility(View.VISIBLE);
                visibilityLowerWeb.setVisibility(View.VISIBLE);
                lowerWebView.loadUrl(url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SpltVertical.this,SplitAlertWindow.class);

        if (splitmodeactionview.getVisibility()==View.VISIBLE){
            splitmodeactionview.setVisibility(View.GONE);
        }else {
            if (upperWebToolbar.getVisibility()==View.VISIBLE){
                if (lowerWebToolbar.getVisibility()==View.VISIBLE){
                    intent.putExtra("lowerUrl",lowerUrl);
                    intent.putExtra("upperUrl",upperUrl);
                }else {
                    intent.putExtra("upperUrl",upperUrl);
                }
                startActivity(intent);
            }else if (lowerWebToolbar.getVisibility()==View.VISIBLE){
                intent.putExtra("lowerUrl",lowerUrl);
                startActivity(intent);
            }else {
                CardView animVisible=findViewById(R.id.splitAnimVisible);
                Animation animation8= AnimationUtils.loadAnimation(SpltVertical.this,R.anim.frag_exit);
                animation8.setDuration(100);
                animVisible.startAnimation(animation8);
                animVisible.setVisibility(View.GONE);
                animation8.setAnimationListener(new Animation.AnimationListener() {
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
        }
    }
}
