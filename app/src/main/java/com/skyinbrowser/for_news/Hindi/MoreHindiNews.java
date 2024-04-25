package com.skyinbrowser.for_news.Hindi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;
import com.skyinbrowser.for_news.English.fragment.ViewFindUtils;
import com.skyinbrowser.for_news.Hindi.Fragment.BusinessHin;
import com.skyinbrowser.for_news.Hindi.Fragment.EntertainmentHin;
import com.skyinbrowser.for_news.Hindi.Fragment.HealthHin;
import com.skyinbrowser.for_news.Hindi.Fragment.InternationalHin;
import com.skyinbrowser.for_news.Hindi.Fragment.NationalHin;
import com.skyinbrowser.for_news.Hindi.Fragment.ScienceHin;
import com.skyinbrowser.for_news.Hindi.Fragment.SportsHin;

import java.util.ArrayList;

public class MoreHindiNews extends AppCompatActivity implements FragmentToActivity,
        OnTabSelectListener{

    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "अंतरराष्ट्रीय", "राष्ट्रीय", "मनोरंजन", "व्यापार"
            , "खेल", "विज्ञान","स्वास्थ्य"};
    private MyPagerAdapter mAdapter;
    private ImageView back;
    private CardView toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.DarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.AppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                window.setStatusBarColor(getResources().getColor(R.color.moreNewsStatus));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.DarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
                        window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.AppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                        window.setStatusBarColor(getResources().getColor(R.color.moreNewsStatus));
                    }
                    break;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_hindi_news);

        back=findViewById(R.id.hindiNewsBack);
        toolbar=findViewById(R.id.hindiNewsToolbar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (String title : mTitles) {
            mFragments.add(InternationalHin.getInstance(title));
            mFragments.add(NationalHin.getInstance(title));
            mFragments.add(EntertainmentHin.getInstance(title));
            mFragments.add(BusinessHin.getInstance(title));
            mFragments.add(SportsHin.getInstance(title));
            mFragments.add(ScienceHin.getInstance(title));
            mFragments.add(HealthHin.getInstance(title));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.hindiNewsViewPager);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout_9 = ViewFindUtils.find(decorView, R.id.hindiNewsTabLayout);
        tabLayout_9.setViewPager(vp);
        vp.setCurrentItem(0);

        tabLayout_9.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) { }
            @Override
            public void onTabReselect(int position) { }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (toolbar.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(MoreHindiNews.this,R.anim.slide_down_search);
                    toolbar.setAnimation(animation);
                    animation.start();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (toolbar.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(MoreHindiNews.this, R.anim.slide_down_search);
                    toolbar.setAnimation(animation);
                    animation.start();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (toolbar.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(MoreHindiNews.this,R.anim.slide_down_search);
                    toolbar.setAnimation(animation);
                    animation.start();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(toolbarVisible,new IntentFilter("toolbarVisible"));
        LocalBroadcastManager.getInstance(this).registerReceiver(toolbarGone,new IntentFilter("toolbarGone"));
        LocalBroadcastManager.getInstance(this).registerReceiver(finishMoreNews,new IntentFilter("finishMoreNews"));
    }

    @Override
    public void communicate(String comm) {
        Intent intent=new Intent(MoreHindiNews.this, Mpage.class);
        intent.putExtra("newssUrl",comm);
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private BroadcastReceiver toolbarVisible=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (toolbar.getVisibility()==View.GONE){
                Animation animation= AnimationUtils.loadAnimation(MoreHindiNews.this,R.anim.slide_down_search);
                toolbar.setAnimation(animation);
                animation.start();
                toolbar.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver toolbarGone=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (toolbar.getVisibility()==View.VISIBLE){
                Animation animation= AnimationUtils.loadAnimation(MoreHindiNews.this,R.anim.slide_up_search);
                toolbar.setAnimation(animation);
                animation.start();
                toolbar.setVisibility(View.GONE);
            }
        }
    };

    private BroadcastReceiver finishMoreNews=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}
