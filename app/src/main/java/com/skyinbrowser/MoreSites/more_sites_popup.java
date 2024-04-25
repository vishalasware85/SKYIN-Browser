package com.skyinbrowser.MoreSites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skyinbrowser.MoreSites.Fragments.Games;
import com.skyinbrowser.MoreSites.Fragments.Music;
import com.skyinbrowser.MoreSites.Fragments.Shopping;
import com.skyinbrowser.MoreSites.Fragments.Sports;
import com.skyinbrowser.MoreSites.Fragments.Travel;
import com.skyinbrowser.PermissionDenyDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class more_sites_popup extends AppCompatActivity implements FragmentToActivity {

    TextView explore_text,stores_text;
    ImageButton explore_cancel,back_bt;
    LinearLayout shoppingLayout,musicLayout,sportsLayout,onlineGamesLayout,travelLayout;

    ScrollView full_explore_view;

    private static final int PERMISSION_REQUEST_CODE=1000;

    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorLight));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.TranslucentDarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(android.R.color.black));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentAppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorLight));
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                    }
                    break;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_sites_popup);

        if (ActivityCompat.checkSelfPermission(more_sites_popup.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSION_REQUEST_CODE);
            }
        }

        shoppingLayout=findViewById(R.id.shopping_layout);
        musicLayout=findViewById(R.id.music_layout);
        sportsLayout=findViewById(R.id.sports_layout);
        onlineGamesLayout=findViewById(R.id.online_games_layout);
        travelLayout=findViewById(R.id.travel_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        CardView animVisible=findViewById(R.id.enterView);
        Animation animation= AnimationUtils.loadAnimation(more_sites_popup.this,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        fragmentTransaction.add(R.id.shopping_layout,new Shopping());
        fragmentTransaction.add(R.id.sports_layout,new Sports());
        fragmentTransaction.add(R.id.travel_layout,new Travel());
        fragmentTransaction.add(R.id.music_layout,new Music());
        fragmentTransaction.add(R.id.online_games_layout,new Games());
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void finish(View view) {
        back();
    }

    public void open_shopping_layout(View view) {
        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_out);
        explore_text.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_out1);
        explore_cancel.setVisibility(View.GONE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.GONE);

        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_in);
        back_bt.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        stores_text=findViewById(R.id.stores_text);
        stores_text.startAnimation(fade_in1);
        stores_text.setText("Shopping");
        stores_text.setVisibility(View.VISIBLE);

        shoppingLayout.setVisibility(View.VISIBLE);
    }

    public void open_music_layout(View view) {
        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_out);
        explore_text.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_out1);
        explore_cancel.setVisibility(View.GONE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.GONE);

        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_in);
        back_bt.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        stores_text=findViewById(R.id.stores_text);
        stores_text.setText("Music");
        stores_text.startAnimation(fade_in1);
        stores_text.setVisibility(View.VISIBLE);

        musicLayout.setVisibility(View.VISIBLE);
    }

    public void open_sports_layout(View view) {
        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_out);
        explore_text.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_out1);
        explore_cancel.setVisibility(View.GONE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.GONE);

        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_in);
        back_bt.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        stores_text=findViewById(R.id.stores_text);
        stores_text.setText("Sports");
        stores_text.startAnimation(fade_in1);
        stores_text.setVisibility(View.VISIBLE);

        sportsLayout.setVisibility(View.VISIBLE);
    }

    public void open_onlinegames_layout(View view) {
        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_out);
        explore_text.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_out1);
        explore_cancel.setVisibility(View.GONE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.GONE);

        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_in);
        back_bt.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        stores_text=findViewById(R.id.stores_text);
        stores_text.setText("Online Games");
        stores_text.startAnimation(fade_in1);
        stores_text.setVisibility(View.VISIBLE);

        onlineGamesLayout.setVisibility(View.VISIBLE);
    }

    public void back_to_explore_layout(View view) {
        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_in);
        explore_text.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_in1);
        explore_cancel.setVisibility(View.VISIBLE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.VISIBLE);

        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_out);
        back_bt.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        stores_text=findViewById(R.id.stores_text);
        stores_text.startAnimation(fade_out1);
        stores_text.setVisibility(View.GONE);

        shoppingLayout.setVisibility(View.GONE);
        musicLayout.setVisibility(View.GONE);
        sportsLayout.setVisibility(View.GONE);
        onlineGamesLayout.setVisibility(View.GONE);
        travelLayout.setVisibility(View.GONE);
    }

    public void open_travel_layout(View view) {
        Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_text=findViewById(R.id.explore_text);
        explore_text.startAnimation(fade_out);
        explore_text.setVisibility(View.GONE);

        Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        explore_cancel=findViewById(R.id.explore_cancel);
        explore_cancel.startAnimation(fade_out1);
        explore_cancel.setVisibility(View.GONE);

        full_explore_view=findViewById(R.id.full_explore_view);
        full_explore_view.setVisibility(View.GONE);

        Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        back_bt=findViewById(R.id.back_bt);
        back_bt.startAnimation(fade_in);
        back_bt.setVisibility(View.VISIBLE);

        Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        stores_text=findViewById(R.id.stores_text);
        stores_text.setText("Travel");
        stores_text.startAnimation(fade_in1);
        stores_text.setVisibility(View.VISIBLE);

        travelLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(more_sites_popup.this,"permission granted", Toast.LENGTH_SHORT).show();
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
        if (comm != null){
            if (comm.equals("storagePermission")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }else { }
        }else { }

        if (comm != null){
            if (comm.equals("dismiss")){
                Intent reviedIntent=getIntent();
                if (reviedIntent.hasExtra("opened settings")){
                    Intent manager=new Intent("closeForMoreSite");
                    LocalBroadcastManager.getInstance(more_sites_popup.this).sendBroadcast(manager);
                }else { }
                back();
            }else { }
        }else { }
    }

    @Override
    public void onBackPressed() {
        stores_text=findViewById(R.id.stores_text);
        if (stores_text.getVisibility()==View.VISIBLE){
            Animation fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            explore_text=findViewById(R.id.explore_text);
            explore_text.startAnimation(fade_in);
            explore_text.setVisibility(View.VISIBLE);

            Animation fade_in1= AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            explore_cancel=findViewById(R.id.explore_cancel);
            explore_cancel.startAnimation(fade_in1);
            explore_cancel.setVisibility(View.VISIBLE);

            full_explore_view=findViewById(R.id.full_explore_view);
            full_explore_view.setVisibility(View.VISIBLE);

            Animation fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_out);
            back_bt=findViewById(R.id.back_bt);
            back_bt.startAnimation(fade_out);
            back_bt.setVisibility(View.GONE);

            Animation fade_out1= AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_out);
            stores_text=findViewById(R.id.stores_text);
            stores_text.startAnimation(fade_out1);
            stores_text.setVisibility(View.GONE);

            shoppingLayout.setVisibility(View.GONE);
            musicLayout.setVisibility(View.GONE);
            sportsLayout.setVisibility(View.GONE);
            onlineGamesLayout.setVisibility(View.GONE);
            travelLayout.setVisibility(View.GONE);
        }else {
            back();
        }
    }

    private void back(){
        CardView animVisible=findViewById(R.id.enterView);
        Animation animation= AnimationUtils.loadAnimation(more_sites_popup.this,R.anim.frag_exit);
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
}
