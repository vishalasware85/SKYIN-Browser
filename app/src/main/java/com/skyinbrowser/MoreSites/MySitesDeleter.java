package com.skyinbrowser.MoreSites;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.MpageFrag.SpeedDialFrag;
import com.skyinbrowser.R;

import static android.content.Context.MODE_PRIVATE;

public class MySitesDeleter extends SuperBottomSheetFragment {

    private Button delete,edit,clearAll;
    private FragmentToActivity mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    public void updateEditText(CharSequence newText) {

    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SharedPreferences appptheme=getActivity().getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            getActivity().setTheme(R.style.DarkTheme);
        }
        if (appptheme.contains("lightOn")){
            getActivity().setTheme(R.style.AppTheme);
        }
        if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    getActivity().setTheme(R.style.DarkTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    getActivity().setTheme(R.style.AppTheme);
                    break;
            }
        }
        View view=inflater.inflate(R.layout.my_sites_deleter,container,false);

        delete=view.findViewById(R.id.deleteMySites);
        edit=view.findViewById(R.id.editMySites);
        clearAll=view.findViewById(R.id.clearAllMySites);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("deleteMySites");
                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("editMySites");
                dismiss();
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("clearAllMySites");
                dismiss();
            }
        });

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public float getCornerRadius() {
        return getResources().getDimension(R.dimen.btsheet);
    }

    @Override
    public boolean animateCornerRadius() {
        return true;
    }


    @Override
    public boolean isSheetCancelableOnTouchOutside() {
        return true;
    }
}
