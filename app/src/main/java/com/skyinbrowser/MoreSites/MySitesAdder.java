package com.skyinbrowser.MoreSites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyinbrowser.CustomJavaFiles.GetDomainNameFromURL;
import com.skyinbrowser.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Url;

public class MySitesAdder extends AppCompatActivity {

    EditText sitename,siteurll;
    MaterialRippleLayout close;
    Button addBtn;

    String siteUrl,iconUrl,name;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

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
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.customSiteAddNavColor));
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
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColorDark));
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
                        window.setNavigationBarColor(getResources().getColor(R.color.customSiteAddNavColor));
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                    }
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sites_adder);

        sitename=findViewById(R.id.sitenameET);
        siteurll=findViewById(R.id.siteurlET);
        close=findViewById(R.id.closeBtnMysitesadder);
        addBtn=findViewById(R.id.mysitesaddbtn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(siteurll.getWindowToken(), 0);
                finish();
            }
        });

        SharedPreferences preferences = getSharedPreferences("MySitesData", MODE_PRIVATE);
        Gson gson = new Gson();

        if (preferences.contains("MySiteList")) {
            String json = preferences.getString("MySiteList", null);
            Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
            dataList = gson.fromJson(json, type);
        } else {

        }


        Intent intent=getIntent();
        if (intent.hasExtra("webViewUrl")) {
            siteUrl = intent.getStringExtra("webViewUrl");
            if (siteurll.equals("")){

            }else {
                siteurll.setText(siteUrl);
            }

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = sitename.getText().toString();
                    String url=siteurll.getText().toString();
                    if (name.equals("")&&url.equals("")){
                        Toast.makeText(MySitesAdder.this, "Please fill the required fields.", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            iconUrl = GetDomainNameFromURL.getUrlDomain(url);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        iconUrl = "http://logo.clearbit.com/"+iconUrl+"?size=200";
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("siteName",name);
                        map.put("siteUrl",url);
                        map.put("iconUrl",iconUrl);
                        dataList.add(map);

                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(siteurll.getWindowToken(), 0);

                        Intent intent1=new Intent("refreshMySites");
                        LocalBroadcastManager.getInstance(MySitesAdder.this).sendBroadcast(intent1);
                        saveMySitesData();
                    }
                }
            });
        }

        //Button button=findViewById(R.id.mysitesfetchbtn);
        //CircleImageView imageView=findViewById(R.id.mySitesIcon);
        //button.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        String url=siteurll.getText().toString();
        //        String domainName="http://api.grabz.it/services/icon.ashx?key=MzBjMTUyNGVlZmVhNDIxZmI2NDUxMTJhNmFiODI4OGY=&size=200&url="+url;
        //        Picasso.get().load(domainName).into(imageView);
        //    }
        //});

        if (intent.hasExtra("updateMYSITES")){
            addBtn.setText("Update");

            AlertDialog.Builder builder=new AlertDialog.Builder(MySitesAdder.this);
            builder.setCancelable(false);
            builder.setTitle("Warning!");
            builder.setMessage("We are not responsible for your My-Site data crash do it on your own responsibility.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setNegativeButton("No go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });

            builder.setPositiveButton("Go forward", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

            String name=intent.getStringExtra("name");
            String url=intent.getStringExtra("url");

            sitename.setText(name);
            siteurll.setText(url);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnntent=new Intent();
                    String ur=siteurll.getText().toString();
                    if (sitename.equals("")&&siteurll.equals("")){
                        Toast.makeText(MySitesAdder.this, "Please fill the required fields.", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            ur = GetDomainNameFromURL.getUrlDomain(ur);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        ur = "http://logo.clearbit.com/"+ur+"?size=200";

                        returnntent.putExtra("url",siteurll.getText().toString());
                        returnntent.putExtra("name",sitename.getText().toString());
                        returnntent.putExtra("icon", ur);

                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(siteurll.getWindowToken(), 0);


                        setResult(Activity.RESULT_OK,returnntent);
                        finish();
                    }

                }
            });
        }
    }

    private void saveMySitesData() {
        SharedPreferences preferences = getSharedPreferences("MySitesData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        editor.putString("MySiteList", json);
        editor.apply();
        finish();
    }
}
