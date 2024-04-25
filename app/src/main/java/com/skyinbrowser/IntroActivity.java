package com.skyinbrowser;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.skyinbrowser.tabs.TabsMain;

public class IntroActivity extends AppCompatActivity {

    private TextView introtxt;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window=getWindow();
        setTheme(R.style.SplashThemeLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        introtxt=findViewById(R.id.introtxt);
        animationView=findViewById(R.id.introLottieAnim);

        Animation animation= AnimationUtils.loadAnimation(IntroActivity.this, R.anim.fade_in);
        animation.setDuration(500);
        introtxt.startAnimation(animation);
        introtxt.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationView.setAnimation(R.raw.hii_there);
                animationView.setMinAndMaxFrame(0,230);
                animationView.playAnimation();

                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) { }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Animation animation1= AnimationUtils.loadAnimation(IntroActivity.this,
                                R.anim.fade_out);
                        animation1.setDuration(400);
                        introtxt.startAnimation(animation1);
                        introtxt.setVisibility(View.GONE);

                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) { }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation2= AnimationUtils.loadAnimation(IntroActivity.this,
                                        R.anim.fade_in);
                                introtxt.startAnimation(animation2);
                                animation2.setDuration(400);
                                introtxt.setVisibility(View.VISIBLE);
                                introtxt.setText(R.string.settingthings);
                                animationView.setAnimation(R.raw.setting_things);
                                animationView.playAnimation();

                                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                                    @Override public void onAnimationStart(Animator animator) { }
                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        Animation animation3= AnimationUtils.loadAnimation(IntroActivity.this,
                                                R.anim.fade_out);
                                        introtxt.startAnimation(animation3);
                                        animation3.setDuration(400);
                                        introtxt.setVisibility(View.GONE);
                                        introtxt.setText(R.string.settingthings);

                                        animation3.setAnimationListener(new Animation.AnimationListener() {
                                            @Override public void onAnimationStart(Animation animation) { }
                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                Animation animation3= AnimationUtils.loadAnimation(IntroActivity.this,
                                                        R.anim.fade_in);
                                                introtxt.startAnimation(animation3);
                                                animation3.setDuration(400);
                                                introtxt.setVisibility(View.VISIBLE);
                                                introtxt.setText(R.string.establishingSecurity);
                                                animationView.setAnimation(R.raw.establishing_security);
                                                animationView.playAnimation();

                                                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                                                    @Override public void onAnimationStart(Animator animator) { }
                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        Animation animation3= AnimationUtils.loadAnimation(IntroActivity.this,
                                                                R.anim.fade_out);
                                                        introtxt.startAnimation(animation3);
                                                        animation3.setDuration(400);
                                                        introtxt.setVisibility(View.GONE);
                                                        introtxt.setText(R.string.wego);

                                                        animation3.setAnimationListener(new Animation.AnimationListener() {
                                                            @Override
                                                            public void onAnimationStart(Animation animation) { }
                                                            @Override
                                                            public void onAnimationEnd(Animation animation) {
                                                                Animation animation3= AnimationUtils.loadAnimation(IntroActivity.this,
                                                                        R.anim.fade_in);
                                                                introtxt.startAnimation(animation3);
                                                                animation3.setDuration(400);
                                                                introtxt.setVisibility(View.VISIBLE);
                                                                animationView.setAnimation(R.raw.here_we_go);
                                                                animationView.playAnimation();

                                                                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                                                                    @Override public void onAnimationStart(Animator animator) { }
                                                                    @Override
                                                                    public void onAnimationEnd(Animator animator) {
                                                                        SharedPreferences firstTime=getSharedPreferences("notFirstTime",MODE_PRIVATE);
                                                                        SharedPreferences.Editor firstEditor=firstTime.edit();
                                                                        firstEditor.putString("notFirstTime","notFirstTime");
                                                                        firstEditor.apply();
                                                                        Intent intent=new Intent(IntroActivity.this,TabsMain.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    @Override public void onAnimationCancel(Animator animator) { }
                                                                    @Override public void onAnimationRepeat(Animator animator) { }
                                                                });
                                                            }
                                                            @Override
                                                            public void onAnimationRepeat(Animation animation) { }
                                                        });
                                                    }
                                                    @Override public void onAnimationCancel(Animator animator) { }
                                                    @Override public void onAnimationRepeat(Animator animator) { }
                                                });
                                            }
                                            @Override
                                            public void onAnimationRepeat(Animation animation) { }
                                        });
                                    }
                                    @Override public void onAnimationCancel(Animator animator) { }
                                    @Override public void onAnimationRepeat(Animator animator) { }
                                });


                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) { }
                        });
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) { }
                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
            }
            @Override
            public void onAnimationEnd(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

}
