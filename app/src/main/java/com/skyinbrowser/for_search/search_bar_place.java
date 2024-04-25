package com.skyinbrowser.for_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.skyinbrowser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class search_bar_place extends AppCompatActivity {

    EditText editText;
    ImageView searchbutton;
    String url;
    public static final String QUERY_URL = "http://google.com/complete/search?output=toolbar&q=";
    ArrayList<HashMap<String, String>> userList = new ArrayList<>();
    HashMap<String,String> completeSuggestion = new HashMap<>();
    ListView lv;
    ListAdapter adapter;
    private boolean goBack = false;

    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();
    RSSParser rssParser = new RSSParser();
    List<RSSItem> rssItems = new ArrayList<>();
    private static String TAG_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentDarkTheme);
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentAppTheme);
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.TranslucentDarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    setTheme(R.style.TranslucentAppTheme);
                    break;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar_place);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //  if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <21){
        //      setWindowFlag(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,true);
        //  }
        //  if (Build.VERSION.SDK_INT >= 19 ){
        //      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        //  }
        //  if (Build.VERSION.SDK_INT >= 21){
        //      setWindowFlag(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
        //      getWindow().setStatusBarColor(Color.TRANSPARENT);
        //  }

        editText = findViewById(R.id.for_search_editText);
        searchbutton = findViewById(R.id.search_button);
        lv=findViewById(R.id.suggestionList);

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.slide_down_search);

                                LinearLayout search_bar = findViewById(R.id.search_bardj);
                                search_bar.startAnimation(slide_down);
                                search_bar.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                },
                100
        );

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                rssItemList.clear();
                rssItems.clear();
                String url=QUERY_URL+editText.getText().toString();
                new LoadRSSFeedItems().execute(url.replace(" ","%20"));
                if (lv.getVisibility()==View.GONE){
                    lv.setVisibility(View.VISIBLE);
                }else { }
                if (editText.getText().toString().equals("")){
                    lv.setVisibility(View.GONE);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView=view.findViewById(R.id.data);
                MaterialRippleLayout materialRippleLayout=view.findViewById(R.id.suggestionMaterialRipple);
                String suggest=textView.getText().toString();
                if (suggest != null){
                    editText.setText(suggest);
                    editText.setSelection(editText.getText().length());
                }else { }
            }
        });


        editText.setOnEditorActionListener(editorActionListener);

        //webViewUrl = getIntent().getExtras().getString("webViewUrl");
        //editText.setText(webViewUrl);
        //editText.selectAll();

        Intent mainweb_url = this.getIntent();
        if (mainweb_url != null) {
            if (mainweb_url.hasExtra("mainweb_view_url")){
                //url=data.getString("mainweb_view_url");
                searchbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(search_bar_place.this,
                                    "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        } else {
                            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up_search);

                            LinearLayout search_bar = findViewById(R.id.search_bardj);
                            search_bar.startAnimation(slide_up);
                            search_bar.setVisibility(View.GONE);

                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                            Intent returnintent = new Intent();
                            String result = "https://www.google.com/search?q=" + editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }}); }}, 200);
                        }
                    }
                });
            }
        }

        Intent websiteurl = this.getIntent();
        if (websiteurl != null) {
            if (websiteurl.hasExtra("websiteurl_text")){
                searchbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up_search);

                        LinearLayout search_bar = findViewById(R.id.search_bardj);
                        search_bar.startAnimation(slide_up);
                        search_bar.setVisibility(View.GONE);

                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        Intent returnintent = new Intent();

                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(search_bar_place.this,
                                    "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                            goBack =false;
                        }

                        if (editText.getText().toString().startsWith("http")){
                            String result = editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }

                        if (editText.getText().toString().startsWith("http://www.")) {
                            if (editText.getText().toString().endsWith(".com")) {
                                String result = editText.getText().toString();
                                returnintent.putExtra("result", result);
                                setResult(Activity.RESULT_OK, returnintent);
                                goBack =true;
                            }
                        }

                        if (editText.getText().toString().startsWith("https://www.")) {
                            if (editText.getText().toString().endsWith(".com")) {
                                String result = editText.getText().toString();
                                returnintent.putExtra("result", result);
                                setResult(Activity.RESULT_OK, returnintent);
                                goBack =true;
                            }
                        }

                        if (editText.getText().toString().startsWith("https://www.")) {
                            String result = editText.getText().toString()+".com";
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }

                        if (editText.getText().toString().startsWith("www.")) {
                            if (editText.getText().toString().endsWith(".com")) {
                                String result = "https://"+editText.getText().toString();
                                returnintent.putExtra("result", result);
                                setResult(Activity.RESULT_OK, returnintent);
                                goBack =true;
                            }
                        }

                        if (editText.getText().toString().endsWith(".com")){
                            String result = "https://www."+editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }

                        if (editText.getText().toString().startsWith("www.")){
                            String result = "https://"+editText.getText().toString()+".com";
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() { runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (goBack){
                                        new Timer().schedule(new TimerTask(){
                                            @Override
                                            public void run() { runOnUiThread(new Runnable() {@Override
                                            public void run() {
                                                finish();
                                            }}); }}, 200);
                                    }else {
                                        String result = "https://www.google.com/search?q=" + editText.getText().toString();
                                        returnintent.putExtra("result", result);
                                        setResult(Activity.RESULT_OK, returnintent);

                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() { runOnUiThread(new Runnable() {@Override
                                            public void run() {
                                                finish();
                                            }}); }}, 200);
                                    }

                                }}); }}, 5);
                    }
                });
            }
        }


        Intent openedweb=this.getIntent();
        if (openedweb!=null) {
            if (openedweb.hasExtra("openedwebUrl")){
                url = openedweb.getStringExtra("openedwebUrl");
                url = getIntent().getExtras().getString("openedwebUrl");
                editText.setText(url);
                editText.selectAll();

                searchbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(search_bar_place.this,
                                    "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        } else {
                            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up_search);

                            LinearLayout search_bar = findViewById(R.id.search_bardj);
                            search_bar.startAnimation(slide_up);
                            search_bar.setVisibility(View.GONE);

                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                            Intent returnintent = new Intent();
                            String result = "https://www.google.com/search?q=" + editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);

                            new Timer().schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            });
                                        }
                                    },
                                    200
                            );
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
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up_search);

                    LinearLayout search_bar = findViewById(R.id.search_bardj);
                    search_bar.startAnimation(slide_up);
                    search_bar.setVisibility(View.GONE);

                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    Intent returnintent = new Intent();

                    if (editText.getText().toString().equals("")) {
                        Toast.makeText(search_bar_place.this,
                                "Make shure that you have entered url", Toast.LENGTH_SHORT).show();
                        goBack =false;
                    }

                    if (editText.getText().toString().startsWith("http")){
                        String result = editText.getText().toString();
                        returnintent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, returnintent);
                        goBack =true;
                    }

                    if (editText.getText().toString().startsWith("http://www.")) {
                        if (editText.getText().toString().endsWith(".com")) {
                            String result = editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }
                    }

                    if (editText.getText().toString().startsWith("https://www.")) {
                        if (editText.getText().toString().endsWith(".com")) {
                            String result = editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }
                    }

                    if (editText.getText().toString().startsWith("https://www.")) {
                        String result = editText.getText().toString()+".com";
                        returnintent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, returnintent);
                        goBack =true;
                    }

                    if (editText.getText().toString().startsWith("www.")) {
                        if (editText.getText().toString().endsWith(".com")) {
                            String result = "https://"+editText.getText().toString();
                            returnintent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, returnintent);
                            goBack =true;
                        }
                    }

                    if (editText.getText().toString().endsWith(".com")){
                        String result = "https://www."+editText.getText().toString();
                        returnintent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, returnintent);
                        goBack =true;
                    }

                    if (editText.getText().toString().startsWith("www.")){
                        String result = "https://"+editText.getText().toString()+".com";
                        returnintent.putExtra("result", result);
                        setResult(Activity.RESULT_OK, returnintent);
                        goBack =true;
                    }

                    new Timer().schedule(new TimerTask() {
                        @Override public void run() { runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (goBack){
                                    new Timer().schedule(new TimerTask(){
                                        @Override
                                        public void run() { runOnUiThread(new Runnable() {@Override
                                        public void run() {
                                            finish();
                                        }}); }}, 200);
                                }else {
                                    String result = "https://www.google.com/search?q=" + editText.getText().toString();
                                    returnintent.putExtra("result", result);
                                    setResult(Activity.RESULT_OK, returnintent);

                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() { runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }}); }}, 200);
                                }
                            }}); }}, 5);
                    break;
            }
            return true;
        }
    };

    private class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
            for (RSSItem item : rssItems) {
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_DATA, item.title);

                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            search_bar_place.this,
                            rssItemList, R.layout.suggestion_list_row,
                            new String[]{ TAG_DATA},
                            new int[]{ R.id.data});

                    // updating listview
                    lv.setAdapter(adapter);
                }
            });
            return null;
        }

        protected void onPostExecute(String args) { }
    }

    @Override
    public void onBackPressed() {
        Animation slide_up= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_search);

        LinearLayout search_bar=findViewById(R.id.search_bardj);
        search_bar.startAnimation(slide_up);
        search_bar.setVisibility(View.GONE);

        slide_up.setAnimationListener(new Animation.AnimationListener() {
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