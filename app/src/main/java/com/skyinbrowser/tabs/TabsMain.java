package com.skyinbrowser.tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.skyinbrowser.BuildConfig;
import com.skyinbrowser.CustomJavaFiles.TabScrollView;
import com.suke.widget.SwitchButton;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TabsMain extends TabActivity {
    private static int tabIndex = 1;
    private static int tabSpec = 1;
    private TabHost tabHost;
    Button tabBtn;
    private TabWidget tabWidget;
    ArrayList<TabSpec> list = new ArrayList<TabSpec>();
    TabSpec setContent;
    Button closeAllTabs,tabSetter;
    CardView tabview;
    LinearLayout layoutaddtab;
    ImageButton closeTabBar;
    String websiteurl;
    int tbno;
    TextView tabBotn;
    private SwitchButton switchButton;
    TabScrollView tabscroolview;
    private LayoutInflater layoutInflate;
    private TextView textView,curtab;
    private boolean focusRght=false;

    public static Activity tabsmain;

    String fileurlstr;

    FirebaseRemoteConfig mRemoteConfig;

    //remote config fields
    private static final String CONFIG_IS_PROMO_ON = "crash_app";

    public static final String KEY_UPDATE_ENABLE ="is_update";
    public static final String KEY_UPDATE_VERSION ="version";
    public static final String KEY_UPDATE_URL ="update_url";

    private String backtext="";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.DarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
            }
        }
        if (appptheme.contains("lightOn")){
            setTheme(R.style.AppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
            }
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.DarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                    }
                    break;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);



        tabHost = getTabHost();

        tabsmain=this;

        tabview = findViewById(R.id.tabview);
        closeTabBar = findViewById(R.id.closeTabBar);
        closeAllTabs = findViewById(R.id.closeAllTabs);
        switchButton=findViewById(R.id.incoSwitchSwitchTab);
        tabscroolview=findViewById(R.id.tabScrollView);
        tabWidget=findViewById(android.R.id.tabs);

        Animation animation = AnimationUtils.loadAnimation(TabsMain.this, R.anim.slide_up_fade_in);
        tabWidget.startAnimation(animation);
        tabWidget.setVisibility(View.VISIBLE);

        tabscroolview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP){
                    tabscroolview.startScrollerTask();
                }
                return false;
            }
        });

        layoutInflate = LayoutInflater.from(TabsMain.this);
        layoutaddtab=(LinearLayout) layoutInflate.inflate(R.layout.tab_event,tabWidget,false);
        tabBotn=layoutaddtab.findViewById(R.id.add_button);

        //for crash firebase
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        Resources res = getResources();

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("crash_app", res.getBoolean(R.bool.crash_config_promo_on));
        mRemoteConfig.setDefaultsAsync(defaults);

        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(100)
                .build();
        mRemoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        mRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()){
                            boolean updated=task.getResult();
                        }else {

                        }
                    }
                });

        if (mRemoteConfig.getBoolean(CONFIG_IS_PROMO_ON)){
            AlertDialog alertDialogcrash=new AlertDialog.Builder(this)
                    .setTitle(".............")
                    .setMessage("This app is no longer available.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Mpage.mpage.finish();
                            TabsMain.tabsmain.finish();
                        }
                    }).create();
            alertDialogcrash.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    Mpage.mpage.finish();
                    TabsMain.tabsmain.finish();
                }
            });
            alertDialogcrash.setCanceledOnTouchOutside(false);
            alertDialogcrash.show();
        }

        //for update firebase
        FirebaseRemoteConfig updateRemoteConfig = FirebaseRemoteConfig.getInstance();

        HashMap<String, Object> updateDefaults = new HashMap<>();
        updateDefaults.put("is_update", res.getBoolean(R.bool.update_app_enabled));
        updateDefaults.put("update_url",res.getString(R.string.update_app_url));
        updateDefaults.put("version",res.getString(R.string.update_app_version));
        updateRemoteConfig.setDefaultsAsync(updateDefaults);

        updateRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()){
                            boolean updated=task.getResult();
                        }else {

                        }
                    }
                });

        if (updateRemoteConfig.getBoolean(KEY_UPDATE_ENABLE)){
            String updateURL=updateRemoteConfig.getString(KEY_UPDATE_URL);
            String updateVersion=updateRemoteConfig.getString(KEY_UPDATE_VERSION);
            String currVersion= BuildConfig.VERSION_NAME;

            if (!currVersion.equals(updateVersion)){
                AlertDialog alertDialog=new AlertDialog.Builder(this)
                        .setTitle("Hey dear user,")
                        .setMessage(getResources().getString(R.string.updateMessage1)+updateVersion+". "+
                                getResources().getString(R.string.updateMessage2))
                        .setPositiveButton("Go Forward", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=" + getPackageName())));
                                }catch (ActivityNotFoundException e){
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        }).setNegativeButton("Stay Tuned", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }else {

            }
        }

        SharedPreferences firstDarkWeb=getSharedPreferences("webViewSettings",MODE_PRIVATE);
        SharedPreferences.Editor firstDarkWebEditor=firstDarkWeb.edit();
        if (!firstDarkWeb.contains("firstDarkWeb")){
            firstDarkWebEditor.putString("applydarkweb","applydarkweb");
            firstDarkWebEditor.putString("adBlockOn","adBlockOn");
            firstDarkWebEditor.putString("firstDarkWeb","firstDarkWeb");
            firstDarkWebEditor.apply();
        }

        Intent tabIntent = new Intent(TabsMain.this, Mpage.class);

        TabSpec spec = tabHost.newTabSpec(String.valueOf(tabSpec));
        spec.setContent(tabIntent);
        spec.setIndicator(layoutaddtab);
        tabHost.addTab(spec);
        list.add(spec);

        textView=findViewById(R.id.tabIndextxt);
        curtab=findViewById(R.id.currtabtxt);
        textView.setText(""+tabIndex);
        Intent intent=new Intent("IndexNo").putExtra("IndexNo",tabIndex);
        LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
        curtab.setText(""+tbno);
        tabHost.getTabWidget().getChildAt(tbno).getLayoutParams().width=
                (int) getResources().getDimension(R.dimen.tabwidth);

        ((Button) findViewById(R.id.add_tab)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                addTab();
            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                tabHost.getCurrentTab();
                tbno = tabHost.getCurrentTab();
                textView.setText(""+tabIndex);
                Intent intent=new Intent("IndexNo").putExtra("IndexNo",tabIndex);
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
                curtab.setText(""+tbno);

                tabHost.getTabWidget().getChildAt(tbno).getLayoutParams().width=
                        (int) getResources().getDimension(R.dimen.tabwidth);

                centerTab(tbno);
                //for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                //    SharedPreferences appptheme=getSharedPreferences("Apptheme",MODE_PRIVATE);
                //    Window window=getWindow();
                //    if (appptheme.contains("DarkOn")){
                //        tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#2E2E2E"));
                //    }else {
                //        tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                //    }
                //}
                //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#7c4dff"));
                new java.util.Timer().schedule(new java.util.TimerTask(){
                    @Override
                    public void run() { runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabBarClose();
                        }}); }}, 100);
            }
        });

        SharedPreferences preferences=getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
        if (preferences.contains("mainIncoOn")){
            switchButton.setChecked(true);
        }else {
            switchButton.setChecked(false);
        }

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    SharedPreferences preferences = getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("mainIncoOn", "mainIncoOn");
                    editor.apply();

                    Intent intent=new Intent("Incognito_ModeOnn");
                    LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
                }else {
                    SharedPreferences preferences = getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("mainIncoOn");
                    editor.apply();

                    Intent intent=new Intent("Incognito_ModeOff");
                    LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
                }
            }
        });

        Button removeBtn = findViewById(R.id.remove_tab);
        removeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tabIndex--;
                list.remove(tbno);
                tabHost.clearAllTabs();
                int nTabIndex = 0;       // <== ***SECOND EDIT***
                for (TabSpec spec : list) {
                    tabHost.addTab(spec);
                    tabHost.setCurrentTab(tbno-1);
                    curtab.setText(""+tbno);    // <== ***THIRD EDIT***
                    tbno=nTabIndex;
                }
                textView.setText(""+tabIndex);
                Intent intent=new Intent("IndexNo").putExtra("IndexNo",tabIndex);
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);

                SharedPreferences urlcheck=getSharedPreferences("mainweb url",MODE_PRIVATE);
                SharedPreferences.Editor editor= urlcheck.edit();
                editor.remove("url");
                editor.apply();

                if (tabHost.getCurrentTab() == -1) {
                    list.clear();
                    tabIndex = 0;

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tabIndex++;
                                            tabSpec++;
                                            layoutaddtab=(LinearLayout) layoutInflate.inflate(R.layout.tab_event,null);

                                            Intent tabIntent = new Intent(TabsMain.this, Mpage.class);

                                            TabSpec spec = tabHost.newTabSpec(String.valueOf(tabSpec));
                                            spec.setContent(tabIntent);
                                            spec.setIndicator(layoutaddtab);
                                            tabHost.addTab(spec);
                                            list.add(spec);

                                            Animation animation = AnimationUtils.loadAnimation(TabsMain.this, R.anim.slide_up_fade_in);
                                            tabWidget.startAnimation(animation);
                                            tabWidget.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }, 20
                    );
                }
            }
        });

        tabview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tabBarClose();
            }
        });

        closeTabBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tabBarClose();
            }
        });

        closeAllTabs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(TabsMain.this, R.anim.slide_up_fade_out);
                tabWidget.startAnimation(animation);
                tabWidget.setVisibility(View.INVISIBLE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tabHost.clearAllTabs();
                        tabIndex = 0;
                        list.clear();

                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tabIndex++;
                                                tabSpec++;
                                                layoutaddtab=(LinearLayout) layoutInflate.inflate(R.layout.tab_event,null);
                                                Intent tabIntent = new Intent(TabsMain.this, Mpage.class);

                                                TabSpec spec = tabHost.newTabSpec(String.valueOf(tabSpec));
                                                spec.setContent(tabIntent);
                                                spec.setIndicator(layoutaddtab);
                                                tabHost.addTab(spec);
                                                list.add(spec);

                                            }
                                        });
                                    }
                                }, 50
                        );
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });


                SharedPreferences urlcheck=getSharedPreferences("mainweb url",MODE_PRIVATE);
                SharedPreferences.Editor editor= urlcheck.edit();
                editor.remove("url");
                editor.apply();
            }
        });

        if (savedInstanceState == null && Intent.ACTION_SEND.equals(getIntent().getAction())
                && getIntent().getType() != null && "text/plain".equals(getIntent().getType())) {
            String externalLink = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            if (externalLink != null
                    && (externalLink.contains("https://") || externalLink.contains("http://"))) {
                tabIndex++;
                tabSpec++;
                layoutaddtab=(LinearLayout) layoutInflate.inflate(R.layout.tab_event,null);
                tabBotn=layoutaddtab.findViewById(R.id.add_button);
                tabBotn.setText("Tab " + tabIndex);
                Intent tabIntenthi = new Intent(TabsMain.this, Mpage.class);

                TabSpec spechi = tabHost.newTabSpec(String.valueOf(tabSpec));
                spec.setContent(tabIntenthi);
                spec.setIndicator(layoutaddtab);
                tabHost.addTab(spechi);
                list.add(spechi);


                Intent intent1=new Intent("RecivedExternalUrl").putExtra("externalUrl",externalLink);
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent1);
            } else {
                Toast.makeText(this, "Not an openable link", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(openTabBar, new IntentFilter("openTabBar"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(closedTabBar, new IntentFilter("closeTabBar"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(normalColor, new IntentFilter("normalColor"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(purpleColor, new IntentFilter("purpleColor"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(incoSwitchONN, new IntentFilter("Incognito_ModeOnn"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(incoSwitchOFF, new IntentFilter("Incognito_ModeOff"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(LightThemeOn, new IntentFilter("LightThemeOn"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(DarkThemeOn, new IntentFilter("DarkThemeOn"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(SysDefOn, new IntentFilter("SysDefOn"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(backtxt, new IntentFilter("backtext"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(tabTitle, new IntentFilter("tabtitle"));
        LocalBroadcastManager.getInstance(TabsMain.this).registerReceiver(tabImage, new IntentFilter("tabImage"));

        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#7c4dff"));

        if(savedInstanceState==null){
            Intent listhhh=new Intent("tabmain").putExtra("fileurl", fileurlstr);
            LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(listhhh);
        }
    }

    private BroadcastReceiver LightThemeOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Window window=getWindow();
            setTheme(R.style.AppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
            }
            recreate();
        }
    };

    private BroadcastReceiver DarkThemeOn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Window window=getWindow();
            setTheme(R.style.DarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
            }
            recreate();
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
                    }
                    recreate();
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                    }
                    recreate();
                    break;
            }
        }
    };

    private BroadcastReceiver normalColor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Window window=getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
                if (appptheme.contains("sysDef")){
                    int nightModeFlags=getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags){
                        case Configuration.UI_MODE_NIGHT_YES:
                            window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            window.setStatusBarColor(getResources().getColor(R.color.normalStatusBar));
                            break;
                    }
                }else if (appptheme.contains("DarkOn")){
                    window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                }else {
                    window.setStatusBarColor(getResources().getColor(R.color.normalStatusBar));
                }

            }
        }
    };

    private BroadcastReceiver purpleColor = new BroadcastReceiver() {
        @SuppressLint("ResourceType")
        @Override
        public void onReceive(Context context, Intent intent) {
            Window window=getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
                if (appptheme.contains("sysDef")){
                    int nightModeFlags=getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags){
                        case Configuration.UI_MODE_NIGHT_YES:
                            window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                            break;
                    }
                }else if (appptheme.contains("DarkOn")){
                    window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                }else {
                    window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                }

            }
        }
    };

    private BroadcastReceiver openTabBar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tabview.setVisibility(View.VISIBLE);
            centerTab(tbno);
            Animation animation = AnimationUtils.loadAnimation(TabsMain.this, R.anim.slide_up_fade_in);
            animation.setDuration(500);
            tabWidget.startAnimation(animation);
            tabWidget.setVisibility(View.VISIBLE);
            if (focusRght){
                new java.util.Timer().schedule(new java.util.TimerTask(){
                    @Override
                    public void run() { runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabscroolview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }}); }}, 10);
                focusRght=false;
            }
            Window window=getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
                if (appptheme.contains("sysDef")){
                    int nightModeFlags=getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags){
                        case Configuration.UI_MODE_NIGHT_YES:
                            window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                            break;
                    }
                }else if (appptheme.contains("DarkOn")){
                    window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                }else {
                    window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                }

            }
        }
    };

    private BroadcastReceiver closedTabBar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tabBarClose();
        }
    };

    private void tabBarClose(){
        if (tabview.getVisibility()==View.VISIBLE){
            Intent intent=new Intent("webResume");
            LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
            tabview.setVisibility(View.GONE);
            Window window=getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
                if (appptheme.contains("sysDef")){
                    int nightModeFlags=getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags){
                        case Configuration.UI_MODE_NIGHT_YES:
                            window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                            break;
                    }
                }else if (appptheme.contains("DarkOn")){
                    window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                }else {
                    window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                }

            }
        }
    }

    private BroadcastReceiver incoSwitchONN = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchButton.setChecked(true);
        }
    };

    private BroadcastReceiver incoSwitchOFF = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchButton.setChecked(false);
        }
    };

    private BroadcastReceiver backtxt = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            backtext=intent.getStringExtra("titletext");
        }
    };

    private BroadcastReceiver tabTitle = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tabtitle=intent.getStringExtra("title");
            if (tabtitle.equals("")){
                ((TextView)tabHost.getTabWidget().getChildAt(tbno)
                        .findViewById(R.id.add_button))
                        .setText("SKY!N Browser");
            }else {
                ((TextView)tabHost.getTabWidget().getChildAt(tbno)
                        .findViewById(R.id.add_button))
                        .setText(tabtitle);
            }
        }
    };

    private BroadcastReceiver tabImage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tabimage=intent.getStringExtra("tabImage");
            if (!tabimage.equals("")){
                ((ImageView)tabHost.getTabWidget().getChildAt(tbno)
                        .findViewById(R.id.tabImage))
                        .setImageBitmap(decodeToBase64(tabimage));
            }
        }
    };

    private void addTab(){
        tabIndex++;
        tabSpec++;
        layoutaddtab=(LinearLayout) layoutInflate.inflate(R.layout.tab_event,tabWidget,false);
        Intent tabIntent = new Intent(TabsMain.this, Mpage.class);
        textView.setText(""+tabIndex);
        Intent intent=new Intent("IndexNo").putExtra("IndexNo",tabIndex);
        LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);

        TabSpec spec = tabHost.newTabSpec(String.valueOf(tabSpec));
        spec.setContent(tabIntent);
        spec.setIndicator(layoutaddtab);
        tabHost.addTab(spec);
        list.add(spec);

        curtab.setText(""+tbno);
        tabHost.getTabWidget().getChildAt(tbno).getLayoutParams().width=
                (int) getResources().getDimension(R.dimen.tabwidth);

        new java.util.Timer().schedule(new java.util.TimerTask(){
            @Override
            public void run() { runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabscroolview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }}); }}, 10);

        new java.util.Timer().schedule(new java.util.TimerTask(){
            @Override
            public void run() { runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabHost.setCurrentTab(tabIndex - 1);
                }}); }}, 100);
    }

    @Override
    public void onBackPressed() {
        switch (backtext){
            case "web":
                Intent intent=new Intent("webcangoack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent);
                break;

            case "tab":
                tabBarClose();
                backtext="web";
                break;

            case "bottomSheet":
                Intent intent1 = new Intent("bottomSheetBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent1);
                break;

            case "downloadActivity":
                Intent intent2 = new Intent("downloadActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent2);
                break;

            case "MainHistory":
                Intent intent3 = new Intent("MainHistoryBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent3);
                break;

            case "SplitHistory":
                Intent intent4 = new Intent("SplitHistoryBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent4);
                break;

            case "FloatHistory":
                Intent intent5 = new Intent("FloatHistoryBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent5);
                break;

            case "bookmarksActivity":
                Intent intent6 = new Intent("bookmarksActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent6);
                break;

            case "addonsActivity":
                Intent intent7 = new Intent("addonsActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent7);
                break;

            case "SettingsActivity":
                Intent intent8 = new Intent("SettingsActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent8);
                break;

            case "FeedbackActivity":
                Intent intent9 = new Intent("FeedbackActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent9);
                break;

            case "AboutActivity":
                Intent intent10 = new Intent("AboutActivityBack");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent10);
                break;

            case "":
                Intent intent11 = new Intent("AppExitDialog");
                LocalBroadcastManager.getInstance(TabsMain.this).sendBroadcast(intent11);
                break;
        }
    }

    public static Bitmap decodeToBase64(String input){
        byte[] decodeByte= Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }

    public void doNoting(View view) { }

    public void centerTab(int position){
        final TabWidget tabWidget=tabHost.getTabWidget();
        final int screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        final int leftX=tabWidget.getChildAt(position).getLeft();
        int newX=0;

        newX=leftX+(tabWidget.getChildAt(position).getWidth()/2)-(screenWidth/2);
        if (newX<0){
            newX=0;
        }
        tabscroolview.smoothScrollTo(newX,0);
        tabscroolview.setOnScrollStoppedListener(new TabScrollView.onScrollStoppedListener() {
            @Override
            public void onScrollStopped() {
                tabHost.setCurrentTab(position);
            }
        });
    }
}
