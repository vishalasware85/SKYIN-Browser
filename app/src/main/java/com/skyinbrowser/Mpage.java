package com.skyinbrowser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Paint;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.floating_bubble.overlay.OverlayPermission;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.HomeWatcher;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.CustomJavaFiles.OnHomePressedListener;
import com.skyinbrowser.CustomJavaFiles.ProgressBarAnimation;
import com.skyinbrowser.CustomJavaFiles.ScondaryPrgressBar;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.Dialogs.AdBlockSwitchingDialog;
import com.skyinbrowser.Dialogs.AppExitDialog;
import com.skyinbrowser.Dialogs.NightModeSwitchingDialog;
import com.skyinbrowser.DownloadManager.DownloadStarter;
import com.skyinbrowser.MpageFrag.NewsMpageFrag;
import com.skyinbrowser.MpageFrag.SpeedDialFrag;
import com.skyinbrowser.Settings.SettingsActivity;
import com.skyinbrowser.bookmark.BookmarkActivity;
import com.skyinbrowser.feedback.Feedback;
import com.skyinbrowser.history.FloatHistory;
import com.skyinbrowser.history.HistoryActivity;
import com.skyinbrowser.history.SplitHistory;
import com.skyinbrowser.weather.Constants;
import com.skyinbrowser.weather.WeatherCustomLocationSetter;
import com.suke.widget.SwitchButton;
import com.skyinbrowser.Floating.SingleSectionNotificationHoverMenuService;

