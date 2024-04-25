package com.skyinbrowser.for_news;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.skyinbrowser.DatabaseAndUnits.NewsBookmarkRecord;
import com.skyinbrowser.DatabaseAndUnits.NewsBookmarkRecordAdapter;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.NonScrollListView;
import com.skyinbrowser.R;

import java.util.List;

import static android.view.View.GONE;

public class NewsBookmarks extends AppCompatActivity implements FragmentToActivity {

    NewsBookmarkRecordAdapter recordAdapter;
    List<NewsBookmarkRecord> recordList;
    int location;
    RecordAction action;
    List<NewsBookmarkRecord> list;

    private NonScrollListView listView;
    private ImageView backBtn,menuBtn;
    private NestedScrollView mainScroll;
    private CardView toolbar,newsMenuPopupBg,newsMenuPopupCard;;
    private LinearLayout swipeUpAnimGone;
    private LottieAnimationView lottieAnimationView;
    private MaterialRippleLayout clearAllBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentNewsDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.navigationBarColorDark));
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentNewsAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.newsBgColor));
                window.setStatusBarColor(getResources().getColor(R.color.newsBgColor));
            }
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.TranslucentNewsDarkTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.black));
                        window.setStatusBarColor(getResources().getColor(android.R.color.black));
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentNewsAppTheme);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.newsBgColor));
                        window.setStatusBarColor(getResources().getColor(R.color.newsBgColor));
                    }
                    break;
            }
        }
        setContentView(R.layout.news_bookmarks);

        listView=findViewById(R.id.newsBookmarkListView);
        backBtn=findViewById(R.id.newsBookmarkBack);
        menuBtn=findViewById(R.id.newsBookmarkMenuBtn);
        mainScroll=findViewById(R.id.newsBookmarkScroll);
        toolbar=findViewById(R.id.newsBookmarkToolbar);
        newsMenuPopupBg=findViewById(R.id.newsMenuPopupBookmark);
        newsMenuPopupCard=findViewById(R.id.mainPopupActionViewNewsBookmark);
        swipeUpAnimGone=findViewById(R.id.swipeUpAnimGoneNewsBookmark);
        lottieAnimationView=findViewById(R.id.lottieAnimationViewNewsBookmark);
        clearAllBtn=findViewById(R.id.clearAllNewsBookmark);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsMenuPopupBg.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(GONE);
                swipeUpAnimGone.setVisibility(View.VISIBLE);
                Animation animation= AnimationUtils.loadAnimation(NewsBookmarks.this,R.anim.frag_enter);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                newsMenuPopupCard.setVisibility(View.VISIBLE);
            }
        });

        menuBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(NewsBookmarks.this, "More", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        newsMenuPopupBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(NewsBookmarks.this,R.anim.frag_exit);
                animation.setDuration(100);
                newsMenuPopupCard.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        newsMenuPopupCard.setVisibility(GONE);
                        newsMenuPopupBg.setVisibility(GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slide_up_fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_out);
                slide_up_fade_out.setDuration(400);
                swipeUpAnimGone.startAnimation(slide_up_fade_out);
                slide_up_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        swipeUpAnimGone.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                lottieAnimationView.setAnimation(R.raw.trash);
                action.clearNewsBookmarks();
                list.removeAll(list);

                Animation slide_up_fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_in);
                slide_up_fade_in.setDuration(600);
                lottieAnimationView.startAnimation(slide_up_fade_in);
                slide_up_fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) { }
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                lottieAnimationView.setVisibility(View.INVISIBLE);
                                lottieAnimationView.clearAnimation();
                                newsMenuPopupCard.setVisibility(GONE);
                                newsMenuPopupBg.setVisibility(GONE);
                                recordAdapter.notifyDataSetChanged();
                                listView.setAdapter(recordAdapter);
                            }
                            @Override
                            public void onAnimationCancel(Animator animator) { }
                            @Override
                            public void onAnimationRepeat(Animator animator) { }
                        });
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        mainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>oldScrollY){
                    //Toast.makeText(Mpage.this, "down", Toast.LENGTH_SHORT).show();
                    if (toolbar.getVisibility()==View.VISIBLE){
                        Animation animation= AnimationUtils.loadAnimation(NewsBookmarks.this,R.anim.slide_up_search);
                        toolbar.setAnimation(animation);
                        animation.start();
                        toolbar.setVisibility(View.GONE);
                    }
                }else {
                    //Toast.makeText(Mpage.this, "top", Toast.LENGTH_SHORT).show();
                    if (toolbar.getVisibility()==View.GONE){
                        Animation animation= AnimationUtils.loadAnimation(NewsBookmarks.this,R.anim.slide_down_search);
                        toolbar.setAnimation(animation);
                        animation.start();
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        action = new RecordAction(NewsBookmarks.this);
        action.open(false);

        listView.setChoiceMode(NonScrollListView.CHOICE_MODE_MULTIPLE);

        list = action.listNewsBookmarks();
        recordAdapter = new NewsBookmarkRecordAdapter(NewsBookmarks.this, R.layout.list_row, list);
        listView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();

        LottieAnimationView animationView=findViewById(R.id.newsBookmarkListEmptyLottieAnim);
        animationView.setAnimation(R.raw.empty_state);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { }
            @Override
            public void onAnimationEnd(Animator animator) {
                animationView.setMinAndMaxFrame(110,175);
                animationView.reverseAnimationSpeed();
                animationView.playAnimation();
            }
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) { }
        });
        listView.setEmptyView(animationView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView titletxt=view.findViewById(R.id.title);
                TextView link=view.findViewById(R.id.link);
                TextView souceText=view.findViewById(R.id.source);
                TextView language=view.findViewById(R.id.language);
                recordList=list;
                location=i;
                final NewsBookmarkRecord record = recordList.get(location);
                String desc=record.getDescription();

                Intent intent=new Intent(NewsBookmarks.this,NewsDetailActivity.class);
                intent.putExtra("title",titletxt.getText().toString());
                intent.putExtra("sourcetxt",souceText.getText().toString());
                intent.putExtra("BookmarkedNews","BookmarkedNews");
                if (language.getText().toString().equals("englishNews")){
                    intent.putExtra("englishNews","englishNews");
                    intent.putExtra("url",link.getText().toString());
                    intent.putExtra("desc",desc);
                }else {
                    intent.putExtra("location",i);
                    intent.putExtra("link",link.getText().toString());
                }
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                recordList=list;
                location=i;
                final NewsBookmarkRecord record = recordList.get(location);
                String url=record.getURL();
                TextView titletxt=view.findViewById(R.id.title);
                TextView language=view.findViewById(R.id.language);

                newsMenuPopupBg.setVisibility(View.VISIBLE);

                Bundle modify_intent=new Bundle();
                modify_intent.putString("url", url);
                modify_intent.putString("title",titletxt.getText().toString());
                if (language.getText().toString().equals("englishNews")){
                    modify_intent.putString("fileName",record.getDescription());
                    modify_intent.putString("englishNews","englishNews");
                }

                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                Fragment prev=getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null){
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment deleteActivity=new DeleteBookMarkNewsFragment();
                deleteActivity.setArguments(modify_intent);
                deleteActivity.show(fragmentTransaction,"dialog");
                return true;
            }
        });

        Intent intent=new Intent("dismiss Progress");
        LocalBroadcastManager.getInstance(NewsBookmarks.this).sendBroadcast(intent);
    }

    @Override
    public void communicate(String comm) {
        if (comm.equals("remove")){
            list.remove(location);
            recordAdapter.notifyDataSetChanged();
            listView.setAdapter(recordAdapter);
        }

        if (comm.equals("dismiss")){
            newsMenuPopupBg.setVisibility(GONE);
        }
    }
}
