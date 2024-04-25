package com.skyinbrowser.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skyinbrowser.Addons.AddonsActivity;
import com.skyinbrowser.DownloadManager.DownloadActivity;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.MoreSites.more_sites_popup;
import com.skyinbrowser.PrivacyPolicy;
import com.skyinbrowser.R;

import static android.app.Activity.RESULT_OK;

public class SettingsActivity extends Fragment implements IOnBackPressed {

    ImageView closeSettings,openNingImage;
    ImageButton openHistory,openBookmark,openAddons,openDownloads,openUseCenter;
    ScrollView layout1;
    LinearLayout toolbar;
    CardView animVisible,btsheet;
    Button openGeneralSettings,openVideoSettings,openCustomizeSettings,privacyPolicy,
    openBrowsingSettings,openNewsSettings;
    SharedPreferences preferences;
    public static Activity settingsActivity;
    private FrameLayout generalFragContainer,videoFragContainer,customizeFragContainer,
            browsingFragContainer,newsFragContainer;
    private TextView titleText;

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.settings_activity, container, false);
        mContext = view.getContext();

        closeSettings=view.findViewById(R.id.btnclose_settings);
        openGeneralSettings=view.findViewById(R.id.openGeneralSettings);
        layout1=view.findViewById(R.id.layout1);
        openVideoSettings=view.findViewById(R.id.openvideosettings);
        openHistory=view.findViewById(R.id.openHistoryOnSetting);
        generalFragContainer=view.findViewById(R.id.generalSettingsFragContainer);
        videoFragContainer=view.findViewById(R.id.videoSettingsFragContainer);
        titleText=view.findViewById(R.id.titleTxt);
        openCustomizeSettings=view.findViewById(R.id.openCustomizeSettings);
        customizeFragContainer=view.findViewById(R.id.customizeSettingsFragContainer);
        openBookmark=view.findViewById(R.id.openBookmarkOnSetting);
        openAddons=view.findViewById(R.id.openAddonsOnSetting);
        openDownloads=view.findViewById(R.id.openDownloadsOnSetting);
        openUseCenter=view.findViewById(R.id.openUserCenterOnSetting);
        openNingImage=view.findViewById(R.id.openNingImageFromSettings);
        animVisible=view.findViewById(R.id.settingsAnimVisible);
        toolbar=view.findViewById(R.id.mainSettingsToolbar);
        btsheet=view.findViewById(R.id.mainSeiitngsBTSheet);
        privacyPolicy=view.findViewById(R.id.openPrivacyPolicy);
        openBrowsingSettings=view.findViewById(R.id.openBrowsingSettings);
        browsingFragContainer=view.findViewById(R.id.browsingSettingsFragContainer);
        openNewsSettings=view.findViewById(R.id.openNewsSettings);
        newsFragContainer=view.findViewById(R.id.newsSettingsFragContainer);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        closeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               back();
            }
        });

        inflateFragments();

        openGeneralSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("General Settings");

                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_left_gone);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.GONE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                generalFragContainer.startAnimation(animation1);
                generalFragContainer.setVisibility(View.VISIBLE);
            }
        });

        openVideoSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("Video Settings");

                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_left_gone);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.GONE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                videoFragContainer.startAnimation(animation1);
                videoFragContainer.setVisibility(View.VISIBLE);
            }
        });

        openCustomizeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("SK!YN Customization Wizard");
                titleText.setTextSize(22);

                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_left_gone);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.GONE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                customizeFragContainer.startAnimation(animation1);
                customizeFragContainer.setVisibility(View.VISIBLE);
            }
        });

        openBrowsingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("Browsing Settings");

                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_left_gone);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.GONE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                browsingFragContainer.startAnimation(animation1);
                browsingFragContainer.setVisibility(View.VISIBLE);
            }
        });

        openNewsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("News Settings");

                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_left_gone);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.GONE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                newsFragContainer.startAnimation(animation1);
                newsFragContainer.setVisibility(View.VISIBLE);
            }
        });


        openHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("historyActivityWithSettings");
            }
        });

        openBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("bookmarkActivityWithSettings");
            }
        });

        openAddons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,AddonsActivity.class);
                intent.putExtra("opened settings","opened settings");
                startActivity(intent);
            }
        });

        openDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, DownloadActivity.class);
                startActivity(intent);
            }
        });

        openUseCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, more_sites_popup.class);
                intent.putExtra("opened settings","opened settings");
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void inflateFragments(){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.generalSettingsFragContainer,new GeneralSettings());
        fragmentTransaction.add(R.id.customizeSettingsFragContainer,new CustomizeSettings());
        fragmentTransaction.add(R.id.browsingSettingsFragContainer,new BrowsingSettings());
        fragmentTransaction.add(R.id.newsSettingsFragContainer,new NewsSettings());
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                layout1.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }

    private void back(){
        switch (titleText.getText().toString()){
            case "General Settings":
                Animation animation= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right_visible);
                layout1.startAnimation(animation);
                layout1.setVisibility(View.VISIBLE);

                Animation animation1=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                generalFragContainer.startAnimation(animation1);
                generalFragContainer.setVisibility(View.GONE);
                titleText.setText("Settings");
                String titletext="SettingsActivity";
                Intent backtxt=new Intent("backtext").putExtra("titletext",titletext);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(backtxt);
                break;
            case "Video Settings":
                Animation animation2= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right_visible);
                layout1.startAnimation(animation2);
                layout1.setVisibility(View.VISIBLE);

                Animation animation3=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                videoFragContainer.startAnimation(animation3);
                videoFragContainer.setVisibility(View.GONE);
                titleText.setText("Settings");
                String titletext1="SettingsActivity";
                Intent backtxt1=new Intent("backtext").putExtra("titletext",titletext1);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(backtxt1);
                break;

            case "SK!YN Customization Wizard":
                Animation animation4= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right_visible);
                layout1.startAnimation(animation4);
                layout1.setVisibility(View.VISIBLE);

                Animation animation5=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                customizeFragContainer.startAnimation(animation5);
                customizeFragContainer.setVisibility(View.GONE);
                titleText.setText("Settings");
                titleText.setTextSize(25);
                String titletext2="SettingsActivity";
                Intent backtxt2=new Intent("backtext").putExtra("titletext",titletext2);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(backtxt2);
                break;

            case "Browsing Settings":
                Animation animation7= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right_visible);
                layout1.startAnimation(animation7);
                layout1.setVisibility(View.VISIBLE);

                Animation animation6=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                browsingFragContainer.startAnimation(animation6);
                browsingFragContainer.setVisibility(View.GONE);
                titleText.setText("Settings");
                String titletext3="SettingsActivity";
                Intent backtxt3=new Intent("backtext").putExtra("titletext",titletext3);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(backtxt3);
                break;

            case "News Settings":
                Animation animation8= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right_visible);
                layout1.startAnimation(animation8);
                layout1.setVisibility(View.VISIBLE);

                Animation animation9=AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                newsFragContainer.startAnimation(animation9);
                newsFragContainer.setVisibility(View.GONE);
                titleText.setText("Settings");
                String titletext4="SettingsActivity";
                Intent backtxt4=new Intent("backtext").putExtra("titletext",titletext4);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(backtxt4);
                break;

            case "Settings":
                Animation animation10= AnimationUtils.loadAnimation(mContext,R.anim.frag_exit);
                animation10.setDuration(100);
                animVisible.startAnimation(animation10);
                animVisible.setVisibility(View.GONE);
                animation10.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        sendData("SettingsClose");
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                break;
        }
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
