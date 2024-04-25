package com.skyinbrowser.split;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.skyinbrowser.R;

public class SplitAlertWindow extends AppCompatActivity {

    TextView yesTxt;
    CheckBox yesBos;
    Button okbtn;
    CardView exitCard;
    boolean animationanim = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        }else {
            setTheme(R.style.TranslucentAppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.split_alert_window);

        yesBos=findViewById(R.id.splitExitAlertBox);
        yesTxt=findViewById(R.id.splitExitAlertTxt);
        okbtn=findViewById(R.id.oksplitbtn);
        exitCard=findViewById(R.id.splitAlertCard);

        Animation animation= AnimationUtils.loadAnimation(SplitAlertWindow.this,
                R.anim.zoom_in);
        exitCard.startAnimation(animation);
        exitCard.setVisibility(View.VISIBLE);

        Intent intent=this.getIntent();
        Bundle data=getIntent().getExtras();

        yesBos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    yesBos.setChecked(true);
                }else {
                    yesBos.setChecked(false);
                }
            }
        });

        yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesBos.isChecked()){
                    yesBos.setChecked(false);
                }else {
                    yesBos.setChecked(true);
                }
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesBos.isChecked()){
                    if (intent.hasExtra("upperUrl")){
                        String upperUrl=data.getString("upperUrl");
                        SharedPreferences preferences=getSharedPreferences("Split Web Url",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("upperSplitUrl",upperUrl);
                        editor.apply();
                    }
                    if (intent.hasExtra("lowerUrl")){
                        String lowerUrl=data.getString("lowerUrl");
                        SharedPreferences preferences=getSharedPreferences("Split Web Url",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("lowerSplitUrl",lowerUrl);
                        editor.apply();
                    }

                    Animation animation= AnimationUtils.loadAnimation(SplitAlertWindow.this,
                            R.anim.zoom_out);
                    exitCard.startAnimation(animation);
                    exitCard.setVisibility(View.INVISIBLE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            finish();
                            SpltVertical.splitActivity.finish();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                } else {
                    SharedPreferences preferences=getSharedPreferences("Split Web Url",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.remove("upperSplitUrl");
                    editor.remove("lowerSplitUrl");
                    editor.apply();

                    Animation animation= AnimationUtils.loadAnimation(SplitAlertWindow.this,
                            R.anim.zoom_out);
                    exitCard.startAnimation(animation);
                    exitCard.setVisibility(View.INVISIBLE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            finish();
                            SpltVertical.splitActivity.finish();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Animation animation= AnimationUtils.loadAnimation(SplitAlertWindow.this,
                R.anim.zoom_out);
        exitCard.startAnimation(animation);
        exitCard.setVisibility(View.INVISIBLE);
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
