package com.skyinbrowser.weather;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skyinbrowser.R;

public class WeatherCustomLocationSetter extends AppCompatActivity {

    EditText city_location;
    private Intent intent;
    CardView cityLocCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.darkStatusBar));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.transparentStatus));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.TranslucentDarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.darkStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.darkStatusBar));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentAppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorLight));
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                    }
                    break;
            }
        }
        setContentView(R.layout.weather_custom_location_setter);

        intent=getIntent();
        city_location=findViewById(R.id.city_location_edittext);
        cityLocCard=findViewById(R.id.citylocCard);

        city_location.setOnEditorActionListener(editorActionListener);

        if (intent.hasExtra("city_name")) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask(){
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation zoom_in= AnimationUtils.loadAnimation(getApplicationContext(),
                                            R.anim.zoom_in);
                                    LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                                    weather_location_card.startAnimation(zoom_in);
                                    weather_location_card.setVisibility(View.VISIBLE);
                                }
                            });
                        }},
                    100
            );
            CardView cityLocCard=findViewById(R.id.citylocCard);
            city_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator move_up=ObjectAnimator.ofFloat(cityLocCard,"y",250);
                    move_up.setDuration(500);
                    move_up.start();
                }
            });
            Button go=findViewById(R.id.go_btn);
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (city_location.getText().toString().equals("")){
                        Toast.makeText(WeatherCustomLocationSetter.this,"Please Enter City Name",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(city_location.getWindowToken(), 0);
                        Animation zoom_out= AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.zoom_out);
                        LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                        weather_location_card.startAnimation(zoom_out);
                        weather_location_card.setVisibility(View.GONE);
                        Intent returnintent = new Intent();
                        String result_city_location = city_location.getText().toString();
                        returnintent.putExtra("result_city_location", result_city_location);
                        setResult(Activity.RESULT_OK, returnintent);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }},
                                200
                        );
                    }
                }
            });
        }

        Intent intent_settings=this.getIntent();
        if (intent_settings!=null) {
            Bundle data = getIntent().getExtras();
            if (data.containsKey("setting_city_name")) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Animation zoom_in= AnimationUtils.loadAnimation(getApplicationContext(),
                                                R.anim.zoom_in);
                                        LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                                        weather_location_card.startAnimation(zoom_in);
                                        weather_location_card.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        },
                        100
                );

                city_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator move_up=ObjectAnimator.ofFloat(cityLocCard,"y",250);
                        move_up.setDuration(500);
                        move_up.start();
                    }
                });

                Button go=findViewById(R.id.go_btn);
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (city_location.getText().toString().equals("")){
                            Toast.makeText(WeatherCustomLocationSetter.this,"Please Enter City Name",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(city_location.getWindowToken(), 0);
                            Animation zoom_out= AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.zoom_out);
                            LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                            weather_location_card.startAnimation(zoom_out);
                            weather_location_card.setVisibility(View.GONE);

                            Intent returnintent = new Intent();
                            String result_city_location = city_location.getText().toString();
                            returnintent.putExtra("settings_result_city_location", result_city_location);
                            setResult(Activity.RESULT_OK, returnintent);

                            new java.util.Timer().schedule(
                                    new java.util.TimerTask(){
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            });
                                        }
                                    },200);
                        }

                    }
                });

            }
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    if (intent.hasExtra("city_name")) {
                        if (city_location.getText().toString().equals("")){
                            Toast.makeText(WeatherCustomLocationSetter.this,"Please Enter City Name",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(city_location.getWindowToken(), 0);
                            Animation zoom_out= AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.zoom_out);
                            LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                            weather_location_card.startAnimation(zoom_out);
                            weather_location_card.setVisibility(View.GONE);
                            Intent returnintent = new Intent();
                            String result_city_location = city_location.getText().toString();
                            returnintent.putExtra("result_city_location", result_city_location);
                            setResult(Activity.RESULT_OK, returnintent);
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask(){
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            });
                                        }},
                                    200
                            );
                        }

                    }

                    if (intent.hasExtra("setting_city_name")) {
                        if (city_location.getText().toString().equals("")){
                            Toast.makeText(WeatherCustomLocationSetter.this,"Please Enter City Name",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(city_location.getWindowToken(), 0);
                            Animation zoom_out= AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.zoom_out);
                            LinearLayout weather_location_card=findViewById(R.id.weather_location_card);
                            weather_location_card.startAnimation(zoom_out);
                            weather_location_card.setVisibility(View.GONE);

                            Intent returnintent = new Intent();
                            String result_city_location = city_location.getText().toString();
                            returnintent.putExtra("settings_result_city_location", result_city_location);
                            setResult(Activity.RESULT_OK, returnintent);

                            new java.util.Timer().schedule(
                                    new java.util.TimerTask(){
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            });
                                        }
                                    },200);
                        }
                    }
                    break;
            }
            return true;
        }
    };
}
