package com.skyinbrowser.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.R;

public class BookmarkAdderAndEdit extends AppCompatActivity {

    CardView upper_bookmark_addr,lower_bookmark_addr;
    EditText et_title,et_url;
    Button addbookmark,update_bookmark;
    private long _id;
    String title;
    String url;
    TextView main_title;
    RecordAction action;
    Record record;

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
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
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
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorWhite));
                    }
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_adder);

        action = new RecordAction(BookmarkAdderAndEdit.this);
        action.open(false);

        upper_bookmark_addr=findViewById(R.id.upper_bookmark_addr);
        lower_bookmark_addr=findViewById(R.id.lower_bookmark_addr);
        et_title=findViewById(R.id.bookmark_title);
        et_url=findViewById(R.id.bookmark_url);
        addbookmark=findViewById(R.id.addbookmark);
        update_bookmark=findViewById(R.id.update_bookmark);
        main_title=findViewById(R.id.main_title);

        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation zoomin= AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.zoom_in);
                                upper_bookmark_addr.startAnimation(zoomin);
                                upper_bookmark_addr.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                },
                100
        );

        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation zoomin2= AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.zoom_in);
                                lower_bookmark_addr.startAnimation(zoomin2);
                                lower_bookmark_addr.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                },
                300
        );

        Intent intent=this.getIntent();
        Bundle data=getIntent().getExtras();
        if (data.containsKey("webview_full_url")){
            String url=data.getString("webview_full_url");
            et_url.setText(url);
            et_url.setTextColor(getResources().getColor(R.color.highlight_txt_color));

            new java.util.Timer().schedule(
                    new java.util.TimerTask(){
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation slide_down= AnimationUtils.loadAnimation(getApplicationContext(),
                                            R.anim.slide_down_search);
                                    addbookmark.startAnimation(slide_down);
                                    addbookmark.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    },
                    500
            );
        }

        if (data.containsKey("update")){
            title = data.getString("title");
            url = data.getString("url");
            String id=data.getString("time");

            et_title.setText(title);
            et_url.setText(url);
            main_title.setText("Edit Your Bookmark");

            _id = Long.parseLong(id);
            record=new Record(title,url,_id);

            new java.util.Timer().schedule(
                    new java.util.TimerTask(){
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation slide_down= AnimationUtils.loadAnimation(getApplicationContext(),
                                            R.anim.slide_down_search);
                                    update_bookmark.startAnimation(slide_down);
                                    update_bookmark.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    },
                    600
            );
        }

        update_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=et_title.getText().toString();
                String desc=et_url.getText().toString();
                if (title.equals("")&&desc.equals("")){
                    Toast.makeText(BookmarkAdderAndEdit.this, "Please fill the required fields.", Toast.LENGTH_SHORT).show();
                }else {
                    action.updateBookmark(record);

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation zoomout= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.zoom_out);
                                            upper_bookmark_addr.startAnimation(zoomout);
                                            upper_bookmark_addr.setVisibility(View.GONE);

                                            zoomout.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    Intent intent1=new Intent();
                                                    setResult(Activity.RESULT_OK,intent1);
                                                    finish();
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {
                                                }
                                            });
                                        }
                                    });
                                }
                            },
                            500
                    );

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation zoomout2= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.zoom_out);
                                            lower_bookmark_addr.startAnimation(zoomout2);
                                            lower_bookmark_addr.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            },
                            300
                    );

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation slide_up= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.slide_up_search);
                                            addbookmark.startAnimation(slide_up);
                                            addbookmark.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            },
                            100
                    );
                }



            }
        });

        addbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookmarkTitle=et_title.getText().toString();
                final String url = et_url.getText().toString();
                if (bookmarkTitle.equals("")&&url.equals("")){
                    Toast.makeText(BookmarkAdderAndEdit.this, "Please fill the required fields.", Toast.LENGTH_SHORT).show();
                }else {
                    action.addBookmark(new Record(bookmarkTitle, url, System.currentTimeMillis()));

                    //Intent bookmarkIntent=new Intent("BookmarkAdderAndEdit").putExtra("bookmarkTitle",bookmarkTitle);
                    //LocalBroadcastManager.getInstance(BookmarkAdderAndEdit.this).sendBroadcast(bookmarkIntent);

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation zoomout= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.zoom_out);
                                            upper_bookmark_addr.startAnimation(zoomout);
                                            upper_bookmark_addr.setVisibility(View.GONE);

                                            zoomout.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    Intent intent1=new Intent();
                                                    setResult(BookmarkActivity.RESULT_CODE,intent1);
                                                    finish();
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {
                                                }
                                            });
                                        }
                                    });
                                }
                            },
                            500
                    );

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation zoomout2= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.zoom_out);
                                            lower_bookmark_addr.startAnimation(zoomout2);
                                            lower_bookmark_addr.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            },
                            300
                    );

                    new java.util.Timer().schedule(
                            new java.util.TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation slide_up= AnimationUtils.loadAnimation(getApplicationContext(),
                                                    R.anim.slide_up_search);
                                            addbookmark.startAnimation(slide_up);
                                            addbookmark.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            },
                            100
                    );
                }

            }
        });
    }
}
