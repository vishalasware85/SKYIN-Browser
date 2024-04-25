package com.skyinbrowser.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

import static android.content.Context.MODE_PRIVATE;

public class NewsLanngChangeDialog extends DialogFragment {

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;
    private Button hindiBtn,englishBtn;

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
        view = inflater.inflate(R.layout.news_lang_change_dialog, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        hindiBtn=view.findViewById(R.id.hindiBtn);
        englishBtn=view.findViewById(R.id.englishBtn);

        hindiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("hindiNewsActivate");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                SharedPreferences hindiNews=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
                SharedPreferences.Editor editor=hindiNews.edit();
                editor.putString("hindiNews","hindiNews");
                editor.remove("englishNews");
                editor.apply();
                Intent intent1=new Intent();
                intent1.putExtra("hindi","hindi");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent1);
                dismiss();
            }
        });

        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("englishNewsActivate");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                SharedPreferences hindiNews=mContext.getSharedPreferences("NewsLanguage",MODE_PRIVATE);
                SharedPreferences.Editor editor=hindiNews.edit();
                editor.putString("englishNews","englishNews");
                editor.remove("hindiNews");
                editor.apply();
                Intent intent1=new Intent();
                intent1.putExtra("english","english");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent1);
                dismiss();
            }
        });

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

}
