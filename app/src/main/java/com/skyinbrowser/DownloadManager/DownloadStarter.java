package com.skyinbrowser.DownloadManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.MoreSites.more_sites_popup;
import com.skyinbrowser.PermissionDenyDialog;
import com.skyinbrowser.R;

public class DownloadStarter extends AppCompatActivity implements FragmentToActivity {

    EditText filename;
    TextView fileurl,dotExtension,txt1,txt2,txt3,txt4,txt5,txt6,fileSize,extensionFull;
    String fileurlst,filenamest,fileExtension,fileSizest;
    ImageView typeOfImage;
    CardView fullCardView;
    private static final int STORAGE_REQUEST_CODE=1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.download_starter);

        typeOfImage=findViewById(R.id.typeOfImage);
        fullCardView=findViewById(R.id.fullCardView);
        txt1=findViewById(R.id.starterText1);
        txt2=findViewById(R.id.starterText2);
        txt3=findViewById(R.id.starterText3);
        txt4=findViewById(R.id.starterText4);
        txt5=findViewById(R.id.starterText5);
        txt6=findViewById(R.id.starterText6);
        fileSize=findViewById(R.id.fileSize);
        extensionFull=findViewById(R.id.extensionFull);

        if (ActivityCompat.checkSelfPermission(DownloadStarter.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, STORAGE_REQUEST_CODE);
            }
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation slide_down_fade_in= AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.slide_down_fade_in);
                                typeOfImage.startAnimation(slide_down_fade_in);
                                typeOfImage.setVisibility(View.VISIBLE);
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
                                        R.anim.slide_up);
                                fullCardView.startAnimation(slide_up);
                                fullCardView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                },
                100
        );

        filename=findViewById(R.id.filename);
        fileurl=findViewById(R.id.fileurl);

        Intent intent=this.getIntent();
        if (intent!=null){
            if (intent.hasExtra("fileurl")){
                filenamest=intent.getStringExtra("filename");
                fileurlst=intent.getStringExtra("fileurl");
                fileSizest=intent.getStringExtra("fileSize");
                fileurl.setText(fileurlst);
                filename.setText(filenamest);
                fileSize.setText(fileSizest);
            }
        }

        fileExtension=filenamest.substring(filenamest.lastIndexOf("."));
        dotExtension=findViewById(R.id.dotExtension);
        dotExtension.setText(fileExtension);
        if (fileExtension.equals(".apk")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.apkico));
            extensionFull.setText("Application File");
        }
        if (fileExtension.equals(".mp3")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.musicico));
            extensionFull.setText("Music File");
        }
        if (fileExtension.equals(".mp4")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.video_ico));
            extensionFull.setText("Video File");
        }
        if (fileExtension.equals(".mkv")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.video_ico));
            extensionFull.setText("Video File");
        }
        if (fileExtension.equals(".pdf")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.pdfico));
            extensionFull.setText("Document File");
        }
        if (fileExtension.equals(".jpg")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.imageico));
            extensionFull.setText("Image File");
        }
        if (fileExtension.equals(".png")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.imageico));
            extensionFull.setText("Image File");
        }
        if (fileExtension.equals(".ico")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.imageico));
            extensionFull.setText("Image File");
        }
        if (fileExtension.equals(".webp")){
            typeOfImage.setImageDrawable(getResources().getDrawable(R.drawable.imageico));
            extensionFull.setText("Image File");
        }

        Button downloadStart=findViewById(R.id.startDownload);
        downloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filenam=filename.getText().toString();
                String url=fileurl.getText().toString();
                Animation slide_up_fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up_fade_out);
                typeOfImage=findViewById(R.id.typeOfImage);
                typeOfImage.startAnimation(slide_up_fade_out);
                slide_up_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        typeOfImage.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                Animation slide_down= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                fullCardView=findViewById(R.id.fullCardView);
                fullCardView.startAnimation(slide_down);
                slide_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fullCardView.setVisibility(View.INVISIBLE);
                        Intent intent2=new Intent(DownloadStarter.this,DownloadActivity.class);
                        intent2.putExtra("fileUrl",url);
                        intent2.putExtra("filename",filenam);
                        intent2.putExtra("fileSize",fileSizest);
                        startActivity(intent2);
                        finish();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Animation slide_up_fade_out= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_fade_out);
        typeOfImage=findViewById(R.id.typeOfImage);
        typeOfImage.startAnimation(slide_up_fade_out);
        slide_up_fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                typeOfImage.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });


        Animation slide_down= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        fullCardView=findViewById(R.id.fullCardView);
        fullCardView.startAnimation(slide_down);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                fullCardView.setVisibility(View.INVISIBLE);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(DownloadStarter.this,"permission granted", Toast.LENGTH_SHORT).show();
                }else {
                    Bundle modify_intent=new Bundle();
                    modify_intent.putString("storageMsg", "storageMsg");

                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    Fragment prev=getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null){
                        fragmentTransaction.remove(prev);
                    }
                    fragmentTransaction.addToBackStack(null);

                    DialogFragment deleteActivity=new PermissionDenyDialog();
                    deleteActivity.setArguments(modify_intent);
                    deleteActivity.show(fragmentTransaction,"dialog");
                }
        }
    }

    @Override
    public void communicate(String comm) {
        if (comm != null){
            if (comm.equals("storagePermission")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
                }
            }else { }
        }else { }
    }
}
