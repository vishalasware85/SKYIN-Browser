package com.skyinbrowser.Addons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class QrScanComplete extends SuperBottomSheetFragment {

    private TextView textView,txt1;
    private Button copy,search;
    private FragmentToActivity mCallback;
    private LinearLayout mainbg;
    private Context mContext;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SharedPreferences appptheme=getActivity().getSharedPreferences("AppTheme",MODE_PRIVATE);
        if (appptheme.contains("DarkOn")){
            getActivity().setTheme(R.style.TranslucentNewsAppTheme);
        }else if (appptheme.contains("lightOn")) {
            getActivity().setTheme(R.style.TranslucentNewsDarkTheme);
        }else if (appptheme.contains("sysDef")){
            int nightModeFlags=getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_YES:
                    getActivity().setTheme(R.style.TranslucentNewsAppTheme);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    getActivity().setTheme(R.style.TranslucentNewsDarkTheme);
                    break;
            }
        }
        View view=inflater.inflate(R.layout.qr_scan_complete,container,false);
        mContext = view.getContext();

        textView=view.findViewById(R.id.codeResult);
        copy=view.findViewById(R.id.copyCodeBtn);
        search=view.findViewById(R.id.searchCodeBtn);
        txt1=view.findViewById(R.id.scanComlttext1);
        mainbg=view.findViewById(R.id.mainqrCompletebg);

        textView.setText(getArguments().getString("scanResult"));

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copytxt=textView.getText().toString();
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                            getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(copytxt);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("qrData", copytxt);
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(mContext, "Copied to clipboard 👍", Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(textView.getText().toString());
                Bundle recivedintent = getArguments();
                if (recivedintent != null){
                    if (recivedintent.containsKey("opened settings")){
                        sendData("addonsCloseWithFragAndSettings");
                    }else {
                        sendData("addonsCloseWithFrag");
                    }
                }
                dismiss();
            }
        });
        return view;
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

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
