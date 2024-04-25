package com.skyinbrowser.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skyinbrowser.weather.WeatherCustomLocationSetter;
import com.skyinbrowser.R;
import com.skyinbrowser.weather.Constants;

import me.omidh.liquidradiobutton.LiquidRadioButton;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class GeneralSettings extends Fragment {

    private Button openLocationSettings,downloadNetworkOkayButton;
    private SharedPreferences preferences;
    private TextView city_name_sett;
    private Context mContext;
    public String recentCity = "";
    private LiquidRadioButton wifiDownload,anyDownload;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.general_settings,container,false);
        mContext=view.getContext();

        openLocationSettings=view.findViewById(R.id.openLocationSettings);
        city_name_sett=view.findViewById(R.id.setting_city_name);
        wifiDownload=view.findViewById(R.id.downloadwifionly);
        anyDownload=view.findViewById(R.id.downloadanynetwork);
        downloadNetworkOkayButton=view.findViewById(R.id.downloadNetworkOkayBtn);

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        city_name_sett.setText(preferences.getString("city", Constants.DEFAULT_CITY));

        openLocationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_name = "";
                Intent weatherIntent = new Intent(mContext, WeatherCustomLocationSetter.class);
                weatherIntent.putExtra("setting_city_name", city_name);
                startActivityForResult(weatherIntent,1);
                weatherIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });

        SharedPreferences downloadSettings=getActivity().getSharedPreferences("downloadSettings",MODE_PRIVATE);
        SharedPreferences.Editor editor=downloadSettings.edit();
        if (downloadSettings.contains("wifionly")){
            wifiDownload.setChecked(true);
        }else {
            anyDownload.setChecked(true);
        }

        downloadNetworkOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiDownload.isChecked()){
                    editor.putString("wifionly","wifionly");
                    editor.remove("anynetwork");
                    editor.apply();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            if (resultCode==RESULT_OK){
                String city_name=data.getStringExtra("settings_result_city_location");
                city_name_sett.setText(city_name);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                recentCity = preferences.getString("city", Constants.DEFAULT_CITY);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("city", city_name);
                editor.apply();
                Intent intent=new Intent("settings_result_city_location").putExtra("cityname",city_name);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }
    }

}
