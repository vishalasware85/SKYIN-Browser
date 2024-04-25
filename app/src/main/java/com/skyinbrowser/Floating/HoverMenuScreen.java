/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skyinbrowser.Floating;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floating_bubble.Content;
import com.example.floating_bubble.HoverView;
import com.skyinbrowser.CustomJavaFiles.ScondaryPrgressBar;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.ProgressBarAnimation;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import im.delight.android.webview.AdvancedWebView;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A screen that is displayed in our Hello World Hover Menu.
 */
public class HoverMenuScreen implements Content {

    private Context mContext;
    private LayoutInflater layoutInflate;
    private View view;
    private EditText floatedittext;
    private ImageView floatSearchButton;
    private LinearLayout floatSearchBarLayout,searchBarGoogle,openedSearchBar,
    floatingAnimationEdt;
    private AdvancedWebView floatWebView;
    private RelativeLayout floatWebLayout;
    private SwipeRefreshLayout floatSwipeRefresh;
    private View toolbarBottomView;
    private ImageButton webback,home,webnext,googleIconFloat,searchBack,googleIconToolbar,refresh,cancelLoading;
    private ProgressBar progressBar;
    private TextView webviewText;
    private CardView incoChange1;
    private LinearLayout incoChange2,floatToolbar;
    RecordAction action;

    public static final String QUERY_URL = "http://google.com/complete/search?output=toolbar&q=";
    ArrayList<HashMap<String, String>> userList = new ArrayList<>();
    HashMap<String,String> completeSuggestion = new HashMap<>();
    ListView lv;
    ListAdapter adapter;

