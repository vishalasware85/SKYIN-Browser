package com.skyinbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skyinbrowser.CustomJavaFiles.MyBounceInterpolator;

import java.text.DecimalFormat;

public class LanguageSelector extends AppCompatActivity {

    private Button english,hindi,start,sysDef,light,dark;
    private boolean systemTheme,lightTheme,darkTheme,englishboo,hindiboo=false;
    private LinearLayout langSelLayout,themeSelLayout;
    private TextView instText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selector);

        english=findViewById(R.id.englishBtnLang);
        hindi=findViewById(R.id.hindiBtnLang);
        start=findViewById(R.id.startBtnLang);
        sysDef=findViewById(R.id.systemDefaultLang);
        light=findViewById(R.id.lightThemeLang);
        dark=findViewById(R.id.darkThemeLang);
        instText=findViewById(R.id.instText);
        langSelLayout=findViewById(R.id.langSelLayout);
        themeSelLayout=findViewById(R.id.themeSelLayout);

        //final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        //myAnim.setInterpolator(interpolator);

        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideGone= AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left_gone);
        Animation slideVisible=AnimationUtils.loadAnimation(this, R.anim.slide_right_to_eft);

        fadeOut.setDuration(100);
        fadeIn.setDuration(100);

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //english.startAnimation(myAnim);
                english.setBackground(getResources().getDrawable(R.drawable.lang_selected_btn_bg));
                english.setTextColor(getResources().getColor(R.color.white));

                hindi.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                hindi.setTextColor(getResources().getColor(R.color.black));

                englishboo=true;
                hindiboo=false;
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hindi.startAnimation(myAnim);
                hindi.setBackground(getResources().getDrawable(R.drawable.lang_selected_btn_bg));
                hindi.setTextColor(getResources().getColor(R.color.white));

                english.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                english.setTextColor(getResources().getColor(R.color.black));
                englishboo=false;
                hindiboo=true;
            }
        });

        sysDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sysDef.setBackground(getResources().getDrawable(R.drawable.lang_selected_btn_bg));
                sysDef.setTextColor(getResources().getColor(R.color.white));

                light.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                light.setTextColor(getResources().getColor(R.color.black));
                dark.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                dark.setTextColor(getResources().getColor(R.color.black));
                systemTheme=true;
                lightTheme=false;
                darkTheme=false;
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                light.setBackground(getResources().getDrawable(R.drawable.lang_selected_btn_bg));
                light.setTextColor(getResources().getColor(R.color.white));

                sysDef.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                sysDef.setTextColor(getResources().getColor(R.color.black));
                dark.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                dark.setTextColor(getResources().getColor(R.color.black));
                systemTheme=false;
                lightTheme=true;
                darkTheme=false;
            }
        });

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dark.setBackground(getResources().getDrawable(R.drawable.lang_selected_btn_bg));
                dark.setTextColor(getResources().getColor(R.color.white));

                light.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                light.setTextColor(getResources().getColor(R.color.black));
                sysDef.setBackground(getResources().getDrawable(R.drawable.lang_sel_circle));
                sysDef.setTextColor(getResources().getColor(R.color.black));
                systemTheme=false;
                lightTheme=false;
                darkTheme=true;
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                instText.startAnimation(fadeIn);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        Intent intent=new Intent(this,IntroActivity.class);

        SharedPreferences newsLanguage=getSharedPreferences("NewsLanguage",MODE_PRIVATE);
        SharedPreferences.Editor newsLangEditor=newsLanguage.edit();

        SharedPreferences appTheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        SharedPreferences.Editor themeEditor=appTheme.edit();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start.getText().toString().equals("NEXT")){
                    if (hindiboo){
                        //Toast.makeText(LanguageSelector.this, "Hindi", Toast.LENGTH_SHORT).show();
                        start.setText("START");
                        instText.startAnimation(fadeOut);
                        instText.setText(getResources().getString(R.string.selAppTheme));
                        langSelLayout.startAnimation(slideGone);
                        langSelLayout.setVisibility(View.GONE);
                        themeSelLayout.startAnimation(slideVisible);
                        themeSelLayout.setVisibility(View.VISIBLE);
                        newsLangEditor.putString("hindiNews","hindiNews");
                        newsLangEditor.apply();
                    }else if (englishboo){
                        //Toast.makeText(LanguageSelector.this, "English", Toast.LENGTH_SHORT).show();
                        start.setText("START");
                        instText.startAnimation(fadeOut);
                        instText.setText(getResources().getString(R.string.selAppTheme));
                        langSelLayout.startAnimation(slideGone);
                        langSelLayout.setVisibility(View.GONE);
                        themeSelLayout.startAnimation(slideVisible);
                        themeSelLayout.setVisibility(View.VISIBLE);
                        newsLangEditor.putString("englishNews","englishNews");
                        newsLangEditor.apply();
                    }else {
                        Toast.makeText(LanguageSelector.this, "Please select your language", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (systemTheme){
                        //Toast.makeText(LanguageSelector.this, "System", Toast.LENGTH_SHORT).show();
                        themeEditor.putString("sysDef","sysDef");
                        themeEditor.apply();
                        startActivity(intent);
                        finish();
                    }else if (lightTheme){
                        //Toast.makeText(LanguageSelector.this, "light", Toast.LENGTH_SHORT).show();
                        themeEditor.putString("lightOn","lightOn");
                        themeEditor.apply();
                        startActivity(intent);
                        finish();
                    }else if (darkTheme){
                        //Toast.makeText(LanguageSelector.this, "dark", Toast.LENGTH_SHORT).show();
                        themeEditor.putString("DarkOn","DarkOn");
                        themeEditor.apply();
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LanguageSelector.this, "Please select your desired theme", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
