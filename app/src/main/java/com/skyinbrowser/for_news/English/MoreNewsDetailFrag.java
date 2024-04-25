package com.skyinbrowser.for_news.English;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.skyinbrowser.DownloadManager.DownloadStarter;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

import java.net.URISyntaxException;

import im.delight.android.webview.AdvancedWebView;

import static android.content.Context.MODE_PRIVATE;

public class MoreNewsDetailFrag extends SuperBottomSheetFragment implements AppBarLayout.OnOffsetChangedListener {

    private Context context;
    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title;
    private boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor;
    private AdvancedWebView webView;
    private FragmentToActivity mCallback;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SharedPreferences appptheme=getActivity().getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getActivity().getWindow();
        if (appptheme.contains("DarkOn")){
            getActivity().setTheme(R.style.DarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            getActivity().setTheme(R.style.AppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                window.setStatusBarColor(getResources().getColor(R.color.transparentStatus));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    getActivity().setTheme(R.style.DarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
                        window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    getActivity().setTheme(R.style.AppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                    }
                    break;
            }
        }
        View view = inflater.inflate(R.layout.news_detail_frag, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        context=view.getContext();
        setHasOptionsMenu(true);

        toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = view.findViewById(R.id.date_behavior);
        titleAppbar = view.findViewById(R.id.title_appbar);
        imageView = view.findViewById(R.id.backdrop);
        appbar_title = view.findViewById(R.id.title_on_appbar);
        appbar_subtitle = view.findViewById(R.id.subtitle_on_appbar);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        title = view.findViewById(R.id.title);
        webView = view.findViewById(R.id.mainweb);
        progressBar=view.findViewById(R.id.moreNewsProgressBar);

        mUrl = getArguments().getString("url");
        mImg = getArguments().getString("img");
        mTitle = getArguments().getString("title");
        mDate = getArguments().getString("date");
        mSource = getArguments().getString("source");
        mAuthor = getArguments().getString("author");

        SharedPreferences apptheme=getActivity().getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (apptheme.contains("DarkOn")){
            progressBar.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.lightlightestcolor),PorterDuff.Mode.SRC_IN);
        }else {
            progressBar.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.horizontal_progress_bar_color),PorterDuff.Mode.SRC_IN);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);

        String author;
        if (mAuthor != null){
            author = " \u2022 " + mAuthor;
        } else {
            author = "";
        }

        time.setText(mSource + author + " \u2022 " + Utils.DateToTimeFormat(mDate));

        initWebView(mUrl);

        return view;
    }

    private void initWebView(String url){
        SharedPreferences webSettings=getActivity().getSharedPreferences("webViewSettings",MODE_PRIVATE);
        SharedPreferences appptheme=getActivity().getSharedPreferences("AppTheme",MODE_PRIVATE);
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
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_NONE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.setWebViewClient(new Browser_home());
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

        SharedPreferences preferences1=getActivity().getSharedPreferences("desktopMode",MODE_PRIVATE);
        if (preferences1.contains("modeOn")){
            String newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
            webView.getSettings().setUserAgentString(newUA);
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url2, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String filename2 = URLUtil.guessFileName(url2, contentDisposition, mimetype);
                Intent intent = new Intent(getActivity(), DownloadStarter.class);
                intent.putExtra("fileurl", url2);
                intent.putExtra("filename", filename2);
                startActivity(intent);
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_news, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.view_web){
            sendData(mUrl);
            dismiss();
            return true;
        }

        else if (id == R.id.share){
            try{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plan");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n" + mUrl + "\n" + "\n" + "Shared from the SKY!N Browser" + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "On the way you are"));
            }catch (Exception e){
                Toast.makeText(context, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public float getCornerRadius() {
        return getResources().getDimension(R.dimen.btsheet);
    }

    @Override
    public boolean animateCornerRadius() {
        return true;
    }

    @Override
    public boolean isSheetAlwaysExpanded() {
        return true;
    }

    @Override
    public boolean isSheetCancelableOnTouchOutside() {
        return true;
    }

    class Browser_home extends WebViewClient {
        Browser_home() {
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view,url,favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view,url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("intent://")){
                try {
                    Context context=view.getContext();
                    Intent intent=Intent.parseUri(url,Intent.URI_INTENT_SCHEME);
                    if (intent != null){
                        context.startActivity(intent);
                    }else {
                        String fallbackUrl=intent.getStringExtra("browser_fallback_url");
                        view.loadUrl(fallbackUrl);
                    }
                    return true;
                }catch (URISyntaxException e){
                }
            }
            CookieManager.getInstance().setAcceptCookie(true);
            return false;
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
            return BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            getActivity().setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getActivity().getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
}
