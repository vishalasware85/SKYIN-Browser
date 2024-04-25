package com.skyinbrowser.Dialogs;

import android.content.Context;
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

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.Mpage;
import com.skyinbrowser.R;
import com.skyinbrowser.tabs.TabsMain;
import com.suke.widget.SwitchButton;

import static android.content.Context.MODE_PRIVATE;

public class AppExitDialog extends DialogFragment {

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    private SwitchButton  mainhistory,floathisory,splithistory;
    private Button cancel,ok;
    private RecordAction action;

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
        view = inflater.inflate(R.layout.app_exit_dialog, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        action = new RecordAction(mContext);
        action.open(false);

        mainhistory=view.findViewById(R.id.clearMainHistorySB);
        floathisory=view.findViewById(R.id.clearFloatHistorySB);
        splithistory=view.findViewById(R.id.clearSplitHistorySB);
        cancel=view.findViewById(R.id.cancelExitDialog);
        ok=view.findViewById(R.id.okayExitDialog);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainhistory.isChecked()){
                    action.clearHistory();
                }
                if (floathisory.isChecked()){
                    action.clearFloatHistory();
                }
                if (splithistory.isChecked()){
                    action.clearLowerSplitHistory();
                    action.clearUpperSplitHistory();
                }
                SharedPreferences urlcheck=mContext.getSharedPreferences("mainweb url",MODE_PRIVATE);
                SharedPreferences.Editor editor= urlcheck.edit();
                editor.remove("url");
                editor.apply();
                Mpage.mpage.finish();
                TabsMain.tabsmain.finish();
                dismiss();
            }
        });

        return view;
    }
}
