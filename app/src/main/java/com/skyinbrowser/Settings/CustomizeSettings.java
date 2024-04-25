package com.skyinbrowser.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.skyinbrowser.R;
import com.suke.widget.SwitchButton;

import me.omidh.liquidradiobutton.LiquidRadioButton;

import static android.content.Context.MODE_PRIVATE;

public class CustomizeSettings extends Fragment {

    private LiquidRadioButton lightTheme,darkTheme,systemDefault;
    private SwitchButton darkWebSwitch;
    private Context mContext;
    private Button okayBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.customize_settings,container,false);
        mContext=view.getContext();

        lightTheme=view.findViewById(R.id.lightThemeCB);
        darkTheme=view.findViewById(R.id.darkThemeCB);
        okayBtn=view.findViewById(R.id.okThemeBtn);
        systemDefault=view.findViewById(R.id.systemDefaultCB);
        darkWebSwitch=view.findViewById(R.id.darkThemeWebSwitch);

        SharedPreferences preferences=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (preferences.contains("sysDef")){
            systemDefault.setChecked(true);
        }
        if (preferences.contains("lightOn")){
            lightTheme.setChecked(true);
        }
        if (preferences.contains("DarkOn")){
            darkTheme.setChecked(true);
        }

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemDefault.isChecked()){
                    SharedPreferences preferences1=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);
                    SharedPreferences.Editor editor1=preferences1.edit();
                    editor1.putString("sysDef","sysDef");
                    editor1.remove("DarkOn");
                    editor1.remove("lightOn");
                    editor1.apply();

                    Intent intent = new Intent("SysDefOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }

                if (lightTheme.isChecked()){
                    SharedPreferences preferences2=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);
                    SharedPreferences.Editor editor2=preferences2.edit();
                    editor2.putString("lightOn","lightOn");
                    editor2.remove("DarkOn");
                    editor2.remove("sysDef");
                    editor2.apply();

                    Intent intent = new Intent("LightThemeOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }

                if (darkTheme.isChecked()){
                    SharedPreferences preferences3=mContext.getSharedPreferences("AppTheme",MODE_PRIVATE);
                    SharedPreferences.Editor editor3=preferences3.edit();
                    editor3.putString("DarkOn","DarkOn");
                    editor3.remove("sysDef");
                    editor3.remove("lightOn");
                    editor3.apply();

                    Intent intent = new Intent("DarkThemeOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        SharedPreferences preferences4=getActivity().getSharedPreferences("webViewSettings",MODE_PRIVATE);
        SharedPreferences.Editor editor4=preferences4.edit();

        if (preferences4.contains("applydarkweb")){
            darkWebSwitch.setChecked(true);
        }else {
            darkWebSwitch.setChecked(false);
        }

        darkWebSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    editor4.putString("applydarkweb","applydarkweb");
                    editor4.apply();
                    Intent intent = new Intent("applydarkweb");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    editor4.remove("applydarkweb");
                    editor4.apply();
                    Intent intent = new Intent("removedarkweb");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        return view;
    }
}