    public HoverMenuScreen(@NonNull Context context, @NonNull String pageTitle) {
        mContext = context.getApplicationContext();
        layoutInflate =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflate = createScreenView();

        floatedittext=view.findViewById(R.id.float_search_editText);
        floatSearchButton=view.findViewById(R.id.floatsearch_button);
        floatSearchBarLayout=view.findViewById(R.id.floatsearch_barlayout);
        floatWebView=view.findViewById(R.id.floatWebView);
        floatWebLayout=view.findViewById(R.id.float_webLayout);
        floatSwipeRefresh=view.findViewById(R.id.float_webview_swiperefresh);
        progressBar=view.findViewById(R.id.floatProgressBar);
        toolbarBottomView=view.findViewById(R.id.toolbarBottomView);
        webback =view.findViewById(R.id.float_webback);
        home=view.findViewById(R.id.float_home);
        webnext=view.findViewById(R.id.float_webnext);
        googleIconFloat=view.findViewById(R.id.google_icon_floar);
        searchBarGoogle=view.findViewById(R.id.searchBarGoogle);
        openedSearchBar=view.findViewById(R.id.floatingOpenedSearchBar);
        webviewText=view.findViewById(R.id.float_webview_text);
        floatingAnimationEdt=view.findViewById(R.id.floatanimationEdt);
        lv=view.findViewById(R.id.float_suggestionList);
        incoChange1=view.findViewById(R.id.floatincochange1);
        incoChange2=view.findViewById(R.id.floatchangeinco2);
        floatToolbar=view.findViewById(R.id.floatToolbar);
        searchBack=view.findViewById(R.id.floatSearchBack);
        googleIconToolbar=view.findViewById(R.id.floatGoogleIconToolbar);
        refresh=view.findViewById(R.id.floatRefresh);
        cancelLoading=view.findViewById(R.id.floatcancelLoading);

        adservers();

        SharedPreferences references=mContext.getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
        if (references.contains("floatIncoOn")){
            incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardviewinco));
            incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.cardviewinco));
        }else {
            incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.purewhite));
            incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.purewhite));
        }

        BroadcastReceiver incoReceiverOnn = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardviewinco));
                incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.cardviewinco));
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(incoReceiverOnn,
                new IntentFilter("float_Incognito_ModeOnn"));
        BroadcastReceiver incoReceiverOff = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.purewhite));
                incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.purewhite));
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(incoReceiverOff,
                new IntentFilter("float_Incognito_ModeOff"));

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);
                floatingAnimationEdt.setVisibility(View.GONE);
                openedSearchBar.setVisibility(View.GONE);
            }
        });

        SharedPreferences preferences1=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);

        if (preferences1.contains("DarkOn")){
            floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
            webviewText.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
            incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.darkWindowBackground));
            floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
            progressBar.getProgressDrawable().setColorFilter(
                    getView().getResources().getColor(R.color.purpleStatusBar), PorterDuff.Mode.SRC_IN);
            incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
            floatedittext.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
        }
        if (preferences1.contains("lightOn")){
            floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
            webviewText.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
            incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
            floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
            progressBar.getProgressDrawable().setColorFilter(
                    getView().getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
            incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
            floatedittext.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
        }
        if (preferences1.contains("sysDef")){
            int nightModeFlags=mContext.getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                    webviewText.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
                    incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.darkWindowBackground));
                    floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                    progressBar.getProgressDrawable().setColorFilter(
                            getView().getResources().getColor(R.color.purpleStatusBar), PorterDuff.Mode.SRC_IN);
                    incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                    floatedittext.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                    webviewText.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
                    incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                    floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                    progressBar.getProgressDrawable().setColorFilter(
                            getView().getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                    incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                    floatedittext.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
                    break;
            }
        }

        BroadcastReceiver LightThemeOn = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                webviewText.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
                incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                progressBar.getProgressDrawable().setColorFilter(
                        getView().getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                floatedittext.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(LightThemeOn,
                new IntentFilter("LightThemeOn"));
        BroadcastReceiver darkThemeOn = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                webviewText.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
                incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.darkWindowBackground));
                floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                progressBar.getProgressDrawable().setColorFilter(
                        getView().getResources().getColor(R.color.purpleStatusBar), PorterDuff.Mode.SRC_IN);
                incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                floatedittext.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(darkThemeOn,
                new IntentFilter("DarkThemeOn"));

        BroadcastReceiver SysDefOn = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int nightModeFlags=mContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags){
                    case Configuration.UI_MODE_NIGHT_YES:
                        floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                        webviewText.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
                        incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.darkWindowBackground));
                        floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                        progressBar.getProgressDrawable().setColorFilter(
                                getView().getResources().getColor(R.color.purpleStatusBar), PorterDuff.Mode.SRC_IN);
                        incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.darkcardbackground));
                        floatedittext.setTextColor(mContext.getResources().getColor(R.color.darktextcolor));
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        floatToolbar.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                        webviewText.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
                        incoChange1.setCardBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                        floatingAnimationEdt.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                        progressBar.getProgressDrawable().setColorFilter(
                                getView().getResources().getColor(R.color.horizontal_progress_bar_color), PorterDuff.Mode.SRC_IN);
                        incoChange2.setBackgroundColor(mContext.getResources().getColor(R.color.lightCardBackground));
                        floatedittext.setTextColor(mContext.getResources().getColor(R.color.lighttextColor));
                        break;
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(SysDefOn,
                new IntentFilter("SysDefOn"));

        SharedPreferences webSettings=mContext.getSharedPreferences("webViewSettings",MODE_PRIVATE);

        BroadcastReceiver applydarkweb = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (preferences1.contains("DarkOn")){
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
                            WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_ON);
                        }else {
                            floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                        }
                    }else {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_OFF);
                        }else {
                            floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                        }
                    }
                }
                if (preferences1.contains("lightOn")){
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                        WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                WebSettingsCompat.FORCE_DARK_OFF);
                    }else {
                        floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                }
                if (preferences1.contains("sysDef")){
                    int nightModeFlags=mContext.getResources().getConfiguration().uiMode &
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
                                    WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                            WebSettingsCompat.FORCE_DARK_ON);
                                }else {
                                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                                }
                            }else {
                                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                                    WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                            WebSettingsCompat.FORCE_DARK_OFF);
                                }else {
                                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                                }
                            }
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                                WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                        WebSettingsCompat.FORCE_DARK_OFF);
                            }else {
                                floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                            }
                            break;
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(applydarkweb,
                new IntentFilter("applydarkweb"));

        BroadcastReceiver removedarkweb = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                }else {
                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(removedarkweb,
                new IntentFilter("removedarkweb"));


        action = new RecordAction(context);
        action.open(true);

        floatSwipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        floatSwipeRefresh.setColorSchemeColors(getView().getResources().getColor(R.color.swipe_reefresh_color11),
                getView().getResources().getColor(R.color.swipe_reefresh_color22),
                getView().getResources().getColor(R.color.swipe_reefresh_color33),
                getView().getResources().getColor(R.color.swipe_reefresh_color44));

        floatSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                floatWebView.reload();
            }
        });

        floatedittext.setOnEditorActionListener(editorActionListener);

        floatedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                AsyncDownloader downloader=new AsyncDownloader();
                downloader.execute();
                userList.clear();
                if (lv.getVisibility()==View.GONE){
                    lv.setVisibility(View.VISIBLE);
                }else {

                }
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);
        floatWebView.getSettings().setJavaScriptEnabled(true);
        floatWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        floatWebView.getSettings().setDomStorageEnabled(true);
        floatWebView.getSettings().setAllowFileAccess(true);
        floatWebView.getSettings().setUseWideViewPort(true);
        floatWebView.getSettings().setSavePassword(true);
        floatWebView.getSettings().setSaveFormData(true);
        floatWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        floatWebView.getSettings().setEnableSmoothTransition(true);
        floatWebView.getSettings().setLoadWithOverviewMode(true);
        floatWebView.getSettings().setUseWideViewPort(true);
        floatSwipeRefresh.setRefreshing(false);

        if (webSettings.contains("adBlockOn")){
            floatWebView.setWebViewClient(new AdBlockWebViewClient());
        }else {
            floatWebView.setWebViewClient(new SampWebViewClient());
        }
        floatWebView.setWebChromeClient(new MyChrome());

        if (webSettings.contains("textSize")){
            int points=webSettings.getInt("textSize",0);
            floatWebView.getSettings().setDefaultFontSize(points);
            floatWebView.getSettings().setDefaultFixedFontSize(points);
        }else {
            int fixedFon=webSettings.getInt("defaultfontSize11",0);
            int fontSiz=webSettings.getInt("defaultfontSize22",0);
            floatWebView.getSettings().setDefaultFontSize(fontSiz);
            floatWebView.getSettings().setDefaultFixedFontSize(fixedFon);
        }
        if (webSettings.contains("swipeRefOff")){
            floatSwipeRefresh.setEnabled(false);
        }else {
            floatSwipeRefresh.setEnabled(true);
        }
        if (webSettings.contains("mainZoomOff")){
            floatWebView.getSettings().setSupportZoom(false);
            floatWebView.getSettings().supportZoom();
            floatWebView.getSettings().getBuiltInZoomControls();
            floatWebView.getSettings().setBuiltInZoomControls(false);
        }else {
            floatWebView.getSettings().setSupportZoom(true);
            floatWebView.getSettings().supportZoom();
            floatWebView.getSettings().getBuiltInZoomControls();
            floatWebView.getSettings().setBuiltInZoomControls(true);
        }
        if (webSettings.contains("zoomControl")){
            floatWebView.getSettings().setDisplayZoomControls(true);
        }else {
            floatWebView.getSettings().setDisplayZoomControls(false);
        }
        if (webSettings.contains("ultraFastModeON")){
            floatWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            floatWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                floatWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            floatWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }else {
            floatWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
            floatWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                floatWebView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                floatWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            floatWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        if (preferences1.contains("DarkOn")){
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
                    WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON);
                }else {
                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                }
            }else {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                }else {
                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                }
            }
        }
        if (preferences1.contains("lightOn")){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                        WebSettingsCompat.FORCE_DARK_OFF);
            }else {
                floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
            }
        }
        if (preferences1.contains("sysDef")){
            int nightModeFlags=mContext.getResources().getConfiguration().uiMode &
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
                            WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_ON);
                        }else {
                            floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,paint);
                        }
                    }else {
                        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                            WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                    WebSettingsCompat.FORCE_DARK_OFF);
                        }else {
                            floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                        }
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                        WebSettingsCompat.setForceDark(floatWebView.getSettings(),
                                WebSettingsCompat.FORCE_DARK_OFF);
                    }else {
                        floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                    break;
            }
        }

        BroadcastReceiver SwipeTurnOn = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatSwipeRefresh.setEnabled(true);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(SwipeTurnOn, new IntentFilter("SwipeTurnOn"));

        BroadcastReceiver SwipeTurnOff = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatSwipeRefresh.setEnabled(false);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(SwipeTurnOff, new IntentFilter("SwipeTurnOff"));

        BroadcastReceiver webTextSize = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int points=intent.getIntExtra("webTextSize",0);
                floatWebView.getSettings().setDefaultFixedFontSize(points);
                floatWebView.getSettings().setDefaultFontSize(points);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(webTextSize, new IntentFilter("webTextSize"));

        BroadcastReceiver mainZoomON = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setSupportZoom(true);
                floatWebView.getSettings().supportZoom();
                floatWebView.getSettings().getBuiltInZoomControls();
                floatWebView.getSettings().setBuiltInZoomControls(true);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mainZoomON, new IntentFilter("mainZoomOn"));

        BroadcastReceiver mainZoomOFF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setSupportZoom(false);
                floatWebView.getSettings().supportZoom();
                floatWebView.getSettings().getBuiltInZoomControls();
                floatWebView.getSettings().setBuiltInZoomControls(false);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mainZoomOFF, new IntentFilter("mainZoomOff"));

        BroadcastReceiver zoomControlON = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setDisplayZoomControls(true);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(zoomControlON, new IntentFilter("mainZoomControlOn"));

        BroadcastReceiver zoomControlOFF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setDisplayZoomControls(false);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(zoomControlOFF, new IntentFilter("mainZoomControlOff"));

        BroadcastReceiver ultraFastModeON = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                floatWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    floatWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                } else {
                    floatWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                floatWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(ultraFastModeON, new IntentFilter("ultraFastModeON"));

        BroadcastReceiver ultraFastModeOFF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                floatWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.NORMAL);
                floatWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    floatWebView.setLayerType(View.LAYER_TYPE_NONE, null);
                } else {
                    floatWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                floatWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(ultraFastModeOFF, new IntentFilter("ultraFastModeOFF"));

        BroadcastReceiver AdBlock = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String adBlockStatus=intent.getStringExtra("adBlockStatus");
                if (adBlockStatus != null){
                    if (adBlockStatus.equals("adBlockOn")){
                        floatWebView.setWebViewClient(new AdBlockWebViewClient());
                        floatWebView.reload();
                    }else if (adBlockStatus.equals("adBlockOff")){
                        floatWebView.setWebViewClient(new SampWebViewClient());
                        floatWebView.reload();
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(AdBlock, new IntentFilter("adBlock"));

        SharedPreferences sharedPreferences=mContext.getSharedPreferences("floatWebURL",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (sharedPreferences.contains("url")){
            String url=sharedPreferences.getString("url",null);
            floatWebView.loadUrl(url);
            floatWebLayout.setVisibility(View.VISIBLE);
            toolbarBottomView.setVisibility(View.VISIBLE);
            webback.setVisibility(View.VISIBLE);

            editor.remove("url");
            editor.apply();
        }

        floatSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatSearchBarLayout.setGravity(Gravity.TOP);

                if (floatedittext.getText().toString().equals("")) {
                    Toast.makeText(mContext,
                            "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);
                } else {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);

                    String url = "https://www.google.co.in/search?q=" + floatedittext.getText().toString();

                    floatWebView.loadUrl(url);
                    floatWebLayout.setVisibility(View.VISIBLE);
                    toolbarBottomView.setVisibility(View.VISIBLE);

                    googleIconFloat.setVisibility(View.GONE);
                    searchBarGoogle.setVisibility(View.VISIBLE);

                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_up_search);
                    floatingAnimationEdt.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            floatingAnimationEdt.setVisibility(View.GONE);
                            openedSearchBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatWebView.reload();
            }
        });

        cancelLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatWebView.stopLoading();
                progressBar.setProgress(0);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        webback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatWebView.canGoBack()) {
                    floatWebView.stopLoading();
                    floatWebView.goBack();
                } else {
                    googleIconFloat.setVisibility(View.VISIBLE);
                    searchBarGoogle.setVisibility(View.GONE);
                    floatWebLayout.setVisibility(View.GONE);
                }
            }
        });

        webnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floatWebLayout.getVisibility()==View.VISIBLE) {
                    if (floatWebView.canGoForward()) {
                        floatWebView.stopLoading();
                        floatWebView.goForward();
                    }
                }else {
                    searchBarGoogle.setVisibility(View.VISIBLE);
                    googleIconFloat.setVisibility(View.GONE);
                    floatWebLayout.setVisibility(View.VISIBLE);
                    floatWebView.setVisibility(View.VISIBLE);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatWebView.stopLoading();
                floatWebLayout.setVisibility(View.GONE);
                searchBarGoogle.setVisibility(View.GONE);
                googleIconFloat.setVisibility(View.VISIBLE);
            }
        });

        webviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openedSearchBar.setVisibility(View.VISIBLE);
                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_down_search);
                floatingAnimationEdt.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        floatingAnimationEdt.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        toolbarBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_up_search);
                floatingAnimationEdt.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        floatingAnimationEdt.setVisibility(View.GONE);
                        openedSearchBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });
    }
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    if (floatedittext.getText().toString().equals("")) {
                        Toast.makeText(mContext,
                                "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);
                    } else if (floatedittext.getText().toString().startsWith("http://www.")) {
                        if (floatedittext.getText().toString().endsWith(".com")) {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);

                            String url = floatedittext.getText().toString();

                            floatWebView.loadUrl(url);
                            floatWebLayout.setVisibility(View.VISIBLE);
                            toolbarBottomView.setVisibility(View.VISIBLE);
                            webback.setVisibility(View.VISIBLE);

                            googleIconFloat.setVisibility(View.GONE);
                            searchBarGoogle.setVisibility(View.VISIBLE);

                            Animation animation= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_up_search);
                            floatingAnimationEdt.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    floatingAnimationEdt.setVisibility(View.GONE);
                                    openedSearchBar.setVisibility(View.GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                    } else if (floatedittext.getText().toString().startsWith("https://www.")) {
                        if (floatedittext.getText().toString().endsWith(".com")) {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);

                            String url = floatedittext.getText().toString();

                            floatWebView.loadUrl(url);
                            floatWebLayout.setVisibility(View.VISIBLE);
                            toolbarBottomView.setVisibility(View.VISIBLE);
                            webback.setVisibility(View.VISIBLE);

                            googleIconFloat.setVisibility(View.GONE);
                            searchBarGoogle.setVisibility(View.VISIBLE);

                            Animation animation= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_up_search);
                            floatingAnimationEdt.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    floatingAnimationEdt.setVisibility(View.GONE);
                                    openedSearchBar.setVisibility(View.GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                    }else if (floatedittext.getText().toString().startsWith("www.")) {
                        if (floatedittext.getText().toString().endsWith(".com")) {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);

                            String url = "https://"+floatedittext.getText().toString();
                            floatWebView.loadUrl(url);
                            floatWebLayout.setVisibility(View.VISIBLE);
                            toolbarBottomView.setVisibility(View.VISIBLE);
                            webback.setVisibility(View.VISIBLE);

                            googleIconFloat.setVisibility(View.GONE);
                            searchBarGoogle.setVisibility(View.VISIBLE);

                            Animation animation= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_up_search);
                            floatingAnimationEdt.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    floatingAnimationEdt.setVisibility(View.GONE);
                                    openedSearchBar.setVisibility(View.GONE);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                    }else {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(floatedittext.getWindowToken(), 0);

                        String url = "https://www.google.co.in/search?q=" + floatedittext.getText().toString();
                        floatWebView.loadUrl(url);
                        floatWebLayout.setVisibility(View.VISIBLE);
                        toolbarBottomView.setVisibility(View.VISIBLE);
                        webback.setVisibility(View.VISIBLE);

                        googleIconFloat.setVisibility(View.GONE);
                        searchBarGoogle.setVisibility(View.VISIBLE);

                        Animation animation= AnimationUtils.loadAnimation(mContext,
                                R.anim.slide_up_search);
                        floatingAnimationEdt.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) { }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                floatingAnimationEdt.setVisibility(View.GONE);
                                openedSearchBar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) { }
                        });
                    }
                    break;
            }
            return true;
        }
    };


    private class AsyncDownloader extends AsyncTask<Object,String,Integer> {

        @Override
        protected Integer doInBackground(Object... objects) {
            XmlPullParser receivedData=tryDownloadingXmlData();
            int recordsFound=tryParsingXmlData(receivedData);
            return null;
        }

        private XmlPullParser tryDownloadingXmlData() {
            floatedittext = view.findViewById(R.id.float_search_editText);
            try {
                String urllll=QUERY_URL+floatedittext.getText().toString();
                urllll = urllll.replace(" ", "%20");
                URL xmlUrl=new URL(urllll);
                XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
                receivedData.setInput(xmlUrl.openStream(),null);
                return receivedData;
            }catch (XmlPullParserException e){

            }catch (IOException e){

            }
            return null;
        }

        private int tryParsingXmlData(XmlPullParser receivedData) {
            if (receivedData != null){
                try {
                    return processReceivedData(receivedData);
                }catch (XmlPullParserException e){

                }catch (IOException e){

                }
            }
            return 0;
        }

        private int processReceivedData(XmlPullParser xmlData) throws IOException, XmlPullParserException {

            int recordFound=8;

            //Find Xml Records in thhe xml records
            String CompleteSuggestion="";

            int eventType=-1;

            while (eventType != XmlResourceParser.END_DOCUMENT){
                String tagName=xmlData.getName();

                switch (eventType){
                    case XmlResourceParser.START_TAG:
                        if (tagName.equals("suggestion")){
                            completeSuggestion = new HashMap<>();
                            CompleteSuggestion=xmlData.getAttributeValue(null,"data");
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equals("suggestion")){
                            recordFound++;
                            publishProgress(CompleteSuggestion);
                        }
                        //switch (tagName){
                        //    case "suggestion": completeSuggestion.put("suggestion",CompleteSuggestion);
                        //        break;
                        //    case "CompleteSuggestion":
                        //        if(completeSuggestion!=null)
                        //            userList.add(completeSuggestion);
                        //        break;
                        //}

                        break;
                }
                eventType=xmlData.next();
            }

            if (recordFound==0){
                publishProgress();
                userList.clear();
            }

            return recordFound;
        }


        @Override
        protected void onProgressUpdate(String... values){
            if (values.length==0){

            }
            if (values.length==1){
                String data=values[0];
                completeSuggestion.put("data",data);
                userList.add(completeSuggestion);

                //pass the items
                adapter = new SimpleAdapter(mContext,
                        userList, R.layout.suggestion_list_row,
                        new String[]{"data"},
                        new int[]{R.id.data});
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView=view.findViewById(R.id.data);
                        String suggest=textView.getText().toString();
                        floatedittext.setText(suggest);
                    }
                });
            }
            super.onProgressUpdate(values);
        }
    }


    class AdBlockWebViewClient extends WebViewClient {
        AdBlockWebViewClient() { }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            floatWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                floatWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            cancelLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=mContext.getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addFloatHistory(new Record(fileTitle, url, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            floatWebView.setVisibility(View.VISIBLE);
            cancelLoading.setVisibility(GONE);
            progressBar.setProgress(0);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.startsWith("http")) {
                Uri parsedUri = Uri.parse(url);
                PackageManager packageManager = mContext.getPackageManager();
                Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
                if (browseIntent.resolveActivity(packageManager) != null) {
                    browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browseIntent);
                }
                // if no activity found, try to parse intent://
                else {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
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
                            mContext.startActivity(marketIntent);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
            webviewText.setText(R.string.error);
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
            floatWebView.onResume();
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                floatWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String fileTitle=view.getTitle();
            SharedPreferences preferences=mContext.getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
            if (preferences.contains("mainIncoOn")){
            }else {
                action.addHistory(new Record(fileTitle, url, System.currentTimeMillis()));
                //browserController.updateAutoComplete();
            }
            floatWebView.setVisibility(View.VISIBLE);
            cancelLoading.setVisibility(GONE);
            progressBar.setProgress(0);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.startsWith("http")) {
                Uri parsedUri = Uri.parse(url);
                PackageManager packageManager = mContext.getPackageManager();
                Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
                if (browseIntent.resolveActivity(packageManager) != null) {
                    browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browseIntent);
                }
                // if no activity found, try to parse intent://
                else {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
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
                            mContext.startActivity(marketIntent);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
            webviewText.setText(R.string.error);
        }
    }

    StringBuilder adservers;
    private void adservers(){
        String strLine2="";
        adservers = new StringBuilder();

        InputStream fis2 = mContext.getResources().openRawResource(R.raw.adblockserverlist);
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
        @Override
        public void onProgressChanged(WebView view, int progress) {
            ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(progressBar, 1000);
            mProgressAnimation.setProgress(65);

            if (progress > 65){
                mProgressAnimation.setProgress(progress);
            }

            if (progressBar.getProgress() == 100){
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setProgress(0);
            }

            if (progress < 100 && progressBar.getVisibility() == View.INVISIBLE) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);
                webviewText.setText("Loading...");
            }
            if (progress == 100) {
                floatSwipeRefresh.setRefreshing(false);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                webviewText.setText(floatWebView.getTitle());
                floatedittext.setText(floatWebView.getUrl());
            }
            super.onProgressChanged(view, progress);
        }
    }


    @NonNull
    private LayoutInflater createScreenView() {

        SharedPreferences preferences1=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (preferences1.contains("DarkOn")){
            mContext.setTheme(R.style.DarkTheme);
        }else if (preferences1.contains("lightOn")){
            mContext.setTheme(R.style.AppTheme);
        }else if (preferences1.contains("sysDef")){
            int nightModeFlags=mContext.getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    mContext.setTheme(R.style.DarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    mContext.setTheme(R.style.AppTheme);
                    break;
            }
        }

        view=layoutInflate.inflate(R.layout.floating_activity,null);
        return layoutInflate;
    }

    // Make sure that this method returns the SAME View.  It should NOT create a new View each time
    // that it is invoked.
    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        // No-op.
    }

    @Override
    public void onHidden() {
        // No-op.
    }
}