import com.skyinbrowser.MoreSites.MySitesAdder;
import com.skyinbrowser.bookmark.BookmarkAdderAndEdit;
import com.skyinbrowser.for_search.search_bar_place;
import com.skyinbrowser.weather.Weather_activity;
import com.skyinbrowser.weather.models.Weather;
import com.skyinbrowser.weather.tasks.GenericRequestTaskTwo;
import com.skyinbrowser.weather.tasks.ParseResult;
import com.skyinbrowser.weather.tasks.TaskOutput;
import com.skyinbrowser.weather.utils.UnitConvertor;
import com.skyinbrowser.weather.widgets.AbstractWidgetProvider;
import com.skyinbrowser.weather.widgets.DashClockWeatherExtension;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.webview.AdvancedWebView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Mpage extends AppCompatActivity implements FragmentToActivity, LocationListener ,
        AppBarLayout.OnOffsetChangedListener, SpeedDialFrag.SpeedDialFragListener {

    ImageButton webback, webnext, home, google_icon_toolbar, buttonOpenBottomSheet,cancelLoading, mainPopup;
    TextView webview_text_button,tab_bar_opner;
    TextView webview_title_button;
    ImageView search_button;
    EditText editText;
    AdvancedWebView webView;
    ProgressBar progressBar;
    LinearLayout gradToolbar,card4,toolbar;
    CoordinatorLayout mainFullLayout;
    SwipeRefreshLayout webswipe_refresh;
    CardView webview_toolbar,weatherCard,mainFullWebLayout,card3,customFragCard;
    private NestedScrollView mainscroll;
    private AppBarLayout appBarLayout;
    private Toolbar maintoolbar;
    private boolean isHideToolbarView = false;
    private String titletext="";
    private FrameLayout vpnFrameLayout,bottomSheetFrameLayout,downloadFrameLayout,historyFrameLayout,floathistoryFrameLayout
            ,splithistoryFrameLayout,bookmarkFrameLayout,
            settingsFrameLayout,splitModeFrameLayout,addonsFrameLayout,feedbackFrameLayout,aboutFrameLayout;
    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
    private AnimCheckBox dmton, dmtoff,fmon,fmoff;
    private MaterialRippleLayout refreshBtn,voiceSearch;
    private static final String APP_KEY = "144a48f104d34a6589b6c16f2de7f846"; // Sample Application Key. Replace this value with your Application Key.
    private static final String LOG_TAG = "SimpleAdSample";
    FloatingActionButton mainScrollFAB;
    private boolean FOCUS;
    private SpeedDialFrag speedDialFrag;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SharedPreferences webViewSettingsPref;

    //for cricket
    String currentmatch = " http://cricapi.com/api/matches/";

    //for weather
    private TextView weatherIcon;
    private TextView location;
    private TextView condition;
    private TextView temp;
    boolean destroyed = false;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    public String recentCity = "";
    Weather todayWeather = new Weather();
    protected static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private static final int NO_UPDATE_REQUIRED_THRESHOLD = 300000;
    private static final int CAMERA_REQUEST_CODE=400;
    private static final int STORAGE_REQUEST_CODE=1;

    //for download
    String vinwon;
    String fileurl;
    RecordAction action;

    //for enable float
    private static final int REQUEST_CODE_HOVER_PERMISSION=300;
    boolean mPermissionsRequested = true;

    public static Activity mpage;
    protected boolean gradientDivider;

    //for vpn
    CircleImageView vpnBtn;

    public static final int custom_location = 0;
    public static final int weather_detail = 1;
    public static final int refresh = 2;
    public static final int locateMeMpage = 3;

    public static final int clearLocationDataMenu = 4;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.DarkTheme);
        }
        if (appptheme.contains("lightOn")){
            setTheme(R.style.AppTheme);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.DarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpage);

        adservers();

        search_button = findViewById(R.id.search_button);
        editText = findViewById(R.id.for_search_editText);
        mainFullWebLayout=findViewById(R.id.mainFullWebLayout);
        webView = findViewById(R.id.mainweb);
        webswipe_refresh = findViewById(R.id.for_webview_swiperefresh);
        webview_title_button = findViewById(R.id.webview_text);
        google_icon_toolbar = findViewById(R.id.google_icon_in_toolbar);
        cancelLoading = findViewById(R.id.mainwebCancelLoading);
        webback = findViewById(R.id.webback);
        progressBar = findViewById(R.id.search_progressbar);
        webnext = findViewById(R.id.webnext);
        home = findViewById(R.id.home);
        //listNews = findViewById(R.id.listNews);
        buttonOpenBottomSheet = findViewById(R.id.open_bottom_sheet);
        tab_bar_opner = findViewById(R.id.tab_bar_opner);
        toolbar = findViewById(R.id.toolbar);
        webview_toolbar = findViewById(R.id.webview_toolbar);
        webview_text_button = findViewById(R.id.webview_text_button);
        mainscroll = findViewById(R.id.main_scroll);
        mainPopup = findViewById(R.id.main_popup);
        location=findViewById(R.id.location);
        condition=findViewById(R.id.condition);
        temp=findViewById(R.id.temp);
        weatherIcon=findViewById(R.id.weather_icon);
        weatherCard=findViewById(R.id.weatherCard);
        mainFullLayout=findViewById(R.id.mainFullLayout);
        gradToolbar=findViewById(R.id.gradToolbar);
        card3=findViewById(R.id.incochange3);
        card4=findViewById(R.id.mpagecard4);
        vpnBtn=findViewById(R.id.vpnBtnWebtoolbar);
        mainScrollFAB=findViewById(R.id.mainScrollFAB);
        customFragCard=findViewById(R.id.customFragCard);
        bottomSheetFrameLayout=findViewById(R.id.bottomSheetFrameLayout);
        downloadFrameLayout=findViewById(R.id.downloadsFrameLayout);
        historyFrameLayout=findViewById(R.id.historyFrameLayout);
        bookmarkFrameLayout=findViewById(R.id.bookmarkFrameLayout);
        settingsFrameLayout=findViewById(R.id.settingsFrameLayout);
        splitModeFrameLayout=findViewById(R.id.splitModeFrameLayout);
        addonsFrameLayout=findViewById(R.id.addonsFrameLayout);
        feedbackFrameLayout=findViewById(R.id.feedbackFrameLayout);
        aboutFrameLayout=findViewById(R.id.aboutFrameLayout);
        floathistoryFrameLayout=findViewById(R.id.floathistoryFrameLayout);
        splithistoryFrameLayout=findViewById(R.id.splithistoryFrameLayout);
        voiceSearch=findViewById(R.id.voiceSearchMpage);

        MaterialRippleLayout addTo, floatingMode, desktopMode, savePage;
        TextView tvdmon,tvdmoff;
        ImageView main_popup = findViewById(R.id.main_popup);
        CardView popupLayout = findViewById(R.id.mainPopupLayout);
        CardView popupActionView = findViewById(R.id.mainPopupActionView);
        dmton = findViewById(R.id.dmton);
        dmtoff = findViewById(R.id.dmtoff);
        refreshBtn=findViewById(R.id.refreshBtn);

        addTo = findViewById(R.id.addto);
        floatingMode = findViewById(R.id.openFloat);
        desktopMode = findViewById(R.id.desktopMode);
        savePage = findViewById(R.id.save_page);

        CardView desktopLayout = findViewById(R.id.desktoplayout);
        tvdmon=findViewById(R.id.tvdmtOn);
        tvdmoff=findViewById(R.id.tvdmtoff);

        CardView desktopalertLayout = findViewById(R.id.desktopalertlayout);

        CardView floatingLayout = findViewById(R.id.floatingLayout);
        SwitchButton floatSwutcher=findViewById(R.id.enablefloatmode);
        TextView tvfloaState=findViewById(R.id.tvfloatState);

        CardView addToLayoout=findViewById(R.id.addToLayout);
        MaterialRippleLayout mySitesAdder=findViewById(R.id.mySitesAdder);
        MaterialRippleLayout bookmarksAdder=findViewById(R.id.bookmarksAdder);

        mpage=this;

        Typeface weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        weatherIcon.setTypeface(weatherFont);

        maintoolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("");

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.main_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.main_appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mainscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>oldScrollY){
                    //Toast.makeText(Mpage.this, "down", Toast.LENGTH_SHORT).show();
                    if (mainScrollFAB.getVisibility()== GONE){
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_fade_in);
                        animation.setDuration(100);
                        mainScrollFAB.startAnimation(animation);
                        mainScrollFAB.setVisibility(VISIBLE);
                    }
                }
                if (scrollY==0){
                    //Toast.makeText(Mpage.this, "top", Toast.LENGTH_SHORT).show();
                    if (mainScrollFAB.getVisibility()== VISIBLE){
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_fade_out);
                        animation.setDuration(100);
                        mainScrollFAB.startAnimation(animation);
                        mainScrollFAB.setVisibility(GONE);
                    }
                }
            }
        });

        if (appptheme.contains("DarkOn")){
            progressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("lightOn")){
            progressBar.getProgressDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.lightlightestcolor), PorterDuff.Mode.SRC_IN);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                    break;
            }
        }

        mainScrollFAB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                mainscroll.fullScroll(ScrollView.FOCUS_UP);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_fade_out);
                animation.setDuration(100);
                mainScrollFAB.startAnimation(animation);
                mainScrollFAB.setVisibility(GONE);
                appBarLayout.setExpanded(true);
            }
        });

        SharedPreferences gotTheUrl=getSharedPreferences("gotTheUrl",MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("mainweb url", MODE_PRIVATE);
        if (gotTheUrl.contains("gotTheUrl")){
            String gotUrl=gotTheUrl.getString("gotTheUrl",null);
            toolbar.setVisibility(GONE);
            animationVisible(R.anim.slide_down_search, webview_toolbar);
            mainFullWebLayout.setVisibility(VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);
            webView.loadUrl(gotUrl);
            SharedPreferences.Editor editor=gotTheUrl.edit();
            editor.remove("gotTheUrl");
            editor.apply();
        }else if (preferences.contains("url")) {
            String url = preferences.getString("url", "");
            toolbar.setVisibility(GONE);
            animationVisible(R.anim.slide_down_search, webview_toolbar);
            mainFullWebLayout.setVisibility(VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);
            webView.loadUrl(url);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("url");
            editor.apply();
        }

        //AdRegistration.enableLogging(false);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds.
        //AdRegistration.enableTesting(false);
        //this.amazonadView = (AdLayout) findViewById(R.id.amazonad_view);
        //this.amazonadView.setListener(new SampleAdListener());
        //try {
        //    AdRegistration.setAppKey(APP_KEY);
        //} catch (final IllegalArgumentException e) {
        //    Log.e(LOG_TAG, "IllegalArgumentException thrown: " + e.toString());
        //    return;
        //}
        //this.amazonadView.enableAutoShow();
        //if (!this.amazonadView.showAd()) {
        //    Log.w(LOG_TAG, "The ad was not shown. Check the logcat for more information.");
        //}
        //this.amazonadView.loadAd();


        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.setAdListener(new AdListener(){
        //    @Override
        //    public void onAdOpened() {
        //        AdRequest adRequest = new AdRequest.Builder().build();
        //        mAdView.loadAd(adRequest);
        //    }
        //});
        //mAdView.loadAd(adRequest);

        SharedPreferences references=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
        if (references.contains("mainIncoOn")){
            CardView incochange3=findViewById(R.id.incochange3);
            incochange3.setBackgroundColor(getResources().getColor(R.color.btsheetInco));
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    card3.setBackgroundColor(getResources().getColor(R.color.darkcardbackground));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    card3.setBackgroundColor(getResources().getColor(R.color.lightCardBackground));
                    break;
            }
        }else{
            if (appptheme.contains("DarkOn")){
                card3.setBackgroundColor(getResources().getColor(R.color.darkcardbackground));
            }else {
                card3.setBackgroundColor(getResources().getColor(R.color.lightCardBackground));
            }
        }

        action = new RecordAction(Mpage.this);
        action.open(true);

        mainscroll.fullScroll(ScrollView.FOCUS_UP);

        webswipe_refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        webswipe_refresh.setColorSchemeColors(getResources().getColor(R.color.swipe_reefresh_color11),
                getResources().getColor(R.color.swipe_reefresh_color22),
                getResources().getColor(R.color.swipe_reefresh_color33),
                getResources().getColor(R.color.swipe_reefresh_color44));

        //progressThumb1.getIndeterminateDrawable().setColorFilter(
        //        getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
//
        //progressThumb2.getIndeterminateDrawable().setColorFilter(
        //        getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
//
        //progressThumb3.getIndeterminateDrawable().setColorFilter(
        //        getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
//
        //progressThumb4.getIndeterminateDrawable().setColorFilter(
        //        getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);
//
        //progressThumb5.getIndeterminateDrawable().setColorFilter(
        //        getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.MULTIPLY);

        webViewSettingsPref=getSharedPreferences("webViewSettings",MODE_PRIVATE);

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);
        registerForContextMenu(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setEnableSmoothTransition(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        SharedPreferences.Editor editor=webViewSettingsPref.edit();
        int fixedFont=webView.getSettings().getDefaultFixedFontSize();
        int fontSize=webView.getSettings().getDefaultFontSize();
        editor.putInt("defaultfontSize11",fixedFont);
        editor.putInt("defaultfontSize22",fontSize);
        editor.apply();
        if (webViewSettingsPref.contains("textSize")){
            int points=webViewSettingsPref.getInt("textSize",0);
            webView.getSettings().setDefaultFontSize(points);
            webView.getSettings().setDefaultFixedFontSize(points);
        }else {
            int fixedFon=webViewSettingsPref.getInt("defaultfontSize11",0);
            int fontSiz=webViewSettingsPref.getInt("defaultfontSize22",0);
            webView.getSettings().setDefaultFontSize(fontSiz);
            webView.getSettings().setDefaultFixedFontSize(fixedFon);
        }
        if (webViewSettingsPref.contains("swipeRefOff")){
            webswipe_refresh.setEnabled(false);
        }else {
            webswipe_refresh.setEnabled(true);
        }
        if (webViewSettingsPref.contains("mainZoomOff")){
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setBuiltInZoomControls(false);
        }else {
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().supportZoom();
            webView.getSettings().getBuiltInZoomControls();
            webView.getSettings().setBuiltInZoomControls(true);
        }
        if (webViewSettingsPref.contains("zoomControl")){
            webView.getSettings().setDisplayZoomControls(true);
        }else {
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (webViewSettingsPref.contains("ultraFastModeON")){
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webswipe_refresh.setRefreshing(false);
        if (webViewSettingsPref.contains("adBlockOn")){
            webView.setWebViewClient(new AdBlockWebViewClient());
        }else {
            webView.setWebViewClient(new SampWebViewClient());
        }
        webView.setWebChromeClient(new MyChrome());

        if (appptheme.contains("DarkOn")){
            if (webViewSettingsPref.contains("applydarkweb")){
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
                    if (webViewSettingsPref.contains("applydarkweb")){
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
        SharedPreferences.Editor editor1=preferences1.edit();
        String mobileUserAgent = webView.getSettings().getUserAgentString();
        editor1.putString("mobileUserAgentString",mobileUserAgent);
        editor1.apply();
        if (preferences1.contains("modeOn")){
            String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
            webView.getSettings().setUserAgentString(newUA);
            dmtoff.setChecked(false);
            dmton.setChecked(true);
        }else {
            String newUA = preferences1.getString("mobileUserAgentString",null);
            webView.getSettings().setUserAgentString(newUA);
            dmtoff.setChecked(true);
            dmton.setChecked(false);
        }

        webswipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.clearCache(true);
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
                    totallength= new DecimalFormat("##.##").format(l1)+" GB";
                }

                Intent intent = new Intent(Mpage.this, DownloadStarter.class);
                intent.putExtra("fileurl", url2);
                intent.putExtra("filename", filename2);
                intent.putExtra("fileSize",totallength);

                startActivity(intent);
            }
        });

        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mainFullWebLayout.getVisibility() == VISIBLE) {
                    saveSharedPrefrence("mainweb url", "url", fileurl,
                            "", "", "", "", "", "");
                } else {
                    SharedPreferences urlcheck=getSharedPreferences("mainweb url",MODE_PRIVATE);
                    SharedPreferences.Editor editor= urlcheck.edit();
                    editor.remove("url");
                    editor.apply();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mainFullWebLayout.getVisibility() == VISIBLE) {
                    saveSharedPrefrence("mainweb url", "url", fileurl,
                            "", "", "", "", "", "");
                } else {
                    SharedPreferences urlcheck=getSharedPreferences("mainweb url",MODE_PRIVATE);
                    SharedPreferences.Editor editor= urlcheck.edit();
                    editor.remove("url");
                    editor.apply();
                }
            }
        });
        mHomeWatcher.startWatch();

        webnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainFullWebLayout.getVisibility() == VISIBLE) {
                    if (webView.canGoForward()) {
                        webView.stopLoading();
                        webView.goForward();
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                } else {
                    toolbar.setVisibility(GONE);
                    animationVisible(R.anim.slide_down_search, webview_toolbar);
                    mainFullWebLayout.setVisibility(VISIBLE);
                    titletext="web";
                    home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                    home.setPadding(20,20,20,20);
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    mainFullLayout.setVisibility(GONE);
                    webView.setVisibility(VISIBLE);
                    webView.onResume();
                }
            }
        });

        cancelLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (home.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.search).getConstantState()){
                    String mainweb_view_url = "";
                    Intent intent = new Intent(Mpage.this, search_bar_place.class);
                    intent.putExtra("mainweb_view_url", mainweb_view_url);
                    startActivityForResult(intent, 1);
                }else {
                    if (mainFullWebLayout.getVisibility()== VISIBLE){
                        webView.stopLoading();
                        animationGone(R.anim.slide_up_search, webview_toolbar);
                        mainFullWebLayout.setVisibility(GONE);
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                        mainFullLayout.setVisibility(VISIBLE);
                        mainscroll.fullScroll(ScrollView.FOCUS_UP);
                        toolbar.setVisibility(VISIBLE);
                        webView.onPause();
                        home.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        home.setPadding(0,0,0,0);
                    }else { }
                }
            }
        });

        webback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webBack();
            }
        });

        webview_title_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mainweb_view_url = "";
                Intent intent = new Intent(Mpage.this, search_bar_place.class);
                intent.putExtra("mainweb_view_url", mainweb_view_url);
                startActivityForResult(intent, 1);
                appBarLayout.setExpanded(false);
            }
        });

        buttonOpenBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customFragCard.setVisibility(VISIBLE);
                bottomSheetFrameLayout.setVisibility(VISIBLE);
                FragmentManager fm=getSupportFragmentManager();
                BottomSheetLayout qrScanComplete=new BottomSheetLayout();
                fm.beginTransaction().replace(R.id.bottomSheetFrameLayout,qrScanComplete).commit();
                titletext="bottomSheet";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
        });

        buttonOpenBottomSheet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String mainweb_view_url = "";
                Intent intent = new Intent(Mpage.this, search_bar_place.class);
                intent.putExtra("mainweb_view_url", mainweb_view_url);
                startActivityForResult(intent, 1);
                return true;
            }
        });

        webview_text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openedwebUrl = "" + webView.getOriginalUrl();
                Intent intent = new Intent(Mpage.this, search_bar_place.class);
                intent.putExtra("openedwebUrl", openedwebUrl);
                startActivityForResult(intent, 1);
                appBarLayout.setExpanded(false);
            }
        });

        tab_bar_opner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    webView.setDrawingCacheEnabled(true);
                    Bitmap bitmap=webView.getDrawingCache();

                    webView.onPause();

                    Intent tabIag=new Intent("tabImage")
                            .putExtra("tabImage",encodeTobase64(bitmap));
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(tabIag);

                    if (mainFullWebLayout.getVisibility()== VISIBLE){
                        String fileTitle=webView.getTitle();
                        Intent intent=new Intent("tabtitle").putExtra("title",fileTitle);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
                    }else {
                        String fileTitle=getResources().getString(R.string.app_name);
                        Intent intent=new Intent("tabtitle").putExtra("title",fileTitle);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
                    }
                }else {
                    mainscroll.setDrawingCacheEnabled(true);
                    Bitmap bitmap=mainscroll.getDrawingCache();

                    Intent tabIag=new Intent("tabImage")
                            .putExtra("tabImage",encodeTobase64(bitmap));
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(tabIag);
                }
                Intent intent = new Intent("openTabBar");
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
                titletext="tab";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);

            }
        });

        //for weather
        progressDialog = new ProgressDialog(Mpage.this);
        destroyed = false;

        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });



























        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_right_to_left_gone);
                popupActionView.startAnimation(animation1);
                popupActionView.setVisibility(GONE);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        popupLayout.setVisibility(GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                webView.reload();

            }
        });

        main_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupLayout.setVisibility(VISIBLE);
                animationVisible(R.anim.slide_left_to_right_visible, popupActionView);
            }
        });

        addTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToLayoout.getVisibility()== VISIBLE){
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    addToLayoout.startAnimation(animation);
                    addToLayoout.setVisibility(GONE);
                }else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_left_to_right_visible);
                    addToLayoout.startAnimation(animation);
                    addToLayoout.setVisibility(VISIBLE);
                }

            }
        });

        mySitesAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webViewUrl = webView.getUrl();
                Intent intent = new Intent(Mpage.this, MySitesAdder.class);
                intent.putExtra("webViewUrl", webViewUrl);
                startActivity(intent);
            }
        });

        bookmarksAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webview_full_url = webView.getUrl();
                Intent bookmarkIntent = new Intent(Mpage.this, BookmarkAdderAndEdit.class);
                bookmarkIntent.putExtra("webview_full_url", webview_full_url);
                startActivity(bookmarkIntent);
                overridePendingTransition(0, 0);
                bookmarkIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });

        floatingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingLayout.getVisibility() == GONE) {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_left_to_right_visible);
                    floatingLayout.startAnimation(animation);
                    floatingLayout.setVisibility(VISIBLE);
                } else {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    floatingLayout.startAnimation(animation);
                    floatingLayout.setVisibility(GONE);
                }
            }
        });

        desktopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desktopLayout.getVisibility() == GONE) {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_left_to_right_visible);
                    desktopLayout.startAnimation(animation);
                    desktopLayout.setVisibility(VISIBLE);
                } else {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    desktopLayout.startAnimation(animation);
                    desktopLayout.setVisibility(GONE);
                }
            }
        });

        savePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        popupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desktopLayout.getVisibility() == VISIBLE) {
                    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    desktopLayout.startAnimation(animation);
                    desktopLayout.setVisibility(GONE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_right_to_left_gone);
                            popupActionView.startAnimation(animation1);
                            popupActionView.setVisibility(GONE);
                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    popupLayout.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }

                if (floatingLayout.getVisibility() == VISIBLE) {
                    Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    floatingLayout.startAnimation(animation1);
                    floatingLayout.setVisibility(GONE);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_right_to_left_gone);
                            popupActionView.startAnimation(animation1);
                            popupActionView.setVisibility(GONE);
                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    popupLayout.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }

                Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_right_to_left_gone);
                popupActionView.startAnimation(animation1);
                popupActionView.setVisibility(GONE);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        popupLayout.setVisibility(GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                if (desktopalertLayout.getVisibility()== VISIBLE){
                    Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    desktopalertLayout.startAnimation(animation1);
                    desktopalertLayout.setVisibility(GONE);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_right_to_left_gone);
                            popupActionView.startAnimation(animation1);
                            popupActionView.setVisibility(GONE);
                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    popupLayout.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }

                if (desktopLayout.getVisibility()== VISIBLE){
                    Animation animation3=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    desktopLayout.startAnimation(animation1);
                    desktopLayout.setVisibility(GONE);
                    animation3.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_right_to_left_gone);
                            popupActionView.startAnimation(animation1);
                            popupActionView.setVisibility(GONE);
                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    popupLayout.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }

                if (addToLayoout.getVisibility()== VISIBLE){
                    Animation animation4=AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right_to_left_gone);
                    addToLayoout.startAnimation(animation1);
                    addToLayoout.setVisibility(GONE);
                    animation4.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_right_to_left_gone);
                            popupActionView.startAnimation(animation1);
                            popupActionView.setVisibility(GONE);
                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    popupLayout.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        dmton.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(AnimCheckBox animCheckBox, boolean b) {
                if (dmton.isChecked()) {
                    boolean animation = true;
                    dmtoff.setChecked(false, animation);
                    String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
                    webView.getSettings().setUserAgentString(newUA);
                    if (fileurl != null){
                        webView.loadUrl(fileurl);
                    }else {
                        webView.reload();
                    }
                    SharedPreferences preferences2=getSharedPreferences("desktopMode",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences2.edit();
                    editor.putString("modeOn","modeOn");
                    editor.apply();
                    desktopalertLayout.setVisibility(VISIBLE);
                } else {
                    SharedPreferences preferences2=getSharedPreferences("desktopMode",MODE_PRIVATE);
                    boolean animation = true;
                    dmtoff.setChecked(true, animation);
                    String newUA = preferences2.getString("mobileUserAgentString",null);
                    webView.getSettings().setUserAgentString(newUA);
                    webView.reload();
                    SharedPreferences.Editor editor=preferences2.edit();
                    editor.remove("modeOn");
                    editor.apply();
                    desktopalertLayout.setVisibility(VISIBLE);
                }
            }
        });

        dmtoff.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(AnimCheckBox animCheckBox, boolean b) {
                if (dmtoff.isChecked()){
                    SharedPreferences preferences2=getSharedPreferences("desktopMode",MODE_PRIVATE);
                    boolean animation = true;
                    dmton.setChecked(false, animation);
                    String newUA = preferences2.getString("mobileUserAgentString",null);
                    webView.getSettings().setUserAgentString(newUA);
                    webView.reload();
                    desktopalertLayout.setVisibility(VISIBLE);
                }else {
                    boolean animation = true;
                    dmton.setChecked(true, animation);
                    String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
                    webView.getSettings().setUserAgentString(newUA);
                    webView.reload();
                    desktopalertLayout.setVisibility(VISIBLE);
                }
            }
        });

        tvdmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmton.isChecked()){
                    boolean animation = true;
                    dmton.setChecked(false, animation);

                    boolean animation2 = true;
                    dmtoff.setChecked(true, animation2);

                    desktopalertLayout.setVisibility(VISIBLE);
                }else {
                    boolean animation = true;
                    dmton.setChecked(true, animation);

                    boolean animation2 = true;
                    dmtoff.setChecked(false, animation2);

                    desktopalertLayout.setVisibility(VISIBLE);
                }

            }
        });

        tvdmoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmtoff.isChecked()){
                    boolean animation = true;
                    dmton.setChecked(true, animation);

                    boolean animation2 = true;
                    dmtoff.setChecked(false, animation2);
                    desktopalertLayout.setVisibility(VISIBLE);
                }else {
                    boolean animation = true;
                    dmton.setChecked(false, animation);

                    boolean animation2 = true;
                    dmtoff.setChecked(true, animation2);
                    desktopalertLayout.setVisibility(VISIBLE);
                }

            }
        });

        floatSwutcher.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (floatSwutcher.isChecked()){
                    if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(Mpage.this)){
                        @SuppressWarnings("NewApi")
                        Intent myIntent=OverlayPermission.createIntentToRequestOverlayPermission(Mpage.this);
                        startActivityForResult(myIntent,REQUEST_CODE_HOVER_PERMISSION);
                    }else {
                        Intent startHoverIntent = new Intent(Mpage.this, SingleSectionNotificationHoverMenuService.class);
                        SharedPreferences sharedPreferences=getSharedPreferences("floatWebURL",MODE_PRIVATE);
                        SharedPreferences.Editor editor2=sharedPreferences.edit();
                        editor2.putString("url",webView.getUrl());
                        editor2.apply();
                        startService(startHoverIntent);
                        tvfloaState.setText("Turned On");
                    }
                }else {
                    Intent stopHoverService=new Intent(Mpage.this,SingleSectionNotificationHoverMenuService.class);
                    stopService(stopHoverService);
                    tvfloaState.setText("Turned Off");
                }
            }
        });

        SharedPreferences firsttime=getSharedPreferences("firsttime",MODE_PRIVATE);
        if (firsttime.contains("firsttime")){
            if (!location.getText().toString().contains(getResources().getString(R.string.locationtxt))){
                if (isNetworkAvailable()) {
                    getTodayWeather();
                } else {
                    Toast.makeText(Mpage.this, getString(R.string.msg_connection_not_available), Toast.LENGTH_SHORT).show();
                }
            }
        }else {

        }

        saveSharedPrefrence("firsttime","firsttime",
                "","","","","","","");

        ImageView weather_popup = findViewById(R.id.weather_popup_menu);
        weather_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupWeather = new PopupMenu(Mpage.this, weather_popup);
                popupWeather.getMenuInflater().inflate(R.menu.weather_popup, popupWeather.getMenu());

                popupWeather.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case custom_location:
                                String city_name = "";
                                Intent weatherIntent = new Intent(Mpage.this, WeatherCustomLocationSetter.class);
                                weatherIntent.putExtra("city_name", city_name);
                                startActivityForResult(weatherIntent, 300);
                                return true;

                            case weather_detail:
                                if (location.getText().toString().startsWith("--")){
                                    Toast.makeText(Mpage.this, "Please add location first", Toast.LENGTH_SHORT).show();
                                }else {
                                    String open_weather_layout = "";
                                    Intent intent = new Intent(Mpage.this, Weather_activity.class);
                                    intent.putExtra("open_weather_layout", open_weather_layout);
                                    startActivity(intent);
                                }
                                return true;

                            case refresh:
                                if (location.getText().toString().startsWith("--")){
                                    Toast.makeText(Mpage.this, "Please add location first", Toast.LENGTH_SHORT).show();
                                }else {
                                    location.setText(R.string.locationtxt);
                                    condition.setText(R.string.conditiontxt);
                                    temp.setText(R.string.temptxt);
                                    if (isNetworkAvailable()) {
                                        getTodayWeather();
                                    } else {
                                        Toast.makeText(Mpage.this, getString(R.string.msg_connection_not_available), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                return true;

                            case locateMeMpage:
                                if (isNetworkAvailable()) {
                                    getCityByLocation();
                                } else {
                                    Toast.makeText(Mpage.this, getString(R.string.msg_connection_not_available), Toast.LENGTH_SHORT).show();
                                }
                                return true;

                            case clearLocationDataMenu:
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Mpage.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();

                                SharedPreferences preferences2=getSharedPreferences("loaction",MODE_PRIVATE);
                                SharedPreferences.Editor editor1=preferences2.edit();
                                editor1.clear();
                                Constants.DEFAULT_CITY="";
                                recentCity="";
                                editor1.apply();

                                location.setText(R.string.locationtxt);
                                condition.setText(R.string.conditiontxt);
                                temp.setText(R.string.temptxt);
                                return true;
                        }
                        return true;
                    }
                });
                popupWeather.show();

            }
        });

        vpnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Mpage.this, VpnMainActivity.class);
