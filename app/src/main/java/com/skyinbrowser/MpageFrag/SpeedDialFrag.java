package com.skyinbrowser.MpageFrag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.NonScrollGridView;
import com.skyinbrowser.MoreSites.GridAdapter;
import com.skyinbrowser.MoreSites.MySitesAdder;
import com.skyinbrowser.MoreSites.MySitesDeleter;
import com.skyinbrowser.MoreSites.more_sites_popup;
import com.skyinbrowser.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hari.bounceview.BounceView;

import static android.content.Context.MODE_PRIVATE;

public class SpeedDialFrag extends Fragment {

    private SharedPreferences preferences;
    private Context mContext;
    private CircleImageView facebookbt, youtubebt, cricbuzzbt,amazonbt, instagrambt,wikipediabt;
    private FragmentToActivity mCallback;
    private SpeedDialFragListener listener;
    private ImageView moreSiteBtn,addMoreBtn;

    //for gridview
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private NonScrollGridView gridView;
    private int datalistposition;
    private GridAdapter mysitesadapter;
    String previousSitename,previousSiteUrl,previousSiteIcon;

    public interface SpeedDialFragListener {
        void speedDialSent(CharSequence input);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SpeedDialFragListener) {
            listener = (SpeedDialFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SpeedDialFragListener");
        }
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
        listener = null;
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.speed_dial_layout_mpage, container, false);
        mContext = view.getContext();

        facebookbt = view.findViewById(R.id.facebookbt);
        youtubebt = view.findViewById(R.id.youtubebt);
        cricbuzzbt = view.findViewById(R.id.cricbuzzbt);
        wikipediabt = view.findViewById(R.id.wikipediabt);
        amazonbt = view.findViewById(R.id.amazonbt);
        instagrambt = view.findViewById(R.id.instagrambt);
        moreSiteBtn=view.findViewById(R.id.site_more_button);
        addMoreBtn=view.findViewById(R.id.speedDialBtnAdd);
        gridView=view.findViewById(R.id.speedDialGridView);

        BounceView.addAnimTo(facebookbt);
        BounceView.addAnimTo(youtubebt);
        BounceView.addAnimTo(cricbuzzbt);
        BounceView.addAnimTo(wikipediabt);
        BounceView.addAnimTo(amazonbt);
        BounceView.addAnimTo(instagrambt);
        BounceView.addAnimTo(moreSiteBtn);
        BounceView.addAnimTo(addMoreBtn);

        wikipediabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://en.wikipedia.org/wiki/Main_Page");
            }
        });

        youtubebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://www.youtube.com");
            }
        });

        facebookbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://www.facebook.com");
            }
        });

        instagrambt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://www.instagram.com");
            }
        });

        cricbuzzbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://www.cricbuzz.com");
            }
        });

        amazonbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData("https://www.amazon.in");
            }
        });

        //myrealGamesbt.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        sendData("https://www.myrealgames.com");
        //    }
        //});
//
        //gaanabt.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        sendData("https://gaana.com");
        //    }
        //});

        moreSiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, more_sites_popup.class);
                startActivityForResult(intent,10);
            }
        });

        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webViewUrl = "";
                Intent intent = new Intent(mContext, MySitesAdder.class);
                intent.putExtra("webViewUrl", webViewUrl);
                startActivityForResult(intent, 1);
            }
        });

        loadMySitesData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String siteurl=dataList.get(+position).get("siteUrl");
                //String  sitename=dataList.get(+position).get("siteName");
                //if (siteurl.equals("") && sitename.equals("")){
                //    String webViewUrl = "";
                //    Intent intent = new Intent(mContext, MySitesAdder.class);
                //    intent.putExtra("webViewUrl", webViewUrl);
                //    startActivityForResult(intent, 1);
                //}else {
                //
                //}

                String url=dataList.get(+position).get("siteUrl");
                sendData(url);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MySitesDeleter fragment = new MySitesDeleter ();
                datalistposition=position;
                previousSitename=dataList.get(+position).get("siteName");
                previousSiteUrl=dataList.get(+position).get("siteUrl");
                previousSiteIcon=dataList.get(+position).get("iconUrl");
                fragment.show(getActivity().getSupportFragmentManager(),"deleter");
                return true;
            }
        });

        LocalBroadcastManager.getInstance(mContext).registerReceiver(refreshMySites,new IntentFilter("refreshMySites"));

        return view;
    }

    public void speedDialReceiver(CharSequence newText) {
        if (newText != null){
            if (newText.equals("deleteMySites")){
                dataList.remove(datalistposition);
                gridView.setAdapter(mysitesadapter);
                mysitesadapter.notifyDataSetChanged();
                if (dataList.size()==0){
                    gridView.setVisibility(View.GONE);
                }else {
                    gridView.setVisibility(View.VISIBLE);
                }
                saveMySitesData();
            }
        }

        if (newText != null){
            if (newText.equals("editMySites")){
                Intent intent=new Intent(mContext,MySitesAdder.class);
                intent.putExtra("name",previousSitename);
                intent.putExtra("url",previousSiteUrl);
                intent.putExtra("icon",previousSiteIcon);
                intent.putExtra("updateMYSITES","updateMYSITES");
                startActivityForResult(intent,111);
            }
        }

        if (newText != null){
            if (newText.equals("clearAllMySites")){
                dataList.clear();
                gridView.setAdapter(mysitesadapter);
                mysitesadapter.notifyDataSetChanged();
                saveMySitesData();
                if (dataList.size()==0){
                    gridView.setVisibility(View.GONE);
                }else {
                    gridView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private BroadcastReceiver refreshMySites=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadMySitesData();
        }
    };

    private void saveMySitesData() {
        SharedPreferences preferences = mContext.getSharedPreferences("MySitesData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        editor.putString("MySiteList", json);
        editor.apply();
    }

    private void loadMySitesData() {
        SharedPreferences preferences = mContext.getSharedPreferences("MySitesData", MODE_PRIVATE);
        Gson gson = new Gson();

        if (preferences.contains("MySiteList")) {
            String json = preferences.getString("MySiteList", null);
            Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
            dataList = gson.fromJson(json, type);

            mysitesadapter = new GridAdapter(getActivity(),dataList);
            gridView.setAdapter(mysitesadapter);
            mysitesadapter.notifyDataSetChanged();

            if (dataList.size()==0){
                gridView.setVisibility(View.GONE);
            }else {
                gridView.setVisibility(View.VISIBLE);
            }
        } else {

        }
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (resultCode== Activity.RESULT_OK){
                if (data.hasExtra("moreSitesURL")){
                    String result=data.getStringExtra("moreSitesURL");
                    sendData(result);
                }
            }
        }

        if (requestCode==1){
            if (resultCode==Activity.RESULT_OK){
                loadMySitesData();
            }else {
            }
        }

        if (requestCode==111){
            if (resultCode==Activity.RESULT_OK){
                String newSiteurl,newsiteicon,newsitename;
                newSiteurl=data.getStringExtra("url");
                newsiteicon=data.getStringExtra("icon");
                newsitename=data.getStringExtra("name");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("siteName",newsitename);
                map.put("siteUrl",newSiteurl);
                map.put("iconUrl",newsiteicon);
                dataList.set(datalistposition,map);
                mysitesadapter.notifyDataSetChanged();
                saveMySitesData();
            }else {
            }
        }
    }
}