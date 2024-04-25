package com.skyinbrowser.for_news.English;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;
import com.skyinbrowser.for_news.English.fragment.Business;
import com.skyinbrowser.for_news.English.fragment.Entertainment;
import com.skyinbrowser.for_news.English.fragment.Health;
import com.skyinbrowser.for_news.English.fragment.International;
import com.skyinbrowser.for_news.English.fragment.National;
import com.skyinbrowser.for_news.English.fragment.Science;
import com.skyinbrowser.for_news.English.fragment.Sports;
import com.skyinbrowser.for_news.English.fragment.Techno;
import com.skyinbrowser.for_news.English.fragment.ViewFindUtils;

import java.util.ArrayList;


public class MoreNewsActivity extends AppCompatActivity implements FragmentToActivity,
        OnTabSelectListener {

    public static Activity moreNews;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "International", "National", "Entertainment", "Business"
            , "Sports", "Science", "Techno","Health"
    };
    private MyPagerAdapter mAdapter;
    private ImageView back;
    private CardView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.main_news_activity);

        moreNews=this;

        back=findViewById(R.id.engNewsBack);
        toolbar=findViewById(R.id.newsToolbar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (String title : mTitles) {
            mFragments.add(International.getInstance(title));
            mFragments.add(National.getInstance(title));
            mFragments.add(Entertainment.getInstance(title));
            mFragments.add(Business.getInstance(title));
            mFragments.add(Sports.getInstance(title));
            mFragments.add(Science.getInstance(title));
            mFragments.add(Techno.getInstance(title));
            mFragments.add(Health.getInstance(title));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.newsViewPager);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout_9 = ViewFindUtils.find(decorView, R.id.newsTabLayout);

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
                    Animation animation= AnimationUtils.loadAnimation(MoreNewsActivity.this,R.anim.slide_down_search);
                    toolbar.setAnimation(animation);
                    animation.start();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (toolbar.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(MoreNewsActivity.this, R.anim.slide_down_search);
                    toolbar.setAnimation(animation);
                    animation.start();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (toolbar.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(MoreNewsActivity.this,R.anim.slide_down_search);
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
        Intent intent=new Intent(MoreNewsActivity.this, Mpage.class);
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
            return 8;
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
                Animation animation= AnimationUtils.loadAnimation(MoreNewsActivity.this,R.anim.slide_down_search);
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
                Animation animation= AnimationUtils.loadAnimation(MoreNewsActivity.this,R.anim.slide_up_search);
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
