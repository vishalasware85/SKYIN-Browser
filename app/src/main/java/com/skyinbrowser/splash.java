package com.skyinbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.skyinbrowser.tabs.TabsMain;

public class splash extends AppCompatActivity {

    private boolean skipActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appptheme = getSharedPreferences("AppTheme", MODE_PRIVATE);
        Window window = getWindow();
        if (appptheme.contains("DarkOn")) {
            setTheme(R.style.SplashThemeDark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }
        if (appptheme.contains("lightOn")) {
            setTheme(R.style.SplashThemeLight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.materialNavColor));
            }
        }
        if (appptheme.contains("sysDef")) {
            int nightModeFlags = getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.SplashThemeDark);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.black));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.SplashThemeLight);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.materialNavColor));
                    }
                    break;
            }
        }

        if (appptheme.contains("DarkOn")){

        }else if (appptheme.contains("lightOn")){

        }else if (appptheme.contains("sysDef")) {

        }else {
            setTheme(R.style.SplashThemeLight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.materialNavColor));
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Uri url=getIntent().getData();
        if (url != null){
            String stUrl= String.valueOf(url);
            SharedPreferences gotTheUrl=getSharedPreferences("gotTheUrl",MODE_PRIVATE);
            SharedPreferences.Editor editor=gotTheUrl.edit();
            editor.putString("gotTheUrl",stUrl);
            editor.apply();
            skipActivity=true;
        }

        if (skipActivity){
            Intent intent = new Intent(splash.this, TabsMain.class);
            startActivity(intent);
            finish();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences firstTime=getSharedPreferences("notFirstTime",MODE_PRIVATE);
                    if (firstTime.contains("notFirstTime")){
                        Intent intent = new Intent(splash.this, TabsMain.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(splash.this, LanguageSelector.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 1000);
        }
    }
}
