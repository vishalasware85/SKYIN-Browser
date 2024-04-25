package com.skyinbrowser.Addons;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.PermissionDenyDialog;
import com.skyinbrowser.R;
import com.suke.widget.SwitchButton;

import java.util.Timer;
import java.util.TimerTask;

public class AddonsActivity extends AppCompatActivity implements FragmentToActivity{

    //RippleView instaDownloader,pageSaver;
    MaterialRippleLayout qrCodeScanner,closeBtn,adBlockRipple;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int CAMERA_REQUEST_CODE=400;
    String scanresult;
    TextView txt3;
    LinearLayout tollbar;
    CardView mainBg;
    private SwitchButton adBlock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences appptheme=getSharedPreferences("AppTheme",MODE_PRIVATE);
        Window window=getWindow();
        if (appptheme.contains("DarkOn")){
            setTheme(R.style.TranslucentNewsDarkTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.black));
                window.setStatusBarColor(getResources().getColor(android.R.color.black));
            }
        }else if (appptheme.contains("lightOn")){
            setTheme(R.style.TranslucentNewsAppTheme);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(getResources().getColor(R.color.newsBgColor));
                window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
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
                        window.setStatusBarColor(getResources().getColor(R.color.purpleStatusBar));
                    }
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addons_activity);

        //instaDownloader=findViewById(R.id.instaDownloader);
        qrCodeScanner=findViewById(R.id.qrCodeScanner);
        //pageSaver=findViewById(R.id.savePageRipple);
        closeBtn=findViewById(R.id.addonsClose);
        txt3=findViewById(R.id.addonstext3);
        tollbar=findViewById(R.id.addonsToolbar);
        mainBg=findViewById(R.id.addonsBG);
        adBlockRipple=findViewById(R.id.adBlockAddons);
        adBlock=findViewById(R.id.adBlockAddonsSwitch);

        Animation animation= AnimationUtils.loadAnimation(AddonsActivity.this,R.anim.frag_enter);
        animation.setDuration(100);
        mainBg.startAnimation(animation);
        mainBg.setVisibility(View.VISIBLE);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override public void run() { runOnUiThread(new Runnable() {
                        @Override public void run() {
                            back();
                        }}); }},150);
            }
        });

        //instaDownloader.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
        //    @Override
        //    public void onComplete(RippleView rippleView) {
//
        //    }
        //});

        qrCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddonsActivity.this,
                        Manifest.permission.CAMERA) != PackageManager
                        .PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.CAMERA
                        }, CAMERA_REQUEST_CODE);
                    }
                }else {
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override public void run() { runOnUiThread(new Runnable() {
                            @Override public void run() {
                                Intent intent=new Intent(AddonsActivity.this, QrCodeActivity.class);
                                startActivityForResult(intent,REQUEST_CODE_QR_SCAN);
                            }}); }},150);
                }
            }
        });


        SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
        if (sharedPreferences.contains("adBlockOn")){
            adBlock.setChecked(true);
        }else {
            adBlock.setChecked(false);
        }
        adBlockRipple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adBlock.isChecked()){
                    adBlock.setChecked(false);
                    SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.remove("adBlockOn");
                    editor.apply();
                    Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOff");
                    LocalBroadcastManager.getInstance(AddonsActivity.this).sendBroadcast(intent);
                }else {
                    adBlock.setChecked(true);
                    SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("adBlockOn","adBlockOn");
                    editor.apply();
                    Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOn");
                    LocalBroadcastManager.getInstance(AddonsActivity.this).sendBroadcast(intent);
                }
            }
        });

        adBlock.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("adBlockOn","adBlockOn");
                    editor.apply();
                    Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOn");
                    LocalBroadcastManager.getInstance(AddonsActivity.this).sendBroadcast(intent);
                }else {
                    SharedPreferences sharedPreferences=getSharedPreferences("webViewSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.remove("adBlockOn");
                    editor.apply();
                    Intent intent=new Intent("adBlock").putExtra("adBlockStatus","adBlockOff");
                    LocalBroadcastManager.getInstance(AddonsActivity.this).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddonsActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            scanresult = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            QrScanComplete qrScanComplete = new QrScanComplete();
            Bundle bundle = new Bundle();
            bundle.putString("scanResult", scanresult);
            qrScanComplete.setArguments(bundle);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrScanComplete.show(getSupportFragmentManager(), "completeResult");
                        }
                    });
                }
            }, 200);
        }
    }

    @Override
    public void communicate(String comm) {
        if (comm != null){
            Intent intent=new Intent("scanResult");
            intent.putExtra("scanResult",scanresult);
            Intent recivedintent = getIntent();
            if (recivedintent.hasExtra("opened settings")){
                intent.putExtra("opened settings","opened settings");
            }
            LocalBroadcastManager.getInstance(AddonsActivity.this).sendBroadcast(intent);
            back();
        }else {

        }

        if (comm != null){
            if (comm.equals("cameraPermission")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }
            }else { }
        }else { }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override public void run() { runOnUiThread(new Runnable() {
                            @Override public void run() {
                                Intent intent=new Intent(AddonsActivity.this, QrCodeActivity.class);
                                startActivityForResult(intent,REQUEST_CODE_QR_SCAN);
                            }}); }},150);
                }else {
                    Bundle modify_intent=new Bundle();
                    modify_intent.putString("cameraMsg", "cameraMsg");

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

    private void back(){
        Animation animation= AnimationUtils.loadAnimation(AddonsActivity.this,R.anim.frag_exit);
        animation.setDuration(100);
        mainBg.startAnimation(animation);
        mainBg.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        back();
    }
}
