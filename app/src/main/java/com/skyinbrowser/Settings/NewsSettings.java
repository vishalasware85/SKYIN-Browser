package com.skyinbrowser.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;
import com.skyinbrowser.for_news.NewsBookmarks;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class NewsSettings extends Fragment {

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    private Button langChangeBn,bookmarkNewsOpenBtn;
    private TextView langText;
    private KProgressHUD kProgressHUD;

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
        view = inflater.inflate(R.layout.news_settings, container, false);
        mContext = view.getContext();

        langChangeBn=view.findViewById(R.id.newsLangChangeBtn);
        langText=view.findViewById(R.id.newsLangText);
        bookmarkNewsOpenBtn=view.findViewById(R.id.bookMarkNewsOpenSettings);

        SharedPreferences hindiNews=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
        if (hindiNews.contains("englishNews")){
            langText.setText("Current : English");
        }else if (hindiNews.contains("hindiNews")){
            langText.setText("Current : Hindi");
        }

        langChangeBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                Fragment prev=getFragmentManager().findFragmentByTag("dialog");
                if (prev != null){
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment deleteActivity=new NewsLanngChangeDialog();
                deleteActivity.setTargetFragment(NewsSettings .this,250);
                deleteActivity.show(fragmentTransaction,"dialog");
            }
        });

        bookmarkNewsOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kProgressHUD= KProgressHUD.create(mContext)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                Intent intent1=new Intent(mContext, NewsBookmarks.class);
                startActivity(intent1);
            }
        });

        LocalBroadcastManager.getInstance(mContext).registerReceiver(dismissProgress,new IntentFilter("dismiss Progress"));

        return view;
    }

    private BroadcastReceiver dismissProgress=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (kProgressHUD==null){ }else {
                kProgressHUD.dismiss();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==250){
            if (resultCode==RESULT_OK){
                if (data.hasExtra("hindi")){
                    langText.setText("Current : Hindi");
                }else if (data.hasExtra("english")){
                    langText.setText("Current : English");
                }
            }
        }
    }
}
