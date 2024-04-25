package com.skyinbrowser.Dialogs;

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

public class NightModeSwitchingDialog extends DialogFragment {

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    private Button yes,no;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        onDestroy();

        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.night_mode_switching_dialog, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        yes=view.findViewById(R.id.yesNightModeDialog);
        no=view.findViewById(R.id.noNightModeDialog);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences4=mContext.getSharedPreferences("webViewSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor4=preferences4.edit();
                editor4.putString("applydarkweb","applydarkweb");
                editor4.apply();
                Intent intent = new Intent("applydarkweb");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences4=mContext.getSharedPreferences("webViewSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor4=preferences4.edit();
                editor4.remove("applydarkweb");
                editor4.apply();
                Intent intent = new Intent("removedarkweb");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                dismiss();
            }
        });
        return view;
    }
}