//                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(mReciver, new IntentFilter("settings_result_city_location"));
        //LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(videoReciver1, new IntentFilter("Video1"));
        //LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(videoReciver2, new IntentFilter("Video2"));
        //LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(videoReciver3, new IntentFilter("Video3"));
        //LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(videoReciver4, new IntentFilter("Video4"));
        //LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(videoReciver5, new IntentFilter("Video5"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(sateurl, new IntentFilter("sateurl"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(incoReceiverOnn, new IntentFilter("Incognito_ModeOnn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(incoReceiverOff, new IntentFilter("Incognito_ModeOff"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(RecivedExternalUrl, new IntentFilter("RecivedExternalUrl"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(scanResult, new IntentFilter("scanResult"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(SwipeTurnOn, new IntentFilter("SwipeTurnOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(SwipeTurnOff, new IntentFilter("SwipeTurnOff"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(webTextSize, new IntentFilter("webTextSize"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(mainZoomON, new IntentFilter("mainZoomOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(mainZoomOFF, new IntentFilter("mainZoomOff"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(zoomControlON, new IntentFilter("mainZoomControlOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(zoomControlOFF, new IntentFilter("mainZoomControlOff"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(ultraFastModeON, new IntentFilter("ultraFastModeON"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(ultraFastModeOFF, new IntentFilter("ultraFastModeOFF"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(LightThemeOn, new IntentFilter("LightThemeOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(DarkThemeOn, new IntentFilter("DarkThemeOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(SysDefOn, new IntentFilter("SysDefOn"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(applydarkweb, new IntentFilter("applydarkweb"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(removedarkweb, new IntentFilter("removedarkweb"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(webcangoack, new IntentFilter("webcangoack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(webResume, new IntentFilter("webResume"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(bottomSheetClosed, new IntentFilter("bottomSheetClosed"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(bottomSheetBack, new IntentFilter("bottomSheetBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(downloadActivityBack, new IntentFilter("downloadActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(MainHistoryBack, new IntentFilter("MainHistoryBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(SplitHistoryBack, new IntentFilter("SplitHistoryBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(FloatHistoryBack, new IntentFilter("FloatHistoryBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(bookmarksActivityBack, new IntentFilter("bookmarksActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(addonsActivity, new IntentFilter("addonsActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(SettingsActivity1, new IntentFilter("SettingsActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(FeedbackActivityBack, new IntentFilter("FeedbackActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(AboutActivity, new IntentFilter("AboutActivityBack"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(closeForMoreSite, new IntentFilter("closeForMoreSite"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(PermissionRequest, new IntentFilter("PermissionRequest"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(AppExitDialog1, new IntentFilter("AppExitDialog"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(IndexNo, new IntentFilter("IndexNo"));
        LocalBroadcastManager.getInstance(Mpage.this).registerReceiver(adBlockBroadcast, new IntentFilter("adBlock"));

        inflateFragments();
    }

    private void inflateFragments(){
        speedDialFrag=new SpeedDialFrag();
        fragmentTransaction.add(R.id.speedDialFrameLayout,speedDialFrag);
        fragmentTransaction.add(R.id.newsMpageFrameLayout,new NewsMpageFrag());
        fragmentTransaction.add(R.id.bottomSheetFrameLayout,new BottomSheetLayout());
        fragmentTransaction.commit();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver LightThemeOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTheme(R.style.AppTheme);
        }
    };

    private BroadcastReceiver DarkThemeOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTheme(R.style.DarkTheme);
        }
    };

    private BroadcastReceiver SysDefOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Window window=getWindow();
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.DarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }
    };

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            weatherCard.setVisibility(GONE);
            Intent intent = new Intent("normalColor");
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            weatherCard.setVisibility(View.VISIBLE);
            Intent intent = new Intent("purpleColor");
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
            isHideToolbarView = !isHideToolbarView;
        }

    }

    private BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getTodayWeather();
        }
    };

    private BroadcastReceiver sateurl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recived = intent.getStringExtra("sateurl");
            toolbar.setVisibility(GONE);

            fileurl=recived;

            animationVisible(R.anim.slide_down_search, webview_toolbar);
            mainFullWebLayout.setVisibility(View.VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);

            webView.loadUrl(recived);
        }
    };

    private BroadcastReceiver incoReceiverOnn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CardView incochange3=findViewById(R.id.incochange3);
            incochange3.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
        }
    };

    private BroadcastReceiver incoReceiverOff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences appptheme=getSharedPreferences("Apptheme",MODE_PRIVATE);
            CardView incochange3=findViewById(R.id.incochange3);
            if (appptheme.contains("sysDef")){
                int nightModeFlags=getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags){
                    case Configuration.UI_MODE_NIGHT_YES:
                        incochange3.setCardBackgroundColor(getResources().getColor(R.color.darkcardbackground));
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        incochange3.setCardBackgroundColor(getResources().getColor(R.color.lightCardBackground));
                        break;
                }
            }else if (appptheme.contains("DarkOn")){
                incochange3.setCardBackgroundColor(getResources().getColor(R.color.darkcardbackground));
            }else {
                incochange3.setCardBackgroundColor(getResources().getColor(R.color.lightCardBackground));
            }
        }
    };

    private BroadcastReceiver RecivedExternalUrl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String externalUrl=intent.getStringExtra("externalUrl");
            toolbar.setVisibility(GONE);
            animationVisible(R.anim.slide_down_search, webview_toolbar);
            mainFullWebLayout.setVisibility(View.VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);
            webView.loadUrl(externalUrl);
        }
    };

    private BroadcastReceiver scanResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String scanresult=intent.getStringExtra("scanResult");
            String moreSite=intent.getStringExtra("moreSitesURL");
            toolbar.setVisibility(GONE);
            animationVisible(R.anim.slide_down_search, webview_toolbar);
            mainFullWebLayout.setVisibility(View.VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);

            if (intent.hasExtra("opened settings")){
                settingsFrameLayout.setVisibility(GONE);
                settingsFrameLayout.removeAllViews();
            }

            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.removeAllViews();

            if (moreSite != null){
                webView.loadUrl(moreSite);
            }else {
                webView.loadUrl(scanresult);
            }
        }
    };

    private BroadcastReceiver SwipeTurnOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webswipe_refresh.setEnabled(true);
        }
    };

    private BroadcastReceiver SwipeTurnOff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webswipe_refresh.setEnabled(false);
        }
    };

    private BroadcastReceiver webTextSize = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int points=intent.getIntExtra("webTextSize",0);
            webView.getSettings().setDefaultFixedFontSize(points);
            webView.getSettings().setDefaultFontSize(points);
        }
    };
    private BroadcastReceiver mainZoomON = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().supportZoom();
            webView.getSettings().getBuiltInZoomControls();
            webView.getSettings().setBuiltInZoomControls(true);
        }
    };

    private BroadcastReceiver mainZoomOFF = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().supportZoom();
            webView.getSettings().getBuiltInZoomControls();
            webView.getSettings().setBuiltInZoomControls(false);
        }
    };

    private BroadcastReceiver zoomControlON = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setDisplayZoomControls(true);
        }
    };

    private BroadcastReceiver zoomControlOFF = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setDisplayZoomControls(false);
        }
    };

    private BroadcastReceiver ultraFastModeON = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
    };

    private BroadcastReceiver ultraFastModeOFF = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    };

    private BroadcastReceiver applydarkweb = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
            SharedPreferences webSettings=getSharedPreferences("webViewSettings",MODE_PRIVATE);
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
        }
    };

    private BroadcastReceiver removedarkweb = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(webView.getSettings(),
                        WebSettingsCompat.FORCE_DARK_OFF);
            }else {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
            }
        }
    };

    private BroadcastReceiver webcangoack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webBack();
        }
    };

    private BroadcastReceiver webResume = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.onResume();
        }
    };

    private BroadcastReceiver bottomSheetClosed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            customFragCard.setVisibility(GONE);
        }
    };

    private BroadcastReceiver bottomSheetBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.bottomSheetFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver downloadActivityBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.downloadsFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver MainHistoryBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.historyFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver SplitHistoryBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.splithistoryFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver FloatHistoryBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.floathistoryFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver bookmarksActivityBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.bookmarkFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver addonsActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.addonsFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver SettingsActivity1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.settingsFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }
        }
    };

    private BroadcastReceiver FeedbackActivityBack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.feedbackFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver AboutActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.aboutFrameLayout);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()) {
                Mpage.super.onBackPressed();
            }else {
                if (mainFullWebLayout.getVisibility()==VISIBLE){
                    titletext="web";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }else {
                    titletext="";
                    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                }
            }
        }
    };

    private BroadcastReceiver closeForMoreSite = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            settingsFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.removeAllViews();
            settingsFrameLayout.removeAllViews();
        }
    };

    private BroadcastReceiver PermissionRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("cameraPermission")){
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                addonsFrameLayout.setVisibility(GONE);
                settingsFrameLayout.setVisibility(GONE);
                bottomSheetFrameLayout.setVisibility(GONE);
                customFragCard.setVisibility(GONE);
                addonsFrameLayout.removeAllViews();
                settingsFrameLayout.removeAllViews();
                bottomSheetFrameLayout.removeAllViews();

                if (ActivityCompat.checkSelfPermission(mpage,
                        Manifest.permission.CAMERA) != PackageManager
                        .PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.CAMERA
                        },CAMERA_REQUEST_CODE);
                    }
                }
            }

            if (intent.hasExtra("storagePermission")){
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                addonsFrameLayout.setVisibility(GONE);
                settingsFrameLayout.setVisibility(GONE);
                bottomSheetFrameLayout.setVisibility(GONE);
                customFragCard.setVisibility(GONE);
                addonsFrameLayout.removeAllViews();
                settingsFrameLayout.removeAllViews();
                bottomSheetFrameLayout.removeAllViews();
                if (ActivityCompat.checkSelfPermission(mpage,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },STORAGE_REQUEST_CODE);
                    }
                }
            }
        }
    };

    private BroadcastReceiver AppExitDialog1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppExitDialog appExitDialog=new AppExitDialog();
            appExitDialog.show(getSupportFragmentManager(),"dialog");
        }
    };

    private BroadcastReceiver IndexNo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int tabno=intent.getIntExtra("IndexNo",0);
            tab_bar_opner.setText(""+tabno);
        }
    };

    private BroadcastReceiver adBlockBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String adBlockStatus=intent.getStringExtra("adBlockStatus");
            if (adBlockStatus.equals("adBlockOn")){
                webView.setWebViewClient(new AdBlockWebViewClient());
                webView.reload();
            }else if (adBlockStatus.equals("adBlockOff")){
                webView.setWebViewClient(new SampWebViewClient());
                webView.reload();
            }
        }
    };

    private void webBack(){
        if (webView.canGoBack()) {
            webView.stopLoading();
            webView.goBack();
        } else {
            webView.onPause();
            toolbar.setVisibility(View.VISIBLE);
            animationGone(R.anim.slide_up_search, webview_toolbar);
            mainFullWebLayout.setVisibility(GONE);
            home.setImageDrawable(getResources().getDrawable(R.drawable.search));
            home.setPadding(0,0,0,0);
            titletext="";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(View.VISIBLE);
            mainscroll.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    @Override
    public void speedDialSent(CharSequence input) {
        //fragmentB.updateEditText(input);
        speedDialFrag.speedDialReceiver(input);
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();

        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.DarkTheme);
        }
        if (appptheme.contains("lightOn")){
            setTheme(R.style.AppTheme);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.DarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTodayWeatherUI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(getBaseContext());
    }

    //for weather
    private void preloadWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Mpage.this);

        String lastToday = sp.getString("lastToday", "");
        if (!lastToday.isEmpty()) {
            new TodayWeatherTask(Mpage.this, this, progressDialog).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "cachedResponse", lastToday);
        }
    }

    private void getTodayWeather() {
        new TodayWeatherTask(this, this, progressDialog).execute();
    }

    private void saveLocation(String result) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Mpage.this);
        recentCity = preferences.getString("city", Constants.DEFAULT_CITY);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", result);
        editor.apply();

        if (!recentCity.equals(result)) {
            // New location, update weather
            getTodayWeather();
        }
    }

    private String setWeatherIcon(int actualId, int hourOfDay) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            if (hourOfDay >= 7 && hourOfDay < 20) {
                icon = this.getString(R.string.mpage_weather_sunny);
            } else {
                icon = this.getString(R.string.mpage_weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = this.getString(R.string.mpage_weather_thunder);
                    break;
                case 3:
                    icon = this.getString(R.string.mpage_weather_drizzle);
                    break;
                case 7:
                    icon = this.getString(R.string.mpage_weather_foggy);
                    break;
                case 8:
                    icon = this.getString(R.string.mpage_weather_cloudy);
                    break;
                case 6:
                    icon = this.getString(R.string.mpage_weather_snowy);
                    break;
                case 5:
                    icon = this.getString(R.string.mpage_weather_rainy);
                    break;
            }
        }
        return icon;
    }

    public static String getRainString(JSONObject rainObj) {
        String rain = "0";
        if (rainObj != null) {
            rain = rainObj.optString("3h", "fail");
            if ("fail".equals(rain)) {
                rain = rainObj.optString("1h", "0");
            }
        }
        return rain;
    }

    private ParseResult parseTodayJson(String result) {
        try {
            JSONObject reader = new JSONObject(result);

            final String code = reader.optString("cod");
            if ("404".equals(code)) {
                return ParseResult.CITY_NOT_FOUND;
            }

            String city = reader.getString("name");
            String country = "";
            JSONObject countryObj = reader.optJSONObject("sys");
            if (countryObj != null) {
                country = countryObj.getString("country");
                todayWeather.setSunrise(countryObj.getString("sunrise"));
                todayWeather.setSunset(countryObj.getString("sunset"));
            }
            todayWeather.setCity(city);
            todayWeather.setCountry(country);

            JSONObject coordinates = reader.getJSONObject("coord");
            if (coordinates != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putFloat("latitude", (float) coordinates.getDouble("lon")).putFloat("longitude", (float) coordinates.getDouble("lat")).commit();
            }

            JSONObject main = reader.getJSONObject("main");

            todayWeather.setTemperature(main.getString("temp"));
            todayWeather.setDescription(reader.getJSONArray("weather").getJSONObject(0).getString("description"));
            JSONObject windObj = reader.getJSONObject("wind");
            todayWeather.setWind(windObj.getString("speed"));
            if (windObj.has("deg")) {
                todayWeather.setWindDirectionDegree(windObj.getDouble("deg"));
            } else {
                Log.e("parseTodayJson", "No wind direction available");
                todayWeather.setWindDirectionDegree(null);
            }
            todayWeather.setPressure(main.getString("pressure"));
            todayWeather.setHumidity(main.getString("humidity"));

            JSONObject rainObj = reader.optJSONObject("rain");
            String rain;
            if (rainObj != null) {
                rain = getRainString(rainObj);
            } else {
                JSONObject snowObj = reader.optJSONObject("snow");
                if (snowObj != null) {
                    rain = getRainString(snowObj);
                } else {
                    rain = "0";
                }
            }
            todayWeather.setRain(rain);

            final String idString = reader.getJSONArray("weather").getJSONObject(0).getString("id");
            todayWeather.setId(idString);
            todayWeather.setIcon(setWeatherIcon(Integer.parseInt(idString), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Mpage.this).edit();
            editor.putString("lastToday", result);
            editor.apply();

        } catch (JSONException e) {
            Log.e("JSONException Data", result);
            e.printStackTrace();
            return ParseResult.JSON_EXCEPTION;
        }

        return ParseResult.OK;
    }

    @SuppressLint("SetTextI18n")
    private void updateTodayWeatherUI() {
        try {
            if (todayWeather.getCountry().isEmpty()) {
                preloadWeather();
                return;
            }
        } catch (Exception e) {
            preloadWeather();
            return;
        }
        String city = todayWeather.getCity();
        String country = todayWeather.getCountry();
        location.setText(city + (country.isEmpty() ? "" : ", " + country));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Mpage.this);

        // Temperature
        float temperature = UnitConvertor.convertTemperature(Float.parseFloat(todayWeather.getTemperature()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }

        // Rain
        double rain = Double.parseDouble(todayWeather.getRain());

        // Wind
        double wind;
        try {
            wind = Double.parseDouble(todayWeather.getWind());
        } catch (Exception e) {
            e.printStackTrace();
            wind = 0;
        }

        temp.setText(new DecimalFormat("0.#").format(temperature) + " " + sp.getString("unit", "°C"));
        condition.setText("(" + todayWeather.getDescription().substring(0, 1).toUpperCase() +
                todayWeather.getDescription().substring(1) + ")");
        weatherIcon.setText(todayWeather.getIcon());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void getCityByLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explanation not needed, since user requests this themmself

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }

        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.getting_location));
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        locationManager.removeUpdates(Mpage.this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });
            progressDialog.show();
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } else {
            showLocationSettingsDialog();
        }
    }

    private void showLocationSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode==MY_PERMISSIONS_ACCESS_FINE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCityByLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        progressDialog.hide();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Log.e("LocationManager", "Error while trying to stop listening for location updates. This is probably a permissions issue", e);
        }
        Log.i("LOCATION (" + location.getProvider().toUpperCase() + ")", location.getLatitude() + ", " + location.getLongitude());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        new ProvideCityNameTask(Mpage.this, this, progressDialog).execute("coords", Double.toString(latitude), Double.toString(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class TodayWeatherTask extends GenericRequestTaskTwo {
        public TodayWeatherTask(Context context, Mpage activity, ProgressDialog progressDialog) {
            super(context, activity, progressDialog);
        }

        @Override
        protected void onPreExecute() {
            loading = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TaskOutput output) {
            super.onPostExecute(output);
            // Update widgets
            AbstractWidgetProvider.updateWidgets(Mpage.this);
            DashClockWeatherExtension.updateDashClock(Mpage.this);
        }

        @Override
        protected ParseResult parseResponse(String response) {
            return parseTodayJson(response);
        }

        @Override
        protected String getAPIName() {
            return "weather";
        }

        @Override
        protected void updateMainUI() {
            updateTodayWeatherUI();
        }
    }

    public class ProvideCityNameTask extends GenericRequestTaskTwo {

        public ProvideCityNameTask(Context context, Mpage activity, ProgressDialog progressDialog) {
            super(context, activity, progressDialog);
        }

        @Override
        protected void onPreExecute() { /*Nothing*/ }

        @Override
        protected String getAPIName() {
            return "weather";
        }

        @Override
        protected ParseResult parseResponse(String response) {
            Log.i("RESULT", response.toString());
            try {
                JSONObject reader = new JSONObject(response);

                final String code = reader.optString("cod");
                if ("404".equals(code)) {
                    Log.e("Geolocation", "No city found");
                    return ParseResult.CITY_NOT_FOUND;
                }

                String city = reader.getString("name");
                String country = "";
                JSONObject countryObj = reader.optJSONObject("sys");
                if (countryObj != null) {
                    country = ", " + countryObj.getString("country");
                }

                saveLocation(city + country);

            } catch (JSONException e) {
                Log.e("JSONException Data", response);
                e.printStackTrace();
                return ParseResult.JSON_EXCEPTION;
            }

            return ParseResult.OK;
        }

        @Override
        protected void onPostExecute(TaskOutput output) {
            /* Handle possible errors only */
            handleTaskOutput(output);
        }
    }

    public static long saveLastUpdateTime(SharedPreferences sp) {
        Calendar now = Calendar.getInstance();
        sp.edit().putLong("lastUpdate", now.getTimeInMillis()).apply();
        return now.getTimeInMillis();
    }

    public static String formatTimeWithDayIfNotToday(Context context, long timeInMillis) {
        Calendar now = Calendar.getInstance();
        Calendar lastCheckedCal = new GregorianCalendar();
        lastCheckedCal.setTimeInMillis(timeInMillis);
        Date lastCheckedDate = new Date(timeInMillis);
        String timeFormat = android.text.format.DateFormat.getTimeFormat(context).format(lastCheckedDate);
        if (now.get(Calendar.YEAR) == lastCheckedCal.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == lastCheckedCal.get(Calendar.DAY_OF_YEAR)) {
            // Same day, only show time
            return timeFormat;
        } else {
            return android.text.format.DateFormat.getDateFormat(context).format(lastCheckedDate) + " " + timeFormat;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();

        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            contextMenu.setHeaderTitle(webViewHitTestResult.getExtra());
            contextMenu.add(0, 1, 0, "Download Image")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            String DownloadImageURL = webViewHitTestResult.getExtra();

                            if(URLUtil.isValidUrl(DownloadImageURL)){
                                String filename2 = URLUtil.guessFileName(DownloadImageURL, null, null);

                                Intent intent = new Intent(Mpage.this, DownloadStarter.class);
                                intent.putExtra("fileurl", DownloadImageURL);
                                intent.putExtra("filename", filename2);
                                intent.putExtra("fileSize","Er.");
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Mpage.this,"Sorry.. Something Went Wrong.",Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });

            contextMenu.add(2,2,2,"Share Link")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_TITLE, webView.getTitle());
                            i.putExtra(Intent.EXTRA_TEXT, webViewHitTestResult.getExtra());
                            startActivity(Intent.createChooser(i, "On the way you are😊"));
                            return false;
                        }
                    });

            contextMenu.add(3,3,3,"Copy Link")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(webView.getTitle(), webViewHitTestResult.getExtra());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(Mpage.this, "Link copied to clipboard👍", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
        }else if (webViewHitTestResult.getType() == WebView.HitTestResult.ANCHOR_TYPE || webViewHitTestResult.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE || webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // Menu options for a hyperlink.
            //set the header title to the link url
            contextMenu.setHeaderTitle(webViewHitTestResult.getExtra());
            contextMenu.add(4, 4, 4, "Share Link")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_TITLE, webView.getTitle());
                            i.putExtra(Intent.EXTRA_TEXT, webViewHitTestResult.getExtra());
                            startActivity(Intent.createChooser(i, "On the way you are😊"));
                            return false;
                        }
                    });
            contextMenu.add(6, 6, 6, "Copy Link")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(webView.getTitle(), webViewHitTestResult.getExtra());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(Mpage.this, "Link copied to clipboard👍", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        webView.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_HOVER_PERMISSION==requestCode){
            mPermissionsRequested=true;
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra("result");
                toolbar.setVisibility(GONE);
                animationVisible(R.anim.slide_down_search, webview_toolbar);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                fileurl=url;
                webView.loadUrl(url);
            }else {
                appBarLayout.setExpanded(true);
            }
        }

        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                String city_location = data.getStringExtra("result_city_location");

                saveLocation(city_location);

                location.setText(R.string.locationtxt);
                condition.setText(R.string.conditiontxt);
                temp.setText(R.string.temptxt);

                saveSharedPrefrence("loaction", "loccity", city_location,
                        "cityname", String.valueOf(location),
                        "temperature", String.valueOf(temp),
                        "condition", String.valueOf(condition));
                //loadWeather(city_location);
            }
        }

        if (requestCode==30){
            if (resultCode==RESULT_OK){
                String url = data.getStringExtra("newsUrl");
                toolbar.setVisibility(GONE);
                animationVisible(R.anim.slide_down_search, webview_toolbar);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                fileurl=url;
                webView.loadUrl(url);
            }
        }

        if (requestCode==155){
            if (resultCode==RESULT_OK){
                String url=data.getStringExtra("newssUrl");
                toolbar.setVisibility(GONE);
                animationVisible(R.anim.slide_down_search, webview_toolbar);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                fileurl=url;
                webView.loadUrl(url);
            }
        }

        if (requestCode==786){
            if (resultCode==RESULT_OK){
                String url = data.getStringExtra("moreSitesURL");
                toolbar.setVisibility(GONE);
                animationVisible(R.anim.slide_down_search, webview_toolbar);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                webView.loadUrl(url);
            }
        }

        if (requestCode==50){
            if (resultCode==RESULT_OK){
                String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
                webView.getSettings().setUserAgentString(newUA);
                webView.clearCache(true);
                webView.clearFormData();
                webView.clearMatches();
                webView.clearSslPreferences();
                webView.reload();
                dmton.setChecked(true);
                dmtoff.setChecked(false);
            }else if (resultCode==11){
                SharedPreferences preferences2=getSharedPreferences("desktopMode",MODE_PRIVATE);
                String newUA = preferences2.getString("mobileUserAgentString",null);
                webView.getSettings().setUserAgentString(newUA);
                webView.clearCache(true);
                webView.reload();
                dmton.setChecked(false);
                dmtoff.setChecked(true);
            }
        }

        if (requestCode==REQ_CODE_SPEECH_INPUT){
            if (resultCode == RESULT_OK && null != data) {
                toolbar.setVisibility(GONE);
                animationVisible(R.anim.slide_down_search, webview_toolbar);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                ArrayList result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String voiceURL= (String) result.get(0);
                webView.loadUrl("https://www.google.com/search?q="+voiceURL);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void communicate(String s) {
        if (s.equals("bottomSheetClosed")){
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
        }

        //if (s.equals("downloadActivity")){
        //    downloadFrameLayout.setVisibility(VISIBLE);
        //    titletext="downloadActivity";
        //    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
        //    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
        //    FragmentManager fm=getSupportFragmentManager();
        //    DownloadActivity qrScanComplete=new DownloadActivity();
        //    fm.beginTransaction().replace(R.id.downloadsFrameLayout,qrScanComplete).commit();
        //}
//
        //if (s.equals("downloadsClosed")){
        //    titletext="bottomSheet";
        //    Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
        //    LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
        //    downloadFrameLayout.setVisibility(GONE);
        //    downloadFrameLayout.removeAllViews();
        //}
//
        //if (s.equals("downloadsClosedWithFrag")){
        //    if (mainFullWebLayout.getVisibility()==VISIBLE){
        //        titletext="web";
        //        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
        //        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
        //    }else {
        //        titletext="";
        //        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
        //        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
        //    }
        //    downloadFrameLayout.setVisibility(GONE);
        //    customFragCard.setVisibility(GONE);
        //    downloadFrameLayout.removeAllViews();
        //}

        if (s.equals("historyActivity")){
            titletext="MainHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            historyFrameLayout.setVisibility(VISIBLE);
            floathistoryFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(GONE);
            FragmentManager fm=getSupportFragmentManager();
            HistoryActivity qrScanComplete=new HistoryActivity();
            fm.beginTransaction().replace(R.id.historyFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("historyActivityWithSettings")){
            titletext="MainHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            historyFrameLayout.setVisibility(VISIBLE);
            floathistoryFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(GONE);
            FragmentManager fm=getSupportFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putString("opened settings","opened settings");
            HistoryActivity qrScanComplete=new HistoryActivity();
            qrScanComplete.setArguments(bundle);
            fm.beginTransaction().replace(R.id.historyFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("MainHistoryClose")){
            if (settingsFrameLayout.getVisibility()==VISIBLE){
                titletext="SettingsActivity";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="bottomSheet";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            historyFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("MainHistoryCloseWithFrag")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            historyFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("MainHistoryCloseWithFragAndSetting")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            historyFrameLayout.setVisibility(GONE);
            settingsFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
            settingsFrameLayout.removeAllViews();
        }

        if (s.equals("OpenFloatHistoryWithoutSettings")){
            titletext="FloatHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            floathistoryFrameLayout.setVisibility(VISIBLE);
            historyFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            FloatHistory qrScanComplete=new FloatHistory();
            fm.beginTransaction().replace(R.id.floathistoryFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("OpenFloatHistoryWithSettings")){
            titletext="FloatHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            floathistoryFrameLayout.setVisibility(VISIBLE);
            historyFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(VISIBLE);
            Bundle bundle=new Bundle();
            bundle.putString("opened settings","opened settings");
            FragmentManager fm=getSupportFragmentManager();
            FloatHistory qrScanComplete=new FloatHistory();
            qrScanComplete.setArguments(bundle);
            fm.beginTransaction().replace(R.id.floathistoryFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("FloatHistoryClose")){
            if (settingsFrameLayout.getVisibility()==VISIBLE){
                titletext="SettingsActivity";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="bottomSheet";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            floathistoryFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("FloatHistoryCloseWithFrag")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            floathistoryFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("FloatHistoryCloseWithFragAndSetting")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            floathistoryFrameLayout.setVisibility(GONE);
            settingsFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            floathistoryFrameLayout.removeAllViews();
            settingsFrameLayout.removeAllViews();
        }

        if (s.equals("OpenSplitHistoryWithoutSettings")){
            titletext="SplitHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            floathistoryFrameLayout.setVisibility(GONE);
            historyFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(VISIBLE);
            customFragCard.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            SplitHistory qrScanComplete=new SplitHistory();
            fm.beginTransaction().replace(R.id.splithistoryFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("OpenSplitHistoryWithSettings")){
            titletext="SplitHistory";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            floathistoryFrameLayout.setVisibility(GONE);
            historyFrameLayout.setVisibility(GONE);
            splithistoryFrameLayout.setVisibility(VISIBLE);
            customFragCard.setVisibility(VISIBLE);
            Bundle bundle=new Bundle();
            bundle.putString("opened settings","opened settings");
            FragmentManager fm=getSupportFragmentManager();
            SplitHistory qrScanComplete=new SplitHistory();
            qrScanComplete.setArguments(bundle);
            fm.beginTransaction().replace(R.id.splithistoryFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("SplitHistoryClose")){
            if (settingsFrameLayout.getVisibility()==VISIBLE){
                titletext="SettingsActivity";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="bottomSheet";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            splithistoryFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("SplitHistoryCloseWithFrag")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            splithistoryFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            historyFrameLayout.removeAllViews();
            splithistoryFrameLayout.removeAllViews();
            floathistoryFrameLayout.removeAllViews();
        }

        if (s.equals("SplitHistoryCloseWithFragAndSetting")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            splithistoryFrameLayout.setVisibility(GONE);
            settingsFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            splithistoryFrameLayout.removeAllViews();
            settingsFrameLayout.removeAllViews();
        }

        if (s.equals("bookmarkActivity")){
            titletext="bookmarksActivity";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            bookmarkFrameLayout.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            BookmarkActivity qrScanComplete=new BookmarkActivity();
            fm.beginTransaction().replace(R.id.bookmarkFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("bookmarkActivityWithSettings")){
            titletext="bookmarksActivity";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            bookmarkFrameLayout.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putString("opened settings","opened settings");
            BookmarkActivity qrScanComplete=new BookmarkActivity();
            qrScanComplete.setArguments(bundle);
            fm.beginTransaction().replace(R.id.bookmarkFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("bookmarkClose")){
            if (settingsFrameLayout.getVisibility()==VISIBLE){
                titletext="SettingsActivity";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="bottomSheet";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            bookmarkFrameLayout.setVisibility(GONE);
            bookmarkFrameLayout.removeAllViews();
        }

        if (s.equals("bookmarkCloseWithFrag")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            bookmarkFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bookmarkFrameLayout.removeAllViews();
            bottomSheetFrameLayout.removeAllViews();
        }

        if (s.equals("bookmarkCloseWithFragAndSett")){
            if (mainFullWebLayout.getVisibility()==VISIBLE){
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }else {
                titletext="";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            }
            bookmarkFrameLayout.setVisibility(GONE);
            customFragCard.setVisibility(GONE);
            bottomSheetFrameLayout.setVisibility(GONE);
            settingsFrameLayout.setVisibility(GONE);
            bottomSheetFrameLayout.removeAllViews();
            bookmarkFrameLayout.removeAllViews();
            settingsFrameLayout.removeAllViews();
        }

        if (s.equals("SettingsActivity")){
            titletext="SettingsActivity";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            settingsFrameLayout.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            SettingsActivity qrScanComplete=new SettingsActivity();
            fm.beginTransaction().replace(R.id.settingsFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("SettingsClose")){
            titletext="bottomSheet";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            settingsFrameLayout.setVisibility(GONE);
            settingsFrameLayout.removeAllViews();
        }

        if (s.equals("FeedbackActivity")){
            titletext="FeedbackActivity";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            feedbackFrameLayout.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            Feedback qrScanComplete=new Feedback();
            fm.beginTransaction().replace(R.id.feedbackFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("FeedbackActivityClose")){
            titletext="bottomSheet";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            feedbackFrameLayout.setVisibility(GONE);
            feedbackFrameLayout.removeAllViews();
        }

        if (s.equals("AboutActivity")){
            titletext="AboutActivity";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            aboutFrameLayout.setVisibility(VISIBLE);
            FragmentManager fm=getSupportFragmentManager();
            About qrScanComplete=new About();
            fm.beginTransaction().replace(R.id.aboutFrameLayout,qrScanComplete).commit();
        }

        if (s.equals("AboutActivityClose")){
            titletext="bottomSheet";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            aboutFrameLayout.setVisibility(GONE);
            aboutFrameLayout.removeAllViews();
        }

        if (s.startsWith("http")){
            Animation slide_down_search= AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down_search);
            webview_toolbar.startAnimation(slide_down_search);
            webview_toolbar.setVisibility(VISIBLE);
            mainFullWebLayout.setVisibility(VISIBLE);
            titletext="web";
            Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
            mainFullLayout.setVisibility(GONE);
            toolbar.setVisibility(GONE);
            webback.setVisibility(VISIBLE);
            webView.clearView();
            webView.loadUrl(s);
        }
        if (s.equals("deleteMySites")){
            speedDialSent("deleteMySites");
        }

        if (s.equals("editMySites")){
            speedDialSent("editMySites");
        }

        if (s.equals("clearAllMySites")){
            speedDialSent("clearAllMySites");
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        switch (titletext){
            case "web":
                webBack();
                break;

            case "tab":
                Intent intent = new Intent("closeTabBar");
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
                titletext="web";
                break;

            case "bottomSheet":
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.bottomSheetFrameLayout);
                if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()){
                    super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "downloadActivity":
                Fragment fragment1=getSupportFragmentManager().findFragmentById(R.id.downloadsFrameLayout);
                if (!(fragment1 instanceof IOnBackPressed) || !((IOnBackPressed)fragment1).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "MainHistory":
                Fragment fragment2=getSupportFragmentManager().findFragmentById(R.id.historyFrameLayout);
                if (!(fragment2 instanceof IOnBackPressed) || !((IOnBackPressed)fragment2).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "SplitHistory":
                Fragment fragment3=getSupportFragmentManager().findFragmentById(R.id.splithistoryFrameLayout);
                if (!(fragment3 instanceof IOnBackPressed) || !((IOnBackPressed)fragment3).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "FloatHistory":
                Fragment fragment4=getSupportFragmentManager().findFragmentById(R.id.floathistoryFrameLayout);
                if (!(fragment4 instanceof IOnBackPressed) || !((IOnBackPressed)fragment4).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "bookmarksActivity":
                Fragment fragment5=getSupportFragmentManager().findFragmentById(R.id.bookmarkFrameLayout);
                if (!(fragment5 instanceof IOnBackPressed) || !((IOnBackPressed)fragment5).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "SettingsActivity":
                Fragment fragment6=getSupportFragmentManager().findFragmentById(R.id.settingsFrameLayout);
                if (!(fragment6 instanceof IOnBackPressed) || !((IOnBackPressed)fragment6).onBackPressed()) {
                    super.onBackPressed();
                }
                break;

            case "addonsActivity":
                Fragment fragment7=getSupportFragmentManager().findFragmentById(R.id.addonsFrameLayout);
                if (!(fragment7 instanceof IOnBackPressed) || !((IOnBackPressed)fragment7).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "FeedbackActivity":
                Fragment fragment8=getSupportFragmentManager().findFragmentById(R.id.feedbackFrameLayout);
                if (!(fragment8 instanceof IOnBackPressed) || !((IOnBackPressed)fragment8).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            case "AboutActivity":
                Fragment fragment9=getSupportFragmentManager().findFragmentById(R.id.aboutFrameLayout);
                if (!(fragment9 instanceof IOnBackPressed) || !((IOnBackPressed)fragment9).onBackPressed()) {
                    Mpage.super.onBackPressed();
                }else {
                    if (mainFullWebLayout.getVisibility()==VISIBLE){
                        titletext="web";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }else {
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                    }
                }
                break;

            default:
                AppExitDialog exitDialog=new AppExitDialog();
                exitDialog.show(getSupportFragmentManager(),"dialog");
                break;
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void saveSharedPrefrence(String preferenceName, String putString1, String sideString1,
                                    String putString2, String sideString2
            , String putString3, String sideString3, String putString4, String sideString4) {
        SharedPreferences preferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(putString1, sideString1);
        editor.putString(putString2, sideString2);
        editor.putString(putString3, sideString3);
        editor.putString(putString4, sideString4);
        editor.apply();
    }

    public void sendBrodcast(String action, String name, String sideName) {
        Intent intent = new Intent(action).putExtra(name, sideName);
        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
    }

    private void animationVisible(int id, View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), id);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    private void animationGone(int id, View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), id);
        view.startAnimation(animation);
        view.setVisibility(GONE);
    }

    public void doNothing(View view) {
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
            fileurl = url;
            webView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            cancelLoading.setVisibility(View.VISIBLE);
            mainPopup.setVisibility(GONE);
            home.setImageDrawable(getResources().getDrawable(R.drawable.home));
            home.setPadding(20,20,20,20);

            Intent intent = new Intent("normalColor");
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            Intent intent=new Intent("tabtitle").putExtra("title",fileTitle);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);

            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addHistory(new Record(fileTitle, fileurl, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            webView.setVisibility(View.VISIBLE);
            mainPopup.setVisibility(View.VISIBLE);
            cancelLoading.setVisibility(GONE);
            progressBar.setProgress(0);

            SharedPreferences firstTime=getSharedPreferences("firstTime",MODE_PRIVATE);
            SharedPreferences.Editor firstTimeEditor=firstTime.edit();
            if (!firstTime.contains("firstTime")){
                firstTimeEditor.putString("firstTime","firstTime");
                firstTimeEditor.apply();

                AdBlockSwitchingDialog adBlockSwitchingDialog=new AdBlockSwitchingDialog();
                adBlockSwitchingDialog.show(getSupportFragmentManager(),"dialog");
            }

            if (!firstTime.contains("firstTimeDarkMode")){
                SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
                if (appptheme.contains("DarkOn")){
                    NightModeSwitchingDialog nightModeSwitchingDialog=new NightModeSwitchingDialog();
                    nightModeSwitchingDialog.show(getSupportFragmentManager(),"dialog");

                    firstTimeEditor.putString("firstTimeDarkMode","firstTimeDarkMode");
                    firstTimeEditor.apply();
                }else if (appptheme.contains("sysDef")){
                    int nightModeFlags=getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags){
                        case Configuration.UI_MODE_NIGHT_YES:
                            NightModeSwitchingDialog nightModeSwitchingDialog=new NightModeSwitchingDialog();
                            nightModeSwitchingDialog.show(getSupportFragmentManager(),"dialog");

                            firstTimeEditor.putString("firstTimeDarkMode","firstTimeDarkMode");
                            firstTimeEditor.apply();
                            break;
                    }
                }
            }
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(Mpage.this);
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
            webview_text_button.setText(R.string.error);
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
            fileurl = url;
            webView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            cancelLoading.setVisibility(View.VISIBLE);
            mainPopup.setVisibility(GONE);
            home.setImageDrawable(getResources().getDrawable(R.drawable.home));
            home.setPadding(20,20,20,20);

            Intent intent = new Intent("normalColor");
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            Intent intent=new Intent("tabtitle").putExtra("title",fileTitle);
            LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(intent);

            SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addHistory(new Record(fileTitle, url, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            webView.setVisibility(View.VISIBLE);
            mainPopup.setVisibility(View.VISIBLE);
            cancelLoading.setVisibility(GONE);
            progressBar.setProgress(0);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(Mpage.this);
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
            webview_text_button.setText(R.string.error);
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
                webview_text_button.setText(R.string.loading);
                webview_toolbar.setVisibility(View.VISIBLE);
                mainFullWebLayout.setVisibility(View.VISIBLE);
                titletext="web";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                mainFullLayout.setVisibility(GONE);
                //refresh_button.setVisibility(View.GONE);
                //cancel_button_toolbar.setVisibility(View.VISIBLE);
                toolbar.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);
                cancelLoading.setVisibility(View.VISIBLE);
                mainPopup.setVisibility(GONE);
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
                webview_text_button.setText(webView.getTitle());
                //cancel_button_toolbar.setVisibility(View.GONE);
                //refresh_button.setVisibility(View.VISIBLE);
                google_icon_toolbar.setVisibility(View.VISIBLE);
                webswipe_refresh.setRefreshing(false);
                progressBar.setVisibility(View.INVISIBLE);
                vinwon = "" + webView.getOriginalUrl();
                cancelLoading.setVisibility(GONE);
                mainPopup.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
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

                if (webView.getOriginalUrl()==null || webView.getOriginalUrl().equals("null") || webView.getOriginalUrl().equals("")){
                        webView.stopLoading();
                        animationGone(R.anim.slide_up_search, webview_toolbar);
                        mainFullWebLayout.setVisibility(GONE);
                        titletext="";
                        Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                        LocalBroadcastManager.getInstance(Mpage.this).sendBroadcast(backtxt);
                        mainFullLayout.setVisibility(VISIBLE);
                        mainscroll.fullScroll(ScrollView.FOCUS_UP);
                        toolbar.setVisibility(VISIBLE);
                        webView.onPause();
                        home.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        home.setPadding(0,0,0,0);
                }
            }
            super.onProgressChanged(view, progress);
        }
    }
}
