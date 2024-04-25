package com.skyinbrowser.DownloadManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.skyinbrowser.Addons.AddonsActivity;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.PermissionDenyDialog;
import com.tonyodev.fetch2.AbstractFetchListener;
import com.tonyodev.fetch2.DefaultFetchNotificationManager;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Downloader;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;
import com.tonyodev.fetch2.Error;
import com.skyinbrowser.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class DownloadActivity extends AppCompatActivity implements ActionListener,FragmentToActivity {

    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final long UNKNOWN_REMAINING_TIME = -1;
    private static final long UNKNOWN_DOWNLOADED_BYTES_PER_SECOND = 0;
    private static final int GROUP_ID = "listGroup".hashCode();
    static final String FETCH_NAMESPACE = "DownloadActivity";

    private FileAdapter fileAdapter;
    private Fetch fetch;
    public static Vector<String> vecString=new Vector<String>();
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder notification;
    private String filename;
    private String fileURI;

    private static int CHANNEL_ID =0;

    String URL2;
    ImageView closeDownloads;
    LinearLayout toolbar;
    CardView animVisible;
    private static String filename1;

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_download);

        animVisible=findViewById(R.id.downloadAnimVisible);
        Animation animation= AnimationUtils.loadAnimation(DownloadActivity.this,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);
        closeDownloads=findViewById(R.id.closeDownloads);

        setUpViews();
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .createDownloadFileOnEnqueue(true)
                .setHttpDownloader(new OkHttpDownloader(Downloader.FileDownloaderType.PARALLEL))
                .setNamespace(FETCH_NAMESPACE)
                .setNotificationManager(new DefaultFetchNotificationManager(this) {
                    @NotNull
                    @Override
                    public Fetch getFetchInstanceForNamespace(@NotNull String s) {
                        return fetch;
                    }
                })
                .build();
        fetch = Fetch.Impl.getInstance(fetchConfiguration);
        notificationManager = NotificationManagerCompat.from(this);
        checkStoragePermissions();

        closeDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        LocalBroadcastManager.getInstance(DownloadActivity.this).registerReceiver(update,new IntentFilter("updateDownload"));
        LocalBroadcastManager.getInstance(DownloadActivity.this).registerReceiver(downloadFile,new IntentFilter("downloadFile"));
    }

    private void setUpViews() {
        final RecyclerView recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences=getSharedPreferences("downloadSettings",MODE_PRIVATE);
        if (preferences.contains("wifionly")){
            fetch.setGlobalNetworkType(NetworkType.WIFI_ONLY);
        }
        if (preferences.contains("anynetwork")){
            fetch.setGlobalNetworkType(NetworkType.ALL);
        }

        fileAdapter = new FileAdapter(this);

        Intent intent=this.getIntent();
        if (intent!=null){
            if (intent.hasExtra("fileUrl")){
                String url=intent.getStringExtra("fileUrl");
                filename1=intent.getStringExtra("filename");
                new Timer().schedule(new TimerTask(){
                    @Override
                    public void run() { runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vecString.add(url);
                        }}); }}, 10);
            }
        }

        recyclerView.setAdapter(fileAdapter);
    }

    private BroadcastReceiver update=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newURL= intent.getStringExtra("url");
            int id= intent.getIntExtra("downloadId",0);
            Request request=new Request(newURL,fileURI);

            fetch.updateRequest(id, request, true, new Func<Download>() {
                @Override
                public void call(@NotNull Download result) {
                    Toast.makeText(DownloadActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }, new Func<Error>() {
                @Override
                public void call(@NotNull Error result) {
                    Toast.makeText(DownloadActivity.this, "Error"+request , Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private BroadcastReceiver downloadFile=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fileURI= intent.getStringExtra("file");
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        fetch.getDownloadsInGroup(GROUP_ID, downloads -> {
            final ArrayList<Download> list = new ArrayList<>(downloads);
            Collections.sort(list, (first, second) -> Long.compare(first.getCreated(), second.getCreated()));
            for (Download download : list) {
                fileAdapter.addDownload(download);
            }
        }).addListener(fetchListener);
    }

    private final FetchListener fetchListener = new AbstractFetchListener() {
        @Override
        public void onAdded(@NotNull Download download) {
            fileAdapter.addDownload(download);
        }

        @Override
        public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onCompleted(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onProgress(@NotNull Download download, long etaInMilliseconds, long downloadedBytesPerSecond) {
            fileAdapter.update(download, etaInMilliseconds, downloadedBytesPerSecond);
        }

        @Override
        public void onPaused(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onResumed(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onCancelled(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onRemoved(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }

        @Override
        public void onDeleted(@NotNull Download download) {
            fileAdapter.update(download, UNKNOWN_REMAINING_TIME, UNKNOWN_DOWNLOADED_BYTES_PER_SECOND);
        }
    };

    @Override
    public void onPauseDownload(int id) {
        fetch.pause(id);
    }

    @Override
    public void onResumeDownload(int id) {
        fetch.resume(id);
    }

    @Override
    public void onRemoveDownload(int id) {
        fetch.remove(id);
        fetch.deleteGroup(id);
        fetch.delete(id);
        fetch.cancel(id);
    }

    @Override
    public void onRetryDownload(int id) {
        fetch.retry(id);
    }

    private void checkStoragePermissions() {
        if (ActivityCompat.checkSelfPermission(DownloadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, STORAGE_PERMISSION_CODE);
            }
        }else {
            enqueueDownloads();
        }
    }

    private static List<Request> getFetchRequests() {
        final List<Request> requests = new ArrayList<>();
        for (String sampleUrl : vecString) {
            final Request request = new Request(sampleUrl, String.valueOf(getFilePath()));
            requests.add(request);
        }
        return requests;
    }

    public static List<Request> getFetchRequestWithGroupId(final int groupId) {
        final List<Request> requests = getFetchRequests();
        for (Request request : requests) {
            request.setGroupId(groupId);
        }
        return requests;
    }

    private static File getFilePath() {
        File dirPath=new File(Environment.getExternalStorageDirectory()+"/SKY!N Browser");
        if (!dirPath.exists()){
            dirPath.mkdir();
        }
        File Saved_Pages=new File(dirPath,"/Downloads");
        if (!Saved_Pages.exists()){
            Saved_Pages.mkdir();
        }
        return (new File(Saved_Pages,filename1));
    }


    private void enqueueDownloads() {
        final List<Request> requests = getFetchRequestWithGroupId(GROUP_ID);
        fetch.enqueue(requests, updatedRequests -> {

        });
    }

    private void back(){
        Animation animation= AnimationUtils.loadAnimation(DownloadActivity.this,R.anim.frag_exit);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.GONE);
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

    @Override
    public void communicate(String comm) {
        if (comm != null){
            if (comm.equals("storagePermission")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                }
            }else { }
        }else { }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(DownloadActivity.this,"permission granted", Toast.LENGTH_SHORT).show();
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
}
