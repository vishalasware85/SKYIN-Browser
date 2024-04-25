package com.skyinbrowser.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skyinbrowser.R;
import com.suke.widget.SwitchButton;

import abak.tr.com.boxedverticalseekbar.BoxedVertical;

import static android.content.Context.MODE_PRIVATE;

public class BrowsingSettings extends Fragment {

    private Context mContext;
    private BoxedVertical boxedVertical;
    private int pointsval;
    private Button resetTS;
    private TextView pointvalue,tontxt,zoomtxt,ultraFastTxt;
    private SwitchButton swipeSwitch,zoomSwitch,zoomControlSwitch,ultraFastSwitch;
    private CardView zoomControlCard;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.browsing_settings,container,false);
        mContext=view.getContext();

        resetTS=view.findViewById(R.id.textSizeDefaultBtn);
        pointvalue=view.findViewById(R.id.pointValue);
        boxedVertical =view.findViewById(R.id.boxed_vertical);
        swipeSwitch=view.findViewById(R.id.swipeTurnOnswitch);
        tontxt=view.findViewById(R.id.swipeturnontxt);
        zoomtxt=view.findViewById(R.id.tvWPZoom);
        zoomControlSwitch=view.findViewById(R.id.zoomControlSwitch);
        zoomSwitch=view.findViewById(R.id.wpZoomSwitch);
        zoomControlCard=view.findViewById(R.id.zoomcontrolBtnCard);
        ultraFastTxt=view.findViewById(R.id.tvultrafast);
        ultraFastSwitch=view.findViewById(R.id.ultrafastSwitch);

        SharedPreferences preferences1=getActivity().getSharedPreferences("webViewSettings",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences1.edit();

        if (preferences1.contains("swipeRefOff")){
            swipeSwitch.setChecked(false);
            tontxt.setText("Turn on");
        }else{
            swipeSwitch.setChecked(true);
            tontxt.setText("Turn off");
        }

        if (preferences1.contains("textSize")){
            int points=preferences1.getInt("textSize",0);
            pointvalue.setText("Selected is : "+points);
            boxedVertical.setValue(points);
        }else {
            int fontSiz=preferences1.getInt("defaultfontSize22",0);
            pointvalue.setText("Selected is : "+fontSiz);
            boxedVertical.setValue(fontSiz);
        }

        if (preferences1.contains("mainZoomOff")){
            zoomtxt.setText("Turn On");
            zoomControlCard.setVisibility(View.GONE);
            zoomSwitch.setChecked(false);
        }else {
            zoomtxt.setText("Turn Off");
            zoomControlCard.setVisibility(View.VISIBLE);
            zoomSwitch.setChecked(true);
        }

        if (preferences1.contains("zoomControl")){
            zoomControlSwitch.setChecked(true);
        }else {
            zoomControlSwitch.setChecked(false);
        }

        if (preferences1.contains("ultraFastModeON")){
            ultraFastTxt.setText("Turn off");
            ultraFastSwitch.setChecked(true);
        }else {
            ultraFastTxt.setText("Turn on");
            ultraFastSwitch.setChecked(false);
        }

        swipeSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    tontxt.setText("Turn off");
                    editor.remove("swipeRefOff");
                    editor.apply();
                    Intent intent = new Intent("SwipeTurnOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    tontxt.setText("Turn on");
                    editor.putString("swipeRefOff","swipeRefOff");
                    editor.apply();
                    Intent intent = new Intent("SwipeTurnOff");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        boxedVertical.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(BoxedVertical boxedPoints, final int value) {
                pointvalue.setText("Selected is : " +value);
                pointsval=value;
            }
            @Override
            public void onStartTrackingTouch(BoxedVertical boxedPoints) { }
            @Override
            public void onStopTrackingTouch(BoxedVertical boxedPoints) {
                editor.putInt("textSize", pointsval);
                editor.apply();
                Intent intent = new Intent("webTextSize").putExtra("webTextSize",pointsval);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });

        resetTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fontSiz=preferences1.getInt("defaultfontSize22",0);
                pointvalue.setText("Selected is : "+fontSiz);
                boxedVertical.setValue(fontSiz);

                Intent intent = new Intent("webTextSize").putExtra("webTextSize",fontSiz);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                if (preferences1.contains("textSize")){
                    editor.remove("textSize");
                    editor.apply();
                }
            }
        });

        zoomSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    zoomtxt.setText("Turn Off");
                    zoomControlCard.setVisibility(View.VISIBLE);
                    editor.remove("mainZoomOff");
                    editor.apply();
                    Intent intent = new Intent("mainZoomOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    zoomtxt.setText("Turn On");
                    zoomControlCard.setVisibility(View.GONE);
                    editor.putString("mainZoomOff","mainZoomOff");
                    editor.apply();
                    Intent intent = new Intent("mainZoomOff");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        zoomControlSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    editor.putString("zoomControl","zoomControl");
                    editor.apply();
                    Intent intent = new Intent("mainZoomControlOn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    editor.remove("zoomControl");
                    editor.apply();
                    Intent intent = new Intent("mainZoomControlOff");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        ultraFastSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    ultraFastTxt.setText("Turn off");
                    editor.putString("ultraFastModeON","ultraFastModeON");
                    editor.apply();
                    Intent intent = new Intent("ultraFastModeON");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    ultraFastTxt.setText("Turn on");
                    editor.remove("ultraFastModeON");
                    editor.apply();
                    Intent intent = new Intent("ultraFastModeOFF");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        return view;
    }
}
